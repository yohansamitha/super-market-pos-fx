DROP DATABASE IF EXISTS HappyReaders;
CREATE DATABASE IF NOT EXISTS HappyReaders;
USE HappyReaders;

CREATE TABLE member(
memberID VARCHAR (10) PRIMARY KEY,
name VARCHAR (200) NOT NULL,
address VARCHAR (200) NOT NULL,
type VARCHAR (15) NOT NULL,
contactNumber VARCHAR (15) NOT NULL
);

CREATE TABLE item(
itemCode VARCHAR (10) PRIMARY KEY ,
title VARCHAR (100) NOT NULL,
author VARCHAR (100) NOT NULL,
publishDate DATE NOT NULL,
ISBM VARCHAR (15) NOT NULL,
publisher VARCHAR (100) NOT NULL,
type VARCHAR (20) NOT NULL,
numberOfCopies int (10) NOT NULL
);

CREATE TABLE borrowed(
borrowID VARCHAR (10) PRIMARY KEY,
memberID VARCHAR (10) NOT NULL,
borrowDATE DATE  NOT NULL,
returnDATE DATE  NOT NULL,
FOREIGN KEY (memberID) REFERENCES member(memberID)
);

CREATE TABLE borrowedDetails(
borrowID VARCHAR (10) NOT NULL,
itemCode  VARCHAR (10) NOT NULL,
FOREIGN KEY (itemCode) REFERENCES item(itemCode)
);


insert into member(memberID, name, address, type, contactNumber) values
("M001","kavindu","no69 panadura","elder","0715566221");

insert into member(memberID, name, address, type, contactNumber) values
("M002","siripala","no69 panadura","elder","0715566221");
insert into member(memberID, name, address, type, contactNumber) values
("M003","sumanasiri","no69 panadura","elder","0715566221");
insert into member(memberID, name, address, type, contactNumber) values
("M004","kamala","no69 panadura","elder","0715566221");

insert into item(itemCode, title, author, publishDate, ISBM, publisher, type, numberOfCopies) values
("I001","dilthopagalhe","pakaya",'2000-05-20',"A001010","blake","borrow",5);
