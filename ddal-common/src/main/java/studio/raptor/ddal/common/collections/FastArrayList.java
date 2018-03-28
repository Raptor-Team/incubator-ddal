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

package studio.raptor.ddal.common.collections;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * 非线程安全，使用请注意。
 *
 * @author Sam
 * @since 3.0.0
 */
public class FastArrayList<T> extends AbstractList<T>
    implements List<T>, RandomAccess, Cloneable, Serializable {

  private static final int DEFAULT_CAPACITY = 10;
  private static final long serialVersionUID = 4664462660102683155L;
  private transient Object[] elementData;
  private int size;

  public FastArrayList() {
    this.elementData = new Object[DEFAULT_CAPACITY];
  }

  public FastArrayList(int size) {
    this.elementData = new Object[size];
  }

  @SuppressWarnings("unchecked")
  private T elementData(int index) {
    return (T) elementData[index];
  }

  @Override
  public T get(int index) {
    return elementData(index);
  }

  @Override
  public void add(int index, T element) {
    if (index >= size) {
      elementData = Arrays.copyOf(elementData, index + 1);
    }
    if (null == elementData[index]) {
      size++;
    }
    elementData[index] = element;
  }

  @Override
  public void clear() {
    for (int i = 0; i < elementData.length; i++) {
      elementData[i] = null;
    }
    size = 0;
  }

  @Override
  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public Iterator<T> iterator() {
    return new Itr();
  }

  private class Itr implements Iterator<T> {

    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such

    public boolean hasNext() {
      return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public T next() {
      int i = cursor;
      if (i >= size) {
        throw new NoSuchElementException();
      }
      Object[] elementData = FastArrayList.this.elementData;
      if (i >= elementData.length) {
        throw new ConcurrentModificationException();
      }
      cursor = i + 1;
      return (T) elementData[lastRet = i];
    }

    public void remove() {
      if (lastRet < 0) {
        throw new IllegalStateException();
      }
      try {
        FastArrayList.this.remove(lastRet);
        cursor = lastRet;
        lastRet = -1;
      } catch (IndexOutOfBoundsException ex) {
        throw new ConcurrentModificationException();
      }
    }
  }
}
