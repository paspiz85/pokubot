package it.paspiz85.nanobot;

import it.paspiz85.nanobot.ui.ApplicationAwareController;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application implements Constants {

    public static void main(String[] args) {
        try (InputStream inputStream = Application.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
        try {
            launch(args);
        } finally {
            for (Handler h : Logger.getLogger("").getHandlers()) {
                h.close();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle(NAME);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Application.class.getResource("ui/MainView.fxml"));
        Parent parent = loader.load();
        Object controller = loader.getController();
        if (controller instanceof ApplicationAwareController) {
            ((ApplicationAwareController) controller).setApplication(this);
        }
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
