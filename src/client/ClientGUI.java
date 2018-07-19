package client;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utilities.ThreadUtils;

public class ClientGUI extends Application {
	private Scene mainScene;
	
	private Client client = null;
	
	private static TextArea taChat = new TextArea();
	private static TextArea taClients = new TextArea();
	
	@Override
	public void start(Stage primaryStage) {				
		mainSceneSetup();
		
		primaryStage.setTitle("Simple Chat Client");
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	public void mainSceneSetup() {
		GridPane mainGridPane = new GridPane();
		mainGridPane.setHgap(10);
		mainGridPane.setVgap(10);
		mainGridPane.setPadding(new Insets(10, 10, 10, 10));
    	
		TextField tfUsername = new TextField();
		tfUsername.setPrefWidth(400);
		tfUsername.setPromptText("Username");
		Button btConnect = new Button("Connect");
		taChat.setPrefColumnCount(35);
		taChat.setPrefRowCount(25);
		taChat.setDisable(true);
		TextField tfSend = new TextField();
		tfSend.setPrefWidth(400);
		Button btSend = new Button("Send");
		taClients.setPrefColumnCount(15);
		taClients.setDisable(true);
		
		mainGridPane.add(tfUsername, 0, 0);
		mainGridPane.add(btConnect, 1, 0);
		btConnect.setOnAction(e -> {
			String username = tfUsername.getText();
			
			validateUsername(username);
			
			final String IP = "localhost";
			final int PORT_NUM = 5001;
			final int TIMEOUT = 1800000;
			
			//ThreadUtils.createClientSocket(IP, PORT_NUM, TIMEOUT);
			client = new Client(username, IP, PORT_NUM, TIMEOUT);
			client.sendServerMessage(username);
		});
		
		mainGridPane.add(taChat, 0, 1, 2, 1);
		mainGridPane.add(tfSend, 0, 2);
		mainGridPane.add(btSend, 1, 2);
		btSend.setOnAction(e -> {
			if (client == null) {
				//alert
			}
			
			String message = tfSend.getText();
			checkForCommands(message);
			
			client.sendMessage(message);
			
			tfSend.setText("");
		});
		
		mainGridPane.add(new Label("People Online"), 2, 0);
		mainGridPane.add(taClients, 2, 1, 1, 3);
		
		mainScene = new Scene(mainGridPane, 700, 500);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public static void printToChatBox(String message) {
		if (!message.trim().equals("")) {
			Platform.runLater(() -> taChat.appendText(message + "\n"));
		}
	}
	
	public static void printClientUsername(String username) {
		Platform.runLater(() -> taClients.appendText(username + "\n"));
	}
	
	public static void clearClientsBox() {
		Platform.runLater(() -> taClients.clear());
	}

	public void validateUsername(String username) {
		if (username.trim().equals("")) {
	    	Alert alValidateUsername = new Alert(AlertType.ERROR);
	    	alValidateUsername.setTitle("Validate Username");
	    	alValidateUsername.setHeaderText(null);
	    	alValidateUsername.setContentText("Username cannot be blank.");
	    	alValidateUsername.showAndWait();	
			return;
		}
	}
	
	public void validateLoginStatus(String username) {
		if (username == null) {
	    	Alert alValidateLoginStatus = new Alert(AlertType.ERROR);
	    	alValidateLoginStatus.setTitle("Connection");
	    	alValidateLoginStatus.setHeaderText(null);
	    	alValidateLoginStatus.setContentText("You must logout by typing '/logout' in chat before you can change your name and reconnect.");
	    	alValidateLoginStatus.showAndWait();	
			return;
		}
	}
	
	public void checkForCommands(String message) {
		if (message.equals("/logout")) {
			try {
				client.getSocket().close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			
			client = null;
		}
	}
}

