package edgeConnectivity;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

class EdgeConnectivity extends JFrame {
    private Graph graph = null;
    private Box box1, box2;

    EdgeConnectivity() {
        super("Курсовой проект");

        //создаем окно
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        //создаем меню
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Меню");

        JMenuItem help = new JMenuItem("Справочная информация");
        menu.add(help).addActionListener(e -> JOptionPane.showMessageDialog(EdgeConnectivity.this,
                "Граф - абстрактный математический объект, представляющий собой" + "\n" +
                        "множество вeршин графа и набор ребер, то есть соединений между" + "\n" +
                        "парами вершин. " + "\n" +
                        "Реберная связность графа - наименьшее количество" + "\n" +
                        "ребер, удаление которых приводит к несвязному или тривиальному " + "\n" +
                        "графу." + "\n" + "Например, для несвязного графа рёберная связность равна нулю. " + "\n" +
                        "Для связного графа с единственным мостом рёберная связность " + "\n" +
                        "равна единице."));

        JMenuItem helpEnter = new JMenuItem("Как осуществить ввод?");
        menu.add(helpEnter).addActionListener(e -> JOptionPane.showMessageDialog(EdgeConnectivity.this,
                "Условия для задания графа:" + "\n" +
                        "1) Первая строка - MxN, где M - количество строк, N - количество символов в строке " + "\n" +
                        "(строго \"*\" или целые числа)" + "\n" +
                        "2) Прописываем внешний вид графа, не забывая о заданных нами M и N" + "\n" +
                        "3) Записываем связи между вершинами" + "\n" +
                        "Пример:" + "\n" + "4x15" + "\n" + "**1*******2****" + "\n" + "***************" + "\n"
                        + "***************" + "\n" + "**4*******3****" + "\n" + "1 2" + "\n" + "1 4" + "\n"
                        + "1 3" + "\n" + "2 3" + "\n" + "2 4" + "\n" + "3 4"));

        JMenuItem enterGraph = new JMenuItem("Ввести граф");
        enterGraph.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            int show = fc.showDialog(null, "Открыть файл");
            if (show == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    graph = new Graph(file);
                } catch (NoInputFileException | InputFileIsEmptyException ex) {
                    JOptionPane.showMessageDialog(
                            box1.getParent(),
                            ex.getMessage(),
                            "Предупреждение",
                            JOptionPane.WARNING_MESSAGE
                    );
                } catch (IncorrectGraphEntryException ex) {
                    JOptionPane.showMessageDialog(
                            box1.getParent(),
                            ex.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                box1.removeAll();
                box1.add(new DrawGraph(graph, false));
                box1.revalidate();
                box1.repaint();
                box2.removeAll();
                box2.add(new DrawGraph(graph, true));
                box2.revalidate();
                box2.repaint();
            }

        });

        menu.add(enterGraph);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        box1 = Box.createVerticalBox();
        TitledBorder titledBorder1 = new TitledBorder("Введенный граф");
        box1.setBorder(titledBorder1);
        box1.add(new DrawGraph());
        add(box1);

        box2 = Box.createVerticalBox();
        TitledBorder titledBorder2 = new TitledBorder("Реберная связность");
        box2.setBorder(titledBorder2);
        box2.add(new DrawGraph());
        add(box2);

        pack();
        setLocationRelativeTo(null);  // центрируем окно
        setVisible(true);
    }
}
