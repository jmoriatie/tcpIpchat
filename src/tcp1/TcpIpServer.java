package tcp1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TcpIpServer implements Runnable{
	ServerSocket serverSoket = null;
	Thread[] threadArr; // ++ 3.쓰레드
	
	public static void main(String[] args) {
		
		TcpIpServer server = new TcpIpServer(5); // 소켓준비 및 5개의 쓰레드 생성 
		server.serverStart(); // 쓰레드 실행
		
	}
	
	public TcpIpServer(int num) {
		try {
			// 서버소캣을 연결하여 7777포트와 결합(bind)시킨다
			serverSoket = new ServerSocket(7777);
			System.out.println(getTime() + "서버가 준비되었습니다.");
			
			threadArr = new Thread[num]; // 생성자에 입력한 갯수만큼 쓰레드 인스턴스 초기화
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// thread 시작
	public void serverStart() {
		for(int i=0; i< threadArr.length; i++) {
			threadArr[i] = new Thread(this);
			threadArr[i].start(); // 쓰레드 시작
		}
	}
	
	// 소켓 연결 대기 시작 => 쓰레드가 시작되면 시작
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println(getTime() + "연결요청을 기다립니다.");
				// 서버소켓은 클라이언트의 연결요청이 올 때까지 실행을 멈추고 계속기다린다
				
				// 요청대기시간 5초 설정
				// ++2. 타임설정: 5초동안 접속요청 없으면, SocketTimeoutException 발생
				serverSoket.setSoTimeout(5 * 1000);
				
				// 클라이언트의 연결요청이 들어오면 클라이언트 소켓과 통신할 새로운 소켓을 생성한다
				Socket socket = serverSoket.accept();
				System.out.println(getTime() + socket.getInetAddress() + " 로부터 연결요청이 들어왔습니다.");
				
				// ++1. 포트확인: 연결요청 포트 / 내 포트
				System.out.println("getPort(): " + socket.getPort());
				System.out.println("getLocalPort(): " + socket.getLocalPort());
				
				// 소켓의 출력스트림을 얻는다
				OutputStream out = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(out);

				// 원격 소켓(remote socket)에 데이터를 보낸다
				dos.writeUTF("[Notice] Test Message1 from Server");
				System.out.println(getTime() + "데이터를 전송했습니다.");
				
				// 스트림과 소켓을 닫아준다
				dos.close();
				socket.close();
			} catch (SocketTimeoutException e) {
				System.out.println("지정된 시간동안 접속요청이 없어서 서버를 종료합니다");
				System.exit(0);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getTime() {
		LocalDateTime now = null;
		String timeToString = null;
		
		now = LocalDateTime.now();
		timeToString = "[" + now.getHour() + "시/" + now.getMinute() + "분/" + now.getSecond() + "초] ";
		
		return timeToString;
	}

}
