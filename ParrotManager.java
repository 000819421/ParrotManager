import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * ParrotManager class creates a JavaFX-based GUI to interact with multiple parrot objects.
 * Users can select a parrot, feed it, command it (fly or stay), hit it, and monitor the parrot's health and state.
 */
public class ParrotManager extends Application {

    private Parrot[] parrots = new Parrot[3]; // Array to store multiple Parrots
    private Label outputLabel, healthLabel;
    private ImageView imageView;
    private ComboBox<String> parrotSelection;
    private Button feedButton, commandButton, hitButton;
    private ProgressBar healthBar;
    private ToggleGroup commandGroup;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the Parrots
        parrots[0] = new Parrot("Julius");
        parrots[1] = new Parrot("Nancy");
        parrots[2] = new Parrot("John");

        // Setup the Layout and Components
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Create the title and instructions
        Label title = new Label("Welcome to the Parrot Manager!");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label instructions = new Label("Select a parrot from the dropdown and perform actions like feeding, commanding, or hitting.");

        // Dropdown for selecting parrots
        parrotSelection = new ComboBox<>();
        parrotSelection.getItems().addAll("Julius", "Nancy", "John");
        parrotSelection.setPromptText("Select a Parrot");
        parrotSelection.setOnAction(event -> updateParrotImage());

        // Display the parrot's status
        outputLabel = new Label("Select a parrot to see its status.");
        outputLabel.setStyle("-fx-font-size: 14px;");

        // Health bar to show parrot's health
        healthBar = new ProgressBar(1.0);
        healthLabel = new Label("Health: 100%");
        healthBar.setPrefWidth(200);

        // ImageView for the parrot image
        imageView = new ImageView();
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        // Action buttons
        feedButton = new Button("Feed");
        hitButton = new Button("Hit");

        // Command buttons (Fly/Stay)
        commandButton = new Button("Command");
        RadioButton flyRadioButton = new RadioButton("Fly");
        RadioButton stayRadioButton = new RadioButton("Stay");
        commandGroup = new ToggleGroup();
        flyRadioButton.setToggleGroup(commandGroup);
        stayRadioButton.setToggleGroup(commandGroup);
        HBox commandBox = new HBox(10, flyRadioButton, stayRadioButton);
        commandBox.setAlignment(Pos.CENTER);

        // Set event handlers for buttons
        feedButton.setOnAction(event -> feedParrot());
        hitButton.setOnAction(event -> hitParrot());
        commandButton.setOnAction(event -> commandParrot());

        // Arrange buttons horizontally
        HBox actionButtons = new HBox(15, feedButton, hitButton, commandButton);
        actionButtons.setAlignment(Pos.CENTER);

        // Add components to the layout
        root.getChildren().addAll(title, instructions, parrotSelection, imageView, healthLabel, healthBar, outputLabel, actionButtons, commandBox);

        // Set the Scene and show the stage
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Parrot Manager");
        primaryStage.show();
    }

    // Update the parrot image and health bar based on the selected parrot
    private void updateParrotImage() {
        int index = parrotSelection.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            try {
                String imagePath = String.format("parrot%d.jpg", index + 1); // Assuming the images are named parrot1.jpg, parrot2.jpg, etc.
                imageView.setImage(new Image(new FileInputStream(imagePath)));
                outputLabel.setText(parrots[index].toString()); // Display current status
                updateHealthBar(index); // Update health bar
            } catch (FileNotFoundException e) {
                outputLabel.setText("Error: Image file not found.");
            }
        } else {
            outputLabel.setText("Please select a parrot.");
        }
    }

    // Update the health bar to reflect the current health of the selected parrot
    private void updateHealthBar(int index) {
        double healthPercentage = parrots[index].getHealth() / 3.0;
        healthBar.setProgress(healthPercentage);
        healthLabel.setText(String.format("Health: %.0f%%", healthPercentage * 100));
        updateHealthBarColor(healthPercentage);
    }

    // Update the health bar color based on the current health
    private void updateHealthBarColor(double healthPercentage) {
        if (healthPercentage > 0.66) {
            healthBar.setStyle("-fx-accent: green;");
        } else if (healthPercentage > 0.33) {
            healthBar.setStyle("-fx-accent: yellow;");
        } else {
            healthBar.setStyle("-fx-accent: red;");
        }
    }

    // Feed the parrot
    private void feedParrot() {
        int index = parrotSelection.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            parrots[index].feed(0.5); // Feed 0.5 kg at a time
            outputLabel.setText(parrots[index].toString());
            updateHealthBar(index);

            // Check if the parrot has been tamed
            if (parrots[index].isTamed()) {
                outputLabel.setText(outputLabel.getText() + "\nParrot is now tamed and will follow commands!");
            }
        } else {
            outputLabel.setText("Please select a parrot.");
        }
    }

    // Hit the parrot
    private void hitParrot() {
        int index = parrotSelection.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            parrots[index].hit();
            outputLabel.setText(parrots[index].toString());
            updateHealthBar(index);
        } else {
            outputLabel.setText("Please select a parrot.");
        }
    }

    // Command the parrot (Fly/Stay)
    private void commandParrot() {
        int index = parrotSelection.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            RadioButton selectedCommand = (RadioButton) commandGroup.getSelectedToggle();
            if (selectedCommand != null) {
                String command = selectedCommand.getText().toLowerCase();
                try {
                    parrots[index].command(command);
                    outputLabel.setText(parrots[index].toString());
                } catch (Exception e) {
                    outputLabel.setText("Error: " + e.getMessage());
                }
            } else {
                outputLabel.setText("Please select 'Fly' or 'Stay'.");
            }
        } else {
            outputLabel.setText("Please select a parrot.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
