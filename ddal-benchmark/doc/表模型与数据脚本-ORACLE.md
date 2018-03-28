### 表模型与数据脚本

用户

````sql
CREATE USER ddal_test IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test;
GRANT ALL PRIVILEGES TO ddal_test;
GRANT UNLIMITED TABLESPACE TO ddal_test;

-- 分片0
CREATE USER ddal_test_0 IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test_0;
GRANT ALL PRIVILEGES TO ddal_test_0;
GRANT UNLIMITED TABLESPACE TO ddal_test_0;

-- 分片1
CREATE USER ddal_test_1 IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test_1;
GRANT ALL PRIVILEGES TO ddal_test_1;
GRANT UNLIMITED TABLESPACE TO ddal_test_1;

-- 分片2
CREATE USER ddal_test_2 IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test_2;
GRANT ALL PRIVILEGES TO ddal_test_2;
GRANT UNLIMITED TABLESPACE TO ddal_test_2;
-- 分片3
CREATE USER ddal_test_3 IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test_3;
GRANT ALL PRIVILEGES TO ddal_test_3;
GRANT UNLIMITED TABLESPACE TO ddal_test_3;
-- 分片4
CREATE USER ddal_test_4 IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test_4;
GRANT ALL PRIVILEGES TO ddal_test_4;
GRANT UNLIMITED TABLESPACE TO ddal_test_4;

-- 分片超级用户
CREATE USER ddal_test_root IDENTIFIED BY ddal_test123;
GRANT CONNECT, RESOURCE TO ddal_test_root;
GRANT ALL PRIVILEGES TO ddal_test_root;
GRANT UNLIMITED TABLESPACE TO ddal_test_root;
````

表模型

````sql
-- TEACHER
CREATE TABLE "TEACHER" 
(	
 "TNO" NUMBER(20,0) NOT NULL ENABLE, 
 "TNAME" VARCHAR2(20 BYTE), 
 "SEX" CHAR(2 BYTE), 
 "AGE" NUMBER(2,0), 
 "TPHONE" VARCHAR2(20 BYTE), 
 CONSTRAINT "TEACHER_PK" PRIMARY KEY ("TNO")
);
-- STUDENT
CREATE TABLE "STUDENT" 
(	
 "SNO" NUMBER(20,0) NOT NULL ENABLE, 
 "SNAME" VARCHAR2(20 BYTE), 
 "SEX" CHAR(2 BYTE), 
 "SDEPT" VARCHAR2(12 BYTE), 
 "SPHONE" VARCHAR2(11 BYTE), 
 "BIRTHDAY" VARCHAR2(15 BYTE), 
 CONSTRAINT "STUDENT_PK" PRIMARY KEY ("SNO")
);
-- Scores
CREATE TABLE "SCORES" 
(	
 "SCORENO" NUMBER(20,0) NOT NULL ENABLE, 
 "SNO" NUMBER(20,0), 
 "CNO" NUMBER(6,0), 
 "TERM" VARCHAR2(20 BYTE), 
 "GRADE" NUMBER(2,0), 
 CONSTRAINT "SCORES_PK" PRIMARY KEY ("SCORENO")
);
-- Course
CREATE TABLE "COURSE" 
(	
 "CNO" NUMBER(6,0) NOT NULL ENABLE, 
 "CNAME" VARCHAR2(20 BYTE), 
 "TNO" NUMBER(20,0), 
 CONSTRAINT "COURSE_PK" PRIMARY KEY ("CNO")
);

--Doctor
DROP TABLE IF EXISTS "DOCTOR";
CREATE TABLE "DOCTOR" 
(	
 "DNO" NUMBER(6,0) NOT NULL ENABLE, 
 "DNAME" VARCHAR2(20 BYTE), 
 "DEPARTMENT" VARCHAR2(20 BYTE), 
 "SEX" CHAR(2 BYTE), 
 "AGE" NUMBER(2,0), 
 CONSTRAINT "DOCTOR_PK" PRIMARY KEY ("DNO")
);
````

#### 分库分表

表数据

