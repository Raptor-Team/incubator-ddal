### 隐藏分页

Oracle分页与MySQL在写法上有很大的区别，Oracle不支持MySQL的分页关键字LIMIT。SQL标准也没有对分页有明确的定义。所以我们指定了通过SQL注释传递分页参数进行分页。

分页线索提供两个参数：offset和count。

- offset 表示第一条数据相对表头的偏移量。offset=0时，返回结果从第一条数据开始查询。offset=1时从第二条数据开始查询。
- count 表示返回结果的条数。

在Oracle的分页场景中，遵照注释的写法对原SQL语句进行改写。

假设原语句是：

````sql
select id, name from user
````

如想查询原结果的前10条，SQL语句需改写为：

````sql
/*!raptor page(offset=0,count=10)*/select id, name from user
````



### 隐藏分片

隐藏分片字段的写法跟分页类似，都是通过注释把分片字段传递给dal进行分片的计算。

假设user表的分片字段是id，那么这个表的分片有两种写法，分别是：

#### where条件带分片字段

where条件带分片字段是最常用的用法，DAL通过解析where后的条件取得分片字段进行路由。

````sql
select id, name from user where id=1
````

#### 注释带分片字段

这种注释带分片字段的写法与where带分片字段的效果是一样的。

````sql
/*!raptor shard(id=1)*/select id, name from user
````

在大多数场景下优先考虑使用where带条件的正常写法。在表没有分片字段时再考虑使用这种注释的方式。

### 多场景混合使用

隐藏分片字段与分页可以同时在注释中使用。

以历史订单查询的模型为例，如何查询某个订单(customer_order)的前10条订单项(customer_order_item)数据呢？

使用注释的SQL如下：

````sql
/*!raptor shard(order_id=400012344); page(offset=0,count=10)*/select order_item_id from order_item
````

隐藏分片和分页的注释之间用英文`;`分隔。







