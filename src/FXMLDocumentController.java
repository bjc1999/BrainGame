import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane content_area;
    @FXML
    private ImageView geneticSearch;
    @FXML
    private ImageView normalSearch;
    
    private ScaleTransition st1 ;
    private ScaleTransition st2 ;
    
    
    public void initialize(URL url, ResourceBundle rb) {
        st1 = new ScaleTransition(Duration.millis(500), normalSearch);
        st2 = new ScaleTransition(Duration.millis(500), geneticSearch);
    }    

    @FXML
    /**Enable user to close the program 
     * by pressing the close button
     */
    private void close_program() {
        System.exit(0);
    }
    
    @FXML
    /**Change the pane of window according to the check box checked by user
     */
    public void run_search(MouseEvent event) throws IOException {
        try{
            FXMLLoader loader= new FXMLLoader(getClass().getResource("RunSearch.fxml"));
            AnchorPane root= (AnchorPane) loader.load();
            content_area.getChildren().removeAll();
            content_area.getChildren().setAll(root);  

            RunSearchController control=loader.getController();

            if(event.getSource().equals(normalSearch)){
                control.setHeader("Searching Algorithm", 40.0);
            }
            else{
                control.setHeader("Genetic Algorithm",50.0);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    @FXML
    private void searchAEnt(MouseEvent event) {
            st1.setByX(0.07f);
            st1.setByY(0.07f);
            st1.setCycleCount(2);
            st1.setAutoReverse(true);
            st1.play();
    }
    
    @FXML
    private void searchBEnt(MouseEvent event) {
            st2.setByX(0.07f);
            st2.setByY(0.07f);
            st2.setCycleCount(2);
            st2.setAutoReverse(true);
            st2.play();
    }
}