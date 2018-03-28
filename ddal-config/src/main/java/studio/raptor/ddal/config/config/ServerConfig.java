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

package studio.raptor.ddal.config.config;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.common.FileLoader;
import studio.raptor.ddal.config.model.server.Grants;
import studio.raptor.ddal.config.model.server.Params;
import studio.raptor.ddal.config.model.server.Users;
import studio.raptor.ddal.config.parser.ServerConfigParser;

public class ServerConfig extends AbstractConfig {

    private static final String SERVER_CONFIG_XSD = "server-config.xsd";

    private Params params;
    private Users  users;
    private Grants grants;

    private static ServerConfig instance = new ServerConfig();

    public static ServerConfig getInstance() {
        return instance;
    }

    private ServerConfig() throws GenericException {
        String xmlContent = this.getFileString(this.configFetcher.getServerConfigPath());
        this.validate(xmlContent, FileLoader.readLocalFile(SERVER_CONFIG_XSD));
        ServerConfigParser.parse(this, xmlContent);
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("ServerConfig reloading is not supported.");
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Grants getGrants() {
        return grants;
    }

    public void setGrants(Grants grants) {
        this.grants = grants;
    }

    @Override
    public String toString() {
        return "ServerConfig [params=" + params + ", users=" + users + ", grants=" + grants + "]";
    }

}
