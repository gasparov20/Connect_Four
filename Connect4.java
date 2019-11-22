// Connect4
// Andrew Gasparovich
// CIS 296
// Prof John Baugh

package Connect4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Connect4 extends Application {
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ConnectFXML.fxml"));
        stage.setTitle("Connect Four by Andrew Gasparovich");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}