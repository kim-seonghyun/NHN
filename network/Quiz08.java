package seong.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Quiz08 {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            Socket socket = serverSocket.accept();
            System.out.println("Client[" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort()+ "]가 연결되었습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
