create database if not exists microshop character set utf8mb4;

use microshop;

create user if not exists patrician;
alter user patrician@'%' identified by '';
grant insert, select, delete, update, references on microshop.* to patrician@'%';
grant create on microshop.* to patrician@'%';