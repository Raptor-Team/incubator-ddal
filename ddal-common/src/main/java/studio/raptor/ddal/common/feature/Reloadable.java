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

package studio.raptor.ddal.common.feature;

/**
 * Define an object as reloadable. Class declared as reloadable which means
 * object instanced of this class has reload ability and its reload method
 * can be triggered by reload worker created in a separated thread.
 * <p>
 * Reload job is kind of asynchronous. So in normally we need a 3rd-party
 * cooperative agency to collect reload result which we called coordinator.
 * <p>
 * See {@link } to read more about reload manager.
 *
 * @author Sam
 */
public interface Reloadable {

    /**
     * Send reload signal to reloadable objects which will return a global
     * unique id to the reload-worker.
     *
     * @return global unique job id
     */
    String reload();

}
