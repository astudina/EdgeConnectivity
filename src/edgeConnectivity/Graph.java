package edgeConnectivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

class Graph {
    int numberOfStrings;
    int[][] matrix, sourceNetwork;
    private int[][] residualNetwork;
    ArrayList<Integer> columnNumber, lineNumber, vertexList;

    Graph(File file) throws NoInputFileException, InputFileIsEmptyException, IncorrectGraphEntryException {
        try {
            Scanner sc = new Scanner(file);
            // если файл ввода пуст
            if (!sc.hasNextLine())
                throw new InputFileIsEmptyException();
            String string = sc.nextLine();
            String[] substr;
            // numberOfStrings и objectsInString задаются строго через "x"
            if (!string.contains("x")) {
                throw new IncorrectGraphEntryException();
            } else {
                substr = string.split("x");
            }
            if (substr.length != 2)
                // substr[0] - это кол-во строк, substr[1] - кол-во символов в строках
                throw new IncorrectGraphEntryException();
            try {
                numberOfStrings = Integer.parseInt(substr[0]); // преобразовали строку в число (задаем только интами!!!)
            } catch (NumberFormatException ex) {
                throw new IncorrectGraphEntryException();
            }
            int objectsInString;                      // objectsInString - кол-во объектов в строке (звездочки и числа)
            try {
                objectsInString = Integer.parseInt(substr[1]);
            } catch (NumberFormatException ex) {
                throw new IncorrectGraphEntryException();
            }
            // для хранения массива используем матрицу
            this.matrix = new int[numberOfStrings][objectsInString];
            for (int i = 0; i < numberOfStrings; i++) {
                if (!sc.hasNextLine())
                    throw new IncorrectGraphEntryException();
                string = sc.nextLine();
                int elements = 0; // elements - это сумма всех звёздочек и чисел в строке
                StringBuilder temp = new StringBuilder();
                for (char c : string.toCharArray()) {
                    // обрабатываем ситуацию, когда последним в строке стоит какое-то число (именно число, не цифра)
                    // тогда мы вынуждены считывать до конца, и это будет номер вершинки
                    if (elements > objectsInString - 1)
                        if (Character.isDigit(c)) { // если является цифрой
                            elements--;
                            temp.append(c);
                            this.matrix[i][elements] = Integer.parseInt(temp.toString());
                            elements++;
                        } else
                            temp = new StringBuilder();
                    // ситуация, когда последний элемент - цифра или звездочка (то есть все окей)
                    if (elements == objectsInString - 1) {
                        if (c == '*')
                            this.matrix[i][elements] = 0;
                        else if (Character.isDigit(c)) {
                            temp.append(c);
                            this.matrix[i][elements] = Integer.parseInt(temp.toString());
                            elements++;
                            temp = new StringBuilder();
                        }
                    }
                    if (c == '*') {
                        if (temp.length() > 0) {
                            this.matrix[i][elements] = Integer.parseInt(temp.toString());
                            elements++;
                            if (elements > objectsInString)
                                throw new IncorrectGraphEntryException();
                            temp = new StringBuilder();
                        }
                        this.matrix[i][elements] = 0;
                        elements++;
                        if (elements > objectsInString)
                            throw new IncorrectGraphEntryException();
                    } else if (Character.isDigit(c))
                        temp.append(c);
                    else
                        throw new IncorrectGraphEntryException();
                }
                // в случае, если ввели граф не учитывая количество символов в строке
                if (elements < objectsInString || elements > objectsInString) throw new IncorrectGraphEntryException();
            }
            columnNumber = new ArrayList<>();// номер столбца в матрице для отрисовки
            lineNumber = new ArrayList<>();// номер строки
            vertexList = new ArrayList<>();// список вершинок в том порядке, как они встречаются в матрице для отрисовки
            for (int i = 0; i < numberOfStrings; i++) // собсвенно говоря, заполняем матрицу для отрисовки
                for (int j = 0; j < objectsInString; j++)
                    if (this.matrix[i][j] > 0) {
                        columnNumber.add(j);
                        lineNumber.add(i);
                        vertexList.add(this.matrix[i][j]);
                    }
            numberOfStrings = vertexList.size();
            sourceNetwork = new int[numberOfStrings][numberOfStrings]; //sourceNetwork - наша матрица смежности
            for (int i = 0; i < numberOfStrings; i++)
                for (int j = 0; j < numberOfStrings; j++)
                    sourceNetwork[i][j] = 0;   // занулили на всякий случай
            while (sc.hasNextLine()) {
                if (!sc.hasNextInt())
                    throw new IncorrectGraphEntryException();
                int element = sc.nextInt();
                if (!vertexList.contains(element))
                    throw new IncorrectGraphEntryException();
                int firstIndex = vertexList.indexOf(element); // путь из вершинки "а" в "б"
                if (!sc.hasNextInt())
                    throw new IncorrectGraphEntryException();
                element = sc.nextInt();
                if (!vertexList.contains(element))
                    throw new IncorrectGraphEntryException();
                int secondIndex = vertexList.indexOf(element); // из "б" в "а"
                sourceNetwork[firstIndex][secondIndex] = sourceNetwork[secondIndex][firstIndex] = 1; // наличие связи
                // должны быть равны, так как граф невзвешенный (по этой же причине = 1) (
            }
        } catch (FileNotFoundException ex) {
            throw new NoInputFileException();
        }
    }

