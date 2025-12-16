-- Script para criação das tabelas do sistema AgendaSaaS
-- Execute este script no seu banco de dados MySQL

-- Criação da base de dados (opcional, descomente se necessário)
-- CREATE DATABASE IF NOT EXISTS agenda_saas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE agenda_saas;

-- Tabela de usuários (base para Cliente e Prestador)
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo ENUM('CLIENTE', 'PRESTADOR', 'ADMIN') NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de clientes (herda de usuario)
CREATE TABLE IF NOT EXISTS cliente (
    id INT PRIMARY KEY,
    cpf VARCHAR(14) UNIQUE,
    telefone VARCHAR(15),
    endereco TEXT,
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de prestadores (herda de usuario)
CREATE TABLE IF NOT EXISTS prestador (
    id INT PRIMARY KEY,
    telefone VARCHAR(15),
    especializacao VARCHAR(100),
    cnpj VARCHAR(18) UNIQUE,
    endereco TEXT,
    descricao TEXT,
    FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Tabela de serviços oferecidos pelos prestadores
CREATE TABLE IF NOT EXISTS servico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    duracao_minutos INT DEFAULT 60,
    prestador_id INT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE
);

-- Tabela de horários disponíveis dos prestadores
CREATE TABLE IF NOT EXISTS horario_disponivel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prestador_id INT NOT NULL,
    data DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE,
    INDEX idx_prestador_data (prestador_id, data),
    INDEX idx_data_hora (data, hora_inicio, hora_fim),
    UNIQUE KEY uk_prestador_horario (prestador_id, data, hora_inicio, hora_fim)
);

-- Tabela de agendamentos
CREATE TABLE IF NOT EXISTS agendamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    prestador_id INT NOT NULL,
    servico_id INT NOT NULL,
    data_hora DATETIME NOT NULL,
    status ENUM('PENDENTE', 'CONFIRMADO', 'CANCELADO', 'CONCLUIDO') DEFAULT 'PENDENTE',
    observacoes TEXT,
    valor DECIMAL(10, 2),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (prestador_id) REFERENCES prestador(id) ON DELETE CASCADE,
    FOREIGN KEY (servico_id) REFERENCES servico(id) ON DELETE CASCADE,
    INDEX idx_cliente_data (cliente_id, data_hora),
    INDEX idx_prestador_data (prestador_id, data_hora),
    INDEX idx_status (status)
);

-- Tabela de pagamentos
CREATE TABLE IF NOT EXISTS pagamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    agendamento_id INT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDENTE', 'PAGO', 'CANCELADO', 'ESTORNADO') DEFAULT 'PENDENTE',
    metodo_pagamento ENUM('DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'TRANSFERENCIA') NOT NULL,
    data_pagamento DATETIME,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id) ON DELETE CASCADE
);

-- Tabela de notificações
CREATE TABLE IF NOT EXISTS notificacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    tipo ENUM('AGENDAMENTO', 'CANCELAMENTO', 'CONFIRMACAO', 'LEMBRETE', 'PROMOCAO') NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    mensagem TEXT NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    INDEX idx_usuario_lida (usuario_id, lida)
);

-- Inserção de dados de exemplo (opcional para testes)

-- Usuário administrador
INSERT IGNORE INTO usuario (nome, email, senha, tipo) VALUES 
('Admin', 'admin@agendasaas.com', 'admin123', 'ADMIN');

-- Cliente de exemplo
INSERT IGNORE INTO usuario (nome, email, senha, tipo) VALUES 
('João Silva', 'joao.silva@email.com', '123456', 'CLIENTE');
INSERT IGNORE INTO cliente (id, cpf, telefone) VALUES 
(LAST_INSERT_ID(), '123.456.789-00', '(11) 99999-1111');

-- Prestador de exemplo
INSERT IGNORE INTO usuario (nome, email, senha, tipo) VALUES 
('Maria Santos', 'maria.santos@email.com', '123456', 'PRESTADOR');
SET @prestador_id = LAST_INSERT_ID();
INSERT IGNORE INTO prestador (id, telefone, especializacao, cnpj) VALUES 
(@prestador_id, '(11) 99999-2222', 'Beleza e Estética', '12.345.678/0001-90');

-- Serviço de exemplo
INSERT IGNORE INTO servico (nome, descricao, preco, duracao_minutos, prestador_id) VALUES 
('Corte de Cabelo', 'Corte masculino e feminino', 35.00, 30, @prestador_id),
('Manicure', 'Cuidados com as unhas', 25.00, 45, @prestador_id);

-- Horários disponíveis de exemplo (próximos 7 dias)
INSERT IGNORE INTO horario_disponivel (prestador_id, data, hora_inicio, hora_fim) VALUES
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:00:00', '08:30:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '08:30:00', '09:00:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '09:30:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:00:00', '14:30:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '14:30:00', '15:00:00'),
(@prestador_id, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '15:00:00', '15:30:00');

-- Criar views úteis para relatórios

-- View de agendamentos completos
CREATE OR REPLACE VIEW v_agendamentos_completos AS
SELECT 
    a.id,
    a.data_hora,
    a.status,
    a.observacoes,
    a.valor,
    c.nome AS cliente_nome,
    c.email AS cliente_email,
    p.nome AS prestador_nome,
    p.especializacao AS prestador_especializacao,
    s.nome AS servico_nome,
    s.duracao_minutos AS servico_duracao,
    a.data_criacao
FROM agendamento a
JOIN cliente cl ON a.cliente_id = cl.id
JOIN usuario c ON cl.id = c.id
JOIN prestador pr ON a.prestador_id = pr.id
JOIN usuario p ON pr.id = p.id
JOIN servico s ON a.servico_id = s.id;

-- View de horários disponíveis com informações do prestador
CREATE OR REPLACE VIEW v_horarios_disponiveis AS
SELECT 
    h.id,
    h.data,
    h.hora_inicio,
    h.hora_fim,
    h.disponivel,
    u.nome AS prestador_nome,
    u.email AS prestador_email,
    pr.telefone AS prestador_telefone,
    pr.especializacao
FROM horario_disponivel h
JOIN prestador pr ON h.prestador_id = pr.id
JOIN usuario u ON pr.id = u.id
WHERE h.disponivel = TRUE 
AND h.data >= CURDATE();

COMMIT;