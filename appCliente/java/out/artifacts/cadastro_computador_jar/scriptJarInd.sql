CREATE DATABASE medtech;
USE medtech;

CREATE TABLE endereco(
	idEndereco INT PRIMARY KEY AUTO_INCREMENT,
	cep CHAR(8),
	rua VARCHAR(100) NOT NULL,
	numero INT NOT NULL,
	complemento VARCHAR(255),
	uf CHAR(2) NOT NULL
) AUTO_INCREMENT = 1;

CREATE TABLE hospital(
	idHospital INT PRIMARY KEY AUTO_INCREMENT,
	nomeFantasia VARCHAR(100), 
	razaoSocial VARCHAR(100) NOT NULL,
	cnpj CHAR(14) NOT NULL,
	senha VARCHAR(255) NOT NULL,
	email VARCHAR(100) NOT NULL,
	-- verificado VARCHAR(5) NOT NULL, CONSTRAINT CHECK (verificado IN('true', 'false')),
	verificado TINYINT,
	fkEndereco INT NOT NULL,
	CONSTRAINT fkEnderecoHosp FOREIGN KEY (fkEndereco) REFERENCES endereco(idEndereco)
) AUTO_INCREMENT = 1;

CREATE TABLE funcionario(
	idFuncionario INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(100),
	cpf CHAR(11),
	telefone CHAR(11),
	cargo VARCHAR(45), CONSTRAINT chkCargo CHECK (cargo in ('MEDICO_GERENTE','TECNICO_TI','GESTOR_TI')),
	email VARCHAR(100),
	senha VARCHAR(255),
	fkHospital INT, CONSTRAINT fkHospitalFunc FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
) AUTO_INCREMENT = 1000;

CREATE TABLE departamento(
    idDepartamento INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45),
    fkHospital INT NOT NULL,
    CONSTRAINT fkDepartamentoHosp FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
) AUTO_INCREMENT = 1 ;

CREATE TABLE acesso(
    fkFuncionario INT,
    fkDepartamento INT,
    fkHospital INT,
    responsavel TINYINT,
    primary key (fkFuncionario, fkDepartamento, fkHospital),
    CONSTRAINT fkFuncionarioAcesso FOREIGN KEY (fkFuncionario) REFERENCES funcionario(idFuncionario),
    CONSTRAINT fkDepartamentoAcesso FOREIGN KEY (fkDepartamento) REFERENCES departamento(idDepartamento),
    CONSTRAINT fkHospitalAcesso FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
);

CREATE TABLE processador(
	idProcessador INT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(255),
	identificador VARCHAR(255),
	microarquitetura VARCHAR(255),
	frequencia VARCHAR(255),
	pacotesFisicos INT,
	cpusFisicas INT,
	cpusLogicas INT
) AUTO_INCREMENT = 1;

CREATE TABLE computador(
    idComputador INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR (50),
    sistemaOperacional VARCHAR(50),
    codPatrimonio VARCHAR(50),
    senha VARCHAR(255),
    gbRAM VARCHAR(50),
    gbDisco VARCHAR(50),
    tempoDeAtividade VARCHAR(50),
    fkProcessador INT,
    fkDepartamento INT NOT NULL,
    fkHospital  INT NOT NULL,
    CONSTRAINT fkProcessadorComputador FOREIGN KEY (fkProcessador) REFERENCES processador(idProcessador),
    CONSTRAINT fkDepartamentoComputador FOREIGN KEY (fkDepartamento) REFERENCES departamento(idDepartamento),
    CONSTRAINT fkHospitalComputador FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
);

CREATE TABLE leituraRamCpu(
    idLeituraRamCpu INT PRIMARY KEY AUTO_INCREMENT,
    ram DOUBLE,
    cpu DOUBLE,
    dataLeitura DATETIME,
    fkComputador INT NOT NULL,
    fkDepartamento INT NOT NULL,
    fkHospital INT NOT NULL,
    CONSTRAINT fkComputadorLeitura FOREIGN KEY (fkComputador) REFERENCES computador(idComputador),
    CONSTRAINT fkDepartamentoLeitura FOREIGN KEY (fkDepartamento) REFERENCES departamento(idDepartamento),
    CONSTRAINT fkHospitalLeitura FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
);

