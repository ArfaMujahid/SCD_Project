create database texteditor;
use texteditor;
create table users(
    name varchar(255) primary key,
    password varchar(255)
);

insert into users values ("Ali", "123"), ("Hassan","456"), ("Faiza", 789);

create table Document(
    username varchar(255),
    documentName varchar(255),
    text blob,
    attributes blob,
    constraint FK_Constraint foreign key (username) references users(username)
);

