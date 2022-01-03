package algorithems.uninformed;

import models.Node;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class IDS {
    private final DLS dls = new DLS();

    public void search(Node initialNode){
        ReturnResult result;
        int nodeCount = 0;
        for (int limit = 0; limit < Integer.MAX_VALUE; limit++) {
            result = dls.search(initialNode, limit);
            nodeCount += result.nodeCount;
            switch (result.status) {
                case FOUND: {
                    Result.print_writeInFile(result.node, "IDS", nodeCount);
                    return;
                }
                case FAILURE:{
                    System.out.println("Goal not found.");
                    return;
                }
            }
        }
        System.out.println("Goal not found.");
    }

}

class DLS{

    public ReturnResult search(Node initialNode, int limit){
        int nodeCount = 0;
        Stack<Node> frontier = new Stack<>();
        Hashtable<String, Boolean> currentPath; // from current node to initial node

        Status status = Status.FAILURE;
        frontier.push(initialNode);

        while (!frontier.isEmpty()){
            Node temp = frontier.pop();
            nodeCount++;
            if (temp.isGoal()) {
                return new ReturnResult(Status.FOUND, temp, nodeCount);
            }
            else if (temp.getDepth() >= limit) {
                status = Status.CUTOFF;
            }
            else {
                currentPath = temp.getCurrentPath();
                ArrayList<Node> children = temp.successor();
                for (Node child : children) {
                    if (!(currentPath.containsKey(child.hash()))) {
                        frontier.push(child);
                    }
                }
            }
        }
        return new ReturnResult(status, null, nodeCount);
    }
}

enum Status
{
    FAILURE, CUTOFF, FOUND
}

class ReturnResult {
    public Status status;
    public Node node;
    public int nodeCount;
    public ReturnResult(Status status, Node node, int nodeCount) {
        this.status = status;
        this.node = node;
        this.nodeCount = nodeCount;
    }
}
