CREATE DATABASE dcw;

USE dcw;

CREATE TABLE form (
  id                 VARCHAR(36)  NOT NULL PRIMARY KEY,
  creattime          DATETIME     NOT NULL,
  lastmodifytime     DATETIME,
  browser            VARCHAR(50),
  author             VARCHAR(36)  NOT NULL,
  submitprivilege    INT         DEFAULT 0,
  submitcountlimited INT         DEFAULT 5,
  iscollect          BIT(1)      DEFAULT 0,
  viewcount          INT         DEFAULT 0,
  resultviewcount    INT         DEFAULT 0,
  resultshow         VARCHAR(20) DEFAULT 'SHOW_ALL',
  labels             VARCHAR(64),
  title              VARCHAR(256) NOT NULL,
  des                VARCHAR(256) NOT NULL
);

CREATE TABLE formproperty (
  id         VARCHAR(36) NOT NULL PRIMARY KEY,
  type       VARCHAR(30) NOT NULL,
  value      VARCHAR(256),
  form       VARCHAR(36),
  resultshow BIT(1) DEFAULT 1,
  name       VARCHAR(64) NOT NULL,
  des        VARCHAR(256)
);


CREATE TABLE formvalue (
  id               VARCHAR(36) NOT NULL PRIMARY KEY,
  value            TEXT,
  author           VARCHAR(36) NOT NULL,
  creattime        DATETIME    NOT NULL,
  lastmodifytime   DATETIME,
  submitip         VARCHAR(64),
  browser          VARCHAR(36),
  os               VARCHAR(36),
  lastmodifyperson VARCHAR(36),
  form             VARCHAR(36) NOT NULL

);

CREATE TABLE user (
  id       VARCHAR(36) NOT NULL PRIMARY KEY,
  username VARCHAR(36) NOT NULL,
  password VARCHAR(36) NOT NULL,
  tel      VARCHAR(11),
  email    VARCHAR(64),
  role     VARCHAR(36) NOT NULL DEFAULT 'ROLE_USER'
);


CREATE TABLE mailrecord (
  id         VARCHAR(36) NOT NULL PRIMARY KEY,
  createtime DATETIME    NOT NULL,
  email      VARCHAR(64) NOT NULL,
  token      VARCHAR(64) NOT NULL,
  invalid    BIT(1) DEFAULT 0
);

CREATE TABLE formlog (
  id        VARCHAR(36) NOT NULL PRIMARY KEY,
  form      VARCHAR(36) NOT NULL,
  user      VARCHAR(64) NOT NULL,
  opttime   DATETIME,
  operation VARCHAR(256)
);