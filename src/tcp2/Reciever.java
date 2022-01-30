package tcp2;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//메세지 받는 별도의 Thread Class
public class Reciever extends Thread {
	Socket socket;
	DataInputStream dis;

	public Reciever(Socket socket) {
		this.socket = socket;
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (dis != null) { // 읽어드리는 내용이 null이 아니라면
			try {
				System.out.println(dis.readUTF()); // 계속 읽어주기
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
