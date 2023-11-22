package seong.network;

import java.io.IOException;
import java.net.ServerSocket;

public class Exam05 {
    public static void main(String[] args) {


        try(ServerSocket serverSocket = new ServerSocket(8080);){
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
