import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jgraph.JGraph;
import org.jgraph.graph.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    JGraph graph;
    GraphModel model;
    JPopupMenu popupMenu;
    JMenuItem addMenuItem, removeMenuItem, setSourceMenuItem, setTargetMenuItem, bindMenuItem, setLoopMenuItem, bindAllMenuItem,
                    saveMenuItem, loadMenuItem, saveImageMenuItem, matrixMenuItem, characteristicMenuItem, aStarMenuItem;
    Point point;
    DefaultGraphCell source, target;
    public MainFrame(){
        setTitle("Построитель графов");
        model = new DefaultGraphModel();
        graph = new JGraph(model);
        graph.setSizeable(false);
        graph.addMouseListener(new GraphPanelMouseListener(graph, this));
        getContentPane().add(new JScrollPane(graph));
        createPopupMenu();
        setBounds(50,50,512,512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void createPopupMenu(){
        ActionListener actionListener = new PopupActionListener(this);
        popupMenu = new JPopupMenu();
        addMenuItem = new JMenuItem("Добавить вершину");
        addMenuItem.addActionListener(actionListener);
        popupMenu.add(addMenuItem);
        removeMenuItem = new JMenuItem("Удалить");
        removeMenuItem.addActionListener(actionListener);
        popupMenu.add(removeMenuItem);
        setSourceMenuItem = new JMenuItem("Выбрать как начальную вершину");
        setSourceMenuItem.addActionListener(actionListener);
        popupMenu.add(setSourceMenuItem);
        setTargetMenuItem = new JMenuItem("Выбрать как конечную вершину");
        setTargetMenuItem.addActionListener(actionListener);
        popupMenu.add(setTargetMenuItem);
        setLoopMenuItem = new JMenuItem("Сделать петлю");
        setLoopMenuItem.addActionListener(actionListener);
        popupMenu.add(setLoopMenuItem);
        bindMenuItem = new JMenuItem("Связать вершины");
        bindMenuItem.addActionListener(actionListener);
        popupMenu.add(bindMenuItem);
        bindAllMenuItem = new JMenuItem("Связать выделенные вершины");
        bindAllMenuItem.addActionListener(actionListener);
        popupMenu.add(bindAllMenuItem);
        saveMenuItem = new JMenuItem("Сохранить граф");
        saveMenuItem.addActionListener(actionListener);
        popupMenu.add(saveMenuItem);
        saveImageMenuItem = new JMenuItem("Сохранить как картинку");
        saveImageMenuItem.addActionListener(actionListener);
        popupMenu.add(saveImageMenuItem);
        loadMenuItem = new JMenuItem("Загрузить граф");
        loadMenuItem.addActionListener(actionListener);
        popupMenu.add(loadMenuItem);

        matrixMenuItem = new JMenuItem("Построить матрицу смежности");
        matrixMenuItem.addActionListener(actionListener);
        popupMenu.add(matrixMenuItem);

        characteristicMenuItem = new JMenuItem("Получить характеристики");
        characteristicMenuItem.addActionListener(actionListener);
        popupMenu.add(characteristicMenuItem);

        aStarMenuItem= new JMenuItem("A*");
        aStarMenuItem.addActionListener(actionListener);
        popupMenu.add(aStarMenuItem);

        graph.setComponentPopupMenu(popupMenu);
    }

    public void paint(Graphics g){
        graph.refresh();
    }
    public void update(Graphics g) {
        paint(g);
    }


    public void clickBySelected(MouseEvent e){
        addMenuItem.setVisible(false);
        removeMenuItem.setVisible(true);
        setSourceMenuItem.setVisible(true);
        setTargetMenuItem.setVisible(true);
        setLoopMenuItem.setVisible(true);
        bindAllMenuItem.setVisible(true);
        saveMenuItem.setVisible(false);
        loadMenuItem.setVisible(false);
        saveImageMenuItem.setVisible(false);
        aStarMenuItem.setVisible(false);
        if (source != null && target != null) {
            bindMenuItem.setVisible(true);
            aStarMenuItem.setVisible(true);
        }
        else {
            bindMenuItem.setVisible(false);
            aStarMenuItem.setVisible(false);
        }
        point = e.getPoint();
    }
    public void clickByEmpty(MouseEvent e){
        addMenuItem.setVisible(true);
        removeMenuItem.setVisible(false);
        setSourceMenuItem.setVisible(false);
        setTargetMenuItem.setVisible(false);
        setLoopMenuItem.setVisible(false);
        bindAllMenuItem.setVisible(false);
        saveMenuItem.setVisible(true);
        loadMenuItem.setVisible(true);
        saveImageMenuItem.setVisible(true);

        if (source != null && target != null) {
            bindMenuItem.setVisible(true);
            aStarMenuItem.setVisible(true);
        }
        else {
            bindMenuItem.setVisible(false);
            aStarMenuItem.setVisible(false);
        }
        point = e.getPoint();
    }
    public void addVertex(){
        GraphHelper.createVertex(graph, "#"+graph.getGraphLayoutCache().getCells(false, true, false, false).length, point.getX(), point.getY());
    }
    public void setSource(){
        source = (DefaultGraphCell) graph.getSelectionCell();
    }
    public void setTarget(){
        target = (DefaultGraphCell) graph.getSelectionCell();
    }
    public void bindVertex(){
        GraphHelper.createEdge(graph, "5", (DefaultGraphCell)source.getChildAt(0), (DefaultGraphCell)target.getChildAt(0), true);
        source = null;
        target = null;
    }
    public void bindAllVertex(){
        Object[] cells = graph.getSelectionCells(graph.getGraphLayoutCache().getCells(false, true, false, false));
        for (int i = 0, cellsLength = cells.length; i < cellsLength; i++) {
            DefaultGraphCell left = (DefaultGraphCell)cells[i];
            for (int j = i+1, cells1Length = cells.length; j < cells1Length; j++) {
                DefaultGraphCell right = (DefaultGraphCell) cells[j];
                GraphHelper.createEdge(graph, "5", (DefaultGraphCell)left.getChildAt(0), (DefaultGraphCell)right.getChildAt(0), false);
            }
        }
    }
    public void removeSelectedVertex(){
        Object[] removedCells = graph.getDescendants(graph.getSelectionCells());
        graph.getGraphLayoutCache().remove(removedCells, false, true);
        source = null;
        target = null;
    }
    public void setLoop(){
        for (Object cell :  graph.getSelectionCells(graph.getGraphLayoutCache().getCells(false, true, false, false))){
            DefaultGraphCell tmp = (DefaultGraphCell)cell;
            GraphHelper.createEdge(graph, "5", (DefaultGraphCell)tmp.getChildAt(0), (DefaultGraphCell)tmp.getChildAt(0), true);
        }
    }
    public void saveToFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);
        if (fileChooser.showDialog(null, "Сохранить файл") != JFileChooser.APPROVE_OPTION)
            return;
        FileOutputStream fos = null;
        File file = fileChooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if(!path.endsWith(".xml")) {
            file = new File(path + ".xml");
        }
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        XMLEncoder encoder = new XMLEncoder(bos);
        for (Object cell : graph.getGraphLayoutCache().getCells(true, true, true, true))
            encoder.writeObject(cell);
        encoder.close();
    }
    public void loadFromFile(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);
        if (fileChooser.showDialog(null, "Загрузить файл") != JFileChooser.APPROVE_OPTION)
            return;

        FileInputStream fos = null;
        File file = fileChooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if(!path.endsWith(".xml")) {
            file = new File(path + ".xml");
        }
        try {

            fos = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedInputStream bos = new BufferedInputStream(fos);
        XMLDecoder encoder = new XMLDecoder(bos);
        Object element;
        graph.getGraphLayoutCache().remove(graph.getGraphLayoutCache().getCells(true,true,true,true));
        while (true)
        {
            try{
                element = encoder.readObject();
            }
            catch (Exception e)
            {
                break;
            }
            if (element == null)
                break;
            graph.getGraphLayoutCache().insert(element);
        }
        encoder.close();
    }
    public void saveToImage(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("jpg files (*.jpg)", "jpg");
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);
        if (fileChooser.showDialog(null, "Сохранить картинку") != JFileChooser.APPROVE_OPTION)
            return;
        File file = fileChooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if(!path.endsWith(".jpg")) {
            file = new File(path + ".jpg");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Color bg = null;
        bg = graph.getBackground();
        BufferedImage img = graph.getImage(bg, GraphConstants.DEFAULTINSET);
        try {
            ImageIO.write(img, "jpg", fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void printMatrix(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("excel files (*.xls)", "xls");
        fileChooser.addChoosableFileFilter(xmlFilter);
        fileChooser.setFileFilter(xmlFilter);
        if (fileChooser.showDialog(null, "Сохранить картинку") != JFileChooser.APPROVE_OPTION)
            return;
        File file = fileChooser.getSelectedFile();
        String path = file.getAbsolutePath();
        if(!path.endsWith(".xls")) {
            file = new File(path + ".xls");
        }

        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Matrix");
        Object[] vertex = graph.getGraphLayoutCache().getCells(false,true,false,false);
        int length = vertex.length;
        Row[] rows = new Row[length+1];
        Cell[][] cells = new Cell[length+1][length+1];
        for (int i = 0; i < length+1; i++){
            rows[i] = sheet.createRow(i);
            for (int j = 0; j < length+1; j++) {
                cells[i][j] = rows[i].createCell(j);
                cells[i][j].setCellValue("");
            }
        }
        cells[0][0].setCellValue("Vertex");
        for (int i = 1; i < length+1; i++){
            cells[i][0].setCellValue(vertex[i-1].toString());
            cells[0][i].setCellValue(vertex[i-1].toString());
        }
        Object[] edges = graph.getGraphLayoutCache().getCells(false,false, false, true);

        for (Object obj : edges){
            DefaultEdge edge = (DefaultEdge)obj;
            DefaultGraphCell target = (DefaultGraphCell)(edge.getTarget());
            DefaultGraphCell source = (DefaultGraphCell)(edge.getSource());

            int i = 0;
            int j = 0;
            for (; i < length; i++)
                if (vertex[i] == target.getParent())
                    break;
            for (; j < length; j++)
                if (vertex[j] == source.getParent())
                    break;

            if (GraphConstants.getLineEnd(edge.getAttributes()) != GraphHelper.ARROW) {
                cells[j+1][i+1].setCellValue(obj.toString());
                cells[i+1][j+1].setCellValue(obj.toString());
            }
            else {
                cells[j+1][i+1].setCellValue(obj.toString());
            }
        }
        try {
            book.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void getCharacteristic(){
        Object[] edges = graph.getGraphLayoutCache().getCells(false,false, false, true);
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Object obj : edges){
            int edge = Integer.parseInt(obj.toString());
            if (edge < min)
                min = edge;
            if (edge > max)
                max = edge;
        }
        ArrayList<Object>center = new ArrayList<>();
        for (Object obj : edges){
            int tmp = Integer.parseInt(obj.toString());
            if (tmp == min){
                DefaultEdge edge = (DefaultEdge) obj;
                DefaultGraphCell source = (DefaultGraphCell)(edge.getSource());
                DefaultGraphCell target = (DefaultGraphCell)(edge.getTarget());
                if (GraphConstants.getLineEnd(edge.getAttributes()) == GraphConstants.ARROW_TECHNICAL){
                    if (!center.contains(target.getParent()))
                        center.add(target.getParent());
                }
                else {
                    if (!center.contains(source.getParent()))
                        center.add(source.getParent());
                    if (!center.contains(target.getParent()))
                            center.add(target.getParent());
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Радиус: %d\nДиаметр: %d\nЦентральные вершины:\n", min,max));
        for (Object obj : center)
            sb.append(obj + ";");
        Object[] vertexList = graph.getGraphLayoutCache().getCells(false,true,false,false);
        sb.append("\nВектор степеней вершин:\n");
        Map<Object, Integer> vector = new HashMap<>();
        for (Object obj : vertexList)
            vector.put(obj, 0);
        for (Object obj : vertexList) {
            for (Object edge : graph.getGraphLayoutCache().getOutgoingEdges(obj, null,true,true)){
                DefaultEdge edg = (DefaultEdge) edge;
                if (GraphConstants.getLineEnd( edg.getAttributes()) == GraphConstants.ARROW_TECHNICAL){
                    vector.replace(obj, vector.get(obj)+1);
                }
                else {
                    Object target = ((DefaultGraphCell)(edg.getTarget())).getParent();
                    Object source = ((DefaultGraphCell)(edg.getSource())).getParent();
                    vector.replace(target, vector.get(target)+1);
                    vector.replace(source, vector.get(source)+1);
                }
            }
        }
        vector = MapUtil.sortByValue(vector);
        for (Object obj : vector.keySet()) {
            sb.append(obj + ";");
        }
        sb.append("\n");
        for (int value : vector.values()) {
            sb.append(value + ";");
        }
        JDialog dlg = new JDialog();
        JTextArea tf = new JTextArea(sb.toString());
        tf.setEnabled(false);
        tf.setDisabledTextColor(Color.blue);
        dlg.add(tf);
        dlg.pack();
        dlg.setVisible(true);
    }

    public void findWayAstar(){
        refreshColors();
        HashMap<DefaultGraphCell, Integer> visited = new HashMap<>();
        ArrayList<DefaultGraphCell> frontier = new ArrayList<>();
        HashMap<DefaultGraphCell, DefaultGraphCell> trueWay = new HashMap<>();
        frontier.add(source);
        visited.put(source, 0);
        while (!frontier.isEmpty()) {
            DefaultGraphCell current = frontier.get(0);
            frontier.remove(0);
            Integer way = visited.get(current);
            for (Object edge : graph.getGraphLayoutCache().getOutgoingEdges(current, null, true, false)){
                DefaultGraphCell next = (DefaultGraphCell)((DefaultGraphCell)((DefaultEdge) edge).getTarget()).getParent();
                if (!visited.containsKey(next)){
                    frontier.add(next);
                    visited.put(next, way + Integer.parseInt(edge.toString()));
                    trueWay.put(next, (DefaultGraphCell)((DefaultGraphCell)((DefaultEdge) edge).getSource()).getParent());
                    continue;
                }
                if ((visited.get(next) > way + Integer.parseInt(edge.toString()))){
                    frontier.add(next);
                    visited.replace(next, way + Integer.parseInt(edge.toString()));
                    trueWay.replace(next, (DefaultGraphCell)((DefaultGraphCell)((DefaultEdge) edge).getSource()).getParent());
                }
            }
        }
        for (DefaultGraphCell cell : trueWay.values())
            setColor(cell);
        setColor(target);
    }
    public void setColor(Object item) {
        GraphLayoutCache cache = graph.getGraphLayoutCache();
        GraphCell cell = (GraphCell) item;
        CellView view = cache.getMapping(cell, true);
        AttributeMap map = view.getAttributes();
        map.applyValue(GraphConstants.BACKGROUND, Color.GREEN);

        cache.reload();
        graph.repaint();
        graph.refresh();
    }
    public void refreshColors() {
        GraphLayoutCache cache = graph.getGraphLayoutCache();
        for (Object item : graph.getRoots()) {
            GraphCell cell = (GraphCell) item;
            CellView view = cache.getMapping(cell, true);
            AttributeMap map = view.getAttributes();
            map.applyValue(GraphConstants.BACKGROUND, Color.BLUE);
        }
        cache.reload();
        graph.repaint();
        graph.refresh();
    }

}