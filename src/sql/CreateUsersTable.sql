-- CREATE DATABASE trueCare;
-- GO

-- USE trueCare
-- GO

-- DROP TABLE Users;

-- USE trueCare
-- GO


CREATE TABLE trueCare.dbo.Users
(
    id          int identity (1,1) not null,
    firstName   varchar(30)        not null,
    lastName    varchar(50)        not null,
    address     varchar(50)        not null,
    email       varchar(50)        not null unique,
    password    varchar(100)        not null,
    phoneNumber varchar(50)        not null,
    createDate datetime           not null,
    updateDate datetime           null,
    CONSTRAINT PK_User PRIMARY KEY (id)
)
go

use trueCare
insert into Users (firstName, lastName, address, email, password, phoneNumber, createDate, updateDate)
values ('Ivan', 'Zhigalev', '2713 South Alder St.', 'vangogimomo@gmail.com','1234', '+14847617991', GETDATE(), null)
go
