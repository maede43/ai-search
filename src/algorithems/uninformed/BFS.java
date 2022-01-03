package algorithems.uninformed;

import models.Node;

import java.util.*;

public class BFS {
    public void search(Node initialNode) {
        int nodeCount = 0;
        Queue<Node> frontier = new LinkedList<>();
        Hashtable<String, Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> explored = new Hashtable<>();

        if (initialNode.isGoal()) {
            Result.print_writeInFile(initialNode, "BFS", nodeCount);
            return;
        }
        frontier.add(initialNode);
        inFrontier.put(initialNode.hash(), true);

        while (!frontier.isEmpty()) {

            Node temp = frontier.poll();
            inFrontier.remove(temp.hash());
            explored.put(temp.hash(), true);
            nodeCount++;

            ArrayList<Node> children = temp.successor();
            for (Node child : children) {
                if (!(inFrontier.containsKey(child.hash())) && !(explored.containsKey(child.hash()))) {
                    if (child.isGoal()) {
                        Result.print_writeInFile(child, "BFS", nodeCount);
                        return;
                    }
                    frontier.add(child);
                    inFrontier.put(child.hash(), true);
                }
            }
        }
        System.out.println("Goal not found.");
    }

    public Node getGoalNode(Node initialNode) {
        Queue<Node> frontier = new LinkedList<>();
        Hashtable<String, Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> explored = new Hashtable<>();

        if(initialNode.isGoal()){
            return initialNode;
        }
        frontier.add(initialNode);
        inFrontier.put(initialNode.hash(),true);

        while (!frontier.isEmpty()){
            Node temp = frontier.poll();
            inFrontier.remove(temp.hash());
            explored.put(temp.hash(),true);
            ArrayList<Node> children = temp.successor();
            for (Node child : children) {
                if (!(inFrontier.containsKey(child.hash())) && !(explored.containsKey(child.hash()))) {
                    if (child.isGoal()) {
                        return child;
                    }
                    frontier.add(child);
                    inFrontier.put(child.hash(), true);
                }
            }
        }
        return null;
    }

}
