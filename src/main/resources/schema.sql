-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Mar 12, 2020 at 05:41 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pm-tool`
--

-- --------------------------------------------------------

--
-- Table structure for table `Client`
--

CREATE TABLE `Client` (
  `clientId` varchar(255) NOT NULL,
  `clientName` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `projectId` varchar(255) NOT NULL,
  `projectName` varchar(255) NOT NULL,
  `clientId` varchar(255) NOT NULL,
  `projectStartDate` timestamp NOT NULL DEFAULT current_timestamp(),
  `projectEndDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `projectStatus` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `ProjectRole`
--

CREATE TABLE `ProjectRole` (
  `projectRoleId` int(11) NOT NULL,
  `projectRoleName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `Project_User`
--

CREATE TABLE `Project_User` (
  `projectId` varchar(255) NOT NULL,
  `assigneeId` varchar(255) NOT NULL,
  `assignedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `assigneeJobRole` varchar(255) NOT NULL,
  `assigneeProjectRole` int(11) NOT NULL,
  `isBlocked` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `SubTask`
--

CREATE TABLE `SubTask` (
  `subtaskId` varchar(255) NOT NULL,
  `taskId` varchar(255) NOT NULL,
  `subtaskName` varchar(255) NOT NULL,
  `subtaskStatus` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `Task`
--

CREATE TABLE `Task` (
  `taskId` varchar(255) NOT NULL,
  `projectId` varchar(255) NOT NULL,
  `taskName` varchar(255) NOT NULL,
  `taskInitiator` varchar(255) NOT NULL,
  `taskAssignee` varchar(255) NOT NULL,
  `taskNote` varchar(255) DEFAULT NULL,
  `taskStatus` varchar(255) NOT NULL,
  `taskCreatedAt` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `taskDueDateAt` timestamp NULL DEFAULT NULL,
  `taskReminderAt` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `TaskFile`
--

CREATE TABLE `TaskFile` (
  `taskFileId` varchar(255) NOT NULL,
  `taskId` varchar(255) NOT NULL,
  `taskFileName` varchar(255) NOT NULL,
  `taskFileUrl` varchar(255) NOT NULL,
  `fileCreatortaskFileCreator` varchar(255) NOT NULL,
  `taskFileDate` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE `User` (
  `userId` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `idpUserId` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Client`
--
ALTER TABLE `Client`
  ADD PRIMARY KEY (`clientId`);

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`projectId`);

--
-- Indexes for table `ProjectRole`
--
ALTER TABLE `ProjectRole`
  ADD PRIMARY KEY (`projectRoleId`);

--
-- Indexes for table `Project_User`
--
ALTER TABLE `Project_User`
  ADD PRIMARY KEY (`projectId`,`assigneeId`);

--
-- Indexes for table `SubTask`
--
ALTER TABLE `SubTask`
  ADD PRIMARY KEY (`subtaskId`);

--
-- Indexes for table `Task`
--
ALTER TABLE `Task`
  ADD PRIMARY KEY (`taskId`);

--
-- Indexes for table `TaskFile`
--
ALTER TABLE `TaskFile`
  ADD PRIMARY KEY (`taskFileId`);

--
-- Indexes for table `User`
--
ALTER TABLE `User`
  ADD PRIMARY KEY (`userId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;