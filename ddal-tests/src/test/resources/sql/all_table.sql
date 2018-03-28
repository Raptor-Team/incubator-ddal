create schema if not exists ddal_test_0;
create schema if not exists ddal_test_1;
create schema if not exists ddal_test_2;
create schema if not exists ddal_test_3;

DROP TABLE IF EXISTS `ddal_test_0`.`teacher`;
DROP TABLE IF EXISTS `ddal_test_1`.`teacher`;
DROP TABLE IF EXISTS `ddal_test_2`.`teacher`;
DROP TABLE IF EXISTS `ddal_test_3`.`teacher`;
CREATE TABLE `ddal_test_0`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `age` int(2) COMMENT '年龄', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) )  COMMENT='教师表';
CREATE TABLE `ddal_test_1`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `age` int(2) COMMENT '年龄', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) )  COMMENT='教师表';
CREATE TABLE `ddal_test_2`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `age` int(2) COMMENT '年龄', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) )  COMMENT='教师表';
CREATE TABLE `ddal_test_3`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `age` int(2) COMMENT '年龄', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) )  COMMENT='教师表';

DROP TABLE IF EXISTS `ddal_test_0`.`student`;
DROP TABLE IF EXISTS `ddal_test_1`.`student`;
DROP TABLE IF EXISTS `ddal_test_2`.`student`;
DROP TABLE IF EXISTS `ddal_test_3`.`student`;
CREATE TABLE `ddal_test_0`.`student` ( `sno` bigint(15) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) )  COMMENT='学生表';
CREATE TABLE `ddal_test_1`.`student` ( `sno` bigint(15) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) )  COMMENT='学生表';
CREATE TABLE `ddal_test_2`.`student` ( `sno` bigint(15) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) )  COMMENT='学生表';
CREATE TABLE `ddal_test_3`.`student` ( `sno` bigint(15) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) )  COMMENT='学生表';

DROP TABLE IF EXISTS `ddal_test_0`.`scores`;
DROP TABLE IF EXISTS `ddal_test_1`.`scores`;
DROP TABLE IF EXISTS `ddal_test_2`.`scores`;
DROP TABLE IF EXISTS `ddal_test_3`.`scores`;
CREATE TABLE `ddal_test_0`.`scores` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';
CREATE TABLE `ddal_test_1`.`scores` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';
CREATE TABLE `ddal_test_2`.`scores` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';
CREATE TABLE `ddal_test_3`.`scores` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';

