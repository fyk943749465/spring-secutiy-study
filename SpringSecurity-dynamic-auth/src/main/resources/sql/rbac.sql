create database spring_rbac;

user spring_rbac;

create table menu (
                      id int(11) not null auto_increment,
                      pattern varchar(128) default null,
                      primary key(id)
);

insert into menu values(1, '/admin/**');
insert into menu values(2, '/user/**');
insert into menu values(3, '/guest/**');

create table menu_role(
                          id int(11) primary key auto_increment,
                          mid int(11) default null,
                          rid int(11) default null
);

insert into menu_role values(1,1,1);
insert into menu_role values(2,2,2);
insert into menu_role values(3,3,3);
insert into menu_role values(4,3,2);

create table role(
                     id int(11) primary key auto_increment,
                     name varchar(32),
                     name_zh varchar(32)
);

insert into role values (1, 'ROLE_ADMIN', '系统管理员');
insert into role values (2, 'ROLE_USER', '普通用户');
insert into role values (3, 'ROLE_GUEST', '游客');

create table user(
                     id int(11) primary key auto_increment,
                     username varchar(32),
                     password varchar(255),
                     enabled tinyint(1),
                     locked tinyint(1)
);

insert into user values(1,'admin', '{noop}123',1,0);
insert into user values(2,'user', '{noop}123',1,0);
insert into user values(3,'javaboy','{noop}123',1,0);

create table user_role(
                          id int(11) primary key auto_increment,
                          uid int(11) default null,
                          rid int(11) default null
);

insert into user_role values(1, 1, 1);
insert into user_role values(2, 1, 2);
insert into user_role values(3, 2, 2);
insert into user_role values(4, 3, 3);