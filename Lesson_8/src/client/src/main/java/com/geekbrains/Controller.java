package com.geekbrains;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    @FXML
    TextField textField, userField;
    @FXML
    TextArea textArea;
    @FXML
    HBox loginPanel, msgPanel;
    @FXML
    ListView<String> clientList;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String username;


    public void sendMsg(ActionEvent actionEvent) {
        String msg = textField.getText() + "\n";

        try {
            out.writeUTF(msg);
            textField.clear();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно отправить сообщение!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void login(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        if (userField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не должно быть пустым", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try {
            out.writeUTF("/login " + userField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
        if (username != null) {
            loginPanel.setVisible(false);
            loginPanel.setManaged(false);
            msgPanel.setVisible(true);
            msgPanel.setManaged(true);

        } else {
            loginPanel.setVisible(true);
            loginPanel.setManaged(true);
            msgPanel.setVisible(false);
            msgPanel.setManaged(false);

        }
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread dataThread = new Thread(() -> {

                try {
                    while (true) {
                        String msg = in.readUTF();
                        if (executeCommand(msg))
                            break;

                    }

                    //цикл общения
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            executeCommand(msg);
                            continue;
                        }
                        textArea.appendText(msg);
                    }
                }
                catch (EOFException e){
                    System.out.println("Вы успешно разлогинились");
                }catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            });
            dataThread.start();

        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server [localhost:8189]");
        }
    }

    private boolean executeCommand(String cmd) {
        if (cmd.startsWith("/login_ok ")) {
            setUsername(cmd.split("\\s")[1]);
            return true;
        } else if (cmd.startsWith("/login_failed ")) {
            String cause = cmd.split("\\s", 2)[1];
            textArea.appendText(cause + '\n');
        }
        if (cmd.startsWith("/clients_list ")) {
            String[] tokens = cmd.split("\\s");
            Platform.runLater(() -> {
                System.out.println(Thread.currentThread().getName());
                clientList.getItems().clear();
                for (int i = 1; i < tokens.length; i++) {
                    clientList.getItems().add(tokens[i]);

                }
            });

        }
        return false;
    }

    public void disconnect() {
        setUsername(null);
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        out.writeUTF("/logout");
    }
}