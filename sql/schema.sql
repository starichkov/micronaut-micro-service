create database if not exists micro_notes character set utf8mb4;

use micro_notes;

create user if not exists author;
alter user author@'%' identified by '';
grant insert, select, delete, update, references on micro_notes.* to author@'%';
grant create on micro_notes.* to author@'%';