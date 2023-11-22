package seong.network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * ServerSocket을 사용하여 client와 연결해보자!
 *
 */
public class Exam04 {
    public static void main(String[] args) {
        //port를 지정하여 ServerSocket Object 생성
        // 연결 되면 server에서 "Hello" 전송
        // Output Stream은 int, byte[]만가능 하다.

        try(ServerSocket serverSocket = new ServerSocket(1234);){
            Socket socket =  serverSocket.accept();
            BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String line = "Hello";
            socketOut.write(line);
            socketOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
