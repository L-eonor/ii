-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: 31-Maio-2020 às 00:50
-- Versão do servidor: 10.1.44-MariaDB-0+deb9u1
-- PHP Version: 7.2.31-1+0~20200514.41+debian9~1.gbpe2a56b

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `up201504515`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `machineStats`
--

CREATE TABLE `machineStats` (
  `id_machine` varchar(20) NOT NULL,
  `total_op_time` int(11) NOT NULL DEFAULT '0',
  `total_op` int(11) NOT NULL DEFAULT '0',
  `P1` int(11) DEFAULT '0',
  `P2` int(11) NOT NULL DEFAULT '0',
  `P3` int(11) NOT NULL DEFAULT '0',
  `P4` int(11) NOT NULL DEFAULT '0',
  `P5` int(11) NOT NULL DEFAULT '0',
  `P6` int(11) NOT NULL DEFAULT '0',
  `P7` int(11) NOT NULL DEFAULT '0',
  `P8` int(11) NOT NULL DEFAULT '0',
  `P9` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `machineStats`
--

INSERT INTO `machineStats` (`id_machine`, `total_op_time`, `total_op`, `P1`, `P2`, `P3`, `P4`, `P5`, `P6`, `P7`, `P8`, `P9`) VALUES
('C1T3', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C1T4', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C1T5', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C3T3', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C3T4', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C3T5', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C5T3', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C5T4', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('C5T5', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

-- --------------------------------------------------------

--
-- Estrutura da tabela `orderListLoad`
--

CREATE TABLE `orderListLoad` (
  `id` smallint(6) NOT NULL,
  `submit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `status` varchar(20) DEFAULT 'por_iniciar',
  `type` varchar(5) NOT NULL,
  `duration (s)` int(11) AS (`end_time`-`submit_time`) VIRTUAL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `orderListLoad`
--

INSERT INTO `orderListLoad` (`id`, `submit_time`, `start_time`, `end_time`, `status`, `type`, `duration (s)`) VALUES
(1, '2020-05-30 22:24:55', '2020-05-30 22:24:55', '2020-05-30 22:25:15', 'concluida', 'P2', 60);

-- --------------------------------------------------------

--
-- Estrutura da tabela `orderListTransformation`
--

CREATE TABLE `orderListTransformation` (
  `id` smallint(6) NOT NULL,
  `submit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `type` varchar(20) NOT NULL DEFAULT 'transform',
  `status` varchar(20) NOT NULL DEFAULT 'por_iniciar',
  `Px` varchar(5) NOT NULL,
  `Py` varchar(5) NOT NULL,
  `quantity_total` int(11) NOT NULL,
  `quantity_sent` int(11) NOT NULL DEFAULT '0',
  `quantity_in_production` int(11) AS (`quantity_sent` - `quantity_done`) VIRTUAL,
  `quantity_done` int(11) NOT NULL DEFAULT '0',
  `quantity_queued` int(11) AS (`quantity_total` - `quantity_sent`) VIRTUAL,
  `max_delay` int(11) NOT NULL,
  `expected_end_time` timestamp AS (`submit_time` + INTERVAL `max_delay` SECOND) VIRTUAL,
  `duration (s)` int(11) AS (`end_time`-`submit_time`) VIRTUAL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `orderListTransformation`
--

INSERT INTO `orderListTransformation` (`id`, `submit_time`, `start_time`, `end_time`, `type`, `status`, `Px`, `Py`, `quantity_total`, `quantity_sent`, `quantity_in_production`, `quantity_done`, `quantity_queued`, `max_delay`, `expected_end_time`, `duration (s)`) VALUES
(1, '2020-05-30 22:22:51', '2020-05-30 22:37:59', '2020-05-30 22:29:51', 'transform', 'em_processamento', 'P2', 'P6', 30, 26, 4, 22, 4, 300, '2020-05-30 22:27:51', 700),
(2, '2020-05-30 22:22:54', '2020-05-30 22:38:03', '2020-05-30 22:38:59', 'transform', 'concluida', 'P3', 'P5', 7, 7, 0, 7, 0, 300, '2020-05-30 22:27:54', 1605),
(3, '2020-05-30 22:22:54', '2020-05-30 22:38:03', '2020-05-30 22:42:06', 'transform', 'concluida', 'P7', 'P9', 10, 10, 0, 10, 0, 300, '2020-05-30 22:27:54', 1952),
(4, '2020-05-30 22:22:56', '2020-05-30 22:38:05', '2020-05-30 22:41:24', 'transform', 'concluida', 'P4', 'P8', 7, 7, 0, 7, 0, 300, '2020-05-30 22:27:56', 1868),
(7, '2020-05-30 22:24:19', '2020-05-30 22:42:21', '2020-05-30 22:32:13', 'transform', 'em_processamento', 'P1', 'P9', 20, 2, -2, 4, 18, 900, '2020-05-30 22:39:19', 794),
(101, '2020-05-30 22:24:20', '2020-05-30 22:40:29', '2020-05-30 22:43:00', 'transform', 'concluida', 'P4', 'P5', 6, 6, 0, 6, 0, 1000, '2020-05-30 22:41:00', 1880);

-- --------------------------------------------------------

--
-- Estrutura da tabela `orderListUnload`
--

CREATE TABLE `orderListUnload` (
  `id` smallint(6) NOT NULL,
  `submit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `type` varchar(20) NOT NULL DEFAULT 'unload',
  `status` varchar(20) NOT NULL DEFAULT 'por_iniciar',
  `Px` varchar(5) NOT NULL,
  `Dy` varchar(5) NOT NULL,
  `quantity_total` int(11) NOT NULL,
  `quantity_sent` int(11) NOT NULL DEFAULT '0',
  `quantity_queued` int(11) AS (`quantity_total` - `quantity_sent`) VIRTUAL,
  `duration (s)` int(11) AS (`end_time`-`submit_time`) VIRTUAL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `orderListUnload`
--

INSERT INTO `orderListUnload` (`id`, `submit_time`, `start_time`, `end_time`, `type`, `status`, `Px`, `Dy`, `quantity_total`, `quantity_sent`, `quantity_queued`, `duration (s)`) VALUES
(5, '2020-05-30 22:22:57', '2020-05-30 22:38:45', '2020-05-30 22:38:57', 'unload', 'concluida', 'P9', 'D2', 8, 8, 0, 1600),
(6, '2020-05-30 22:22:57', '2020-05-30 22:23:22', '2020-05-30 22:23:49', 'unload', 'concluida', 'P6', 'D3', 6, 6, 0, 92);

-- --------------------------------------------------------

--
-- Estrutura da tabela `unloadStats`
--

CREATE TABLE `unloadStats` (
  `id_pusher` varchar(20) NOT NULL,
  `total` int(11) NOT NULL DEFAULT '0',
  `P1` int(11) NOT NULL DEFAULT '0',
  `P2` int(11) NOT NULL DEFAULT '0',
  `P3` int(11) NOT NULL DEFAULT '0',
  `P4` int(11) NOT NULL DEFAULT '0',
  `P5` int(11) NOT NULL DEFAULT '0',
  `P6` int(11) NOT NULL DEFAULT '0',
  `P7` int(11) NOT NULL DEFAULT '0',
  `P8` int(11) NOT NULL DEFAULT '0',
  `P9` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `unloadStats`
--

INSERT INTO `unloadStats` (`id_pusher`, `total`, `P1`, `P2`, `P3`, `P4`, `P5`, `P6`, `P7`, `P8`, `P9`) VALUES
('PM1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('PM1', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
('PM2', 6, 0, 0, 0, 0, 0, 0, 0, 0, 6),
('PM3', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `machineStats`
--
ALTER TABLE `machineStats`
  ADD PRIMARY KEY (`id_machine`);

--
-- Indexes for table `orderListLoad`
--
ALTER TABLE `orderListLoad`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orderListTransformation`
--
ALTER TABLE `orderListTransformation`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `orderListUnload`
--
ALTER TABLE `orderListUnload`
  ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
