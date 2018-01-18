/**
 * ===========================================================================
 * Copyright Adam Sample code
 * All Rights Reserved
 * ===========================================================================
 * 
 * File Name: SyncBuffer.java
 * Brief: 
 * 
 * Author: AdamChen
 * Create Date: 2018/1/18
 */

package com.adam.app.test.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncBuffer {

    private final byte[] mBuffer;
    private final int mCapacity;

    private int mFront;
    private int mRear;
    private volatile int mCount;

    private Lock mLock = new ReentrantLock();

    private final Condition notFull = mLock.newCondition();
    private final Condition notEmpty = mLock.newCondition();

    public SyncBuffer(int capacity) {
        super();

        mCapacity = capacity;
        mBuffer = new byte[capacity];
    }

    public void put(byte data) throws InterruptedException {
        mLock.lock();

        try {

            while (mCount == mCapacity) {
                notFull.await();
            }

            mBuffer[mRear] = data;
            mRear = (mRear + 1) % mCapacity;
            mCount++;

            notEmpty.signal();

        } finally {
            mLock.unlock();
        }

    }

    public byte get() throws InterruptedException {
        mLock.lock();

        try {

            while (mCount == 0) {
                notEmpty.await();
            }

            byte data = mBuffer[mFront];
            mFront = (mFront + 1) % mCapacity;
            mCount--;

            notFull.signal();

            return data;

        } finally {
            mLock.unlock();
        }
    }

}
