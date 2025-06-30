INSERT INTO USUARIO (NOME, EMAIL, SENHA_HASH, TIPO_USUARIO)
VALUES
  ('Admin Master', 'admin@exemplo.com', SHA2('admin123', 256), 'ADMIN'),
  ('Bruna Assis', 'brunassisn@gmail.com', SHA2('bruna123', 256), 'NORMAL');

-- Pegue os IDs gerados para as contas, ou use LAST_INSERT_ID() se for um por um.
-- Suponha IDs 1 e 2 para os usuários acima.

INSERT INTO CONTA (USUARIO_ID, SALDO, NUMERO_CONTA, TIPO_CONTA)
VALUES
  (1, 10000.00, '0001-ADM', 'CORRENTE'),
  (2, 1500.00, '0002-JS', 'DIGITAL');

-- Suponha IDs 1 e 2 para as contas acima.

INSERT INTO MOVIMENTACAO (CONTA_ID, CONTA_RELACIONADA_ID, TIPO_MOVIMENTACAO, VALOR, DESCRICAO)
VALUES
  (2, NULL, 'ENTRADA', 1000.00, 'Depósito inicial'),
  (2, NULL, 'SAIDA', 200.00, 'Pagamento de boleto'),
  (2, NULL, 'ENTRADA', 700.00, 'Transferência recebida');
