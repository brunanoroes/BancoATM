EXEC sp_MSForEachTable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'
--ALTER TABLE MYSQL.administrador DROP CONSTRAINT FK_user_adm;
--ALTER TABLE MYSQL.USUARIOS DROP PRIMARY KEY;
--DELETE FROM mysql.administrador;
--DELETE FROM mysql.usuarios;
ALTER TABLE mysql.usuarios AUTO_INCREMENT=1;
ALTER TABLE mysql.Categoria AUTO_INCREMENT=1;

INSERT INTO mysql.usuarios (nome, cpf, endereco, data_nascimento,senha) 
	VALUES ('LuizGustavo', '858', 'Av 250','1980-01-01', '123');


INSERT INTO mysql.administrador (id_usuario, tipo_administrador) 
	VALUES (1, 'cadastrador');

INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Alimentação', 'Gastos com alimentação');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Transporte', 'Gastos com transporte');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Saude', 'Gastos com Saude');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Residencial', 'Gastos com Luz, água e gás');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Aluguel', 'Gastos/Receita com aluguel');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Salário', 'Receitas com salários ou proventos');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Loteria', 'Gastos/Receita com Loteria');
INSERT INTO mysql.Categoria (categoria, descricao) 
	VALUES ('Conta investimento', 'Gastos/Receita com investimentos');

INSERT INTO mysql.Transacao (valor, data_op, cat, tipo, recorrencia) 
	VALUES ('10,00', '2024-01-01', 1, 1, 0);

