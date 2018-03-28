DDAL的数据源组包含一个写库数据源和多个读库数据源。一般情况下，DDAL会用读写数据源执行SQL，但若想使用只读数据源的话，可以使用「只读注解」来标记SQL。

### 只读注解语法

只读注解有两种使用方法：

- readonly 路由至读库执行，使用DDAL默认的读库负载。
- readonly(seq) 路由至指定读库，seq指向DataSourceGroup下某一个具体的读库。

只读注解样例：

````sql
-- ddal根据负载策略选择读库
/*!hint readonly */ select * from t_order;
-- 路由至序号为1的读库
/*!hint readonly(1) */ select * from t_order;
````

### 只读数据源的序号配置

以下是DDAL工程单元测试使用的数据源配置

````xml
<dataSources>
  <group name="group_1" relaCluster="cluster1">
    <dataSource user="root" dbInstName="inst_w_1" dbDriver="jdbc" pwd="root">
      <params>
        <property name="minIdle" value="1"/>
        <property name="maxIdle" value="5"/>
        <property name="maxTotal" value="10"/>
      </params>
    </dataSource>
    <dataSource user="root" dbInstName="inst_r_1" dbDriver="jdbc" pwd="root" seq="1">
      <params>
        <property name="minIdle" value="1"/>
        <property name="maxIdle" value="5"/>
        <property name="maxTotal" value="10"/>
      </params>
    </dataSource>
    <dataSource user="root" dbInstName="inst_r_3" dbDriver="jdbc" pwd="root" seq="3">
      <params>
        <property name="minIdle" value="1"/>
        <property name="maxIdle" value="5"/>
        <property name="maxTotal" value="10"/>
      </params>
    </dataSource>
    <dataSource user="root" dbInstName="inst_r_2" dbDriver="jdbc" pwd="root" seq="2">
      <params>
        <property name="minIdle" value="1"/>
        <property name="maxIdle" value="5"/>
        <property name="maxTotal" value="10"/>
      </params>
    </dataSource>
  </group>
</dataSources>
````

### 注意事项

1. 如果注解指定的读库序号不存在，SQL将无法执行，DDAL会抛出只读连接获取异常。
2. 不是所有带了只读注解的SQL都会被强制在读库执行。带只读注解SQL被执行之前，与其对应的分片已存在事务，那么此只读SQL的注解将会被忽略，使用上下文中的读写连接执行。



