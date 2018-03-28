#版权声明模板设置
指引：idea>preferences>editor>copyright
模板：
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

#代码注释模板设置
指引：idea>preferences>editor>file and code templates
内容：
```
/**
 * 功能描述
 * 
 * @author ${USER}
 * @since  0.0
 */
 ```

#本地仓库镜像设置
指引：/Users/XXXX/.m2/
内容：
  
```<mirrors>
    <mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        
    </mirror>
  </mirrors>
```
  
#安装第三方版权artifact
  
`mvn install:install-file -Dfile=<filepath> -DgroupId=<groupid> -DartifactId=<artifactid> 
-Dversion=<version> -Dpackaging=<packaging>`

#分支权限说明
常规分支：项目成员（开发者权限及以上）可Push分支
保护分支：项目管理员才能管理（Push）被保护的分支。
只读分支：任何人都无法Push代码（包括管理员和所有者），需要Push代码时应设为“常规”或“保护”分支。
**本项目的master设置为保护分支，develop为常规分支**

#开发工作流程

For committers

1、准备代码
clone project _raptor-ddal_  (git clone https://git.oschina.net/f150/raptor-ddal.git)
checkout _origin/develop_ as new local branch _devlop_ (git checkout -u -b devlop origin/devlop)
2、提交代码
add/modify some new files to your local branch _develop_
commit your modified files to your local branch _develop_
push local branch _develop_ to remote branch _origin/develop_
3、冲突解决
at first,fetch and merge _origin/develop_ 
commit -m "xxxxxx" after resolving conflict
git push
4、回滚修正

`撤销已经push的提交`
git revert <SHA>

`撤销已经commit的修改`
git reset <last good SHA> 或 git reset --hard <last good SHA>

`修正最后一个提交的commit message`
git commit --amend -m "commit msg"

`撤销未commit的修改`
利用编辑器的undo功能，如果需要彻底撤销并回到上一次提交的状态
git checkout -- <bad filename>

`更多`
如何在 Git 里撤销(几乎)任何操作
http://blog.jobbole.com/87700/




For keeper

1、

2、









