package tcp2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpIpServer2 {
	public static void main(String[] args) {
		ServerSocket serverSoket = null;
		Socket socket = null;

		try {
			// 서버소켓 결합하여 7777포트와 결합(bind)
			serverSoket = new ServerSocket(7777);
			System.out.println("서버가 준비되었습니다");

			socket = serverSoket.accept(); // 요청이 있다면

			// 별도의 Thread인 Sender Reciever 클래스 실행해서, 실시간 채팅이 가능하도록 구현
			Sender sender = new Sender(socket);
			Reciever reciever = new Reciever(socket);

			// 쓰레드 실행
			sender.start();
			reciever.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}




