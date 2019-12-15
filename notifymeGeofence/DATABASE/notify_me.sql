-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 04, 2018 at 09:04 PM
-- Server version: 10.1.29-MariaDB
-- PHP Version: 7.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `notify_me`
--

-- --------------------------------------------------------

--
-- Table structure for table `category_mobile`
--

CREATE TABLE `category_mobile` (
  `vendorId` smallint(5) NOT NULL,
  `vendorFirstname` varchar(20) NOT NULL,
  `vendorLastname` varchar(20) NOT NULL,
  `vendorContact` varchar(10) NOT NULL,
  `vendorEmail` varchar(50) NOT NULL,
  `vendorPsw` varchar(255) NOT NULL,
  `vendorShopId` smallint(5) NOT NULL,
  `vendorShopName` varchar(20) NOT NULL,
  `vendorShopAddress` varchar(100) NOT NULL,
  `vendorShopLat` decimal(12,9) NOT NULL,
  `vendorShopLon` decimal(12,9) NOT NULL,
  `vendorProductCategory` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `category_mobile`
--

INSERT INTO `category_mobile` (`vendorId`, `vendorFirstname`, `vendorLastname`, `vendorContact`, `vendorEmail`, `vendorPsw`, `vendorShopId`, `vendorShopName`, `vendorShopAddress`, `vendorShopLat`, `vendorShopLon`, `vendorProductCategory`) VALUES
(1, 'Rahul', 'Parmar', '1234567891', 'rahul@mail.com', 'rahulparmar', 1, 'NotifyMeShop', 'TrikonBaug', '22.295320940', '70.801746994', 'mobile'),
(2, 'Parmar', 'Rahul', '9876543210', 'parmar@mail.com', 'parmarrahul', 2, 'LenovoShop', 'GuruKul', '22.277565810', '70.799783617', 'mobile');

-- --------------------------------------------------------

--
-- Table structure for table `registration`
--

CREATE TABLE `registration` (
  `cid` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `cemail` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `address` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `registration`
--

INSERT INTO `registration` (`cid`, `name`, `email`, `cemail`, `password`, `mobile`, `address`) VALUES
(2, 'Bgthg', 'ggu@gh.com', 'ggu@gh.com', 'hbjgyfjjh', '998522369', 'bghnni'),
(3, 'Vhih', 'ggu@gh.com', 'ggu@gh.com', 'hbjgyfjjh', '998522369', 'bghnni'),
(4, 'Ali', 'ali@gmail.com', 'ali@gmail.com', '12345678', '123456789', 'rajkot'),
(5, 'Jidjdj', 'j@j.com', 'j@j.com', '123123123', '2147483647', 'adfgjkl'),
(6, 'Jidjdj', 'j@j.com', 'j@j.com', '123123123', '2147483647', 'adfgjkl'),
(7, 'Dg', 'f@gmail.com', 'f@gmail.com', '12345678', '5214893', 'gdghui'),
(8, '', '', '', '', '0', ''),
(9, 'Jigs', 'j@j.com', 'j@j.com', '123123123', '2147483647', 'tabajs'),
(10, 'Krjf', 'k@m.com', 'k@m.com', '12345678', '1234567890', 'jdjfjdufjcj'),
(11, 'Rahulparmar', 'rahul@mail.com', 'rahul@mail.com', 'rahul12345', '1234567890', 'rgrjdhdhdhjkggkdbfg'),
(12, '', '', '', '', '', ''),
(13, '', '', '', '', '', ''),
(14, '', '', '', '', '', ''),
(15, '', '', '', '', '', ''),
(16, 'Rp', 'rp@rp.com', 'rp@rp.com', 'rprp', '', ''),
(17, 'Tjjjjgh', 'gvv@ggs.v', 'gvv@ggs.v', 'hbjjk', '', ''),
(18, 'Rpp', 'rp@r.com', 'rp@r.com', 'rppr', '', ''),
(19, 'rahulparmar', 'rp@g.co', 'rp@g.co', 'rprprp', '1234567891', 'asdfghjkl'),
(20, 'Dh Chicky Gg', 'vmmm@jgvg.vv', 'vmmm@jgvg.vv', 'vvnnn', '968588', 'vhjkk'),
(21, 'Hdhjdshshs', 'shshshs@hsjs.ss', 'shshshs@hsjs.ss', 'shshsjsa', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(6) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `user_email` varchar(30) NOT NULL,
  `user_psw` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='User Account Info';

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_name`, `user_email`, `user_psw`) VALUES
(2, 'rahulparmar', 'rahulparmar@mail.com', 'rahulparmar'),
(3, 'rahulp', 'rahulp@mail.com', 'rahulparmarrp'),
(4, 'Jay', 'jb@gmail.com', '12345'),
(5, 'Aaa', 'aaaa@gmail.com', 'aaa'),
(6, 'parmar', 'parmar@gmail.com', 'rahu'),
(7, 'Keyur', 'keyu@mail.com', 'keyu'),
(8, 'Mkk', 'mp@mail.com', 'mpmp'),
(9, 'R', 'rp@g.com', 'rp12345');

-- --------------------------------------------------------

--
-- Table structure for table `vendor`
--

CREATE TABLE `vendor` (
  `vid` int(5) NOT NULL,
  `firstname` varchar(30) NOT NULL,
  `lastname` varchar(30) NOT NULL,
  `address` varchar(100) NOT NULL,
  `state` text NOT NULL,
  `postcode` int(6) NOT NULL,
  `email` varchar(30) NOT NULL,
  `url` varchar(30) NOT NULL,
  `tel` int(15) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `vendor`
--

INSERT INTO `vendor` (`vid`, `firstname`, `lastname`, `address`, `state`, `postcode`, `email`, `url`, `tel`, `password`) VALUES
(1, 'rahul', 'parmar', 'dsdffsdfs', 'Gujarat', 123456, 'http://rahul', '1234567890', 0, '$2y$10$ZDt04HGQIktkb0S7ESrFZe/HhxvMqfwyK1ktfL58nT9D7vZUBGY9q'),
(2, 'parmar', 'rahul', 'sdfs', 'Gujarat', 98765, 'http://rahulparmar', '1234567908', 0, '$2y$10$Md2QRB8BdExgN7aPv34F/OG7PuUEanstRXUpNLUecZ6JeJoVuh0Ye'),
(3, 'keyur', 'buha', 'asd', 'Gujarat', 123456, 'rahul@rahul.com', 'http://rahul', 1234567890, '$2y$10$LzOxpSX/GhZvtDv3pBQhoOHnplf8okfPTTrwKuqKL9n7DZdMiwhA.'),
(4, 'torab', 'shekh', 'ad', 'Gujarat', 123456, 'torab@t.com', 'http://troab', 1234567890, '$2y$10$HsHueZqhUMdF/14WR2BcJujJvUTRhVK3aaBgiN72bM0MhKPfbka7K'),
(5, 'Rahul', 'Parmar', 'Rajkot', 'Gujarat', 360001, 'rahul@mail.com', 'https://www.rahulparmar.tk', 2147483647, '$2y$10$pUxm6mB8w4TmN83JlS0/C.HpaiWJxPSxnhs3VwcqAW5UbURPw0CS2');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `category_mobile`
--
ALTER TABLE `category_mobile`
  ADD PRIMARY KEY (`vendorId`);

--
-- Indexes for table `registration`
--
ALTER TABLE `registration`
  ADD PRIMARY KEY (`cid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD UNIQUE KEY `user_id` (`user_id`) USING BTREE,
  ADD UNIQUE KEY `user_name` (`user_name`);

--
-- Indexes for table `vendor`
--
ALTER TABLE `vendor`
  ADD PRIMARY KEY (`vid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `category_mobile`
--
ALTER TABLE `category_mobile`
  MODIFY `vendorId` smallint(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `registration`
--
ALTER TABLE `registration`
  MODIFY `cid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `vendor`
--
ALTER TABLE `vendor`
  MODIFY `vid` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
