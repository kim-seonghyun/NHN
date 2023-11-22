package seong.network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Echo server port번호 지정 가능, default: 1234, 1~20000사이의 정수만 가능, 이외는 @Throws IllegalArgumentException client가 접속되면 client
 * 정보 출력 client에게 받은 데이터를 client로 돌려보냄 Client 연결이 끊기면 server socket을 닫고 프로그램 종료
 */
public class Quiz10 {

    public static void main(String[] args) {
        int port = 1234;
        if (args.length == 1) {
            try {
                int inputPort = Integer.parseInt(args[0]);
                if (inputPort < 1 || inputPort > 20000) {
                    throw new IllegalArgumentException("올바른 Port 번호를 입력하세요");
                }
                port = inputPort;
            } catch (NumberFormatException e) {
                throw new NumberFormatException(e.getMessage());
            }
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Socket socket = serverSocket.accept();
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedWriter terminalOut = new BufferedWriter(new OutputStreamWriter(System.out));
            System.out.println(
                    "Client[" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]가 연결됐습니다.");

            while (socket.isConnected()) {
                String line = socketIn.readLine();
                if (line.isEmpty()) {
                    break;
                }
                terminalOut.write(line + "\n");
                terminalOut.flush();

                socketOut.write(line + "\n");
                socketOut.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
