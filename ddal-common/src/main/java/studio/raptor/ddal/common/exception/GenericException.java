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

/**
 * Generic exception for ddal.
 *
 * @author Sam
 * @since 3.0.0
 */
public class GenericException extends RuntimeException {

    private static final long serialVersionUID = -5845555497483005527L;

    private ErrorCodeDefinition errCode;

    private String additionalInfo;

    public GenericException(ErrorCodeDefinition ecd) {
        this(ecd, "");
    }

    public GenericException(final Exception cause) {
        super(cause);
    }

    public GenericException(ErrorCodeDefinition ecd, Throwable e) {
        this(ecd, e, "");
    }

    public GenericException(ErrorCodeDefinition ecd, Object... args) {
        super(String.format("%s-%s: %s \n<proposal solution> %s \n<additional info> %s",
            ecd.getType(),
            ecd.getCode(),
            String.format(ecd.getDesc(), args),
            ecd.getSolution(),
            null));
        this.errCode = ecd;
    }

    public GenericException(ErrorCodeDefinition ecd, Throwable e, String additionalInfo) {
        super(String.format("%s-%s: %s \n<proposal solution> %s \n<additional info> %s",
                ecd.getType(),
                ecd.getCode(),
                ecd.getDesc(),
                ecd.getSolution(),
                additionalInfo), e);
        this.errCode = ecd;
        this.additionalInfo = additionalInfo;
    }

    public GenericException(ErrorCodeDefinition ecd, Throwable e, String additionalInfo, Object... args) {
        super(String.format("%s-%s: %s \n<proposal solution> %s \n<additional info> %s",
                ecd.getType(),
                ecd.getCode(),
                String.format(ecd.getDesc(), args),
                ecd.getSolution(),
                additionalInfo), e);
        this.errCode = ecd;
        this.additionalInfo = additionalInfo;
    }

    public int getCode() {
        return errCode.getCode();
    }

    public String getDesc() {
        return errCode.getDesc();
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
}