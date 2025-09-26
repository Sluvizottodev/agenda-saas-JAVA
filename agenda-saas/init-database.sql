-- Script de inicialização rápida para AgendaSaaS
-- Execute este script na sua extensão MySQL

-- 1. Criar banco de dados
CREATE DATABASE IF NOT EXISTS agenda_saas 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE agenda_saas;

-- 2. Tabela principal de usuários
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo ENUM('CLIENTE', 'PRESTADOR', 'ADMIN') NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabela de clientes
CREATE TABLE IF NOT EXISTS cliente (
    id INT PRIMARY KEY,
    cpf VARCHAR(14),
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- 4. Tabela de prestadores
CREATE TABLE IF NOT EXISTS prestador (
    id INT PRIMARY KEY,
    telefone VARCHAR(15),
    especializacao VARCHAR(100),
    cnpj VARCHAR(18),
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- 5. Tabela de serviços
CREATE TABLE IF NOT EXISTS servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    prestador_id INT NOT NULL,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE
);

-- 6. Tabela de horários disponíveis
CREATE TABLE IF NOT EXISTS horario_disponivel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestador_id INT NOT NULL,
    data DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE
);

-- 7. Tabela de agendamentos
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

-- 8. Inserir dados de teste
INSERT IGNORE INTO usuario (nome, email, senha, tipo) VALUES 
('João Silva', 'joao@email.com', '123456', 'CLIENTE'),
('Maria Santos', 'maria@email.com', '123456', 'PRESTADOR');

-- Obter os IDs inseridos
SET @cliente_id = (SELECT id FROM usuario WHERE email = 'joao@email.com');
SET @prestador_id = (SELECT id FROM usuario WHERE email = 'maria@email.com');

INSERT IGNORE INTO cliente (id, cpf) VALUES (@cliente_id, '12345678900');
INSERT IGNORE INTO prestador (id, telefone, especializacao) VALUES 
(@prestador_id, '11999999999', 'Beleza');

INSERT IGNORE INTO servico (nome, descricao, preco, prestador_id) VALUES 
('Corte de Cabelo', 'Corte masculino e feminino', 35.00, @prestador_id),
('Manicure', 'Cuidados com unhas', 25.00, @prestador_id);

-- Inserir horários de teste para amanhã
INSERT IGNORE INTO horario_disponivel (prestador_id, data, hora_inicio, hora_fim) VALUES
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '09:30:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:30:00', '10:00:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', '10:30:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '14:30:00');

-- Verificar se tudo foi criado corretamente
SELECT 'Tabelas criadas com sucesso!' as status;
SELECT COUNT(*) as total_usuarios FROM usuario;
SELECT COUNT(*) as total_servicos FROM servico;
SELECT COUNT(*) as total_horarios FROM horario_disponivel;