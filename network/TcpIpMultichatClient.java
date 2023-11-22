package seong.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 채팅서버에 접속할수있는 client
 * ClientServer, ReceiverServer가 Thread로 동작한다.
 */
public class TcpIpMultichatClient {

    public static void main(String[] args) {
        try {
            String serverIp = "127.0.0.1";
            Socket socket = new Socket(serverIp, 1234);
            System.out.println("서버에 연결됐습니다");
            Thread sender = new Thread(new ClientSender(socket));
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    static class ClientSender extends Thread {
        Socket socket;
        DataOutputStream out;
        String name;

        ClientSender(Socket socket) {
            this.socket = socket;
            try {
                out = new DataOutputStream(socket.getOutputStream());
                name = "[" + socket.getInetAddress() + ":" + socket.getPort() + "]";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (out != null) {
                try {
                    out.writeUTF(name + scanner.nextLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream in;

        ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            while (in != null) {
                try {
                    System.out.println(in.readUTF());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
