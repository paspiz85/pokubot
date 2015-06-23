package it.paspiz85.nanobot.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import aok.coc.util.ConfigUtils;
import aok.coc.util.coords.Clickable;

public class MainController implements ApplicationAwareController {

	private Application application;
	@FXML
	private ComboBox<String> autoAttackComboBox;
	@FXML
	private Button cancelButton;
	@FXML
	private GridPane configGridPane;
	@FXML
	private AnchorPane controlPane;
	@FXML
	private TextField deField;
	@FXML
	private CheckBox detectEmptyCollectorsCheckBox;
	@FXML
	private Label donateLabel;
	@FXML
	private Hyperlink donateLink;
	@FXML
	private TextField elixirField;
	@FXML
	private Hyperlink githubLink;
	@FXML
	private TextField goldField;
	@FXML
	private ImageView heartImage;
	@FXML
	private CheckBox isMatchAllConditionsCheckBox;
	protected final Logger logger = Logger.getLogger(getClass().getName());
	@FXML
	private TextField maxThField;
	private final MainModel model = new MainModel();
	@FXML
	private CheckBox playSoundCheckBox;
	@FXML
	private ComboBox<String> rax1ComboBox;
	@FXML
	private ComboBox<String> rax2ComboBox;
	@FXML
	private ComboBox<String> rax3ComboBox;
	@FXML
	private ComboBox<String> rax4ComboBox;
	private Service<Void> runnerService = null;
	@FXML
	private Button setupButton;
	@FXML
	private AnchorPane setupPane;
	private Service<Void> setupService = null;

	@FXML
	private Button startButton;

	@FXML
	private Button stopButton;
	@FXML
	private TextArea textArea;

	@FXML
	private Label updateLabel;
	@FXML
	private Label versionLabel;

	@FXML
	public void handleCancelButtonAction() {
		setupPane.setVisible(false);
		controlPane.setVisible(true);
	}

	@FXML
	public void handleSaveButtonAction() {
		if (!goldField.getText().isEmpty()) {
			ConfigUtils.instance().setGoldThreshold(
					Integer.parseInt(goldField.getText()));
		}

		if (!elixirField.getText().isEmpty()) {
			ConfigUtils.instance().setElixirThreshold(
					Integer.parseInt(elixirField.getText()));
		}

		if (!deField.getText().isEmpty()) {
			ConfigUtils.instance().setDarkElixirThreshold(
					Integer.parseInt(deField.getText()));
		}

		if (!maxThField.getText().isEmpty()) {
			ConfigUtils.instance().setMaxThThreshold(
					Integer.parseInt(maxThField.getText()));
		}

		ConfigUtils.instance().setMatchAllConditions(
				isMatchAllConditionsCheckBox.isSelected());
		ConfigUtils.instance().setDetectEmptyCollectors(
				detectEmptyCollectorsCheckBox.isSelected());
		ConfigUtils.instance().setPlaySound(playSoundCheckBox.isSelected());
		ConfigUtils.instance().setAttackStrategy(autoAttackComboBox.getValue());
		ConfigUtils.instance().getRaxInfo()[0] = Clickable
				.fromDescription(rax1ComboBox.getValue());
		ConfigUtils.instance().getRaxInfo()[1] = Clickable
				.fromDescription(rax2ComboBox.getValue());
		ConfigUtils.instance().getRaxInfo()[2] = Clickable
				.fromDescription(rax3ComboBox.getValue());
		ConfigUtils.instance().getRaxInfo()[3] = Clickable
				.fromDescription(rax4ComboBox.getValue());

		ConfigUtils.instance().save();
	}

	@FXML
	public void handleSetupButtonAction() {
		controlPane.setVisible(false);
		setupPane.setVisible(true);
		if (!model.isSetupDone() && setupService.getState() == State.READY) {
			setupService.start();
		}
	}

	@FXML
	public void handleStartButtonAction() {

		if (model.isSetupDone() && runnerService.getState() == State.READY) {
			runnerService.start();
		}
	}

	@FXML
	public void handleStopButtonAction() {
		if (setupService.isRunning()) {
			setupService.cancel();
			setupService.reset();
		}
		if (runnerService.isRunning()) {
			runnerService.cancel();
			runnerService.reset();
		}
	}

	@FXML
	void initialize() {
		model.initialize();
		LogHandler.initialize(textArea);

		initializeLinks();
		initializeLabels();
		initializeTextFields();
		initializeSetupService();
		initializeRunnerService();
		if (model.checkForUpdate()) {
			updateLabel.setVisible(true);
		}
	}

