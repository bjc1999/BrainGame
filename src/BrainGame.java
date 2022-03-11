import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

public class BrainGame extends Application{
    
    private static double xOffset=0;
    private static double yOffset = 0;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root);
        //change my program icon
        Image logo=new Image(getClass().getResourceAsStream("res/logo.png"));
        stage.getIcons().add(logo);
        stage.setScene(scene);
        stage.show();
        System.out.println("");
        draggable(root, stage);
        
        minimize();
        
    }
    
    /**Enable 'draggable' functionality of window
     * @param root the parent pane of the window
     * @param stage the window to be dragged
     */
    public static void draggable(Parent root, Stage stage){
        //get pressed point -> Windows x,y
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        
        // make it move whenever user drag
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            } 
        });
    }
    
    /**Enable minimization functionality by implementing jna library
     */
    public void minimize(){
        long lhwnd = com.sun.glass.ui.Window.getWindows().get(0).getNativeWindow();
        Pointer lpVoid = new Pointer(lhwnd);
        WinDef.HWND hwnd = new WinDef.HWND(lpVoid);
        final User32 user32 = User32.INSTANCE;
        int oldStyle = user32.GetWindowLong(hwnd, GWL_STYLE);
        int newStyle = oldStyle | 0x00020000;//WS_MINIMIZEBOX
        user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
    }
     
}
   