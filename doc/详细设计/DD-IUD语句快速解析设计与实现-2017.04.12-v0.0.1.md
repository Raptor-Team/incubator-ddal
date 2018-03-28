## IUD语句快速解析设计与实现

Raptor-ddal中通用解析器实现较为复杂，对于IUD类型的简单SQL为提高解析性能，不需要经过通用解析器，而是直接对字符串进行解析获取关键路由信息，即可进入下一环节；



### 快速解析SQL场景分析

**场景罗列**

目前只对IUD语句进行快速解析，Select语句场景相对复杂，还是通过通用解析器进行解析；

![](http://git.oschina.net/uploads/images/2017/0412/104041_278cb71a_1025511.png)



**语句分析**

IUD语句有以下特点：

* IUD语句返回结果为affectedRows，类型为int；
* IUD语句不包含聚合等函数操作；
* IUD语句不能包含子句；
* Insert语句只能支持`insert into Table (column1, column2 ...) values (value1, value2 ...)`这种类型；
* Update、Delete语句必须要带条件；




### 快速解析器设计


