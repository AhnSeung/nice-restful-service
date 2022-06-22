create table user(
    id integer not null auto_increment,
    name varchar(255),
    join_Date timestamp,
    ssn varchar(20),
    password varchar(255),
    primary key(id)
);

create table post(
    id integer not null auto_increment,
    description varchar(255),
    primary key(id)
);

insert into user(id,name,ssn,join_date,password) values(80,'dev0','99999-1111111',now(),'dev090');
insert into user(id,name,ssn,join_date,password) values(81,'dev1','99999-1111112',now(),'dev190');
insert into user(id,name,ssn,join_date,password) values(82,'dev2','99999-1111113',now(),'dev290');

insert into post(id,description) values(80,'descriptionFirst');
insert into post(id,description) values(81,'descriptionSecond');