package seong.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import seong.network.TcpIpServer5.Receiver;
import seong.network.TcpIpServer5.Sender;

public class TcpIpClient5 {
    public static void main(String[] args) {
        try{
            String serverIp = "127.0.01";

            Socket socket = new Socket(serverIp, 7777);

            System.out.println("서버에 연결됐습니다.");
            Sender sender = new Sender(socket);
            Receiver receiver = new Receiver(socket);

            sender.start();
            receiver.start();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
