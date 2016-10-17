package br.com.softctrl.socket.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class SCMain {
	
	static final char[] itoa(final int vlr) {
		return String.format("%05d", vlr).toCharArray();	
	}

	static class SocketClientRunnable implements Runnable {

		private Socket mSocket;
		private PrintWriter mPrintWriter;
		private int mTime = 100;
		private char mBuffer[];
		private char mId[];

		public SocketClientRunnable(final String host, final int port, final char buffer[])
				throws UnknownHostException, IOException {
			this.mSocket = new Socket(host, port);
			this.mPrintWriter = new PrintWriter(this.mSocket.getOutputStream(), true);
			this.mBuffer = buffer;
		}
		
		public SocketClientRunnable setId(final int id){
			this.mId = itoa(id);
			return this;
			
		}

		public void run() {
			for (int value = 0; value < 1000000; value++) {
				
				this.mPrintWriter.write(this.mId);
//				this.mPrintWriter.write(' ');
				this.mPrintWriter.write('-');
//				this.mPrintWriter.write(' ');
//				this.mPrintWriter.write(this.mBuffer);
				this.mPrintWriter.write(itoa(value++));
				this.mPrintWriter.write('\n');
				this.mPrintWriter.flush();
//				this._wait();

			}
			try {
				this.mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void _wait() {
			try {
				Thread.sleep(this.mTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static final String SERVER = "localhost";
	private static final int PORT = 9876;

	private static final ConcurrentHashMap<Integer, Thread> CLIENTS = new ConcurrentHashMap<Integer, Thread>();

	public static void main(String[] args) throws UnknownHostException, IOException {

		final char[] buffer = "{\"_\":3}".toCharArray();
		System.out.println("Starting" + new Date().toInstant());
		for (int i = 0; i < 1000; i++) {
			Thread thread = new Thread(new SocketClientRunnable(SERVER, PORT, buffer).setId(i));
			thread.start();
			CLIENTS.put(i, thread);
		}
		System.out.println("Stoping" + new Date().toInstant());

	}
}
