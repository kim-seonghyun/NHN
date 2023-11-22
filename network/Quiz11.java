package seong.network;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 하나 이상의 client가 동시 연결될 수 있는 echo server default port는 1234 cleint의 정보출력
 */
public class Quiz11 {

    static class Echo extends Thread {
        private Socket clientSocket;

        public Echo(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }


        @Override
        public void run() {
            System.out.println("서벼 연결됨");
            try (
                    BufferedWriter socketOut = new BufferedWriter(
                            new OutputStreamWriter(clientSocket.getOutputStream()));
                    BufferedReader terminalIn = new BufferedReader(new InputStreamReader(System.in));
            ) {

                String line;
                while ((line = terminalIn.readLine()) != null) {
                    socketOut.write(line + ":");
                    socketOut.flush();
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }


    public static void main(String[] args) throws IOException {
        int port = 1234;

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignore) {
                System.err.println("Port는 0 ~ 65535 까지의 정수만 가능합니다.");
                System.exit(1);
            }
        }
        ServerSocket serverSocket = new ServerSocket(port);
        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            Echo echo = new Echo(socket);
            echo.start();
        }

    }

}
