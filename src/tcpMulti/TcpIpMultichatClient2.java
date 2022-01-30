package tcpMulti;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class TcpIpMultichatClient2 {
	public static void main(String[] args) {
//		if(args.length != 1) { // ???
//			System.out.println("USAGE: java TcpIpMultiChatClient 대화명");
//			System.exit(0); // ???
//		}
		
		try {
			String serverIp = "127.0.0.1";
			// 소켓 생성, 연결 요청
			Socket socket = new Socket(serverIp, 7777);
			System.out.println("서버에 연결되었습니다");
			
			// 별도의 쓰레드로 메세지 보내고 받기 같이
			Thread sender = new Thread( new ClientSender(socket, "User2") );
			Thread reciever = new Thread( new ClientReciever(socket) );
			
			sender.start();
			reciever.start();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch(Exception e) {
		}
	}
	
	// 메시지 보내는 메서드
	static class ClientSender extends Thread{
		Socket socket;
		DataOutputStream dos;
		String name;
		
		ClientSender(Socket socket, String name){
			this.socket = socket;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				this.name = name;
			} catch (Exception e) {
			}
		}
		
		@Override
		public void run() {
			Scanner scanner = new Scanner(System.in);
			try {
				if(dos != null) {
					dos.writeUTF(name);
				}
				
				while(dos != null) {
					dos.writeUTF("["+name+"] "+scanner.nextLine());
				}
			} catch (IOException e) {
				
			}
		}
	}
	
	// 메시지 받는 메서드
	static class ClientReciever extends Thread {
		Socket socket;
		DataInputStream dis;
		
		ClientReciever(Socket socket){
			this.socket = socket;
			try {
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				
			}
		}
		
		@Override
		public void run() {
			while(dis != null) {
				try {
					System.out.println( dis.readUTF() );
				} catch (IOException e) {
				}
			}
		}
	}
}
