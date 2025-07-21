-- MySQL dump 10.13  Distrib 8.4.5, for Linux (x86_64)
--
-- Host: localhost    Database: ecommerce
-- ------------------------------------------------------
-- Server version	8.4.5

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pedido_itens`
--

DROP TABLE IF EXISTS `pedido_itens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido_itens` (
  `id` binary(16) NOT NULL,
  `pedido_id` binary(16) NOT NULL,
  `produto_id` binary(16) NOT NULL,
  `quantidade` int NOT NULL,
  `preco_unitario` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pedido_itens_pedidos_id_fk` (`pedido_id`),
  KEY `pedido_itens_produtos_id_fk` (`produto_id`),
  CONSTRAINT `pedido_itens_pedidos_id_fk` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  CONSTRAINT `pedido_itens_produtos_id_fk` FOREIGN KEY (`produto_id`) REFERENCES `produtos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido_itens`
--

LOCK TABLES `pedido_itens` WRITE;
/*!40000 ALTER TABLE `pedido_itens` DISABLE KEYS */;
INSERT INTO `pedido_itens` VALUES (_binary '<\��\�\�A\�La�u\�a',_binary '3pf*iK4�=A�p�',_binary '�X=\�L��!Ͷ�#�\�',4,5.50),(_binary 's�_;�\�H?�)�\�-iq',_binary '\��PՕA��\�W�m;',_binary '�X=\�L��!Ͷ�#�\�',4,5.50),(_binary '۵L�TN���\�\�*G�c',_binary '\��PՕA��\�W�m;',_binary '-S����E�\�\�\�\�ʢf',10,4.56),(_binary '\�d&�8YN)�,\�z\�8�',_binary 'd9NW\�\�E���%an�Y',_binary '�X=\�L��!Ͷ�#�\�',4,5.50);
/*!40000 ALTER TABLE `pedido_itens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id` binary(16) NOT NULL,
  `usuario_id` binary(16) NOT NULL,
  `status` varchar(20) NOT NULL,
  `valor_total` decimal(10,2) NOT NULL,
  `data_cadastro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_atualizacao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `observacao` text,
  PRIMARY KEY (`id`),
  KEY `pedidos_usuarios_id_fk` (`usuario_id`),
  CONSTRAINT `pedidos_usuarios_id_fk` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (_binary '3pf*iK4�=A�p�',_binary '\�\�\�\�\�H\�\ra\�bkp','CANCELADO',22.00,'2025-07-21 01:29:45','2025-07-21 01:30:37','Quantidade de estoque insuficiente. Pedido cancelado.'),(_binary '\��PՕA��\�W�m;',_binary '\�\�\�\�\�H\�\ra\�bkp','CANCELADO',67.60,'2025-07-21 01:09:18','2025-07-21 01:24:02','Quantidade de estoque insuficiente. Pedido cancelado.'),(_binary 'd9NW\�\�E���%an�Y',_binary '\�\�\�\�\�H\�\ra\�bkp','PAGO',22.00,'2025-07-21 01:30:01','2025-07-21 01:30:37',NULL),(_binary 'ң�ګ(B��mi�<v\'<',_binary '\�\�\�\�\�H\�\ra\�bkp','CANCELADO',0.00,'2025-07-21 01:29:58','2025-07-21 01:31:27',NULL),(_binary '\�1�`�~B0��\��YI9\�',_binary '\�\�\�\�\�H\�\ra\�bkp','PENDENTE',22.00,'2025-07-21 01:29:41','2025-07-21 01:29:41',NULL);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produtos`
--

DROP TABLE IF EXISTS `produtos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produtos` (
  `id` binary(16) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `descricao` text,
  `preco` decimal(10,2) NOT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `quantidade_em_estoque` int NOT NULL,
  `data_cadastro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_atualizacao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `check_min_preco` CHECK ((`preco` >= 0)),
  CONSTRAINT `check_min_qtd_estoque` CHECK ((`quantidade_em_estoque` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produtos`
--

LOCK TABLES `produtos` WRITE;
/*!40000 ALTER TABLE `produtos` DISABLE KEYS */;
INSERT INTO `produtos` VALUES (_binary '�X=\�L��!Ͷ�#�\�','Produto 4','Descrição do Produto 4',5.50,'Categoria A',1,'2025-07-21 01:06:10','2025-07-21 01:30:37'),(_binary '-S����E�\�\�\�\�ʢf','Produto 6','Descrição do Produto 6',4.56,'Categoria B',9,'2025-07-21 01:06:46','2025-07-21 01:06:46'),(_binary ']yRqH�F�\�\�n��P','Produto 7','Descrição do Produto 7',5.00,'Categoria B',10,'2025-07-21 01:06:58','2025-07-21 01:06:58'),(_binary '`��G���`�\�\�','Produto 8','Descrição do Produto 8',6.50,'Categoria B',10,'2025-07-21 01:07:10','2025-07-21 01:07:10'),(_binary 'u} ;�E���\ZX\�\�]~','Produto 5','Descrição do Produto 5',9.99,'Categoria A',9,'2025-07-21 01:06:25','2025-07-21 01:06:25'),(_binary '��(\�\�oLQ�S\�B�4u3','Produto 9','Descrição do Produto 9',7.50,'Categoria B',10,'2025-07-21 01:07:19','2025-07-21 01:07:19'),(_binary '�\r9�/�A����\�qw+','Produto 0','Descrição do Produto 0',0.50,'Categoria Z',1,'2025-07-21 01:55:55','2025-07-21 01:55:55'),(_binary '�\�6nKHE�\�\�E�I\�R','Produto 3','Descrição do Produto 3',10.50,'Categoria A',10,'2025-07-21 01:05:13','2025-07-21 01:05:54'),(_binary '��@M8�M��|h�\�CR','Produto 10','Descrição do Produto 10',17.50,'Categoria B',10,'2025-07-21 01:07:28','2025-07-21 01:07:28'),(_binary '\�\�	|bI�Vg_$�\�','Produto 2','Descrição do Produto 2',20.00,'Categoria A',50,'2025-07-21 01:05:05','2025-07-21 01:05:05'),(_binary '�DYt\n@񓲢�\\\�\�','Produto 1','Descrição do Produto 1',1.00,'Categoria A',50,'2025-07-21 01:04:56','2025-07-21 01:04:56');
/*!40000 ALTER TABLE `produtos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` binary(16) NOT NULL,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `roles_uk` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (_binary 'uBR�l\�G_�\�\"��^\�','ADMIN'),(_binary '\�]\'49�Aa�¦AWqT','USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_usuarios`
--

DROP TABLE IF EXISTS `roles_usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_usuarios` (
  `usuario_id` binary(16) NOT NULL,
  `role_id` binary(16) NOT NULL,
  PRIMARY KEY (`usuario_id`,`role_id`),
  KEY `roles_usuarios_roles_id_fk` (`role_id`),
  CONSTRAINT `roles_usuarios_roles_id_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `roles_usuarios_usuarios_id_fk` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_usuarios`
--

LOCK TABLES `roles_usuarios` WRITE;
/*!40000 ALTER TABLE `roles_usuarios` DISABLE KEYS */;
INSERT INTO `roles_usuarios` VALUES (_binary '\�\�\�\�\�H\�\ra\�bkp',_binary 'uBR�l\�G_�\�\"��^\�'),(_binary '�=\�B#J#���H\�#��',_binary '\�]\'49�Aa�¦AWqT');
/*!40000 ALTER TABLE `roles_usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` binary(16) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `senha` varchar(300) NOT NULL,
  `data_cadastro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_atualizacao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (_binary '�=\�B#J#���H\�#��','Usuario','user@email.com','$2a$10$pZZVH7a.Owmy6Nfvu6gG.uoP1vLXjkWjvFNduICcUxMNIbp74yBfW','2025-07-21 00:58:58','2025-07-21 00:58:58'),(_binary '\�\�\�\�\�H\�\ra\�bkp','Administrador','admin@email.com','$2a$10$yGsYexlHq/ybinyv5p7YtODtNUd5Ye8JxBFAy2qOpDuM21Dx.EKYi','2025-07-21 00:57:47','2025-07-21 00:57:47');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-21  5:03:07
