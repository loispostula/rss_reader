-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Sam 17 Mai 2014 à 20:11
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `rssreader`
--
CREATE DATABASE IF NOT EXISTS `rssreader` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `rssreader`;

-- --------------------------------------------------------

--
-- Structure de la table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `feed_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `publication_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `user_email` varchar(200) COLLATE utf8_bin NOT NULL,
  `date` date NOT NULL,
  `text` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`feed_url`,`publication_url`,`user_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `contain`
--

CREATE TABLE IF NOT EXISTS `contain` (
  `feed_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `publication_url` varchar(200) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`feed_url`,`publication_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `feed`
--

CREATE TABLE IF NOT EXISTS `feed` (
  `url` varchar(200) COLLATE utf8_bin NOT NULL,
  `title` varchar(200) COLLATE utf8_bin NOT NULL,
  `link` varchar(200) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `image` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `feedsubscription`
--

CREATE TABLE IF NOT EXISTS `feedsubscription` (
  `user_email` varchar(200) COLLATE utf8_bin NOT NULL,
  `feed_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `subscribedDate` date NOT NULL,
  PRIMARY KEY (`user_email`,`feed_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `friendship`
--

CREATE TABLE IF NOT EXISTS `friendship` (
  `user1_email` varchar(200) COLLATE utf8_bin NOT NULL,
  `user2_email` varchar(200) COLLATE utf8_bin NOT NULL,
  `requestDate` date NOT NULL,
  `accepted` tinyint(1) NOT NULL,
  PRIMARY KEY (`user1_email`,`user2_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `publication`
--

CREATE TABLE IF NOT EXISTS `publication` (
  `url` varchar(200) COLLATE utf8_bin NOT NULL,
  `title` varchar(200) COLLATE utf8_bin NOT NULL,
  `releaseDate` date NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `image` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `readstatus`
--

CREATE TABLE IF NOT EXISTS `readstatus` (
  `user_email` varchar(200) COLLATE utf8_bin NOT NULL,
  `publication_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `feed_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`user_email`,`publication_url`,`feed_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `sharedpublication`
--

CREATE TABLE IF NOT EXISTS `sharedpublication` (
  `user_email` varchar(200) COLLATE utf8_bin NOT NULL,
  `publication_url` varchar(200) COLLATE utf8_bin NOT NULL,
  `sharedDate` date NOT NULL,
  `text` text COLLATE utf8_bin,
  PRIMARY KEY (`user_email`,`publication_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `email` varchar(200) COLLATE utf8_bin NOT NULL,
  `nickname` varchar(100) COLLATE utf8_bin NOT NULL,
  `password` varchar(100) COLLATE utf8_bin NOT NULL,
  `country` varchar(100) COLLATE utf8_bin NOT NULL,
  `city` varchar(100) COLLATE utf8_bin NOT NULL,
  `avatar` varchar(200) COLLATE utf8_bin NOT NULL,
  `biography` text COLLATE utf8_bin,
  `joinedDate` date NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
