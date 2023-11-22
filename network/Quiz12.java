package seong.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Client에서 보내온 메시지를 접속한 모든 client에게 전송하는 broadcasting server를 만들어 보자. 실행시 서비스를 위한 port를 지정할 수 있다. 별도의 port 지정이 없는 경우,
 * 1234를 기본으로 한다. Server는 실행 후 대기 상태를 유지하고, client가 접속되면 client 정보를 출력한다. Server는 하나 이상의 client가 접속되어도 동시에 지원 가능하도록 한다.
 * Client에서 보내온 메시지는 접속되어 있는 모든 client에 전달한다.
 */
public class Quiz12 {
    HashMap clients;
    ServerSocket serverSocket;

    public static void main(String[] args) {
        Quiz12 quiz12 = new Quiz12();
        quiz12.start();

    }

    public Quiz12() {
        this.clients = new HashMap();
        try {
            this.serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // socket의 InputStream을 받아서 각 client들에게 message 출력
    void broadCast(String message) {
        Iterator iterator = clients.keySet().iterator();
        try {
            while (iterator.hasNext()) {
                DataOutputStream out = (DataOutputStream) clients.get(iterator.next());
                out.writeUTF(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    void start() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket client = serverSocket.accept();
                ServerReceiver thread = new ServerReceiver(client);
                thread.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
    // Hashmap, 현재 실행중인 client와 연결된 Socket 관리

    // ServerSocket이 accept되면 socket생성

    // client와 연결된 Server Receiver Thread -> client의 socket을 가지고 있다가 입력이 들어오면 broadCast함.
    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream in;
        DataOutputStream out;

        public ServerReceiver(Socket socket) {
            this.socket = socket;

            try {
                this.in = new DataInputStream(socket.getInputStream());
                this.out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            try {
                System.out.println(getThreadName() + "님이 입장하셨습니다.");
                clients.put(getName(), out);
                while (in != null) {
                    broadCast(in.readUTF());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(getThreadName() + "님이 퇴장하셨습니다");
                clients.remove(getName());
            }
        }

        String getThreadName() {
            return Thread.currentThread().getName();
        }


    }


}
