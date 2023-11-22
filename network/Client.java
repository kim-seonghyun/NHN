package seong.network;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter outWriter = new PrintWriter(socket.getOutputStream(), true);

        // 서버에서 받은 메시지를 출력하는 쓰레드
        new Thread(new Runnable() {
            public void run() {
                try {
                    String serverMessage;
                    while ((serverMessage = inputReader.readLine()) != null) {
                        System.out.println("서버로부터 받은 메시지: " + serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 사용자 입력을 서버에 전송하는 부분
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while ((userInput = userInputReader.readLine()) != null) {
            outWriter.println(userInput);
        }

        socket.close();
    }
}