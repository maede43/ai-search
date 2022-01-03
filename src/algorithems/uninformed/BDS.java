package algorithems.uninformed;

import models.Node;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class BDS {
    private static final BFS bfs = new BFS();
    public Node initialNode;
    public Node goal;

    public BDS(Node initialNode) {
        this.initialNode = initialNode;
        goal = bfs.getGoalNode(initialNode);
        goal.parentNode = null;
    }

    public void search() {
        int nodeCount = 0;
        Queue<Node> frontier_I = new LinkedList<>(); // from initial node - forward
        Queue<Node> frontier_G = new LinkedList<>(); // from goal node - backward
        Hashtable<String, Node> inFrontier_I = new Hashtable<>();
        Hashtable<String, Node> inFrontier_G = new Hashtable<>();
        Hashtable<String, Boolean> explored_I = new Hashtable<>();
        Hashtable<String, Boolean> explored_G = new Hashtable<>();

        frontier_I.add(initialNode);
        frontier_G.add(goal);
        inFrontier_I.put(initialNode.hash(),initialNode);
        inFrontier_G.put(goal.hash(),goal);

        while (!frontier_I.isEmpty() && !frontier_G.isEmpty()) {
            // forward
            Node temp = frontier_I.poll();
            inFrontier_I.remove(temp.hash());
            explored_I.put(temp.hash(),true);
            nodeCount++;
            if (inFrontier_G.containsKey(temp.hash())){
                Node intersection = inFrontier_G.get(temp.hash());
                Result.print_writeInFile(temp, intersection, goal, "BDS", nodeCount);
                return;
            }
            ArrayList<Node> children = temp.successor();
            for (Node child : children) {
                if (!(inFrontier_I.containsKey(child.hash())) && !(explored_I.containsKey(child.hash()))) {
                    frontier_I.add(child);
                    inFrontier_I.put(child.hash(), child);
                }
            }
            // backward
            temp = frontier_G.poll();
            inFrontier_G.remove(temp.hash());
            explored_G.put(temp.hash(),true);
            nodeCount++;
            if (inFrontier_I.containsKey(temp.hash())){
                Node intersection = inFrontier_I.get(temp.hash());
                Result.print_writeInFile(intersection, temp, goal, "BDS", nodeCount);
                return;
            }
            children = temp.predecessor();
            for (Node child : children) {
                if (!(inFrontier_G.containsKey(child.hash())) && !(explored_G.containsKey(child.hash()))) {
                    frontier_G.add(child);
                    inFrontier_G.put(child.hash(), child);
                }
            }
        }
        System.out.println("Goal not found.");
    }
}
