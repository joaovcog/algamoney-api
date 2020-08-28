package com.algamoney.api.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//@CrossOrigin(maxAge = 10, origins = "http://localhost:8000") //default = permitir que todas as origens sejam autorizadas a fazer um GET em categorias
	@GetMapping
	public List<Categoria> listar() {
		//List<Categoria> categorias = categoriaRepository.findAll();
		//return !categorias.isEmpty() ? ResponseEntity.ok(categorias) : ResponseEntity.notFound().build();
		
		return categoriaRepository.findAll();
	}
	
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) { //response para trabalhar com o header da resposta
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		//response.setHeader("Location", uri.toASCIIString()); #Já está sendo adicionado na linha abaixo
		
		return ResponseEntity.created(uri).body(categoriaSalva); //retorna status HTTP created (201) e a categoria salva
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> optCategoria = categoriaRepository.findById(codigo);
		//optCategoria.map(categoria -> ResponseEntity.ok(categoria)).orElse(ResponseEntity.notFound().build());
		return optCategoria.isPresent() ? ResponseEntity.ok(optCategoria.get()) : ResponseEntity.notFound().build();
	}

}
