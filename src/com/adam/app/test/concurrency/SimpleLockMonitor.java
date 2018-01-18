/**
 * ===========================================================================
 * Copyright Adam Sample code
 * All Rights Reserved
 * ===========================================================================
 * 
 * File Name: SimpleLockMonitor.java
 * Brief: 
 * 
 * Author: AdamChen
 * Create Date: 2018/1/18
 */

package com.adam.app.test.concurrency;

public class SimpleLockMonitor {

    private static final int BUF_SIZE = 10;

    private static final SyncBuffer mBuffer = new SyncBuffer(BUF_SIZE);

    /**
     * 
     * <h1>WorkRunnable</h1>
     * 
     * @autor AdamChen
     * @since 2018/1/18
     */
    static class WorkRunnable implements Runnable {

        @Override
        public void run() {

            while (!Thread.interrupted()) {
                try {
                    Utils.print(this, "Get data...");
                    byte data = mBuffer.get();
                    Utils.print(this, "data = " + data);
                } catch (InterruptedException e) {
                    Utils.print(this, "Got interrupt");
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

        }

    }

    public static void main(String[] args) {

        WorkRunnable workThread = new WorkRunnable();
        // Start work thread
        new Thread(workThread).start();

        byte[] dataList = { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
                0x08 };

        for (byte data : dataList) {
            try {
                Utils.print("put data: " + data);
                mBuffer.put(data);
                Utils.print("next data >>>>>");
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

}
