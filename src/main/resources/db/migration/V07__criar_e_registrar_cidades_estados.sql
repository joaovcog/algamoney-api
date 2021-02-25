CREATE TABLE estados (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO estados (codigo, nome) VALUES(1, 'Acre');
INSERT INTO estados (codigo, nome) VALUES(2, 'Alagoas');
INSERT INTO estados (codigo, nome) VALUES(3, 'Amazonas');
INSERT INTO estados (codigo, nome) VALUES(4, 'Amapá');
INSERT INTO estados (codigo, nome) VALUES(5, 'Bahia');
INSERT INTO estados (codigo, nome) VALUES(6, 'Ceará');
INSERT INTO estados (codigo, nome) VALUES(7, 'Distrito Federal');
INSERT INTO estados (codigo, nome) VALUES(8, 'Espírito Santo');
INSERT INTO estados (codigo, nome) VALUES(9, 'Goiás');
INSERT INTO estados (codigo, nome) VALUES(10, 'Maranhão');
INSERT INTO estados (codigo, nome) VALUES(11, 'Minas Gerais');
INSERT INTO estados (codigo, nome) VALUES(12, 'Mato Grosso do Sul');
INSERT INTO estados (codigo, nome) VALUES(13, 'Mato Grosso');
INSERT INTO estados (codigo, nome) VALUES(14, 'Pará');
INSERT INTO estados (codigo, nome) VALUES(15, 'Paraíba');
INSERT INTO estados (codigo, nome) VALUES(16, 'Pernambuco');
INSERT INTO estados (codigo, nome) VALUES(17, 'Piauí');
INSERT INTO estados (codigo, nome) VALUES(18, 'Paraná');
INSERT INTO estados (codigo, nome) VALUES(19, 'Rio de Janeiro');
INSERT INTO estados (codigo, nome) VALUES(20, 'Rio Grande do Norte');
INSERT INTO estados (codigo, nome) VALUES(21, 'Rondônia');
INSERT INTO estados (codigo, nome) VALUES(22, 'Roraima');
INSERT INTO estados (codigo, nome) VALUES(23, 'Rio Grande do Sul');
INSERT INTO estados (codigo, nome) VALUES(24, 'Santa Catarina');
INSERT INTO estados (codigo, nome) VALUES(25, 'Sergipe');
INSERT INTO estados (codigo, nome) VALUES(26, 'São Paulo');
INSERT INTO estados (codigo, nome) VALUES(27, 'Tocantins');



CREATE TABLE cidades (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
  	cod_estado BIGINT(20) NOT NULL,
  FOREIGN KEY (cod_estado) REFERENCES estados(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO cidades (codigo, nome, cod_estado) VALUES (1, 'Belo Horizonte', 11);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (2, 'Uberlândia', 11);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (3, 'Uberaba', 11);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (4, 'São Paulo', 26);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (5, 'Campinas', 26);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (6, 'Rio de Janeiro', 19);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (7, 'Angra dos Reis', 19);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (8, 'Goiânia', 9);
INSERT INTO cidades (codigo, nome, cod_estado) VALUES (9, 'Caldas Novas', 9);



ALTER TABLE pessoas DROP COLUMN cidade;
ALTER TABLE pessoas DROP COLUMN estado;
ALTER TABLE pessoas ADD COLUMN cod_cidade BIGINT(20);
ALTER TABLE pessoas ADD CONSTRAINT fk_pessoas_cidades FOREIGN KEY (cod_cidade) REFERENCES cidades(codigo);

UPDATE pessoas SET cod_cidade = 4;