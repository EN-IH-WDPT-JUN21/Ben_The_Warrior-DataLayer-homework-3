CREATE USER 'Ben'@'localhost' IDENTIFIED BY 'warrior';

GRANT ALL PRIVILEGES ON *.* TO 'Ben'@'localhost';

FLUSH PRIVILEGES;

create database datalayer;
use datalayer;

show tables;

select * from leads;
select * from contact;
select * from account;
select * from opportunity;

/*drop tables leads, contact, opportunity, account;*/