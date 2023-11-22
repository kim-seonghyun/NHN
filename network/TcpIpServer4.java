package seong.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
public class TcpIpServer4 implements Runnable {
    ServerSocket serverSocket;
    Thread[] threadArr;

    public TcpIpServer4(int num) {
        try{
            serverSocket = new ServerSocket(7777);
            System.out.println("서버가 준비 되었습니다.");

            threadArr = new Thread[num];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        TcpIpServer4 server = new TcpIpServer4(5);
        server.start();
    }

    public void start(){
        for (int i = 0; i < threadArr.length; i++) {
            threadArr[i] = new Thread(this);
            threadArr[i].start();
        }
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println(getTime() + "가 연결요청을 기다립니다.");
                Socket socket = serverSocket.accept();

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF("Test Message1 from Server");
                System.out.println("데이터를 전송했습니다.");

                dos.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static String getTime(){
        String name = Thread.currentThread().getName();
        return name;
    }
}
