package edgeConnectivity;

import org.junit.jupiter.api.Test;
import java.io.File


class GraphTest {
    // реберная связность равна 2
    private val file_1 = File("example_5.txt")
    // реберная связность равна нулю
    private val file_2 = File("zeroConnectivity.txt")
    // реберная связность равна одному (т.е задан граф с мостом)
    private val file_3 = File("example.txt")

    private val graph_1 = Graph(file_1)
    private val graph_2 = Graph(file_2)
    private val graph_3 = Graph(file_3);

    private val mincut_1 = Array(graph_1.numberOfStrings) {BooleanArray(graph_1.numberOfStrings)}
    private val mincut_2 = Array(graph_2.numberOfStrings) {BooleanArray(graph_2.numberOfStrings)}
    private val mincut_3 = Array(graph_3.numberOfStrings) {BooleanArray(graph_3.numberOfStrings)}

    private var valcut_1 = Array(graph_1.numberOfStrings) {BooleanArray(graph_1.numberOfStrings)}
    private var valcut_2 = Array(graph_2.numberOfStrings) {BooleanArray(graph_2.numberOfStrings)}
    private var valcut_3 = Array(graph_3.numberOfStrings) {BooleanArray(graph_3.numberOfStrings)}

    @Test
    fun test1(){
        mincut_1[0][2] = true
        mincut_1[2][0] = true
        mincut_1[0][1] = true
        mincut_1[1][0] = true

        graph_1.EdmondsKarp(0, 3)
        valcut_1 = graph_1.minCut(0)
        if (mincut_1[0][2] == graph_1.minCut(0)[0][2] && mincut_1[2][0] == graph_1.minCut(0)[2][0] &&
                mincut_1[0][1] == graph_1.minCut(0)[0][1])
            print("ok")
    }

    @Test
    fun test2(){
        graph_2.EdmondsKarp(0, 4)
        valcut_2 = graph_2.minCut(0)
        for(i in mincut_2.indices) {
            for (j in mincut_2[i].indices) {
                if (mincut_2[i][j] == graph_2.minCut(0)[i][j])
                    print("ok")
                else {
                    print("none")
                    break
                }
            }
        }
    }

    @Test
    fun test3(){
        mincut_3[2][4] = true
        mincut_3[4][2] = true
        graph_3.EdmondsKarp(0,4)
        valcut_3 = graph_3.minCut(0)
        if (mincut_3[2][4] == graph_3.minCut(0)[2][4] && mincut_3[4][2] == graph_3.minCut(0)[4][2])
            print("ok")
    }
}