Raptor-ddal集成了全局序列（Raptor-gid），依赖为可选，当要使用全局序列时需引入如下依赖

```xml
<dependency>
  <groupId>studio.raptor</groupId>
  <artifactId>raptor-gid</artifactId>
  <version>4.1.0-SNAPSHOT</version>
</dependency>
```

> 目前仅提供Snapshot版本，待稳定后推出Release版本



### Raptor-ddal序列能力

Raptor-ddal有两种序列使用方式：

- 隐式使用：通过表配置自增主键序列，语句自动填充序列；
- 显式使用：通过ddal语法`select xxx.nextval;`独立获取序列，此为DDAL特有语法，Oracle从数据库获取序列语法为`select xxx.nextval from dual;`，此处要**特别注意**；该方式的前提为表上需要配置序列，才可独立获取；



### 配置说明

由于全局序列需要使用ZK集群服务，所以需要配置ZK连接等信息；

*ddal/system.properties*

```properties
#SequenceServer相关配置
#ZK集群地址，多个用;隔开
sequence.zk.address=192.168.199.24:2181

#序列在ZK上的目录，建议使用业务中心名称
sequence.zk.namespace=bp

#可选配置
#当前进程标识，如ip+port；当使用本地序列[snowflake,ticktock]时，需在${namespace}/workers下创建进程标识的节点，并赋予中心内唯一的workerId
sequence.sysId=127.0.0.1-8888
```



*ddal/shard-config.xml*

> 这里配置有两种形式，一为表关联的序列，需要加上refColumn属性；二为独立序列，与表平级；

```xml
<virtualDB name="school" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
  <!-- 表关联序列配置，此种序列必须配置refColumn属性 -->
  <table name="seq_table" databaseRule="teacher-database-rule">
    <!-- 表关联序列
         name：序列名称
         type：序列类型，分为三种，breadcrumb、snowflake、ticktock，详细使用请参阅全局序列使用文档
               -breadcrumb 属性：cache、incr、start，可不填使用默认配置cache="0" incr="1" start="0"
               -snowflake 属性：workerIdWidth、sequenceWidth，可不填使用默认配置workerIdWidth="10" sequenceWidth="12"
               -ticktock 属性：workerIdWidth、sequenceWidth，可不填使用默认配置workerIdWidth="3" sequenceWidth="4"
         -->
    <sequence name="auto_key_seq" type="breadcrumb" refColumn="auto_key"/>
  </table>
  
  
  <!-- 独立序列
           独立于任何表，即可作为表的主键，也可以在用于程序中序列值的生成
           注：不管是自增主键还是独立配置的序列，都可以通过select xxx.nextval；语法独立获取序列
      -->
  <sequence name="independent_seq" type="breadcrumb"/>
</virtualDB>
```

> 更多关于全局序列类型及使用问题，请参阅全局序列介绍文档；



### 示例代码

独立获取示例

```java
String sql = "select aid_seq.nextval";
try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
     ResultSet resultSet = preparedStatement.executeQuery()) {
  resultSet.next();
  resultSet.getLong(1);
} catch (SQLException e) {
  e.printStackTrace();
}
```



自增主键示例

```java
//注意此处SQL语句不要带入自增主键字段，由DDAL自动填充；若SQL中带有自增主键字段，则使用此值插入，但会存在主键冲突问题，请慎重处理！！！
String sql = "insert into table_a (age, name) values (?, ?)";
try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
  preparedStatement.setInt(1, 12);
  preparedStatement.setString(2, "xxx");
  int affectedRows = preparedStatement.executeUpdate();
} catch (SQLException e) {
  e.printStackTrace();
}
```
