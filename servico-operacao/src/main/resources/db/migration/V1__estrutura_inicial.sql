-- ============================================================
-- SERVICO-OPERACAO
-- V1__estrutura_inicial.sql
--
-- PostgreSQL + Spring Boot + Flyway
--
-- RESPONSABILIDADE:
--   Setores
--   Operacoes
--   Riscos
--   Relacao Operacao x Risco
--   Relacao Operacao x EPI (so o vinculo, o catalogo de EPI
--   pertence ao servico-retirada-epi, validado via EpiClient)
--
-- Este banco pertence EXCLUSIVAMENTE ao servico-operacao.
-- Nao existe tabela "epi" aqui de proposito: o catalogo de EPI
-- e propriedade exclusiva do servico-retirada-epi, duplicar
-- essa tabela quebraria a fronteira de dominio entre os dois
-- servicos.
-- ============================================================


-- ============================================================
-- 1. SETOR
-- ============================================================

CREATE TABLE setor (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    descricao TEXT,

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_setor_nome UNIQUE (nome)
);


-- ============================================================
-- 2. RISCO
-- ============================================================

CREATE TABLE risco (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    descricao TEXT,

    categoria VARCHAR(100) NOT NULL,

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_risco_nome UNIQUE (nome)
);


-- ============================================================
-- 3. OPERACAO
-- ============================================================

CREATE TABLE operacao (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    descricao TEXT,

    setor_id BIGINT NOT NULL,

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_operacao_setor
        FOREIGN KEY (setor_id)
        REFERENCES setor(id)
        ON DELETE RESTRICT,

    CONSTRAINT uk_operacao_setor_nome
        UNIQUE (setor_id, nome)
);


-- ============================================================
-- 4. OPERACAO x RISCO
-- ============================================================

