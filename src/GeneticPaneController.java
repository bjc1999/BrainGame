import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class GeneticPaneController implements Initializable {
    private SearchSpace space;
    private GraphSetup graphObject;
    private SwingNode defaultGraphPane;
    private SwingNode changedGraphPane;
    private GeneticAlgorithm search;
    private LineChart<Number, Number> lineChart;
    private static boolean isEnd;
    private Timeline tl;
    protected static Stage chartStage;
    private static XYChart.Series series, seriesFittest, seriesWorst;
    
    @FXML
    private JFXTextField startnode;
    @FXML
    private JFXTextField endnode;
    @FXML
    private AnchorPane geneticPane;
    @FXML
    private TextField population;
    @FXML
    private TextField mutateRate;
    @FXML
    private TextField crossRate;
    @FXML
    private TextField elitism;
    @FXML
    private TextField tournament;
    @FXML
    private Rectangle animateback;
    @FXML
    private Circle animatebtn;
    @FXML
    private ScrollPane consolePane;
    @FXML
    private TextArea console;
    @FXML
    private Pane graphPane;
    @FXML
    private JFXButton searchpath1;
    @FXML
    private JFXButton searchpath2;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        space = new SearchSpace(1);
        graphObject = new GraphSetup();
        defaultGraphPane = graphObject.setup();
        graphPane.getChildren().add(defaultGraphPane);
        
        //create chart
        chartStage = new Stage();
        final NumberAxis xAxis = new NumberAxis(1, 20, 1);
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        
        //creating the chart line
        lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Performance");
        
        //defining a series
        series = new XYChart.Series();
        seriesFittest = new XYChart.Series();
        seriesWorst = new XYChart.Series();
        series.setName("Average Fitness");
        seriesFittest.setName("Fittest Individual");
        seriesWorst.setName("Worst Individual");
        chartStage.initStyle(StageStyle.UNDECORATED);
        Scene scene  = new Scene(lineChart,800,600);
        BrainGame.draggable(scene.getRoot(), chartStage);
        lineChart.setCreateSymbols(false);
       
        chartStage.setScene(scene);
        

    }
    
    public void displayPath(ArrayList<Integer> path){
        ArrayList<Integer> nodeList = new ArrayList<>();
        ArrayList<Synapse> edgeList = new ArrayList<>();
        for (int iterate1 = 0; iterate1 < path.size(); iterate1++) {
            nodeList.add(path.get(iterate1));
            if (iterate1 > 0) {
                Neuron temp = space.getTreeMap().get(path.get(iterate1 - 1));
                edgeList.add(temp.getSynapseTo(path.get(iterate1)));
            }
        }
        showPath(nodeList,edgeList);
    }
    
    public void showPath(ArrayList<Integer> nodeList , ArrayList<Synapse> edgeList) {
        changedGraphPane = graphObject.changePath(nodeList, edgeList);
        graphPane.getChildren().add(changedGraphPane);
    }

    @FXML
    private void back_program(MouseEvent event) throws IOException {
        try{
            FXMLLoader loader= new FXMLLoader(getClass().getResource("RunSearch.fxml"));
            AnchorPane root= (AnchorPane) loader.load();
            geneticPane.getChildren().removeAll();
            geneticPane.getChildren().setAll(root);  
            
            RunSearchController control=loader.getController();
            control.setHeader("Genetic Algorithm", 50.0);
            control.setData(space, graphObject.getGraph());
            chartStage.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void close_program(MouseEvent event) {
         System.exit(0);
    }

    @FXML
    private void searchpath() {
        searchpath2.setText("Searching...");
        if(!isValid())
            JOptionPane.showMessageDialog(null,"Please fill in all the factors for Genetic Algorithm", "Error", 0);
        else{
            console.setText("Start\n");
            int start = Integer.parseInt(startnode.getText());
            int end = Integer.parseInt(endnode.getText());
            if(space.contains(start)&&space.contains(end)&&space.hasNext(start, 0)){
                lineChart.getData().addAll(series, seriesFittest, seriesWorst);
                searchpath1.setVisible(false);
                searchpath2.setVisible(true);
                isEnd=false;
                double fps;
                graphPane.getChildren().remove(defaultGraphPane);
                search = new GeneticAlgorithm(space.getTreeMap(),Integer.parseInt(population.getText()),Double.parseDouble(mutateRate.getText()),
                        Double.parseDouble(crossRate.getText()), Integer.parseInt(elitism.getText()),Integer.parseInt(tournament.getText()));
                search.setInitPopulation(start, end);
                if(animatebtn.getTranslateX()==0)
                        fps = 1;
                    else
                        fps = 100;
                
                tl = new Timeline();
                tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(fps), 
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent actionEvent) {
                            search.search(start, end);
                            GeneticPaneController.addData(search.getCurrentGeneration()-1, search.getPopulationFitness(),
                                    search.getIndividualFitness(0), search.getIndividualFitness(19));
                            if(animatebtn.getTranslateX()==18)
                                displayPath(search.getFittestIndividual().getPath());
                            if(!isEnd)
                                console.appendText("Generation: "+(search.getCurrentGeneration()-1)+"\n");
                            console.appendText(search.console()+"\n");
                            if(isEnd){
                                if(search.getPath()==null){
                                    displayPath(new ArrayList<Integer>());
                                    console.appendText("No Path Available\nEnd\n");
                                }
                                else{
                                    displayPath(search.getPath());
                                    console.appendText("End\n");
                                }
                                tl.stop();
                                searchpath2.setText("Done");
                            }
                        }
                    }
                ));
                tl.setCycleCount(Animation.INDEFINITE);
                tl.setAutoReverse(true);
                tl.play();
            }else{
                searchpath2.setText("Done");
                displayPath(new ArrayList<>());
                JOptionPane.showMessageDialog(null,"No Path Available.");
                search = null;
                console.appendText("No Path Available\nEnd\n");
            }
        } 
       
    }
    
    @FXML
    private void searching(){
        lineChart.getData().clear();
        series.getData().clear();
        seriesFittest.getData().clear();
        seriesWorst.getData().clear();
        searchpath1.setVisible(true);
        searchpath2.setVisible(false);
        
        if(search!=null && search.getPath()!=null)
            space.deductLifeTimes(search.getPath());
        
        defaultGraphPane = graphObject.changePath(new ArrayList<Integer>(), new ArrayList<Synapse>());
        graphPane.getChildren().clear();
        graphPane.getChildren().add(defaultGraphPane);
        search.reset();
    }
    
    public static void setIsEnd(boolean cond){
        isEnd = cond;
    }

    @FXML
    private void showTable(MouseEvent event) {
        RunSearchController.tableStage.show();
    }    

    @FXML
    private void showGraph(MouseEvent event) {
        if(chartStage.isShowing())
            chartStage.close();
        else
            chartStage.show();
    }
    
    public static void addData(Integer generation, Double Fitness, Double fittest, Double worst){
        series.getData().add(new XYChart.Data<>(generation, Fitness));
        seriesFittest.getData().add(new XYChart.Data<>(generation, fittest));
        seriesWorst.getData().add(new XYChart.Data<>(generation, worst));
    }

    @FXML
    public void slideConsoleIn(){
        TranslateTransition openConsole=new TranslateTransition(new Duration(350), consolePane);
        openConsole.setToX(consolePane.getWidth());
        openConsole.play();
    }
    
    @FXML
    public void slideConsoleOut(){
        TranslateTransition closeConsole=new TranslateTransition(new Duration(350), consolePane);
        closeConsole.setToX(-(consolePane.getWidth()));
        closeConsole.play();
    }
    
    public TextArea getConsole(){
        return console;
    }
    
    @FXML
    public void animateSwitch(){
        TranslateTransition slideOff = new TranslateTransition(new Duration(50), animatebtn);
        slideOff.setToX(0);
        
        TranslateTransition slideOn = new TranslateTransition(new Duration(50), animatebtn);
        slideOn.setToX(18);
        
        if(animatebtn.getTranslateX()!=0){
            slideOff.play();
            animateback.setFill(Color.WHITE);
        }else{
            slideOn.play();
            animateback.setFill(Color.GREENYELLOW);
        }
    }
    
    public boolean isValid(){
        boolean validator = true;
        
        try{
          Integer.parseInt(population.getText());
          validator = validator & true;
        }catch( Exception e ){ 
          validator = validator & false;
        }
        
        try{
          Double.parseDouble(mutateRate.getText());
          validator = validator & true;
        }catch( Exception e ){ 
          validator = validator & false;
        }
        
        try{
          Double.parseDouble(crossRate.getText());
          validator = validator & true;
        }catch( Exception e ){ 
          validator = validator & false;
        }
        
        try{
          Integer.parseInt(elitism.getText());
          validator = validator & true;
        }catch( Exception e ){ 
          validator = validator & false;
        }
        
        try{
          Integer.parseInt(tournament.getText());
          validator = validator & true;
        }catch( Exception e ){ 
          validator = validator & false;
        }
        
        return validator;
    }
}