CREATE TABLE leituraDisco(
	idLeituraDisco INT PRIMARY KEY AUTO_INCREMENT,
    disco DOUBLE,
    dataLeitura DATETIME,
	fkComputador INT NOT NULL,
    fkDepartamento INT NOT NULL,
    fkHospital INT NOT NULL,
    CONSTRAINT fkComputadorLeituraDisc FOREIGN KEY (fkComputador) REFERENCES computador(idComputador),
    CONSTRAINT fkDepartamentoLeituraDisc FOREIGN KEY (fkDepartamento) REFERENCES departamento(idDepartamento),
    CONSTRAINT fkHospitalLeituraDisc FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
);

CREATE TABLE leituraFerramenta(
	idLeituraFerramenta INT PRIMARY KEY AUTO_INCREMENT, 
	nomeApp VARCHAR(255),
	dtLeitura DATETIME,
	caminho VARCHAR(255),
	fkComputador INT NOT NULL,
	fkDepartamento INT NOT NULL,
	fkHospital INT NOT NULL,
	CONSTRAINT fkComputadorLeituraFer FOREIGN KEY (fkComputador) REFERENCES computador(idComputador),
	CONSTRAINT fkDepartamentoLeituraFer FOREIGN KEY (fkDepartamento) REFERENCES departamento(idDepartamento),
	CONSTRAINT fkHospitalLeituraFer FOREIGN KEY (fkHospital) REFERENCES hospital(idHospital)
);

desc processador;
select * from processador;
INSERT INTO processador (nome, identificador, microarquitetura, frequencia, pacotesFisicos, cpusFisicas, cpusLogicas) VALUES
('INTEL CORE I5', 'Family 6 Model 61', 'arquitetura', '100000', 1, 4, 2);
desc computador;

INSERT INTO endereco (cep, rua, numero, complemento, uf) VALUES
('08450160', 'rua antônio thadeo', 373, 'apt04 bl604', 'SP');

INSERT INTO hospital (nomeFantasia, razaoSocial, cnpj, senha, email, verificado, fkEndereco) VALUES
('Clinica Folhas de Outono', 'Gazzoli Silva', '00000000000000', 'gazzoli123','clinicafoutono@outlook.com', true, 1);

INSERT INTO funcionario (nome, cpf, telefone, cargo, email, senha, fkHospital) VALUES
('Fernando Brandão', '12345678910', '11983987068', 'GESTOR_TI', 'fbrandao@sptech.school', 'sptech88', 1);

INSERT INTO departamento (nome, fkHospital) VALUES ('Triagem', 1);

INSERT INTO computador (nome, sistemaOperacional, codPatrimonio, senha, gbRam, gbDisco,fkProcessador, fkDepartamento, fkHospital) VALUES 
('PC_triagem01', 'Ubuntu', 'C057689', 'medtech88', 8, 250, 1, 1, 1);

INSERT INTO computador (nome, sistemaOperacional, codPatrimonio, senha, gbRam, gbDisco,fkProcessador, fkDepartamento, fkHospital) VALUES 
('PC_triagem02', 'Ubuntu', 'C057689', 'medtech77', 8, 250, 1, 1, 1);

UPDATE computador SET tempoDeAtividade = '9 days' WHERE idComputador = 2;
UPDATE computador SET tempoDeAtividade = '9 days' WHERE idComputador = 1;

select * from computador;

CREATE USER 'usuario'@'localhost' IDENTIFIED BY 'usuario';
GRANT insert, update, delete, select ON medtech.* to 'usuario'@'localhost';
FLUSH PRIVILEGES;
