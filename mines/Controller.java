package mines;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField heightText;
    @FXML
    private TextField minesText;
    @FXML
    private TextField widthText;
    @FXML
    private Button reset;

    private static final double DEFAULT = 10;

    public double getHeight() {
        String txt = heightText.getText();
        return txt.isEmpty() ? DEFAULT : Double.parseDouble(txt);
    }

    public double getWidth() {
        String txt = widthText.getText();
        return txt.isEmpty() ? DEFAULT : Double.parseDouble(txt);
    }

    public double getMines() {
        String txt = minesText.getText();
        return txt.isEmpty() ? DEFAULT : Double.parseDouble(txt);
    }

    public Button getReset() {
        return reset;
    }
}