	private void initializeComboBox() {
		autoAttackComboBox.getItems().addAll(
				ConfigUtils.instance().getAttackStrategies());
		autoAttackComboBox.setValue(autoAttackComboBox.getItems().get(0));

		Clickable[] availableTroops = ConfigUtils.instance()
				.getAvailableTroops();
		String[] troops = new String[availableTroops.length];
		for (int i = 0; i < availableTroops.length; i++) {
			Clickable c = availableTroops[i];
			troops[i] = c.getDescription();
		}

		rax1ComboBox.getItems().addAll(troops);
		rax2ComboBox.getItems().addAll(troops);
		rax3ComboBox.getItems().addAll(troops);
		rax4ComboBox.getItems().addAll(troops);
	}

	private void initializeLabels() {
		String version = getClass().getPackage().getImplementationVersion();
		if (version != null) {
			versionLabel.setText("PokuBot v" + version);
		} else {
			versionLabel.setText("");
		}
	}

	private void initializeLinks() {
		githubLink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {
				application.getHostServices()
						.showDocument(githubLink.getText());
				githubLink.setVisited(false);
			}
		});

		Image heartIcon = new Image(getClass().getResourceAsStream(
				"/images/heart.png"));
		donateLink.setGraphic(new ImageView(heartIcon));

		donateLink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				application.getHostServices().showDocument(
						"https://github.com/norecha/pokubot#donate");
				donateLink.setVisited(false);
			}
		});
	}

	private void initializeRunnerService() {
		runnerService = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						model.botLauncherStart();
						return null;
					}
				};
			}
		};

		runnerService.setOnCancelled(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				logger.warning("runner is cancelled.");
				runnerService.reset();
			}
		});

		runnerService.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				logger.log(Level.SEVERE, "runner is failed: "
						+ runnerService.getException().getMessage(),
						runnerService.getException());
				runnerService.reset();
			}
		});
	}

	private void initializeSetupService() {
		setupService = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						model.botLauncherTearDown();
						model.botLauncherSetup();
						return null;
					}
				};
			}
		};
		setupService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				initializeComboBox();
				updateConfigGridPane();
				model.setSetupDone(true);
				logger.info("Setup is successful.");
				logger.info("Click start to run.");
			}
		});

		setupService.setOnFailed(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				model.setSetupDone(false);
				logger.log(Level.SEVERE, "Setup is failed: "
						+ setupService.getException().getMessage(),
						setupService.getException());
				setupService.reset();
			}
		});

		setupService.setOnCancelled(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				model.setSetupDone(false);
				logger.warning("Setup is cancelled.");
				setupService.reset();
			}
		});
	}

	private void initializeTextFields() {
		ChangeListener<String> intFieldListener = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				try {
					if (!newValue.isEmpty()) {
						Integer.parseInt(newValue);
					}
				} catch (NumberFormatException e) {
					((TextField) ((StringProperty) observable).getBean())
							.setText(oldValue);
				}
			}
		};
		goldField.textProperty().addListener(intFieldListener);
		elixirField.textProperty().addListener(intFieldListener);
		deField.textProperty().addListener(intFieldListener);
		maxThField.textProperty().addListener(intFieldListener);
	}

	@Override
	public void setApplication(Application application) {
		this.application = application;
	}

	private void updateConfigGridPane() {
		goldField.setText(ConfigUtils.instance().getGoldThreshold() + "");
		elixirField.setText(ConfigUtils.instance().getElixirThreshold() + "");
		deField.setText(ConfigUtils.instance().getDarkElixirThreshold() + "");
		maxThField.setText(ConfigUtils.instance().getMaxThThreshold() + "");

		isMatchAllConditionsCheckBox.setSelected(ConfigUtils.instance()
				.isMatchAllConditions());
		detectEmptyCollectorsCheckBox.setSelected(ConfigUtils.instance()
				.isDetectEmptyCollectors());
		playSoundCheckBox.setSelected(ConfigUtils.instance().isPlaySound());
		autoAttackComboBox.getSelectionModel().select(
				ConfigUtils.instance().getAttackStrategy().getClass()
						.getSimpleName());
		rax1ComboBox.getSelectionModel().select(
				ConfigUtils.instance().getRaxInfo()[0].getDescription());
		rax2ComboBox.getSelectionModel().select(
				ConfigUtils.instance().getRaxInfo()[1].getDescription());
		rax3ComboBox.getSelectionModel().select(
				ConfigUtils.instance().getRaxInfo()[2].getDescription());
		rax4ComboBox.getSelectionModel().select(
				ConfigUtils.instance().getRaxInfo()[3].getDescription());

		configGridPane.setVisible(true);
	}

}
