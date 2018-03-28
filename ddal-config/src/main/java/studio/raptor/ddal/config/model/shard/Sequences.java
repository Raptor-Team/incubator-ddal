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

package studio.raptor.ddal.config.model.shard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import studio.raptor.ddal.common.exception.SequenceException;
import studio.raptor.ddal.common.exception.SequenceException.Code;
import studio.raptor.gid.def.SequenceDef;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class Sequences extends ArrayList<Object> {

  private static final long serialVersionUID = 1L;
  private Map<String, Object> _sequences = new HashMap<>();

  @Override
  public boolean add(Object obj) {
    SequenceDef def = (SequenceDef) obj;
    if(_sequences.containsKey(def.name())){
      throw SequenceException.create(Code.LOAD_SEQUENCE_ERROR, "Duplicate sequence name, " +def.name());
    }
    _sequences.put(def.name(), def);
    return super.add(def);
  }

  public Object get(String name) {
    return _sequences.get(name);
  }
}
