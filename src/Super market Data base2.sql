DROP DATABASE IF EXISTS superMarket;
CREATE DATABASE IF NOT EXISTS superMarket;
USE superMarket;

create table customer(
custID varchar(6) PRIMARY KEY,
custTital varchar(5) NOT NULL,
custName varchar (30) NOT NULL,
custAddress varchar(30) NOT NULL,
city varchar(20) NOT NULL,
province varchar(20) NOT NULL,
postalcode varchar(9)
);

create table item (
itemCode varchar(6) PRIMARY KEY,
Description varchar(50) NOT NULL,
packSize varchar(20) NOT NULL,
unitPrice DECIMAL(6,2) NOT NULL,
QtyOnHand INT(5) NOT NULL
);

create table orders(
orderID varchar(6) PRIMARY KEY,
orderDate DATE NOT NULL,
custID varchar(6),
FOREIGN KEY (custID) REFERENCES customer(custID)
);

create table orderDetail(
orderID varchar(6),
itemCode varchar(6),
orderQTY int(11) NOT NULL,
Discount DECIMAL(6,2),
FOREIGN KEY (orderID) REFERENCES orders(orderID),
FOREIGN KEY (itemCode) REFERENCES item(itemCode),
CONSTRAINT PRIMARY KEY (orderID,itemCode)
);

create table payment(
payID int AUTO_INCREMENT,
orderID varchar(6),
paymentType varchar(6) default 'unPaid' NOT NULL,
cost int(10) NOT NULL,
FOREIGN KEY (orderID) REFERENCES orders(orderID),
CONSTRAINT PRIMARY KEY (payID)
);



