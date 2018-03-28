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

package studio.raptor.ddal.common.exception;

import java.io.Serializable;

/**
 * Super class of error code definition.
 *
 * @author Sam
 * @since 1.0
 */
public class ErrorCodeDefinition implements Serializable {

    private static final long serialVersionUID = 6384124767035158091L;

    private String type;
    private int code;
    private String desc;
    private String solution;

    public ErrorCodeDefinition(String type, int code, String desc, String solution) {
        this.type = type;
        this.code = code;
        this.desc = desc;
        this.solution = solution;
    }

    String getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    String getDesc() {
        return desc;
    }

    String getSolution() {
        return solution;
    }
}
