-- CREATE DATABASE trueCare;
-- GO

-- USE trueCare
-- GO

-- DROP TABLE Comments;

-- USE trueCare
-- GO


CREATE TABLE trueCare.dbo.Comments
(
    id          int identity (1,1) not null,
    user_id     int        not null,
    item_id     int        not null,
    comment     varchar(MAX)       not null,
    CONSTRAINT PK_Comment PRIMARY KEY (id)
)
go

use trueCare
insert into Comments (user_id , item_id, comment)
values (10, 1, 'Very Tasty')
go
