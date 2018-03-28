create schema if not exists ddal_test_0;
create schema if not exists ddal_test_1;
create schema if not exists ddal_test_2;
create schema if not exists ddal_test_3;

DROP TABLE IF EXISTS `ddal_test_0`.`teacher`;
DROP TABLE IF EXISTS `ddal_test_1`.`teacher`;
DROP TABLE IF EXISTS `ddal_test_2`.`teacher`;
DROP TABLE IF EXISTS `ddal_test_3`.`teacher`;
CREATE TABLE `ddal_test_0`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) ) DEFAULT CHARSET=utf8 COMMENT='教师表';
CREATE TABLE `ddal_test_1`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) ) DEFAULT CHARSET=utf8 COMMENT='教师表';
CREATE TABLE `ddal_test_2`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) ) DEFAULT CHARSET=utf8 COMMENT='教师表';
CREATE TABLE `ddal_test_3`.`teacher` ( `tno` bigint(20) UNSIGNED NOT NULL COMMENT '教师工号', `tname` varchar(20) COMMENT '教师姓名', `sex` char(2) COMMENT '性别', `tphone` varchar(20) COMMENT '联系电话', PRIMARY KEY (`tno`) ) DEFAULT CHARSET=utf8 COMMENT='教师表';

DROP TABLE IF EXISTS `ddal_test_0`.`student`;
DROP TABLE IF EXISTS `ddal_test_1`.`student`;
DROP TABLE IF EXISTS `ddal_test_2`.`student`;
DROP TABLE IF EXISTS `ddal_test_3`.`student`;
CREATE TABLE `ddal_test_0`.`student` ( `sno` bigint(20) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) ) DEFAULT CHARSET=utf8 COMMENT='学生表';
CREATE TABLE `ddal_test_1`.`student` ( `sno` bigint(20) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) ) DEFAULT CHARSET=utf8 COMMENT='学生表';
CREATE TABLE `ddal_test_2`.`student` ( `sno` bigint(20) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) ) DEFAULT CHARSET=utf8 COMMENT='学生表';
CREATE TABLE `ddal_test_3`.`student` ( `sno` bigint(20) UNSIGNED NOT NULL COMMENT '学号', `sname` varchar(20) COMMENT '学生姓名', `sex` char(2) COMMENT '性别', `sdept` varchar(12) COMMENT '专业', `sphone` varchar(11) COMMENT '联系电话', `birthday` varchar(15) COMMENT '生日', PRIMARY KEY (`sno`) ) DEFAULT CHARSET=utf8 COMMENT='学生表';

DROP TABLE IF EXISTS `ddal_test_0`.`scores`;
DROP TABLE IF EXISTS `ddal_test_1`.`scores`;
DROP TABLE IF EXISTS `ddal_test_2`.`scores`;
DROP TABLE IF EXISTS `ddal_test_3`.`scores`;
CREATE TABLE `ddal_test_0`.`scores` ( `scoreno` bigint(20) UNSIGNED not null COMMENT '成绩表', `sno` bigint(20) COMMENT '课程编号', `cno` int(20) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scoreno`) ) DEFAULT CHARSET=utf8 COMMENT = '成绩表';
CREATE TABLE `ddal_test_1`.`scores` ( `scoreno` bigint(20) UNSIGNED not null COMMENT '成绩表', `sno` bigint(20) COMMENT '课程编号', `cno` int(20) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scoreno`) ) DEFAULT CHARSET=utf8 COMMENT = '成绩表';
CREATE TABLE `ddal_test_2`.`scores` ( `scoreno` bigint(20) UNSIGNED not null COMMENT '成绩表', `sno` bigint(20) COMMENT '课程编号', `cno` int(20) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scoreno`) ) DEFAULT CHARSET=utf8 COMMENT = '成绩表';
CREATE TABLE `ddal_test_3`.`scores` ( `scoreno` bigint(20) UNSIGNED not null COMMENT '成绩表', `sno` bigint(20) COMMENT '课程编号', `cno` int(20) COMMENT '学生学号', `term` varchar(20) COMMENT '学期', `grade` int(2) NOT NULL COMMENT '课程得分', primary key (`scoreno`) ) DEFAULT CHARSET=utf8 COMMENT = '成绩表';

DROP TABLE IF EXISTS `ddal_test_0`.`course`;
DROP TABLE IF EXISTS `ddal_test_1`.`course`;
DROP TABLE IF EXISTS `ddal_test_2`.`course`;
DROP TABLE IF EXISTS `ddal_test_3`.`course`;
CREATE TABLE `ddal_test_0`.`course`( `cno` int(20) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) ) DEFAULT CHARSET=utf8 COMMENT = '课程表';
CREATE TABLE `ddal_test_1`.`course`( `cno` int(20) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) ) DEFAULT CHARSET=utf8 COMMENT = '课程表';
CREATE TABLE `ddal_test_2`.`course`( `cno` int(20) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) ) DEFAULT CHARSET=utf8 COMMENT = '课程表';
CREATE TABLE `ddal_test_3`.`course`( `cno` int(20) UNSIGNED not null COMMENT '课程编号', `cname` varchar(20) NOT NULL COMMENT '课程名称', `tno` varchar(20) COMMENT '教师工号', primary key (`cno`) ) DEFAULT CHARSET=utf8 COMMENT = '课程表';