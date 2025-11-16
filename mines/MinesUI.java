package mines;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MinesUI extends Application {

    private Mines field;
    private HBox root;
    private Button[][] buttons;
    private GridPane grid;
    private Controller cont;
    private Stage stage;

    // Color styles for different numbers and flags
    private final Map<String, String> colors = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        initColors();
        loadFXML();

        Scene scene = new Scene(root);
        stage.setScene(scene);

        setupReset();
        stage.show();
    }

    // Initialize color styles for different mine counts
    private void initColors() {
        colors.put("1", "-fx-text-fill: blue; -fx-font-weight: bold;");
        colors.put("2", "-fx-text-fill: green; -fx-font-weight: bold;");
        colors.put("3", "-fx-text-fill: red; -fx-font-weight: bold;");
        colors.put("4", "-fx-text-fill: purple; -fx-font-weight: bold;");
        colors.put("5", "-fx-text-fill: brown; -fx-font-weight: bold;");
        colors.put("6", "-fx-text-fill: cyan; -fx-font-weight: bold;");
        colors.put("7", "-fx-text-fill: black; -fx-font-weight: bold;");
        colors.put("8", "-fx-text-fill: darkgrey; -fx-font-weight: bold;");
        colors.put("F", "-fx-text-fill: purple; -fx-font-weight: bold;");
    }

    private void loadFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MineSweeper.fxml"));
        root = loader.load();
        cont = loader.getController();
    }

    // Setup reset button functionality
    private void setupReset() {
        cont.getReset().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                buildBoard();
            }
        });
        buildBoard(); // initial board
    }

    // Build new game board with specified dimensions
    private void buildBoard() {
        int h = (int) cont.getHeight();
        int w = (int) cont.getWidth();
        int m = (int) cont.getMines();

        field = new Mines(h, w, m);
        buttons = new Button[h][w];

        StackPane stack = (StackPane) root.getChildren().get(1);
        if (grid != null) stack.getChildren().remove(grid);
        grid = createGrid(h, w);
        stack.getChildren().add(grid);
        stage.sizeToScene();
    }

    // Create grid layout with buttons and event handlers
    private GridPane createGrid(int h, int w) {
        GridPane g = new GridPane();

        // Set up column constraints for equal width
        for (int i = 0; i < w; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / w);
            col.setHgrow(Priority.ALWAYS);
            g.getColumnConstraints().add(col);
        }

        // Set up row constraints for equal height
        for (int i = 0; i < h; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / h);
            row.setVgrow(Priority.ALWAYS);
            g.getRowConstraints().add(row);
        }

        // Create buttons with mouse event handlers
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

                final int row = i, col = j;

                // Handle mouse clicks - left click opens, right click flags
                buttons[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent ev) {
                        if (ev.getButton() == MouseButton.PRIMARY) {
                            boolean clear = field.open(row, col);
                            buttons[row][col].setStyle(colors.get(field.get(row, col)));

                            if (!clear) {
                                field.setShowAll(true);
                                showMsg("Game Over!", "You lost!\nBetter luck next time!");
                            } else if (field.isDone()) {
                                field.setShowAll(true);
                                showMsg("Congratulations!", "You won the game!");
                            }
                        } else if (ev.getButton() == MouseButton.SECONDARY) {
                            field.toggleFlag(row, col);
                        }
                        updateButtons();
                    }
                });

                buttons[i][j].setText(field.get(i, j));
                g.add(buttons[i][j], j, i);
            }
        }

        g.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return g;
    }

    // Update all button displays with current game state
    private void updateButtons() {
        for (int r = 0; r < buttons.length; r++) {
            for (int c = 0; c < buttons[0].length; c++) {
                String val = field.get(r, c);
                buttons[r][c].setText(val);
                buttons[r][c].setStyle(colors.get(val));
            }
        }
    }

    // Show game message dialog
    private void showMsg(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}