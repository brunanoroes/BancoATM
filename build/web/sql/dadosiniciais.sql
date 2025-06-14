-- Usuários
INSERT INTO USUARIO (NOME, EMAIL, SENHA_HASH, TIPO_USUARIO)
VALUES 
  ('Admin Master', 'admin@exemplo.com', SHA2('admin123', 256), 'ADMIN'),
  ('Bruna Assis', 'brunassisn@gmail.com', SHA2('bruna123', 256), 'NORMAL');

-- Contas
INSERT INTO CONTA (USUARIO_ID, SALDO, NUMERO_CONTA, TIPO_CONTA)
VALUES 
  (1, 10000.00, '0001-ADM', 'CORRENTE'),
  (2, 1500.00, '0002-JS', 'DIGITAL');

-- Movimentações (para conta do João)
INSERT INTO MOVIMENTACAO (CONTA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO)
VALUES 
  (2, 'ENTRADA', 1000.00, 'Depósito inicial'),
  (2, 'SAIDA', 200.00, 'Pagamento de boleto'),
  (2, 'ENTRADA', 700.00, 'Transferência recebida');

-- Investimentos (para conta do João)
INSERT INTO INVESTIMENTO (CONTA_ID, TIPO_INVESTIMENTO, VALOR_APLICADO, DATA_VENCIMENTO, RENDIMENTO_ESPERADO)
VALUES 
  (2, 'CDB', 500.00, DATE_ADD(NOW(), INTERVAL 6 MONTH), 550.00),
  (2, 'Tesouro Direto', 300.00, DATE_ADD(NOW(), INTERVAL 1 YEAR), 360.00);
