
-- Host: 127.0.0.1:3306
-- Tempo de geração: 15-NOV-2023 às 13:35


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `dbjava`
--

-- --------------------------------------------------------
--DROP TABLE IF EXISTS

--To Disable a Constraint at DB level
--ALTER TABLE mysql.administrador DROP CONSTRAINT FK_user_adm;
--ALTER TABLE mysql.USUARIOS DROP PRIMARY KEY;

DROP TABLE IF EXISTS mysql.administrador;

DROP TABLE IF EXISTS mysql.usuarios;
ALTER TABLE mysql.Transacao DROP CONSTRAINT FK_cat;
ALTER TABLE mysql.Categoria DROP PRIMARY KEY;
DROP TABLE IF EXISTS mysql.Categoria;
DROP TABLE IF EXISTS mysql.Transacao;

--To Enable a Constraint at DB level

--EXEC sp_MSForEachTable 'ALTER TABLE ? CHECK CONSTRAINT ALL'
--
-- Estrutura da tabela `Usuarios`
--
CREATE TABLE IF NOT EXISTS mysql.Usuarios (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cpf` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `endereco` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `data_nascimento` DATE,
  `senha` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)  
) ;--ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
-- Estrutura da tabela `Administrador`
--CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
CREATE TABLE IF NOT EXISTS mysql.Administrador (
    `id_usuario` int NOT NULL,
    `tipo_administrador` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  KEY `FK_user_adm` (`id_usuario`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--

CREATE TABLE IF NOT EXISTS mysql.Transacao (
  `id` int NOT NULL AUTO_INCREMENT,
  `valor` double(12,2) NOT NULL,
  `data_op` DATE,
  `cat` int NOT NULL,
  `tipo` int NOT NULL, --tipo 1 despesa, tipo 2 receita
  `recorrencia` int NOT NULL, -- O SEM RECORRÊNCIA, 1 RECORRENCIA DE 1 MÊS
  PRIMARY KEY (`id`),
  KEY `FK_cat` (`cat`)
) ;

CREATE TABLE IF NOT EXISTS mysql.Categoria (
  `ID_CAT` int NOT NULL AUTO_INCREMENT,
  `categoria` varchar (16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `descricao` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ID_CAT`)  
) ;

--CONSTRAINTS

ALTER TABLE mysql.Administrador
  ADD CONSTRAINT `FK_user_adm` FOREIGN KEY (`id_usuario`) REFERENCES mysql.Usuarios (`id`);
COMMIT;

ALTER TABLE mysql.Transacao
  ADD CONSTRAINT `FK_cat` FOREIGN KEY (`cat`) REFERENCES mysql.Categoria (`ID_CAT`);
COMMIT;