    // находим соседей конкретной вершинки
    ArrayList<Integer> neighbours(int firstVertex) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        for (int secondVertex = 0; secondVertex < numberOfStrings; secondVertex++)
            if (sourceNetwork[firstVertex][secondVertex] != 0) // если есть связь
                neighbours.add(secondVertex);
        return neighbours;
    }


    // поиск в ширину
    boolean bfs(int source, int drain, int[] parent) { // source - исток, drain - сток
        boolean[] used = new boolean[numberOfStrings];   // для того, чтобы отмечать, пройдена ли вершинка
        for (int firstVertex = 0; firstVertex < numberOfStrings; firstVertex++)
            used[firstVertex] = false;
        LinkedList<Integer> queue = new LinkedList<>(); // создали очередь вершин
        queue.add(source); // вначале очередь состоит из одной вершины
        used[source] = true; // отметили её как посещенную
        parent[source] = -1; // без родителя
        while (!queue.isEmpty()) {
            int secondVertex = queue.poll();          // извлекли первую в очереди вершинку
            for (int firstVertex = 0; firstVertex < numberOfStrings; firstVertex++) {
                // теперь для всех дуг из вершины secondVertex, для которых firstVertex не посещена:
                if (!used[firstVertex] && residualNetwork[secondVertex][firstVertex] > 0) {
                    queue.add(firstVertex); // добавили вершинку в конец очереди
                    used[firstVertex] = true; // отметили как посещенную
                    parent[firstVertex] = secondVertex; // родитель secondVertex - это firstVertex
                }
            }
        }
        return used[drain];   // если firstVertex == drain, то все, выходим из циклов, мы нашли кратчайший путь
    }


    // поиск в глубину (ищем кратчайший путь для разреза)
    void dfs(int firstVertex, boolean[] used) {
        used[firstVertex] = true; // отметили вершину как пройденную
        for (int secondVertex = 0; secondVertex < numberOfStrings; secondVertex++)
            if (residualNetwork[firstVertex][secondVertex] > 0 && !used[secondVertex]) // если вершина не помечена и смежна с текущей
                dfs(secondVertex, used);
    }

    int EdmondsKarp(int source, int drain) {
        // это мы создаем нашу остаточную сеть, которая изначально совпадает с исходной (с sourceNetwork)
        residualNetwork = new int[numberOfStrings][numberOfStrings];
        // обнуляем все потоки
        for (int firstVertex = 0; firstVertex < numberOfStrings; firstVertex++)
            System.arraycopy(sourceNetwork[firstVertex], 0, residualNetwork[firstVertex], 0, numberOfStrings);
        int[] parent = new int[numberOfStrings];
        int flow = 0;
        // в остаточной сети находим кратчайший путь из источника в сток. Если такого пути нет, останавливаемся
        while (bfs(source, drain, parent)) {
            // пускаем максимально возможный поток через найденный путь и ищем ребро с min. пропускной способностью
            int karpFlow = Integer.MAX_VALUE;
            for (int firstVertex = drain; firstVertex != source; firstVertex = parent[firstVertex]) {
                int secondVertex = parent[firstVertex];
                // в остаточной сети находим кратчайший путь из источника в сток
                karpFlow = Math.min(karpFlow, residualNetwork[secondVertex][firstVertex]);
            }
            // Для каждого ребра на найденном пути увеличиваем поток на min, а в противоположном ему — уменьшаем на min
            // min - это как раз-таки karpFlow
            for (int firstVertex = drain; firstVertex != source; firstVertex = parent[firstVertex]) {
                int secondVertex = parent[firstVertex];
                residualNetwork[secondVertex][firstVertex] -= karpFlow;
                residualNetwork[firstVertex][secondVertex] += karpFlow;
            }
            // Модифицируем остаточную сеть. Для всех рёбер на найденном пути, а также для противоположных им рёбер,
            // вычисляем новую пропускную способность. Если она стала ненулевой, добавляем ребро к остаточной сети,
            // а если обнулилась, стираем его (и так каждый раз)
            flow += karpFlow;
        }
        return flow;
    }

    // минимальный разрез, он же максимальное значение потока
    // булевская чтобы определить, каким цветом закрашивать
    boolean[][] minCut(int source) {
        boolean[][] mincut = new boolean[numberOfStrings][numberOfStrings];
        for (int firstVertex = 0; firstVertex < numberOfStrings; firstVertex++)
            for (int secondVertex = 0; secondVertex < numberOfStrings; secondVertex++)
                mincut[firstVertex][secondVertex] = false;
        boolean[] used = new boolean[numberOfStrings];
        dfs(source, used);
        for (int firstVertex = 0; firstVertex < numberOfStrings; firstVertex++)
            for (int secondVertex : neighbours(firstVertex))
                if (sourceNetwork[firstVertex][secondVertex] > 0 && used[firstVertex] && !used[secondVertex])
                    mincut[firstVertex][secondVertex] = mincut[secondVertex][firstVertex] = true;
        return mincut; // список ребер в минимальном разрезе
    }

    /* !рёберная связность графа равна минимуму от наименьшего числа рёбер, разделяющих две вершины source и drain,
       взятому среди всевозможных пар! */
    boolean[][] edgeConnectivity() {
        int answer = Integer.MAX_VALUE;
        for (int source = 0; source < numberOfStrings; source++)
            for (int drain = source + 1; drain < numberOfStrings; drain++) {
                int flow = EdmondsKarp(source, drain);
                answer = Math.min(answer, flow);
            }
        for (int source = 0; source < numberOfStrings; source++)
            for (int drain = source + 1; drain < numberOfStrings; drain++) {
                int flow = EdmondsKarp(source, drain);  // т.е величина максимального потока из истока в сток)
                if (flow == answer)
                    return minCut(source);
            }
        return null;
    }
}