````sql
-- teacher
Insert into DDAL_TEST.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1000,'Stephon','M ',30,'15889001158');
Insert into DDAL_TEST.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1001,'Simon','FM',10,'15889001194');
Insert into DDAL_TEST.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1002,'Jackie','M ',17,'15889001165');
Insert into DDAL_TEST.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1003,'Abell','FM',32,'15889001197');
Insert into DDAL_TEST.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1004,'Page','M ',37,'15889001118');

-- course
Insert into DDAL_TEST.COURSE (CNO,CNAME,TNO) values (300001,'Aesthetics',1000);
Insert into DDAL_TEST.COURSE (CNO,CNAME,TNO) values (300002,'Western Economics',1001);
Insert into DDAL_TEST.COURSE (CNO,CNAME,TNO) values (300003,'Optics',1002);
Insert into DDAL_TEST.COURSE (CNO,CNAME,TNO) values (300004,'Geology',1003);
Insert into DDAL_TEST.COURSE (CNO,CNAME,TNO) values (300005,'Ecology',1004);

-- student
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2000,'Nash','M ','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2001,'Marlin','FM','Social','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2002,'Eddie','M ','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2003,'Edward','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2004,'Baker','FM','Social','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2005,'Paul','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2006,'Phil','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2007,'Platt','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2008,'Manley','FM','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2009,'Mark','M ','Computer','18908772991','1989-08-24');

-- scores
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40000, 2000, 300001, 'Term One', 90);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40001, 2000, 300002, 'Term One', 82);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40003, 2000, 300003, 'Term One', 95);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40004, 2001, 300004, 'Term One', 77);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40005, 2001, 300001, 'Term Two', 91);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40006, 2002, 300002, 'Term One', 87);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40007, 2002, 300003, 'Term Two', 96);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40008, 2002, 300005, 'Term Two', 89);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40009, 2003, 300002, 'Term One', 80);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40010, 2003, 300003, 'Term Two', 92);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40011, 2003, 300005, 'Term Two', 93);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40012, 2004, 300001, 'Term One', 80);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40013, 2004, 300002, 'Term Two', 92);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40014, 2004, 300004, 'Term Two', 93);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40015, 2004, 300003, 'Term Two', 91);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40016, 2005, 300002, 'Term One', 80);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40017, 2005, 300004, 'Term Two', 92);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40018, 2005, 300001, 'Term Two', 93);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40019, 2005, 300003, 'Term Two', 91);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40020, 2006, 300002, 'Term One', 81);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40021, 2006, 300004, 'Term Two', 70);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40022, 2007, 300001, 'Term One', 83);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40023, 2007, 300003, 'Term Two', 65);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40024, 2008, 300001, 'Term One', 85);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40025, 2008, 300002, 'Term Two', 68);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40026, 2009, 300005, 'Term One', 73);
insert into ddal_test.scores(scoreno, sno, cno, term, grade) values (40027, 2009, 300003, 'Term Two', 78);

--doctor
Insert into DDAL_TEST.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (55000,'Mark','内科','M',30);
Insert into DDAL_TEST.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values  (15001,'Charley','外科','FM',10);
Insert into DDAL_TEST.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (25002,'Jack','内科','M',17);
Insert into DDAL_TEST.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (35003,'Abell','外科','Sam',32);
Insert into DDAL_TEST.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (45004,'Bruce','外科','M',37);
````

分片数据

````sql
-- clear data
truncate table DDAL_TEST_0.teacher;
truncate table DDAL_TEST_0.COURSE;
truncate table DDAL_TEST_0.STUDENT;
truncate table DDAL_TEST_0.scores;
truncate table DDAL_TEST_0.DOCTOR;
truncate table DDAL_TEST_1.teacher;
truncate table DDAL_TEST_1.COURSE;
truncate table DDAL_TEST_1.STUDENT;
truncate table DDAL_TEST_1.scores;
truncate table DDAL_TEST_1.DOCTOR;
truncate table DDAL_TEST_2.teacher;
truncate table DDAL_TEST_2.COURSE;
truncate table DDAL_TEST_2.STUDENT;
truncate table DDAL_TEST_2.scores;
truncate table DDAL_TEST_2.DOCTOR;
truncate table DDAL_TEST_3.teacher;
truncate table DDAL_TEST_3.COURSE;
truncate table DDAL_TEST_3.STUDENT;
truncate table DDAL_TEST_3.scores;
truncate table DDAL_TEST_3.DOCTOR;
truncate table DDAL_TEST_4.teacher;
truncate table DDAL_TEST_4.COURSE;
truncate table DDAL_TEST_4.STUDENT;
truncate table DDAL_TEST_4.scores;
truncate table DDAL_TEST_4.DOCTOR;

