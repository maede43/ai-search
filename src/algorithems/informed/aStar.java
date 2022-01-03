package algorithems.informed;

import algorithems.uninformed.Result;
import models.Node;
import models.SuperNode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;

public class aStar {

    public void search(Node initialNode) {
        Heuristic heuristic = new Heuristic(initialNode);
        int nodeCount = 0;
        PriorityQueue<SuperNode> openList  = new PriorityQueue<>();
        Hashtable<String, SuperNode> inOpenList = new Hashtable<>();
        Hashtable<String, Boolean> explored = new Hashtable<>();
        SuperNode super_node = new SuperNode(initialNode, 0, heuristic);
        openList.add(super_node);
        inOpenList.put(initialNode.hash(), super_node);
        SuperNode tempSuperNode;
        Node tempNode;
        while (!openList.isEmpty()) {
            tempSuperNode = openList.poll();
            tempNode = tempSuperNode.getNode();
            inOpenList.remove(tempNode.hash());
            explored.put(tempNode.hash(), true);
            nodeCount++;
            if(tempNode.isGoal()){
                Result.print_writeInFile(tempNode, "A*", nodeCount);
                return;
            }
            ArrayList<Node> children = tempNode.successor();
            for (Node child : children) {
                if (!explored.containsKey(child.hash()) && !inOpenList.containsKey(child.hash())) {
                    super_node = new SuperNode(child, tempSuperNode.getG() + 1, heuristic);
                    openList.add(super_node);
                    inOpenList.put(child.hash(), super_node);
                }
            }
        }
        System.out.println("Goal not found.");
    }
}
