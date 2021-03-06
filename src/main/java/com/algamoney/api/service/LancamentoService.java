package com.algamoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.algamoney.api.mail.Mailer;
import com.algamoney.api.model.Lancamento;
import com.algamoney.api.model.Pessoa;
import com.algamoney.api.model.Usuario;
import com.algamoney.api.repository.LancamentoRepository;
import com.algamoney.api.repository.PessoaRepository;
import com.algamoney.api.repository.UsuarioRepository;
import com.algamoney.api.service.exception.PessoaInexistenteOuInativaException;
import com.algamoney.api.storage.S3;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {
	
	private static final String PERMISSAO_DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
	
	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private S3 s3;

	public Lancamento salvar(Lancamento lancamento) {
		validarPessoa(lancamento);
		
		if (StringUtils.hasText(lancamento.getAnexo())) {
			s3.salvar(lancamento.getAnexo());
		}
		
		return lancamentoRepository.save(lancamento);
	}
	
	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = buscarLancamentoExistente(codigo);
		
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa())) {
			validarPessoa(lancamentoSalvo);
		}
		
		if (StringUtils.isEmpty(lancamento.getAnexo()) 
				&& StringUtils.hasText(lancamentoSalvo.getAnexo())) {
			s3.remover(lancamentoSalvo.getAnexo());
		} else if (StringUtils.hasText(lancamento.getAnexo()) 
				&& !lancamento.getAnexo().equals(lancamentoSalvo.getAnexo())) {
			s3.substituir(lancamentoSalvo.getAnexo(), lancamento.getAnexo());
		}
		
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		
		return lancamentoRepository.save(lancamentoSalvo);
	}
	
	public byte[] relatorioPorPessoa(LocalDate dataInicial, LocalDate dataFinal) throws JRException {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(dataInicial, dataFinal);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIAL", Date.valueOf(dataInicial));
		parametros.put("DT_FINAL", Date.valueOf(dataFinal));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, 
				new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	@Scheduled(cron = "0 0 4 * * *")
	//@Scheduled(fixedDelay = 1000 * 60 * 1)
	public void avisarLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de e-mails de aviso de lan??amentos vencidos.");
		}
		
		List<Lancamento> vencidos = lancamentoRepository.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		
		if (vencidos.isEmpty()) {
			logger.info("Sem lan??amentos vencidos para aviso.");
			
			return;
		}
		
		logger.info("Existem {} lan??amentos vencidos.", vencidos.size());
		
		List<Usuario> destinatarios = usuarioRepository.findByPermissoesDescricao(PERMISSAO_DESTINATARIOS);
		
		if (destinatarios.isEmpty()) {
			logger.warn("Existem lan??amentos vencidos, mas o sistema n??o encontrou destinat??rios.");
			
			return;
		}
		
		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
		
		logger.info("Envio de e-mail de aviso conclu??do.");
	}

	private void validarPessoa(Lancamento lancamento) {
		Optional<Pessoa> optPessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo());

		if (!optPessoa.isPresent() || optPessoa.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	private Lancamento buscarLancamentoExistente(Long codigo) {
		Optional<Lancamento> optLancamentoSalvo = lancamentoRepository.findById(codigo);
		return optLancamentoSalvo.orElseThrow(() -> new IllegalArgumentException());
	}
}
