CREATE TABLE pessoas (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL, 
	logradouro VARCHAR(50), 
	numero VARCHAR(10), 
	complemento VARCHAR(50), 
	bairro VARCHAR(20), 
	cep VARCHAR(10), 
	cidade VARCHAR(50), 
	estado CHAR(2), 
	ativo BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoas (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Ana', 'Rua Y', '236', 'A', 'Bairro ABC', '75.500-000', 'Itumbiara', 'GO', true);
INSERT INTO pessoas (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Amanda', 'Rua PR', '785', 'Casa', 'Bairro Norte', '65.785-230', 'Uberlândia', 'MG', true);
INSERT INTO pessoas (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Milton', 'Rua 7', '63', 'Apt', 'Bairro Z', '32.852-000', 'Curitiba', 'PR', false);

INSERT INTO pessoas (nome, logradouro, numero, bairro, cep, cidade, estado, ativo) values ('João', 'Rua X', '123', 'Bairro A', '57.745-582', 'Itumbiara', 'GO', true);
INSERT INTO pessoas (nome, logradouro, numero, bairro, cep, cidade, estado, ativo) values ('Daiane', 'Rua X', '123', 'Bairro A', '57.745-582', 'Itumbiara', 'GO', true);

INSERT INTO pessoas (nome, ativo) values ('José', true);
INSERT INTO pessoas (nome, ativo) values ('Carla', false);
INSERT INTO pessoas (nome, ativo) values ('Marcos', false);
INSERT INTO pessoas (nome, ativo) values ('Roberta', true);