CREATE TABLE operacao_risco (
    id BIGSERIAL PRIMARY KEY,

    operacao_id BIGINT NOT NULL,

    risco_id BIGINT NOT NULL,

    nivel_risco VARCHAR(30) NOT NULL,

    observacao TEXT,

    CONSTRAINT fk_operacao_risco_operacao
        FOREIGN KEY (operacao_id)
        REFERENCES operacao(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_operacao_risco_risco
        FOREIGN KEY (risco_id)
        REFERENCES risco(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_operacao_risco
        UNIQUE (operacao_id, risco_id)
);


-- ============================================================
-- 5. OPERACAO x EPI
--
-- epi_id NAO tem FK: o catalogo de EPI vive em outro banco,
-- de outro servico (servico-retirada-epi). A validacao de
-- existencia acontece em tempo de execucao via EpiClient
-- (REST), nao via constraint de banco.
-- ============================================================

CREATE TABLE operacao_epi (
    id BIGSERIAL PRIMARY KEY,

    operacao_id BIGINT NOT NULL,

    epi_id BIGINT NOT NULL,

    obrigatorio BOOLEAN NOT NULL DEFAULT TRUE,

    observacao TEXT,

    CONSTRAINT fk_operacao_epi_operacao
        FOREIGN KEY (operacao_id)
        REFERENCES operacao(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_operacao_epi
        UNIQUE (operacao_id, epi_id)
);


-- ============================================================
-- 6. INDICES
-- ============================================================

CREATE INDEX idx_operacao_setor
    ON operacao(setor_id);

CREATE INDEX idx_operacao_risco_operacao
    ON operacao_risco(operacao_id);

CREATE INDEX idx_operacao_risco_risco
    ON operacao_risco(risco_id);

CREATE INDEX idx_operacao_epi_operacao
    ON operacao_epi(operacao_id);

CREATE INDEX idx_operacao_epi_epi
    ON operacao_epi(epi_id);


-- ============================================================
-- 7. DADOS INICIAIS - SETORES
-- ============================================================

INSERT INTO setor (nome, descricao) VALUES

('Recebimento de Materiais', 'Recebimento, conferência e movimentação inicial de matérias-primas.'),
('Movimentação de Cargas', 'Movimentação de materiais utilizando ponte rolante, empilhadeira e equipamentos similares.'),
('Corte', 'Corte de chapas, barras, tubos e perfis metálicos.'),
('Caldeiraria', 'Fabricação, preparação e montagem de estruturas e componentes metálicos.'),
('Soldagem', 'Processos de soldagem e união de componentes metálicos.'),
('Usinagem', 'Processos de torneamento, fresamento, furação e retificação.'),
('Fundição', 'Processos de fusão, moldagem e acabamento de metais.'),
('Tratamento de Superfícies', 'Jateamento, pintura e tratamentos químicos de superfícies metálicas.'),
('Montagem', 'Montagem de componentes, equipamentos e estruturas.'),
('Manutenção', 'Manutenção mecânica, elétrica e industrial.'),
('Expedição', 'Preparação, movimentação e carregamento de produtos acabados.'),
('Trabalho em Altura', 'Atividades realizadas em locais com risco de queda.'),
('Limpeza Industrial', 'Limpeza, organização e conservação de máquinas e áreas industriais.');


-- ============================================================
-- 8. DADOS INICIAIS - RISCOS
-- ============================================================

INSERT INTO risco (nome, descricao, categoria) VALUES

('Queda de Objetos', 'Possibilidade de queda de ferramentas, materiais ou componentes.', 'Acidente'),
('Esmagamento', 'Possibilidade de prensagem ou esmagamento de partes do corpo.', 'Acidente'),
('Corte', 'Possibilidade de contato com superfícies ou ferramentas cortantes.', 'Acidente'),
('Projeção de Partículas', 'Possibilidade de projeção de partículas metálicas ou abrasivas.', 'Acidente'),
('Ruído', 'Exposição a níveis elevados de pressão sonora.', 'Físico'),
('Calor', 'Exposição a temperaturas elevadas e fontes de calor.', 'Físico'),
('Radiação Não Ionizante', 'Exposição à radiação ultravioleta e infravermelha.', 'Físico'),
('Fumos Metálicos', 'Exposição a fumos provenientes de processos de soldagem e aquecimento.', 'Químico'),
('Poeira', 'Exposição a partículas suspensas no ambiente.', 'Químico'),
('Produtos Químicos', 'Contato ou exposição a substâncias químicas.', 'Químico'),
('Queda de Altura', 'Possibilidade de queda de trabalhador de nível elevado.', 'Acidente'),
('Choque Elétrico', 'Possibilidade de contato com energia elétrica.', 'Acidente'),
('Arco Elétrico', 'Possibilidade de exposição a arco elétrico.', 'Acidente'),
('Faíscas', 'Projeção de faíscas provenientes de processos térmicos e abrasivos.', 'Físico'),
('Metal Fundido', 'Possibilidade de contato com metal em estado líquido.', 'Físico'),
('Agarramento', 'Possibilidade de contato ou captura por partes móveis de máquinas.', 'Acidente');


-- ============================================================
-- 9. DADOS INICIAIS - OPERACOES
-- ============================================================

INSERT INTO operacao (nome, descricao, setor_id)
SELECT dados.nome, dados.descricao, s.id
FROM (
    VALUES
    ('Recebimento de matéria-prima', 'Recebimento, conferência e descarregamento de matérias-primas.', 'Recebimento de Materiais'),
    ('Movimentação com ponte rolante', 'Movimentação de materiais e cargas utilizando ponte rolante.', 'Movimentação de Cargas'),
    ('Operação de empilhadeira', 'Movimentação e transporte interno de materiais com empilhadeira.', 'Movimentação de Cargas'),
    ('Corte de chapas e perfis', 'Corte de materiais metálicos utilizando máquinas de corte.', 'Corte'),
    ('Esmerilhamento', 'Desbaste, acabamento e remoção de material utilizando esmerilhadeira.', 'Caldeiraria'),
    ('Soldagem MIG/MAG', 'Soldagem de componentes metálicos utilizando processo MIG/MAG.', 'Soldagem'),
    ('Soldagem TIG', 'Soldagem de componentes metálicos utilizando processo TIG.', 'Soldagem'),
    ('Soldagem com eletrodo revestido', 'Soldagem utilizando eletrodos revestidos.', 'Soldagem'),
    ('Oxicorte', 'Corte de materiais metálicos utilizando processo de oxicorte.', 'Corte'),
    ('Corte a plasma', 'Corte de materiais metálicos utilizando plasma.', 'Corte'),
    ('Torneamento', 'Usinagem de peças metálicas utilizando torno.', 'Usinagem'),
    ('Fresamento', 'Usinagem de peças utilizando fresadora.', 'Usinagem'),
    ('Furação', 'Perfuração de materiais utilizando máquinas de furação.', 'Usinagem'),
    ('Retificação', 'Acabamento e usinagem utilizando máquina retificadora.', 'Usinagem'),
    ('Fundição de metais', 'Fusão e moldagem de metais em ambiente industrial.', 'Fundição'),
    ('Jateamento abrasivo', 'Limpeza e preparação de superfícies utilizando abrasivos.', 'Tratamento de Superfícies'),
    ('Pintura industrial', 'Aplicação de revestimentos e tintas em componentes metálicos.', 'Tratamento de Superfícies'),
    ('Tratamento químico de metais', 'Tratamento de superfícies utilizando produtos químicos.', 'Tratamento de Superfícies'),
    ('Montagem de estruturas', 'Montagem de estruturas e componentes metálicos.', 'Montagem'),
    ('Manutenção mecânica', 'Manutenção preventiva e corretiva de máquinas e equipamentos.', 'Manutenção'),
    ('Manutenção elétrica', 'Manutenção preventiva e corretiva de instalações e equipamentos elétricos.', 'Manutenção'),
    ('Trabalho em altura', 'Execução de atividades em locais com risco de queda.', 'Trabalho em Altura'),
    ('Limpeza industrial', 'Limpeza e organização de máquinas, equipamentos e áreas industriais.', 'Limpeza Industrial'),
    ('Carregamento e expedição', 'Preparação e carregamento de produtos para expedição.', 'Expedição')
) AS dados(nome, descricao, setor_nome)
JOIN setor s ON s.nome = dados.setor_nome;


-- ============================================================
-- 10. OPERACAO x RISCO
-- ============================================================

INSERT INTO operacao_risco (operacao_id, risco_id, nivel_risco)
SELECT o.id, r.id, dados.nivel
FROM (
    VALUES
    ('Recebimento de matéria-prima', 'Queda de Objetos', 'Médio'),
    ('Recebimento de matéria-prima', 'Esmagamento', 'Médio'),
    ('Recebimento de matéria-prima', 'Projeção de Partículas', 'Médio'),
    ('Movimentação com ponte rolante', 'Queda de Objetos', 'Alto'),
    ('Movimentação com ponte rolante', 'Esmagamento', 'Alto'),
    ('Operação de empilhadeira', 'Esmagamento', 'Alto'),
    ('Operação de empilhadeira', 'Queda de Objetos', 'Alto'),
    ('Corte de chapas e perfis', 'Corte', 'Alto'),
    ('Corte de chapas e perfis', 'Projeção de Partículas', 'Alto'),
    ('Corte de chapas e perfis', 'Ruído', 'Alto'),
    ('Esmerilhamento', 'Projeção de Partículas', 'Alto'),
    ('Esmerilhamento', 'Ruído', 'Alto'),
    ('Esmerilhamento', 'Faíscas', 'Alto'),
    ('Soldagem MIG/MAG', 'Radiação Não Ionizante', 'Alto'),
    ('Soldagem MIG/MAG', 'Fumos Metálicos', 'Alto'),
    ('Soldagem MIG/MAG', 'Calor', 'Alto'),
    ('Soldagem MIG/MAG', 'Ruído', 'Alto'),
    ('Soldagem MIG/MAG', 'Faíscas', 'Alto'),
    ('Soldagem TIG', 'Radiação Não Ionizante', 'Alto'),
    ('Soldagem TIG', 'Fumos Metálicos', 'Alto'),
    ('Soldagem TIG', 'Calor', 'Alto'),
    ('Soldagem com eletrodo revestido', 'Radiação Não Ionizante', 'Alto'),
    ('Soldagem com eletrodo revestido', 'Fumos Metálicos', 'Alto'),
    ('Soldagem com eletrodo revestido', 'Calor', 'Alto'),
    ('Soldagem com eletrodo revestido', 'Faíscas', 'Alto'),
    ('Oxicorte', 'Calor', 'Alto'),
    ('Oxicorte', 'Faíscas', 'Alto'),
    ('Oxicorte', 'Radiação Não Ionizante', 'Alto'),
    ('Corte a plasma', 'Calor', 'Alto'),
    ('Corte a plasma', 'Ruído', 'Alto'),
    ('Corte a plasma', 'Radiação Não Ionizante', 'Alto'),
    ('Corte a plasma', 'Faíscas', 'Alto'),
    ('Torneamento', 'Projeção de Partículas', 'Alto'),
    ('Torneamento', 'Ruído', 'Alto'),
    ('Torneamento', 'Agarramento', 'Alto'),
    ('Fresamento', 'Projeção de Partículas', 'Médio'),
    ('Fresamento', 'Ruído', 'Médio'),
    ('Fresamento', 'Agarramento', 'Médio'),
    ('Furação', 'Projeção de Partículas', 'Médio'),
    ('Furação', 'Ruído', 'Médio'),
    ('Furação', 'Agarramento', 'Médio'),
    ('Retificação', 'Projeção de Partículas', 'Alto'),
    ('Retificação', 'Ruído', 'Alto'),
    ('Retificação', 'Faíscas', 'Alto'),
    ('Fundição de metais', 'Metal Fundido', 'Muito Alto'),
    ('Fundição de metais', 'Calor', 'Muito Alto'),
    ('Fundição de metais', 'Projeção de Partículas', 'Muito Alto'),
    ('Fundição de metais', 'Faíscas', 'Muito Alto'),
    ('Jateamento abrasivo', 'Poeira', 'Alto'),
    ('Jateamento abrasivo', 'Ruído', 'Alto'),
    ('Jateamento abrasivo', 'Projeção de Partículas', 'Alto'),
    ('Pintura industrial', 'Produtos Químicos', 'Alto'),
    ('Pintura industrial', 'Poeira', 'Alto'),
    ('Tratamento químico de metais', 'Produtos Químicos', 'Alto'),
    ('Montagem de estruturas', 'Queda de Objetos', 'Alto'),
    ('Montagem de estruturas', 'Esmagamento', 'Alto'),
    ('Montagem de estruturas', 'Corte', 'Alto'),
    ('Manutenção mecânica', 'Esmagamento', 'Alto'),
    ('Manutenção mecânica', 'Corte', 'Alto'),
    ('Manutenção mecânica', 'Agarramento', 'Alto'),
    ('Manutenção mecânica', 'Projeção de Partículas', 'Alto'),
    ('Manutenção elétrica', 'Choque Elétrico', 'Muito Alto'),
    ('Manutenção elétrica', 'Arco Elétrico', 'Muito Alto'),
    ('Trabalho em altura', 'Queda de Altura', 'Muito Alto'),
    ('Trabalho em altura', 'Queda de Objetos', 'Muito Alto'),
    ('Limpeza industrial', 'Produtos Químicos', 'Médio'),
    ('Limpeza industrial', 'Poeira', 'Médio'),
    ('Carregamento e expedição', 'Queda de Objetos', 'Alto'),
    ('Carregamento e expedição', 'Esmagamento', 'Alto')
) AS dados(operacao_nome, risco_nome, nivel)
JOIN operacao o ON o.nome = dados.operacao_nome
JOIN risco r ON r.nome = dados.risco_nome;


-- ============================================================
-- FIM DA MIGRATION V1
--
-- Seed de operacao_epi nao entra aqui: os epi_id validos so
-- existem depois de cadastrados no servico-retirada-epi.
-- Popular via POST /operacoes/{id}/epis, nao via SQL.
-- ============================================================
