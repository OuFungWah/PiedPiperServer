create table user (
  member_id int(11) not null unique auto_increment,
  account_id varchar(64) not null unique primary key ,
  nickname varchar(64),
  password varchar(64) not null ,
  gender int(2),
  signature varchar(225),
  address varchar(225),
  register_time date,
  birthday date,
  token varchar(225)
);

insert into user(account_id,nickname,password,register_time) values ('oufenhghua','区枫华','123456','2019-01-22');

select * from user;

drop table user;