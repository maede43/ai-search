package algorithems.uninformed;

import models.Node;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class DFS {

    public void search(Node initialNode) {
        int nodeCount = 0;
        Stack<Node> frontier = new Stack<>();
        Hashtable<String,Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> currentPath; // from current node to initial node

        if (initialNode.isGoal()) {
            Result.print_writeInFile(initialNode, "DFS", nodeCount);
            return;
        }
        frontier.push(initialNode);
        inFrontier.put(initialNode.hash(), true);
        while (!frontier.isEmpty()) {
            Node temp = frontier.pop();
            inFrontier.remove(temp.hash());
            nodeCount++;
            currentPath = temp.getCurrentPath();
            ArrayList<Node> children = temp.successor();
            for (Node child : children) {
                if (!currentPath.containsKey(child.hash())&& !inFrontier.containsKey(child.hash())) {
                    if (child.isGoal()) {
                        Result.print_writeInFile(child, "DFS", nodeCount);
                        return;
                    }
                    frontier.push(child);
                    inFrontier.put(child.hash(), true);
                }
            }
        }
        System.out.println("Goal not found.");
    }
}
