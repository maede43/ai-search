package algorithems.uninformed;

import models.Node;
import models.Visualizer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class Result {
    private static final Visualizer visualizer = new Visualizer();

    public static void print_writeInFile(Node node, String search, int nodeCount) {
        Stack<Node> nodes = new Stack<>();
        while (true) {
            nodes.push(node);
            if (node.parentNode == null) {
                break;
            } else {
                node = node.parentNode;
            }
        }
        nodes.pop();
        try {
            FileWriter myWriter = new FileWriter("./result.txt", true);
            myWriter.write("************* " + search + " - number of explored nodes : " + nodeCount + " *************\n");
            while (!nodes.empty()) {
                Node tempNode = nodes.pop();
                String action = tempNode.previousAction;
                System.out.println(action + " " + tempNode.player.money + " " + tempNode.player.food);
                myWriter.write(action + "\n");
                //print visualized map for every movement
                visualizer.printMap(tempNode.map, tempNode.player);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void print_writeInFile(Node intersection_I, Node intersection_G, Node goal, String search, int nodeCount) {
        Stack<Node> nodes = new Stack<>();
        while (true) {
            nodes.push(intersection_I);
            if (intersection_I.parentNode == null) {
                break;
            } else {
                intersection_I = intersection_I.parentNode;
            }
        }
        nodes.pop();
        try {
            FileWriter myWriter = new FileWriter("./result.txt", true);
            myWriter.write("************* " + search + " - number of explored nodes : " + nodeCount + " *************\n");
            while (!nodes.empty()) {
                Node tempNode = nodes.pop();
                String action = tempNode.previousAction;
                System.out.println(action + " " + tempNode.player.money + " " + tempNode.player.food);
                myWriter.write(action + "\n");
                //print visualized map for every movement
                visualizer.printMap(tempNode.map, tempNode.player);
            }
            while (!intersection_G.hash().equals(goal.hash())) {
                String action = intersection_G.previousAction;
                System.out.println(action + " " + intersection_G.player.money + " " + intersection_G.player.food);
                myWriter.write(action + "\n");
                intersection_G = intersection_G.parentNode;
                //print visualized map for every movement
                visualizer.printMap(intersection_G.map, intersection_G.player);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
