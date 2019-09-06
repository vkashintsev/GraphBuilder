import org.jgraph.JGraph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class GraphPanelMouseListener implements MouseListener {
    JGraph graph;
    MainFrame frame;
    public GraphPanelMouseListener(JGraph c, MainFrame f) {
        graph = c;
        frame = f;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (graph.getSelectionCell() != null) {
                frame.clickBySelected(e);
            }
            else {
                frame.clickByEmpty(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}