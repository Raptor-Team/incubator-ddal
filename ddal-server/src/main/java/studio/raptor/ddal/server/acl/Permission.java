/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.ddal.server.acl;


import studio.raptor.ddal.server.common.exception.GenericException;

import java.util.List;

/**
 * 前端连接的权限
 * 
 * @author bruce shi
 */
public interface Permission {

    /**
     * 是否有访问许可（接入认证）
     * 
     * @param host 主机IP
     * @param user 用户名
     * @param authData 授权数据
     * @param passwd 密码
     * @return
     * @throws GenericException
     */
    public PermitResult hasAccessPermit(String host, String user, byte[] authData, byte[] passwd)
                                                                                                 throws GenericException;

    /**
     * 是否有对象操作许可（操作认证）
     * 
     * @param user 用户名
     * @param metaInfos
     * @return
     */
    public PermitResult hasObjOpsPermit(String user, List<ObjOpsMetaInfo> metaInfos);

}
