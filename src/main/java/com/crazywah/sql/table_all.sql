create table user (
  member_id int(11) not null unique auto_increment,
  account_id varchar(64) not null unique primary key ,
  nickname varchar(64),
  password varchar(64) not null ,
  gender int(2),
  signature varchar(225),
  address varchar(225),
  email varchar(225),
  mobile varchar(13),
  register_time date,
  birthday date,
  token varchar(225)
);

create table friendship (
  origin_id varchar(64) not null,
  taget_id varchar(64) not null,
  relation int(11),
  alias varchar(64),
  remark varchar(225),
  friend_time datetime,
  request_time datetime,
  request_message varchar(225)
);