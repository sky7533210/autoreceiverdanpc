package test;

import java.util.Date;

public class Test3 {
	public static void main(String[] args) throws InterruptedException {
		MyThread myThread=new MyThread();
		myThread.interrupt();	
		Thread.sleep(1000);
		myThread.start();
		
	}
	static class MyThread extends Thread{
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}
