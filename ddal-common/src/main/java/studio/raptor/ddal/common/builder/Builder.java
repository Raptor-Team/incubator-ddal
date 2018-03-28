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
package studio.raptor.ddal.common.builder;

/**
 * <p>
 * The Builder interface is designed to designate a class as a <em>builder</em> 
 * object in the Builder design pattern. Builders are capable of creating and 
 * configuring objects or results that normally take multiple steps to construct 
 * or are very complex to derive. 
 * </p>
 *
 * 
 * @param <T> the type of object that the builder will construct or compute.
 * @since 3.0.0
 */
public interface Builder<T> {

    /**
     * Returns a reference to the object being constructed or result being 
     * calculated by the builder.
     * 
     * @return the object constructed or result calculated by the builder.
     */
    T build();
}
