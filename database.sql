-- SQL Script para Criação do Banco de Dados do Estúdio de Tatuagem
-- Versão: 1.1 (Multi-Tenant Ready)
-- Autor: Gemini
-- Descrição: Esta versão prepara o banco de dados para suportar múltiplos estúdios no futuro,
--            adicionando uma tabela de 'Estudios' e a coluna 'studio_id' nas tabelas relevantes.

-- Garante que as tabelas sejam criadas a partir de um estado limpo.
DROP TABLE IF EXISTS Agendamento_Materiais;
DROP TABLE IF EXISTS Agendamentos;
DROP TABLE IF EXISTS Materiais;
DROP TABLE IF EXISTS Clientes;
DROP TABLE IF EXISTS Tatuadores;
DROP TABLE IF EXISTS Estudios;


-- Tabela para gerenciar os estúdios (tenants).
-- No início, terá apenas um registro.
CREATE TABLE Estudios (
    id SERIAL PRIMARY KEY,
    nome_estudio VARCHAR(255) NOT NULL,
    subdominio VARCHAR(100) NOT NULL UNIQUE, -- Ex: 'alpha-ink' para alpha-ink.meusistema.com
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE Estudios IS 'Tabela de tenants. Cada linha representa um estúdio cliente do sistema.';


-- Tabela de Tatuadores, agora associada a um estúdio.
CREATE TABLE Tatuadores (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (studio_id) REFERENCES Estudios(id) ON DELETE CASCADE,
    UNIQUE (studio_id, email) -- O email do tatuador deve ser único dentro de um estúdio.
);


-- Tabela de Clientes, agora associada a um estúdio.
CREATE TABLE Clientes (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    data_cadastro TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (studio_id) REFERENCES Estudios(id) ON DELETE CASCADE,
    UNIQUE (studio_id, telefone) -- Um telefone de cliente só pode ser cadastrado uma vez por estúdio.
);


-- Tabela de Materiais, agora com estoque pertencente a um estúdio.
CREATE TABLE Materiais (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL,
    nome_material VARCHAR(255) NOT NULL,
    descricao TEXT,
    quantidade_estoque INT NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    unidade_medida VARCHAR(20) NOT NULL,
    ponto_reposicao INT NOT NULL DEFAULT 0,

    FOREIGN KEY (studio_id) REFERENCES Estudios(id) ON DELETE CASCADE,
    UNIQUE (studio_id, nome_material) -- O nome do material deve ser único dentro de um estúdio.
);


-- Tabela de Agendamentos, também associada a um estúdio para consultas rápidas.
CREATE TABLE Agendamentos (
    id SERIAL PRIMARY KEY,
    studio_id INT NOT NULL, -- Facilita queries e reforça o isolamento
    id_tatuador INT NOT NULL,
    id_cliente INT NOT NULL,
    data_agendamento DATE NOT NULL,
    periodo VARCHAR(10) NOT NULL CHECK (periodo IN ('MANHA', 'TARDE', 'NOITE')),
    status VARCHAR(15) NOT NULL CHECK (status IN ('AGENDADO', 'CONCLUIDO', 'CANCELADO')),
    descricao_tatuagem TEXT,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (studio_id) REFERENCES Estudios(id) ON DELETE CASCADE,
    FOREIGN KEY (id_tatuador) REFERENCES Tatuadores(id),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id)
);


-- Tabela de junção, não precisa de studio_id pois a relação já está contida no agendamento.
CREATE TABLE Agendamento_Materiais (
    id SERIAL PRIMARY KEY,
    id_agendamento INT NOT NULL,
    id_material INT NOT NULL,
    quantidade_usada INT NOT NULL CHECK (quantidade_usada > 0),

    FOREIGN KEY (id_agendamento) REFERENCES Agendamentos(id) ON DELETE CASCADE,
    FOREIGN KEY (id_material) REFERENCES Materiais(id),
    UNIQUE (id_agendamento, id_material)
);


-- --- DADOS INICIAIS ---
-- Insere o primeiro estúdio no sistema. Todas as operações iniciais usarão o ID gerado aqui (que será 1).
INSERT INTO Estudios (nome_estudio, subdominio) VALUES ('Meu Primeiro Estúdio', 'default');


-- Fim do Script
