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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import studio.raptor.ddal.common.util.ExecutorUtil;
import studio.raptor.ddal.config.common.ConfigConstant;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class CountDownLatchTest {
    final int COUNT = 100;
    final CountDownLatch completeLatch = new CountDownLatch(COUNT);
    private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) ExecutorUtil
        .createCustomizationThreadPool("sqlExecute", ConfigConstant.DEFAULT_CPU_CORES + 1,
            2 * ConfigConstant.DEFAULT_CPU_CORES + 1, 100);

    public CountDownLatchTest() {
        try {
            for (int i = 0; i < COUNT; ++i) {
                EXECUTOR.submit(new CountDownLatchTest.Worker(i));
            }
            completeLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Done!");
    }

    class Worker implements Runnable {
        int myRow;

        Worker(int row) {
            myRow = row;
        }

        public void run() {
            try {
                if(myRow == 0){
                    throw new Exception("fast fail");
                }
                Thread.sleep(10000);
//                System.err.println(myRow);
                completeLatch.countDown();
            } catch (Exception e) {
                for(int i =0; i < completeLatch.getCount(); i++){
                    completeLatch.countDown();
                }
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        new CountDownLatchTest();
        System.out.println((System.currentTimeMillis() - start)/1000.0);
    }
}
