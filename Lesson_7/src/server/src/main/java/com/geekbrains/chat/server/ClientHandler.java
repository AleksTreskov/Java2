package com.geekbrains.chat.server;

import jdk.net.Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream((socket.getOutputStream()));
        new Thread(() -> {
            try {
                while (true) {
                    String msg = in.readUTF();
                    // /login Alex
                    if (msg.startsWith("/login ")) {
                        String usernameFromLogin = msg.split("\\s")[1];

                        if (server.isUserOnline(usernameFromLogin)) {
                            sendMsg("/login_failed Current nickname has already been occupied");
                            continue;
                        }

                        username = usernameFromLogin;
                        sendMsg("/login_ok " + username);
                        server.subscribe(this);
                        break;
                    }

                }

                while (true) {

                    String msg = in.readUTF();
                    if (msg.startsWith("/")){
                        executeCommand(msg);
                        continue;
                    }
                    server.broadcastMessage(username + ": " + msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }

        }).start();


    }
private void executeCommand(String cmd) throws IOException {
        if(cmd.startsWith("/who_am_i")){
                server.whoAmI(this);
        }
        else if (cmd.startsWith("/w ")){
            String[] tokens = cmd.split("\\s",3);
            server.whisper(this,tokens[1],tokens[2]);

        }
}
    private void disconnect() {
        server.unsubscribe(this);
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void sendMsg(String msg) throws IOException {
        out.writeUTF(msg);
    }

    public String getUsername() {
        return username;
    }
}

