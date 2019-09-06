import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PopupActionListener implements ActionListener {
    MainFrame frame;
    public PopupActionListener(MainFrame f){
        frame = f;
    }
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()){
            case "Добавить вершину":
                frame.addVertex();
                break;
            case "Выбрать как начальную вершину":
                frame.setSource();
                break;
            case "Выбрать как конечную вершину":
                frame.setTarget();
                break;
            case "Связать вершины":
                frame.bindVertex();
                break;
            case "Удалить":
                frame.removeSelectedVertex();
                break;
            case "Сделать петлю":
                frame.setLoop();
                break;
            case "Связать выделенные вершины":
                frame.bindAllVertex();
                break;
            case "Сохранить граф":
                frame.saveToFile();
                break;
            case "Загрузить граф":
                frame.loadFromFile();
                break;
            case "Сохранить как картинку":
                frame.saveToImage();
                break;
            case "Построить матрицу смежности":
                frame.printMatrix();
                break;
            case "Получить характеристики":
                frame.getCharacteristic();
                break;
            case "A*":
                frame.findWayAstar();
                break;

        }
    }
}