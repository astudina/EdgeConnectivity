package edgeConnectivity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;

public class DrawGraph extends JPanel {
    private int width, height;
    private boolean answer;
    private Graph graph;

    DrawGraph() {
        setPreferredSize(new Dimension(400, 300));
        width = 30;
        height = 30;
        graph = null;
    }

    DrawGraph(Graph graph, boolean answer) {
        this();
        this.answer = answer;
        this.graph = graph;
    }

    public void paint(Graphics g) {
        if (graph == null)
            return;

        width = this.getWidth() / graph.matrix[0].length;
        height = this.getHeight() / graph.matrix.length;
        width = height = Math.min(height, width);

        // нашли реберную связность и выделили её красным
        boolean[][] edgeconnectivity = null;
        if (answer) {
            edgeconnectivity = graph.edgeConnectivity();
            g.setColor(Color.red);
            for (int i = 0; i < graph.numberOfStrings; i++)
                for (int j = 0; j < graph.numberOfStrings; j++)
                    if (edgeconnectivity[i][j]) {
                        g.drawLine(
                                width * graph.columnNumber.get(i) + width / 2,
                                height * graph.lineNumber.get(i) + height / 2,
                                width * graph.columnNumber.get(j) + width / 2,
                                height * graph.lineNumber.get(j) + height / 2
                        );
                    }
        }

        // остальные ребра
        g.setColor(Color.black);
        for (int i = 0; i < graph.numberOfStrings; ++i)
            for (int j = 0; j < graph.numberOfStrings; ++j)
                if (graph.sourceNetwork[i][j] > 0) {
                    assert edgeconnectivity != null;
                    if (!answer || !edgeconnectivity[i][j])
                        g.drawLine(
                                width * graph.columnNumber.get(i) + width / 2,
                                height * graph.lineNumber.get(i) + height / 2,
                                width * graph.columnNumber.get(j) + width / 2,
                                height * graph.lineNumber.get(j) + height / 2
                        );
                }
        // вырисовываем и подписываем вершинки
        for (int i = 0; i < graph.numberOfStrings; ++i) {
            FontMetrics f = g.getFontMetrics(); // достать размер
            String number = Integer.toString(graph.vertexList.get(i));
            int nodeWidth = Math.max(width, f.stringWidth(number) + width / 2);
            int nodeHeight = Math.max(height, f.getHeight());
            int x = width * graph.columnNumber.get(i) + width / 2;
            int y = height * graph.lineNumber.get(i) + height / 2;
            g.setColor(Color.white);
            g.fillOval(
                    x - nodeWidth / 2,
                    y - nodeHeight / 2,
                    nodeWidth,
                    nodeHeight
            );
            g.setColor(Color.black);
            g.drawOval(
                    x - nodeWidth / 2,
                    y - nodeHeight / 2,
                    nodeWidth,
                    nodeHeight
            );
            g.drawString(
                    number,
                    x - f.stringWidth(number) / 2,
                    y + f.getHeight() / 2
            );
        }
    }
}
