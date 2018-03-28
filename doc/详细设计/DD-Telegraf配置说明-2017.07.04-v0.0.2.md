表名定义遵守 **业务中心_measurement** 命名方式。

表数据库必须字段：time, host

查询语句：SELECT * FROM centername_cpu WHERE time > now() - 1h order by time desc limit 10

### Agent

1. hostname = "192.168.199.23"，hostname改为应用程序主机IP。由于采集器需要采集机器的性能数据，所以一般跟应用程序部署在一起。
2. ​

### 时序数据库

修改[[outputs.influxdb]]配置块

````shell
urls = ["http://192.168.199.23:8086"] # required
database = "ddal" # required
````

urls支持配置多个时序数据库。database属性指定influxdb数据库，数据库需在influxdb中提前创建好。

#### CPU

插件名称：[[inputs.cpu]]

1. 关闭cpu的percpu选项，只统计totalcpu。

   ````shell
   ## Whether to report per-cpu stats or not
   percpu = false
   ## Whether to report total system cpu stats or not
   totalcpu = true
   ````

2. 设置cpu统计的字段

   只统计以下四个cpu参数：

   ````shell
   fieldpass = ["usage_idle", "usage_iowait", "usage_system", "usage_user"]
   ````

3. 使用name_override自定义cpu数据表名

   ````shell
   name_override = "centername_cpu"
   ````

   命名规则：中心名称_measurement。例如：客户中心cpu度量表名定义为customer_cpu

**CPU数据示例**：

查询语句：SELECT * FROM centername_cpu WHERE time > now() - 1h order by time desc limit 10

![](https://ws4.sinaimg.cn/large/006tNc79gy1fgtzuzzehhj30vy0bnadh.jpg)

### JVM内存

telegraf配置：

````shell
[[inputs.httpjson]]
    name_override="local_proc_mem"
    servers = [
       "http://127.0.0.1:8090/monitor/ProcMemInfo"
    ]
    response_timeout="5s"
    method="GET"
````

name: 内存指标数据表名

servers: 指定监控目标进程服务地址。/monitor/ProcMemInfo服务由DDAL监控功能提供。

内存采集数据样例：

查询语句：select * from local_proc_mem where time > now() - 1h order by time desc limit 20

![](https://ws1.sinaimg.cn/large/006tKfTcgy1fh7zolweodj31kw0khn5b.jpg)

### 网络IO

插件名称 [[inputs.net]]

1. 被监控网卡设置，看考：interfaces = ["eth0"]
2. 修改网络表名：name_override = "centername_net"
3. 数据字段：fieldpass = ["bytes_recv","bytes_sent"]。目前只采集发送和接受的字节数。

数据样例：

查询语句：select * from centername_net where time > now() - 1h order by time desc limit 10

![](https://ws2.sinaimg.cn/large/006tNc79gy1fgu26fz8q4j30vz0bgdii.jpg)



### 连接池

使用telegraf的httpjson插件统计使用ddal的web应用暴露的连接池指标。

````shell
[[inputs.httpjson]]
    name_override="local_conn_pool_stats"
    servers = [
       "http://192.168.37.1:8090/monitor/ConnPoolStats"
    ]
    response_timeout="5s"
    method="GET"
    tag_keys = ["poolName"]
````

使用时只需修改name_override和servers两个参数。

- local_conn_pool_stats保存中心的所有连接池指标，实际使用时local建议换成中心的名称，比如order_conn_pool_stats、customer_conn_pool_stats等。

- servers 是应用暴露出来的指标接口。

  应用可以通过注册ddal提供的servlet暴露http服务。如下是使用springboot暴露ddal监控接口：

  ````java
  @Bean
  public ServletRegistrationBean monitorServlet() {
    return new ServletRegistrationBean(new MonitorServlet(), "/monitor/*");
  }
  ````


influxdb的查询语句

````sql
select * from local_conn_pool_stats where time > now() - 1h order by time desc limit 20
````

数据样例：

![](https://ws3.sinaimg.cn/large/006tKfTcgy1fh7zqw1t4qj31kw0fwgqj.jpg)



### 进程纬度：SQL执行数量指标

telegraf配置：

````shell
[[inputs.httpjson]]
    name_override="local_proc_sql_summary"
    servers = [
       "http://192.168.37.1:8090/monitor/ProcSqlSummary"
    ]
    response_timeout="5s"
    method="GET"
````

servers是ddal暴露的进程SQL指标http服务地址，需要Web应用注册ddal的监控Servlet。

influxdb查询语句

````sql
select * from local_proc_sql_summary where time > now() - 1h order by time desc limit 20
````

![](https://ws3.sinaimg.cn/large/006tKfTcgy1fh3ca4uxrcj31kw0aegod.jpg)



### 进程维度：SQL实时速率（TPS）

telegraf配置

````shell
[[inputs.httpjson]]
    name_override="local_proc_tps"
    servers = [
       "http://127.0.0.1:8090/monitor/ProcTps"
    ]
    response_timeout="5s"
    method="GET"
````

各字段描述可参考其他使用httpjson插件的指标。



完整的telegraf配置，供参考：

````shell
[global_tags]

# Configuration for telegraf agent
[agent]
  interval = "10s"
  round_interval = true
  metric_batch_size = 1000
  metric_buffer_limit = 10000
  collection_jitter = "0s"
  flush_interval = "10s"
  flush_jitter = "0s"
  precision = ""
  debug = false
  quiet = false
  logfile = ""
  hostname = "192.168.37.10"
  omit_hostname = false

###############################################################################
#                            OUTPUT PLUGINS                                   #
###############################################################################

# Configuration for influxdb server to send metrics to
[[outputs.influxdb]]
  urls = ["http://192.168.37.10:8086"] # required
  database = "telegraf" # required
  retention_policy = ""
  write_consistency = "any"
  timeout = "5s"

###############################################################################
#                            INPUT PLUGINS                                    #
###############################################################################

# Read metrics about cpu usage
[[inputs.cpu]]
  percpu = false
  totalcpu = true
  fieldpass = ["usage_idle", "usage_iowait", "usage_system", "usage_user"]
  name_override = "local_cpu"

[[inputs.httpjson]]
    name_override="local_proc_mem"
    servers = [
       "http://127.0.0.1:8090/monitor/ProcMemInfo"
    ]
    response_timeout="5s"
    method="GET"

[[inputs.httpjson]]
    name_override="local_proc_tps"
    servers = [
       "http://127.0.0.1:8090/monitor/ProcTps"
    ]
    response_timeout="5s"
    method="GET"

[[inputs.httpjson]]
    name_override="local_proc_sql_summary"
    servers = [
       "http://127.0.0.1:8090/monitor/ProcSqlSummary"
    ]
    response_timeout="5s"
    method="GET"
    #tag_keys = ["poolName"]

[[inputs.httpjson]]
    name_override="local_conn_pool_stats"
    servers = [
       "http://127.0.0.1:8090/monitor/ConnPoolStats"
    ]
    response_timeout="5s"
    method="GET"
    tag_keys = ["poolName"]

# # Read metrics about network interface usage
[[inputs.net]]
    interfaces = ["en0"]
    name_override = "local_net"
    fieldpass = ["bytes_recv","bytes_sent"]
````

