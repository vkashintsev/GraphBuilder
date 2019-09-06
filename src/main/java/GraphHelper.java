import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

class GraphHelper{
    public final static int ARROW;
    static {
        ARROW = GraphConstants.ARROW_TECHNICAL;
    }
    public static void createEdge(JGraph graph, String name, DefaultGraphCell source, DefaultGraphCell target, boolean arrow){
        DefaultEdge edge = new DefaultEdge(name);
        edge.setSource(source);
        edge.setTarget(target);
        if (arrow) {
            GraphConstants.setLineEnd(edge.getAttributes(), ARROW);
            GraphConstants.setEndFill(edge.getAttributes(), true);
        }
        for (Object obj : graph.getGraphLayoutCache().getCells(false,false,false,true)){
            DefaultEdge graphEdge = (DefaultEdge)obj;
            Object _source = ((DefaultGraphCell) graphEdge.getSource()).getParent();
            Object _target = ((DefaultGraphCell) graphEdge.getTarget()).getParent();
            if (source.equals(_source) && target.equals(_target)) {
                return;
            }
        }

        graph.getGraphLayoutCache().insert(edge);
    }
    public static void createVertex(JGraph graph, String name, double x, double y) {
        DefaultGraphCell cell = new DefaultGraphCell(name);
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, 25, 25));
        GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createEtchedBorder());
        GraphConstants.setGradientColor(cell.getAttributes(), Color.blue);
        GraphConstants.setBackground(cell.getAttributes(), Color.blue);
        GraphConstants.setBorderColor(cell.getAttributes(), Color.blue);
        GraphConstants.setForeground(cell.getAttributes(), Color.GREEN);
        GraphConstants.setOpaque(cell.getAttributes(), true);
        cell.addPort();
        graph.getGraphLayoutCache().insert(cell);
    }
    public static void changeVertexColor(DefaultGraphCell vertex){
        GraphConstants.setForeground(vertex.getAttributes(), Color.green);
    }
}