DROP TABLE IF EXISTS `ddal_test_0`.`_scores_cno`;
DROP TABLE IF EXISTS `ddal_test_1`.`_scores_cno`;
DROP TABLE IF EXISTS `ddal_test_2`.`_scores_cno`;
DROP TABLE IF EXISTS `ddal_test_3`.`_scores_cno`;
CREATE TABLE `ddal_test_0`.`_scores_cno` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';
CREATE TABLE `ddal_test_1`.`_scores_cno` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';
CREATE TABLE `ddal_test_2`.`_scores_cno` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';
CREATE TABLE `ddal_test_3`.`_scores_cno` ( `scorceno` bigint(12) UNSIGNED not null COMMENT '成绩表', `sno` bigint(12) COMMENT '课程编号', `cno` int(6) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scorceno`) )  COMMENT = '成绩表';


DROP TABLE IF EXISTS `ddal_test_0`.`course`;
DROP TABLE IF EXISTS `ddal_test_1`.`course`;
DROP TABLE IF EXISTS `ddal_test_2`.`course`;
DROP TABLE IF EXISTS `ddal_test_3`.`course`;
CREATE TABLE `ddal_test_0`.`course`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_1`.`course`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_2`.`course`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_3`.`course`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';


DROP TABLE IF EXISTS `ddal_test_0`.`sign_records_0`;
DROP TABLE IF EXISTS `ddal_test_0`.`sign_records_1`;
CREATE TABLE `ddal_test_0`.`sign_records_0` (`sign_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT, `sno` bigint(20) DEFAULT NULL, `position` int(11) DEFAULT NULL, `sign_time` datetime DEFAULT NULL, PRIMARY KEY (`sign_id`));
CREATE TABLE `ddal_test_0`.`sign_records_1` (`sign_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT, `sno` bigint(20) DEFAULT NULL, `position` int(11) DEFAULT NULL, `sign_time` datetime DEFAULT NULL, PRIMARY KEY (`sign_id`));

DROP TABLE IF EXISTS ddal_test_0.sign_position_spec;
CREATE TABLE ddal_test_0.`sign_position_spec` (`position_id` int(11) unsigned NOT NULL AUTO_INCREMENT, `position_name` varchar(128) DEFAULT NULL, PRIMARY KEY (`position_id`));

DROP TABLE IF EXISTS ddal_test_0.multi_shard_table;
DROP TABLE IF EXISTS ddal_test_1.multi_shard_table;
DROP TABLE IF EXISTS ddal_test_2.multi_shard_table;
DROP TABLE IF EXISTS ddal_test_3.multi_shard_table;
CREATE TABLE ddal_test_0.`multi_shard_table` (`primary_key` int(6) UNSIGNED not null, `second_key` int(6) UNSIGNED not null, `desc` varchar(20) DEFAULT NULL, PRIMARY KEY (`primary_key`, `second_key`));
CREATE TABLE ddal_test_1.`multi_shard_table` (`primary_key` int(6) UNSIGNED not null, `second_key` int(6) UNSIGNED not null, `desc` varchar(20) DEFAULT NULL, PRIMARY KEY (`primary_key`, `second_key`));
CREATE TABLE ddal_test_2.`multi_shard_table` (`primary_key` int(6) UNSIGNED not null, `second_key` int(6) UNSIGNED not null, `desc` varchar(20) DEFAULT NULL, PRIMARY KEY (`primary_key`, `second_key`));
CREATE TABLE ddal_test_3.`multi_shard_table` (`primary_key` int(6) UNSIGNED not null, `second_key` int(6) UNSIGNED not null, `desc` varchar(20) DEFAULT NULL, PRIMARY KEY (`primary_key`, `second_key`));

DROP TABLE IF EXISTS ddal_test_0.auto_increment;
DROP TABLE IF EXISTS ddal_test_1.auto_increment;
DROP TABLE IF EXISTS ddal_test_2.auto_increment;
DROP TABLE IF EXISTS ddal_test_3.auto_increment;
CREATE TABLE ddal_test_0.`auto_increment` (`auto_key` int(20) UNSIGNED not null, `id` int(6) UNSIGNED not null, `name` varchar(20) DEFAULT NULL, PRIMARY KEY (`auto_key`));
CREATE TABLE ddal_test_1.`auto_increment` (`auto_key` int(20) UNSIGNED not null, `id` int(6) UNSIGNED not null, `name` varchar(20) DEFAULT NULL, PRIMARY KEY (`auto_key`));
CREATE TABLE ddal_test_2.`auto_increment` (`auto_key` int(20) UNSIGNED not null, `id` int(6) UNSIGNED not null, `name` varchar(20) DEFAULT NULL, PRIMARY KEY (`auto_key`));
CREATE TABLE ddal_test_3.`auto_increment` (`auto_key` int(20) UNSIGNED not null, `id` int(6) UNSIGNED not null, `name` varchar(20) DEFAULT NULL, PRIMARY KEY (`auto_key`));

DROP TABLE IF EXISTS `ddal_test_0`.`wildcard_test`;
DROP TABLE IF EXISTS `ddal_test_1`.`wildcard_test`;
DROP TABLE IF EXISTS `ddal_test_2`.`wildcard_test`;
DROP TABLE IF EXISTS `ddal_test_3`.`wildcard_test`;
CREATE TABLE `ddal_test_0`.`wildcard_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_1`.`wildcard_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_2`.`wildcard_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_3`.`wildcard_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';

DROP TABLE IF EXISTS `ddal_test_0`.`test_wild`;
DROP TABLE IF EXISTS `ddal_test_1`.`test_wild`;
DROP TABLE IF EXISTS `ddal_test_2`.`test_wild`;
DROP TABLE IF EXISTS `ddal_test_3`.`test_wild`;
CREATE TABLE `ddal_test_0`.`test_wild`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_1`.`test_wild`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_2`.`test_wild`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_3`.`test_wild`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';

DROP TABLE IF EXISTS `ddal_test_0`.`wild_test`;
DROP TABLE IF EXISTS `ddal_test_1`.`wild_test`;
DROP TABLE IF EXISTS `ddal_test_2`.`wild_test`;
DROP TABLE IF EXISTS `ddal_test_3`.`wild_test`;
CREATE TABLE `ddal_test_0`.`wild_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_1`.`wild_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_2`.`wild_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';
CREATE TABLE `ddal_test_3`.`wild_test`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';


DROP TABLE IF EXISTS `ddal_test_2`.`single_table`;
CREATE TABLE `ddal_test_2`.`single_table`( `cno` int(6) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) )  COMMENT = '课程表';


DROP TABLE IF EXISTS `ddal_test_0`.`table_shard_test_0`;
DROP TABLE IF EXISTS `ddal_test_0`.`table_shard_test_1`;
CREATE TABLE `ddal_test_0`.`table_shard_test_0`( `id` int(6) UNSIGNED not null, `code` varchar(20), primary key (`id`) )  COMMENT = '库内分表';
CREATE TABLE `ddal_test_0`.`table_shard_test_1`( `id` int(6) UNSIGNED not null, `code` varchar(20), primary key (`id`) )  COMMENT = '库内分表';
DROP TABLE IF EXISTS `ddal_test_1`.`table_shard_test_0`;
DROP TABLE IF EXISTS `ddal_test_1`.`table_shard_test_1`;
CREATE TABLE `ddal_test_1`.`table_shard_test_0`( `id` int(6) UNSIGNED not null, `code` varchar(20), primary key (`id`) )  COMMENT = '库内分表';
CREATE TABLE `ddal_test_1`.`table_shard_test_1`( `id` int(6) UNSIGNED not null, `code` varchar(20), primary key (`id`) )  COMMENT = '库内分表';


DROP TABLE IF EXISTS `ddal_test_0`.`substring_test`;
DROP TABLE IF EXISTS `ddal_test_1`.`substring_test`;
CREATE TABLE `ddal_test_0`.`substring_test`( `id` varchar(16) not null, `code` varchar(20), primary key (`id`) );
CREATE TABLE `ddal_test_1`.`substring_test`( `id` varchar(16) not null, `code` varchar(20), primary key (`id`) );

DROP TABLE IF EXISTS `ddal_test_0`.`time_test`;
DROP TABLE IF EXISTS `ddal_test_1`.`time_test`;
CREATE TABLE `ddal_test_0`.`time_test`( `id` varchar(16) not null, `time_string` varchar(20), primary key (`id`) );
CREATE TABLE `ddal_test_1`.`time_test`( `id` varchar(16) not null, `time_string` varchar(20), primary key (`id`) );

DROP TABLE IF EXISTS `ddal_test_0`.`aglori_test`;
DROP TABLE IF EXISTS `ddal_test_1`.`aglori_test`;
CREATE TABLE `ddal_test_0`.`aglori_test`( `id` varchar(16) not null, `name` varchar(20), primary key (`id`) );
CREATE TABLE `ddal_test_1`.`aglori_test`( `id` varchar(16) not null, `name` varchar(20), primary key (`id`) );
