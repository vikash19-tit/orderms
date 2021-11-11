drop database if exists order_db;
create database order_db;
use  order_db;


create table orders(
   order_id varchar(20),
   buyer_id varchar(50),
   amount int,
   address varchar(200),
   status varchar(50)
   );


insert into orders (order_id, buyer_id, amount, address, status) values (1, 'b01', '5000', 'bhopal', 'ORDER PLACED');

create table productsordered(
   buyer_id varchar(50),
   prod_id varchar(20),
   seller_id varchar(20),
   quantity bigint,
   order_id varchar(5),
   status varchar(50)
   );


insert into productsordered(buyer_id,prod_id seller_id,quantity,order_id,status) values ('b01','p101','s101',4,'1','ORDER PLACED');


commit;
