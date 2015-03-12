package com.adam.app.test.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer {

	private final byte[] mBuffer;
	private final int mCapacity;
	
	private int front;
	private int rear;
	private int count;
	
	private Lock lock = new ReentrantLock();
	
	
	private final Condition notFull = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	
	
	public BoundedBuffer(int capacity) {
		super();
		
		mCapacity = capacity;
		mBuffer = new byte[capacity];
	}
	
	public void put(byte data) throws InterruptedException {
		lock.lock();
		
		try {
		
			while(count == mCapacity) {
				notFull.await();
			}
			
			mBuffer[rear] = data;
			rear = (rear + 1) % mCapacity;
			count++;
			
			notEmpty.signal();
			
		} finally {
			lock.unlock();
		}		
		
	}
	
	public byte get() throws InterruptedException {
		lock.lock();
		
		try {
			
			while(count == 0) {
				notEmpty.await();
			}
			
			byte data = mBuffer[front];
			front = (front + 1) % mCapacity;
			count--;
			
			notFull.signal();
			
			return data;
			
		} finally {
			lock.unlock();
		}
	}
	
}
