package it.paspiz85.nanobot.ui;

import it.paspiz85.nanobot.Constants;
import it.paspiz85.nanobot.util.Logging;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application implements
		Constants {

	public static void main(String[] args) {
		Logging.initialize();
		try {
			launch(args);
		} finally {
			Logging.close();
		}
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle(NAME);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Application.class.getResource("MainView.fxml"));
		Parent parent = loader.load();
		Object controller = loader.getController();
		if (controller instanceof ApplicationAwareController) {
			((ApplicationAwareController) controller).setApplication(this);
		}
		primaryStage.setScene(new Scene(parent));
		primaryStage.show();
	}
}
