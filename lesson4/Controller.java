package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller  {
    @FXML
    Button sendMessage;
    @FXML
    TextArea messageHistory;
    @FXML
    TextField messagePrint;

    public void sending(){
        if (!messagePrint.getText().equals(""))
        messageHistory.appendText(messagePrint.getText()+"\n");
        messagePrint.clear();
    }
}
