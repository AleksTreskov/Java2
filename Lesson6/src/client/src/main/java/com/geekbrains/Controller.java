package com.geekbrains;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextField textField;
    @FXML
    TextArea textArea;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    String msg;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread dataThread = new Thread(() -> {

                try {
                    while (true) {
                        if (!(msg = in.readUTF()).equals(""))
                            textArea.appendText(msg + '\n');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            });
            dataThread.start();
        } catch (IOException e) {
            throw new RuntimeException("Unable to connect to server [localhost:8189]");
        }
    }

    public void sendMsg(ActionEvent actionEvent) {
        String msg = textField.getText();
        try {
            out.writeUTF(msg);
            textField.clear();
            textArea.appendText(msg + '\n');
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Невозможно отправить сообщение!", ButtonType.OK);
            alert.showAndWait();
        }
    }

}