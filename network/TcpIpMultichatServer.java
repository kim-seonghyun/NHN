package seong.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 여러 client가 접속해서 chatting 하는것을 지원하는 server
 * Hashmap clients로 각 client의 name과 outputStream을 저장한다.
 * sendToAll() 메서드로 각 client에게 메시지를 보낸다.
 */
public class TcpIpMultichatServer {
    HashMap clients;

    public TcpIpMultichatServer() {
        this.clients = new HashMap();
        Collections.synchronizedMap(clients);
    }


    public void start(){
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("서버가 시작됐습니다");

            while (true) {
                socket = serverSocket.accept();
                System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "에서 접속하셨습니다");
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void sendToAll(String msg) {
        Iterator it = clients.keySet().iterator();

        while (it.hasNext()) {
            try {
                DataOutputStream out = (DataOutputStream) clients.get(it.next());
                out.writeUTF(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        new TcpIpMultichatServer().start();
    }

    /**
     * server의 Receiver이다.
     * client와 연결된 socket들이
     * 메시지를 입력할 때마다 모든 client들에게 메시지가 전달된다.
     *
     */
    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        public ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void run(){
            String name = "";

            try {
                name = in.readUTF();
                sendToAll(name + "님이 입장하셨습니다.");
                clients.put(name, out);
                System.out.println("현재 접속자 수는 "+ clients.size() + "명입니다.");
                while (in != null) {
                    sendToAll(in.readUTF());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                sendToAll(name + "님이 나가셨습니다.");
                clients.remove(name);
                System.out.println(socket.getPort() + "에서 접속을 종료하셨습니다");
                System.out.println("현재 접속자는 " + clients.size());
            }
        }
    }
}
