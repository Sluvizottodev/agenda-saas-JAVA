-- script de inicialização para banco de teste
-- cria o schema agenda_saas_test e popula dados de exemplo.

CREATE DATABASE IF NOT EXISTS agenda_saas_test 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE agenda_saas_test;

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo ENUM('CLIENTE', 'PRESTADOR', 'ADMIN') NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT PRIMARY KEY,
    cpf VARCHAR(14),
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS prestador (
    id INT PRIMARY KEY,
    telefone VARCHAR(15),
    especializacao VARCHAR(100),
    cnpj VARCHAR(18),
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    prestador_id INT NOT NULL,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS horario_disponivel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestador_id INT NOT NULL,
    data DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS agendamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    prestador_id INT NOT NULL,
    servico_id INT NOT NULL,
    data_hora DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (prestador_id) REFERENCES prestador(id),
    FOREIGN KEY (servico_id) REFERENCES servico(id)
);

INSERT IGNORE INTO usuario (nome, email, senha, tipo) VALUES 
('Teste Cliente', 'teste.cliente@example.com', 'senha', 'CLIENTE'),
('Teste Prestador', 'teste.prestador@example.com', 'senha', 'PRESTADOR');

SET @cliente_id = (SELECT id FROM usuario WHERE email = 'teste.cliente@example.com');
SET @prestador_id = (SELECT id FROM usuario WHERE email = 'teste.prestador@example.com');

INSERT IGNORE INTO cliente (id, cpf) VALUES (@cliente_id, '000.000.000-00');
INSERT IGNORE INTO prestador (id, telefone, especializacao) VALUES (@prestador_id, '11999999999', 'Teste');

INSERT IGNORE INTO servico (nome, descricao, preco, prestador_id) VALUES 
('Servico Teste A', 'Descricao A', 10.00, @prestador_id),
('Servico Teste B', 'Descricao B', 20.00, @prestador_id);

INSERT IGNORE INTO horario_disponivel (prestador_id, data, hora_inicio, hora_fim) VALUES
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '09:30:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:30:00', '10:00:00');

SELECT 'Banco de testes criado: agenda_saas_test' as status;
SELECT COUNT(*) as total_usuarios FROM usuario;
SELECT COUNT(*) as total_servicos FROM servico;
