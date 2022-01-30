package tcp2;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class TcpIpClient2 {
	public static void main(String[] args) {
		try {
			String serverIp = "127.0.0.1";
			// 소캣을 생성하여 연결을 요청한다(ip, socket num)
			Socket socket = new Socket(serverIp, 7777);
			
			System.out.println("서버에 연결되었습니다.");
			
			Sender sender = new Sender(socket);
			Reciever reciever = new Reciever(socket);
			
			sender.start();
			reciever.start();
			
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
