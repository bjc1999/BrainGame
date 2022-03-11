import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javafx.embed.swing.SwingNode;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.apache.commons.collections15.Transformer;

public class GraphSetup {

    private static Graph<Integer, Synapse> graph;
    private Layout<Integer, Synapse> layout;
    private VisualizationViewer<Integer, Synapse> vv;

    public void setLayout() {
        layout = new FRLayout<>(graph);
        layout.setSize(new Dimension(600, 600));
    }

    public void setGraph(Graph<Integer, Synapse> graph) {
        this.graph = graph;
    }
    
    public Graph<Integer, Synapse> getGraph(){
        return graph;
    }

    public SwingNode setup() {
        setLayout();

        //change edge colour 
        Transformer<Synapse, Paint> edgeColor = new Transformer<Synapse, Paint>() {
            public Paint transform(Synapse s) {
                return Color.getHSBColor(0, 0, 18.43f);
            }
        };

        //change node colour 
        Transformer<Integer, Paint> vertexColor = new Transformer<Integer, Paint>() {
            public Paint transform(Integer i) {
                return Color.getHSBColor(180f, 0.96f, 40.78f);
            }
        };
        
         //change node size
        Transformer<Integer, Shape> vertexSize = new Transformer<Integer, Shape>() {
            public Shape transform(Integer i) {
                Ellipse2D circle = new Ellipse2D.Double(-13, -13, 20, 20);
                return circle;
            }
        };

        vv = new VisualizationViewer<>(layout);
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
        vv.setPreferredSize(new Dimension(600,600));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);
        SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JPanel panel = vv;
                panel.setBackground(Color.WHITE);
                swingNode.setContent(panel);
            }
        });

        return swingNode;
    }

    public static void deleteNeuron(int ID) {
        graph.removeVertex(ID);
    }

    public SwingNode changePath(ArrayList<Integer> nodeList, ArrayList<Synapse> edgeList) {

        //change node colour 
        Transformer<Integer, Paint> vertexColor = new Transformer<Integer, Paint>() {
            public Paint transform(Integer i) {
                if (nodeList.contains(i)) {
                    if (nodeList.get(0) == i) {
                        return Color.RED;
                    } else if (nodeList.get(nodeList.size() - 1) == i) {
                        return Color.YELLOW;
                    } else {
                        return Color.GREEN;
                    }

                }
                return Color.getHSBColor(180f, 0.96f, 40.78f);
            }
        };

        //change node size
        Transformer<Integer, Shape> vertexSize = new Transformer<Integer, Shape>() {
            public Shape transform(Integer i) {
                Ellipse2D circle = new Ellipse2D.Double(-13, -13, 20, 20);
                // in this case, the vertex is twice as large
                if (nodeList.contains(i)) {
                    return AffineTransform.getScaleInstance(1.5, 1.5).createTransformedShape(circle);
                } else {
                    return circle;
                }
            }
        };

        //change edge colour 
        Transformer<Synapse, Paint> edgeColor = new Transformer<Synapse, Paint>() {
            public Paint transform(Synapse s) {
                for (int iterate = 0; iterate < edgeList.size(); iterate++) {
                    if (edgeList.get(iterate).equals(s)) {
                        return Color.RED;
                    }
                }
                return Color.getHSBColor(0, 0, 18.43f);
            }
        };

        //change edge thicknesss
        Transformer<Synapse, Stroke> edgeStroke = new Transformer<Synapse, Stroke>() {
            public Stroke transform(Synapse s) {
                for (int iterate = 0; iterate < edgeList.size(); iterate++) {
                    if (edgeList.get(iterate).equals(s)) {
                        return new BasicStroke(5f);
                    }
                }
                return new BasicStroke(1f);
            }
        };
        
        //change arrow thkcness
        Transformer<Synapse,Stroke> edgeArrowStroke = new Transformer<Synapse,Stroke>(){
            public Stroke transform(Synapse s){
                for (int iterate = 0; iterate < edgeList.size(); iterate++) {
                    if (edgeList.get(iterate).equals(s)) {
                        return new BasicStroke(3f);
                    }
                }
                return new BasicStroke(1f);
            }
        };

        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
        vv.getRenderContext().setEdgeArrowStrokeTransformer(edgeArrowStroke);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStroke);
        
        SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JPanel panel = vv;
                swingNode.setContent(panel);
            }
        });

        return swingNode;

    }
    
    public String toString(){
        return graph.getVertexCount()+"";
    }
}
