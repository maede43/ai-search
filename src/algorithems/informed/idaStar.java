package algorithems.informed;

import algorithems.uninformed.Result;
import models.Node;
import models.SuperNode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class idaStar {
    private final FLS fls = new FLS();

    public void search(Node initialNode){
        Heuristic heuristic = new Heuristic(initialNode);
        int nodeCount = 0;
        SuperNode super_node = new SuperNode(initialNode, 0, heuristic);
        int cutoff = super_node.getF();
        Output output;
        while (cutoff < Integer.MAX_VALUE/2) {
            output = fls.search(super_node, cutoff, heuristic);
            nodeCount += output.nodeCount;
            if (output.getGoalNode() != null) {
                Result.print_writeInFile(output.getGoalNode(), "IDA*", nodeCount);
                return;
            }
            cutoff = output.getNextCutoff();
        }
        System.out.println("Goal not found.");
    }
}

// f limited search
class FLS{
    public Output search(SuperNode initial, int cutoff, Heuristic heuristic) {
        int nodeCount = 0;
        int nextCutoff = Integer.MAX_VALUE;
        Stack<SuperNode> frontier = new Stack<>();
        Hashtable<String,Boolean> inFrontier = new Hashtable<>();
        Hashtable<String, Boolean> currentPath;
        SuperNode super_node;
        frontier.push(initial);
        inFrontier.put(initial.getNode().hash(), true);
        while (!frontier.isEmpty()){
            SuperNode temp = frontier.pop();
            inFrontier.remove(temp.getNode().hash());
            nodeCount++;
            currentPath = temp.getNode().getCurrentPath();
            ArrayList<Node> children = temp.getNode().successor();
            for (Node child : children) {
                if (!currentPath.containsKey(child.hash()) && !inFrontier.containsKey(child.hash())) {
                    super_node = new SuperNode(child, temp.getG() + 1, heuristic);
                    if (super_node.getF() > cutoff && super_node.getF() < nextCutoff) {
                        nextCutoff = super_node.getF();
                    } else if (super_node.getF() <= cutoff && !inFrontier.containsKey(child.hash())) {
                        if (super_node.getNode().isGoal()) {
                            Output out = new Output(super_node.getNode());
                            out.nodeCount = nodeCount;
                            return out;
                        }
                        frontier.push(super_node);
                        inFrontier.put(child.hash(), true);

                    }
                }
            }
        }
        Output out = new Output(nextCutoff);
        out.nodeCount = nodeCount;
        return out;
    }
}

class Output {
    private int nextCutoff;
    private final Node goalNode;
    public int nodeCount;
    public Output (int nextCutoff) {
        this.nextCutoff = nextCutoff;
        goalNode = null;
    }
    public Output (Node goalNode) {
        this.goalNode = goalNode;
    }

    public int getNextCutoff() {
        return nextCutoff;
    }

    public Node getGoalNode() {
        return goalNode;
    }
}
