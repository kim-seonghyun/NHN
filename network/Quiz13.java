package seong.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Client에서 보내온 메시지를 접속한 모든 client 또는 특정 client에 전송할 수 있도록 multi-chatting client/server를 만들어 보자. 실행시 서비스를 위한 port를 지정할 수
 * 있다. 별도의 port 지정이 없는 경우, 1234를 기본으로 한다. Server는 실행 후 대기 상태를 유지하고, client가 접속되면 client 정보를 출력한다. Server는 하나 이상의 client가
 * 접속되어도 동시에 지원 가능하도록 한다. Server는 접속된 client로부터 ID를 받아 별도 관리한다. Client 메시지 시작에 대상 client id가 추가되어 있는 경우, 해당 client에만
 * 메시지를 전달한다. - 대상 client id는 "@[ID] message"로 @ 다음에 붙여서 온다. - user1에 hello 메시지를 보내기 위해서는 "@user1 hello"로 보내면 된다. - 제어
 * 명령을 구현한다. - !list 명령은 접속되어 있는 client의 id list를 반환한다.
 * <p>
 * 아래의 요구 사항에 맞는 client를 구현해 보자. - 실행시 서비스를 위한 id, host, port를 지정할 수 있다. - 별도의 지정이 없는 경우, - id는 임의의 문자열로 생성한다. - host는
 * localhost - port는 1234로 한다. - Client가 server에 정상적으로 접속하면, 설정된 id를 전송한다. - 특정 client에만 메시지 전송을 원할 경우, 메시지 앞에 @[대상 id]을
 * 추가한다. - 제어 명령을 구현한다. - !exit 명령은 client은 연결을 끊는다.
 */
public class Quiz13 {
    HashMap clients;
    ServerSocket serverSocket;

    public static void main(String[] args) {
        Quiz13 quiz13 = new Quiz13();
        quiz13.start();

    }

    public Quiz13() {
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

    void broadCast(String name , String message) {

        try {
            DataOutputStream out= (DataOutputStream) clients.get(name);
            out.writeUTF(Thread.currentThread().getName() +": " + message);

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
                broadCast(getThreadName() + "님이 입장하셨습니다.");
                clients.put(getThreadName(), out);
                while (in != null) {
                    String line = in.readUTF();
                    if (getMessageContent(line).equals("!exit")) {
                        break;
                    }
                    String username;
                    if ((username= getUserName(getMessageContent(line))) != null ) {
                        broadCast(username, getMessageContent(line));
                        continue;
                    }

                    if (getMessageContent(line).equals("!list")) {
                        Iterator iterator = clients.keySet().iterator();
                        iterator.forEachRemaining(client->{
                            broadCast(Thread.currentThread().getName() , (String) client);
                        });
                        continue;
                    }
                    broadCast(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                broadCast(getThreadName() + "님이 퇴장하셨습니다");
                clients.remove(getName());
            }
        }

        String getThreadName() {
            return Thread.currentThread().getName();
        }


    }

    public String getMessageContent(String sentence) {
        String[] sentences = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < sentences.length; i++) {
            sb.append(sentences[i] + " ");
        }
        return sb.toString().trim();
    }

    public String getUserName(String sentence){
        String name = sentence.split(" ")[0];
        if(name.startsWith("@")){
            return name.substring(1);
        }else{
            return null;
        }
    }



}
