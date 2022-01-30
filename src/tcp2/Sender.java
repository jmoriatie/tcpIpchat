package tcp2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//메세지 보내는 별도의 Thread Class
public class Sender extends Thread {
	Socket socket;
	DataOutputStream dos;
	String name;

	// 생성자: 소캣을 받아서 OutputStream 만들어줌
	public Sender(Socket socket) {
		this.socket = socket;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			name = "[" + socket.getInetAddress() + "가 " + socket.getPort() + "에게]";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		while (dos != null) { // 쓴 내용이 null이 아닐 때까지
			try {
				dos.writeUTF(name + scanner.nextLine()); // 아이디 + 메세지 한줄 출력
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