--shard 0
Insert into DDAL_TEST_0.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1000,'Stephon','M ',30,'15889001158');
Insert into DDAL_TEST_0.COURSE (CNO,CNAME,TNO) values (300001,'Aesthetics',1000);
Insert into DDAL_TEST_0.COURSE (CNO,CNAME,TNO) values (300002,'Western Economics',1001);
Insert into DDAL_TEST_0.COURSE (CNO,CNAME,TNO) values (300003,'Optics',1002);
Insert into DDAL_TEST_0.COURSE (CNO,CNAME,TNO) values (300004,'Geology',1003);
Insert into DDAL_TEST_0.COURSE (CNO,CNAME,TNO) values (300005,'Ecology',1004);
Insert into DDAL_TEST_0.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2000,'Nash','M ','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST_0.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2005,'Paul','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST_0.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (55000,'Mark','内科','M',30);
commit;


--shard 1
Insert into DDAL_TEST_1.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1001,'Simon','FM',10,'15889001194');
Insert into DDAL_TEST_1.COURSE (CNO,CNAME,TNO) values (300001,'Aesthetics',1000);
Insert into DDAL_TEST_1.COURSE (CNO,CNAME,TNO) values (300002,'Western Economics',1001);
Insert into DDAL_TEST_1.COURSE (CNO,CNAME,TNO) values (300003,'Optics',1002);
Insert into DDAL_TEST_1.COURSE (CNO,CNAME,TNO) values (300004,'Geology',1003);
Insert into DDAL_TEST_1.COURSE (CNO,CNAME,TNO) values (300005,'Ecology',1004);
Insert into DDAL_TEST_1.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2001,'Marlin','FM','Social','18908772991','1989-08-24');
Insert into DDAL_TEST_1.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2006,'Phil','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST_1.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values  (15001,'Charley','外科','FM',10);
commit;


--shard 2
Insert into DDAL_TEST_2.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1002,'Jackie','M ',17,'15889001165');
Insert into DDAL_TEST_2.COURSE (CNO,CNAME,TNO) values (300001,'Aesthetics',1000);
Insert into DDAL_TEST_2.COURSE (CNO,CNAME,TNO) values (300002,'Western Economics',1001);
Insert into DDAL_TEST_2.COURSE (CNO,CNAME,TNO) values (300003,'Optics',1002);
Insert into DDAL_TEST_2.COURSE (CNO,CNAME,TNO) values (300004,'Geology',1003);
Insert into DDAL_TEST_2.COURSE (CNO,CNAME,TNO) values (300005,'Ecology',1004);
Insert into DDAL_TEST_2.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2002,'Eddie','M ','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST_2.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2007,'Platt','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST_1.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (25002,'Jack','内科','M',17);
commit;

--shard 3
Insert into DDAL_TEST_3.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1003,'Abell','FM',32,'15889001197');
Insert into DDAL_TEST_3.COURSE (CNO,CNAME,TNO) values (300001,'Aesthetics',1000);
Insert into DDAL_TEST_3.COURSE (CNO,CNAME,TNO) values (300002,'Western Economics',1001);
Insert into DDAL_TEST_3.COURSE (CNO,CNAME,TNO) values (300003,'Optics',1002);
Insert into DDAL_TEST_3.COURSE (CNO,CNAME,TNO) values (300004,'Geology',1003);
Insert into DDAL_TEST_3.COURSE (CNO,CNAME,TNO) values (300005,'Ecology',1004);
Insert into DDAL_TEST_3.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2003,'Edward','M ','Social','18908772991','1989-08-24');
Insert into DDAL_TEST_3.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2008,'Manley','FM','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST_3.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (35003,'Abell','外科','Sam',22);
commit;

