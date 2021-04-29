package com.geekbrains.chat.server;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApp {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен, ожидаем подключение клиента...");
            Socket socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Клиент подключился");
            Scanner sc = new Scanner(System.in);
            String msg;
            Thread sendMsg = new Thread(() -> {


                while (true) {
                    try {
                        out.writeUTF(sc.nextLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendMsg.start();


            while (true) {
                if (!(msg = in.readUTF()).equals("")) {
                    System.out.println(msg);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

