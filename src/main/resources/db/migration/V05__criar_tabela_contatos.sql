CREATE TABLE contatos (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT, 
	cod_pessoa BIGINT(20) NOT NULL, 
	nome VARCHAR(50) NOT NULL, 
	email VARCHAR(100) NOT NULL, 
	telefone VARCHAR(20) NOT NULL, 
	
	FOREIGN KEY (cod_pessoa) REFERENCES pessoas(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into contatos (codigo, cod_pessoa, nome, email, telefone) 
	values (1, 1, 'Marcos Henrique', 'marcos@algamoney.com', '00 00000-0000'); 