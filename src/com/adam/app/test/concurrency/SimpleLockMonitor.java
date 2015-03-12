package com.adam.app.test.concurrency;


public class SimpleLockMonitor {

	private static final BoundedBuffer mBuffer = new BoundedBuffer(5);
	
	
	
	static class WorkRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				try {
					byte data = mBuffer.get();
					System.out.println("work threa: " + data);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WorkRunnable workThread = new WorkRunnable();
		new Thread(workThread).start();
		
		byte[] dataList = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
		for(byte data : dataList) {
			try {
				mBuffer.put(data);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
