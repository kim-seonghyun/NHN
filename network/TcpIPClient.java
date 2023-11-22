package seong.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * TCP/IP와 통신하기 위한 서버 연결하고자 하는 IP와 포트번호로 소켓을 생성하면 자동으로 서버에 연결된다.
 */
public class TcpIPClient {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        final int port = 7777;
        System.out.println("서버에 연결중입니다." + serverIP);

        try (Socket socket = new Socket(serverIP, port);
             InputStream in = socket.getInputStream(); DataInputStream dis = new DataInputStream(in);
        ) {
            System.out.println("서버로 부터 받은 메시지 :" +dis.readUTF());
            System.out.println("연결을 종료 합니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
