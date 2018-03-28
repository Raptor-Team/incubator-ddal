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

package studio.raptor.ddal.core.executor.threadPool.concurrent;

import studio.raptor.ddal.common.util.ExecutorUtil;
import studio.raptor.ddal.config.common.ConfigConstant;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class CyclicBarrierTest {
    final int N = 4;
    final CyclicBarrier barrier;
    // SQL执行线程池（1~2*cpu_cores）
    private static final ExecutorService EXECUTOR = ExecutorUtil.createCachedThreadPool("execute", 1,
            2 * ConfigConstant.DEFAULT_CPU_CORES);

    public CyclicBarrierTest() {
        barrier = new CyclicBarrier(N,
                new Runnable() {
                    public void run() {
                        System.err.println("Done!");
                    }
                });
        for (int i = 0; i < N; ++i) {
            EXECUTOR.submit(new CyclicBarrierTest.Worker(i));
        }
    }

    class Worker implements Runnable {
        int myRow;

        Worker(int row) {
            myRow = row;
        }

        public void run() {
            System.err.println(myRow);
            try {
                barrier.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }
        }
    }
}
