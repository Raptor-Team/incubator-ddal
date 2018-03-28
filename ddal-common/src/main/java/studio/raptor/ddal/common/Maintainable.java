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

package studio.raptor.ddal.common;

/**
 * 抽象可维护性对象特性。
 * <p>
 * Created by Sam on 16/11/2016.
 */
public interface Maintainable {

    /**
     * 对象是否支持可维护性。 定义这个接口的初衷是对象需要支持可维护特性，
     * 但是现阶段功能尚不完善，可以使用此接口来屏蔽上层的维护操作。
     *
     * @return {@link Boolean#TRUE} or {@link Boolean#FALSE}
     */
    boolean isMaintainable();

    /**
     * uncomment
     */
    void maintain();
}
