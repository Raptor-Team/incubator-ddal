create schema if not exists schema0;
create schema if not exists schema1;

drop table if exists schema0.customer;
create table if not exists schema0.`customer` ( `id` int(11) unsigned not null, `name` varchar(32) default null, `gender` int(1) default null, `create_date` datetime default null, primary key (`id`) ) engine=innodb default charset=utf8;

drop table if exists schema1.customer;
create table if not exists schema1.`customer` ( `id` int(11) unsigned not null, `name` varchar(32) default null, `gender` int(1) default null, `create_date` datetime default null, primary key (`id`) ) engine=innodb default charset=utf8;

drop table if exists schema0.area;
create table if not exists schema0.`area` ( `id` int(11) unsigned not null, `name` varchar(32) default null, primary key (`id`) ) engine=innodb default charset=utf8;

drop table if exists schema1.area;
create table if not exists schema1.`area` ( `id` int(11) unsigned not null, `name` varchar(32) default null, primary key (`id`) ) engine=innodb default charset=utf8;

insert into schema0.`customer` (`id`, `name`, `gender`, `create_date`) values (2, '萧景琰', 1, '2016-11-01 11:01:00');
insert into schema0.`customer` (`id`, `name`, `gender`, `create_date`) values (4, '誉王', 1, '2016-11-01 11:01:00');
insert into schema0.`customer` (`id`, `name`, `gender`, `create_date`) values (6, '夏冬', 1, '2016-11-01 11:01:00');
insert into schema0.`customer` (`id`, `name`, `gender`, `create_date`) values (8, '悬镜司', 1, '2016-11-01 11:01:00');

insert into schema1.`customer` (`id`, `name`, `gender`, `create_date`) values (1, '陈大发', 1, '2016-11-01 11:01:00');
insert into schema1.`customer` (`id`, `name`, `gender`, `create_date`) values (3, '胡歌', 1, '2016-11-01 11:01:00');
insert into schema1.`customer` (`id`, `name`, `gender`, `create_date`) values (5, '江左梅郎', 1, '2016-11-01 11:01:00');
insert into schema1.`customer` (`id`, `name`, `gender`, `create_date`) values (7, '飞流', 1, '2016-11-01 11:01:00');