--shard 4
Insert into DDAL_TEST_4.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (1004,'Page','M ',37,'15889001118');
Insert into DDAL_TEST_4.COURSE (CNO,CNAME,TNO) values (300001,'Aesthetics',1000);
Insert into DDAL_TEST_4.COURSE (CNO,CNAME,TNO) values (300002,'Western Economics',1001);
Insert into DDAL_TEST_4.COURSE (CNO,CNAME,TNO) values (300003,'Optics',1002);
Insert into DDAL_TEST_4.COURSE (CNO,CNAME,TNO) values (300004,'Geology',1003);
Insert into DDAL_TEST_4.COURSE (CNO,CNAME,TNO) values (300005,'Ecology',1004);
Insert into DDAL_TEST_4.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2004,'Baker','FM','Social','18908772991','1989-08-24');
Insert into DDAL_TEST_4.STUDENT (SNO,SNAME,SEX,SDEPT,SPHONE,BIRTHDAY) values (2009,'Mark','M ','Computer','18908772991','1989-08-24');
Insert into DDAL_TEST_4.DOCTOR (DNO,DNAME,DEPARTMENT,SEX,AGE) values (45004,'Bruce','外科','M',37);
commit;

--scores
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40000, 2000, 300001, 'Term One', 90);
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40001, 2000, 300002, 'Term One', 82);
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40003, 2000, 300003, 'Term One', 95);
insert into ddal_test_1.scores(scoreno, sno, cno, term, grade) values (40004, 2001, 300004, 'Term One', 77);
insert into ddal_test_1.scores(scoreno, sno, cno, term, grade) values (40005, 2001, 300001, 'Term Two', 91);
insert into ddal_test_2.scores(scoreno, sno, cno, term, grade) values (40006, 2002, 300002, 'Term One', 87);
insert into ddal_test_2.scores(scoreno, sno, cno, term, grade) values (40007, 2002, 300003, 'Term Two', 96);
insert into ddal_test_2.scores(scoreno, sno, cno, term, grade) values (40008, 2002, 300005, 'Term Two', 89);
insert into ddal_test_3.scores(scoreno, sno, cno, term, grade) values (40009, 2003, 300002, 'Term One', 80);
insert into ddal_test_3.scores(scoreno, sno, cno, term, grade) values (40010, 2003, 300003, 'Term Two', 92);
insert into ddal_test_3.scores(scoreno, sno, cno, term, grade) values (40011, 2003, 300005, 'Term Two', 93);
insert into ddal_test_4.scores(scoreno, sno, cno, term, grade) values (40012, 2004, 300001, 'Term One', 80);
insert into ddal_test_4.scores(scoreno, sno, cno, term, grade) values (40013, 2004, 300002, 'Term Two', 92);
insert into ddal_test_4.scores(scoreno, sno, cno, term, grade) values (40014, 2004, 300004, 'Term Two', 93);
insert into ddal_test_4.scores(scoreno, sno, cno, term, grade) values (40015, 2004, 300003, 'Term Two', 91);
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40016, 2005, 300002, 'Term One', 80);
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40017, 2005, 300004, 'Term Two', 92);
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40018, 2005, 300001, 'Term Two', 93);
insert into ddal_test_0.scores(scoreno, sno, cno, term, grade) values (40019, 2005, 300003, 'Term Two', 91);
insert into ddal_test_1.scores(scoreno, sno, cno, term, grade) values (40020, 2006, 300002, 'Term One', 81);
insert into ddal_test_1.scores(scoreno, sno, cno, term, grade) values (40021, 2006, 300004, 'Term Two', 70);
insert into ddal_test_2.scores(scoreno, sno, cno, term, grade) values (40022, 2007, 300001, 'Term One', 83);
insert into ddal_test_2.scores(scoreno, sno, cno, term, grade) values (40023, 2007, 300003, 'Term Two', 65);
insert into ddal_test_3.scores(scoreno, sno, cno, term, grade) values (40024, 2008, 300001, 'Term One', 85);
insert into ddal_test_3.scores(scoreno, sno, cno, term, grade) values (40025, 2008, 300002, 'Term Two', 68);
insert into ddal_test_4.scores(scoreno, sno, cno, term, grade) values (40026, 2009, 300005, 'Term One', 73);
insert into ddal_test_4.scores(scoreno, sno, cno, term, grade) values (40027, 2009, 300003, 'Term Two', 78);
commit;
````



