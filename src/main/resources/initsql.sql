create database dcw;

use dcw;

create table form(
id varchar(36) not null primary key ,
creattime datetime not null,
lastmodifytime datetime ,
browser varchar(50) ,
author varchar(36) not null,
submitprivilege int default 0,
submitcountlimited int default 5,
iscollect bit(1) default 0
);

create table formproperty(
id varchar(36) not null primary key,
type varchar(30) not null,
value varchar(256),
form varchar(36)

);


create table formvalue(
	id varchar(36) not null primary key,
	value varchar(65535),
	author varchar(36) not null,
	creattime datetime not null,
	lastmodifytime datetime,
	submitip varchar(64) ,
	browser varchar(36),
	os varchar(36),
	lastmodifyperson varchar(36)

);

create table user(
	id varchar(36) not null primary key,
	username varchar(36) not null,
	password varchar(36) not null,
	tel varchar(11),
	email varchar(64)

);

create table role(
	id varchar(36) not null primary key,
	name varchar(36) not null,
	des varchar(64)
);

create table user_role(
	rid varchar(36) not null,
	uid varchar(36) not null,
	primary key(rid,uid)
);

create table mailrecord(
	id varchar(36) not null primary key,
	createtime datetime not null,
	email varchar(64) not null,
	token varchar(64) not null,
	invalida bit(1) default 0
);