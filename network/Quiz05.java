package seong.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Quiz05 {

    /**
     * Server에서 전송받은 buffer값을 출력. exit를 입력받으면 종료. port는 1~ 10000사이의 값이다.
     */
    public static void transferData(String host, int port) {
        if (port < 1) {
            throw new IllegalArgumentException();
        }
        if (port > 10000) {
            throw new IllegalArgumentException();
        }
        try (Socket socket = new Socket(host, port); BufferedReader socketIn = new BufferedReader(
                new InputStreamReader(socket.getInputStream())); BufferedWriter terminamOut = new BufferedWriter(
                new OutputStreamWriter(System.out))) {

            String line;
            while ((line = socketIn.readLine()) != null) {
                if (line.equals("exit")) {
                    return;
                }
                terminamOut.write(line);
                terminamOut.flush();
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String host;
        int port;

        if (args.length == 0) {
            transferData();
        }

        if (args.length == 1) {
            host = args[0];
            transferData(host);
        }

        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
            transferData(host, port);
        }
    }

    private static void transferData(String host) {
        transferData(host, 1234);
    }

    private static void transferData() {
        transferData("localhost", 1234);
    }
}