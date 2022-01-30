package tcp1;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

public class TcpIpClient {
	public static void main(String[] args) {
		try {
			String serverIp = "127.0.0.1";
			
			System.out.println("서버에 연결중입니다. 서버 IP : " + serverIp);
			
			// 소캣을 생성하여 연결을 요청한다
			Socket socket= new Socket(serverIp, 7777);
			
			// 소캣의 입력스트림을 얻는다
			InputStream in = socket.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
			// 소캣에서 받은 데이터를 출력한다
			System.out.println("서버로부터 받은 메세지 : " + dis.readUTF());
			System.out.println("연결을 종료합니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
