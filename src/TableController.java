import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class TableController implements Initializable {

    @FXML
    private TableColumn<TableNode, Integer> nodeID;
    @FXML
    private TableColumn<TableNode, Integer> numConnection;
    @FXML
    private TableColumn<TableNode, Integer> lifetime;
    @FXML
    private TableColumn<TableNode, Integer> source;
    @FXML
    private TableColumn<TableNode, Integer> destination;
    @FXML
    private TableColumn<TableNode, Integer> time;
    @FXML
    private TableColumn<TableNode, Integer> distance;
    @FXML
    private TableView<TableNode> neuronTab;
    @FXML
    private TableView<TableNode> synapseTab;
    @FXML
    private ImageView closebtn;

    private static ObservableList<TableNode> nodeData = FXCollections.observableArrayList();
    private static ObservableList<TableNode> synapseData = FXCollections.observableArrayList();
    private static BooleanProperty isChanged;
    
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
        numConnection.setCellValueFactory(new PropertyValueFactory<>("numConnection"));
        lifetime.setCellValueFactory(new PropertyValueFactory<>("lifetime"));
        source.setCellValueFactory(new PropertyValueFactory<>("source"));
        destination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        isChanged = new SimpleBooleanProperty(false);
        
        isChanged.addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    neuronTab.refresh();
                    isChanged.set(false);
                }
            }
        });
        
        neuronTab.setItems(nodeData);
        synapseTab.setItems(synapseData);
        
    }
    
    /**Add a neuron data into neuron table
     * @param nodeID
     * @param numConnection
     * @param lifetime 
     */
    public void addTableNode(Integer nodeID, Integer numConnection, Integer lifetime){
        nodeData.add(new TableNode (nodeID, numConnection, lifetime));
    }
    
    /**Add a synapse data into synapse table
     * @param source
     * @param destination
     * @param time
     * @param distance 
     */
    public void addTableSynapse(Integer source, Integer destination, Integer time, Integer distance){
        synapseData.add(new TableNode (source, destination, time, distance));
    }
    
    public static void deductLifetime(int id){
        nodeData.stream().filter((ptr) -> (ptr.getNodeID() == id)).forEachOrdered((ptr) -> {
            ptr.setLifetime(ptr.getLifetime()-1);
        });
        isChanged.set(true);
    }
    
    public static void removeNode(int id){
        for(TableNode ptr: nodeData)
            if(ptr.getNodeID() == id){
                nodeData.remove(ptr);
                break;
            }
    }
    
    public static void removeSynapse(int id){
        ArrayList<Integer> checkList = new ArrayList<Integer>();
        for(int i=0; i<synapseData.size(); i++){
            if(synapseData.get(i).getSource() == id || synapseData.get(i).getDestination() == id){
                if(synapseData.get(i).getDestination() == id)
                    checkList.add(synapseData.get(i).getSource());
                synapseData.remove(i);
                i--;
            }
        }
        checkList.sort(null);
        int k=0;
        for(int i=0; i<nodeData.size(); i++){
            if(nodeData.get(i).getNodeID() == checkList.get(k)){
                nodeData.get(i).setNumConnection(nodeData.get(i).getNumConnection()-1);
                k++;
            }
            if(k == checkList.size())
                break;
        }
    }
    
    public void removeSourceSynapse(int id){
        for(int i=0; i<synapseData.size(); i++){
            if(synapseData.get(i).getSource() == id){
                synapseData.remove(i);
                i--;
            }
        }
    }
    
    @FXML
    private void closeButtonAction(){
        Stage stage = (Stage) closebtn.getScene().getWindow();
        stage.close();
    }
    
    public void clear(){
        nodeData.clear();
        synapseData.clear();
    }
    
}

