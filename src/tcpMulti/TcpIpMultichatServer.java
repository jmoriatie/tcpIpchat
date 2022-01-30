package tcpMulti;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

public class TcpIpMultichatServer {
	HashMap clients; 

	TcpIpMultichatServer() {
		clients = new HashMap();
		Collections.synchronizedMap(clients); //??? 한명씩 들어올 때마다 동기화 해주는건가
	}

	public void serverStart() {
		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");

//			serverSocket.setSoTimeout(5 * 1000);
			
			while (true) {
				// Client가 요청을 하게되면
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");
				
				// Thread를 별도의 생성해주어, 로그아웃 전까지 유지하게끔 함 
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		} catch (SocketTimeoutException e) {
			System.out.println("정한 시간이 종료되어 서버가 종료됩니다");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 전체 메세지 >> 어떤 때, 어떻게 작동하는지 확인 필요
	void sendToAll(String message) {
		Iterator it = clients.keySet().iterator(); //??
		
		while(it.hasNext()) {
			try {
				DataOutputStream dos = (DataOutputStream)clients.get(it.next());
				dos.writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 메인메서드
	public static void main(String[] args) {
		new TcpIpMultichatServer().serverStart();
	}
	
	// 메시지 받는 별도 쓰레드 클래스
	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		public ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				// input output Stream 동시에 받아, 실시간 채팅하게끔
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}

		@Override
		public void run() {
			String name = "";

			try {
				name = dis.readUTF();
				sendToAll("# "+name+" 님이 들어오셨습니다."); // 
				
				clients.put(name, dos); // 접속자 outputStream과 함께, HashMap에다가 넣어주기
				System.out.println("현재 서버접속자 수는 " + clients.size()+" 명 입니다.");
				
				while(dis != null) { // 추가될 때마다, 메세지를 날려주는 것
					sendToAll(dis.readUTF());
				}
			} catch (IOException e) {
				// ignore
			} finally {
				sendToAll("# "+name+" 님이 나가셨습니다.");
				clients.remove(name);
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+" 에서 접속을 종료하였습니다.");
				System.out.println("현재 서버접속자 수는 " + clients.size()+" 명 입니다.");
			}
		}
	}
}
