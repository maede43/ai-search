package algorithems.informed;

import algorithems.uninformed.Result;
import models.Node;
import models.SuperNode;
import java.util.ArrayList;

public class RBFS {
    private int nodeCount;

    public void search(Node initialNode) {
        nodeCount = 0;
        Heuristic heuristic = new Heuristic(initialNode);
        SuperNode initial = new SuperNode(initialNode, 0, heuristic);
        ReturnResult result = recursiveBF(initial, Integer.MAX_VALUE, heuristic);
        if (result.f_cost == -1) {
            Result.print_writeInFile(result.goal, "RBFS", nodeCount);
        }
    }

    private ReturnResult recursiveBF(SuperNode super_node, int fLimit, Heuristic heuristic) {
        nodeCount++;
        Node node = super_node.getNode();
        if (node.isGoal()) {
            return new ReturnResult(node, -1);
        }
        ArrayList<Node> children = node.successor();
        if (children.size() == 0) { //leaf
            return new ReturnResult(null, Integer.MAX_VALUE);
        }
        ArrayList<SuperNode> superChildren = new ArrayList<>();
        for (Node child : children) {
            superChildren.add(new SuperNode(child, super_node.getG() + 1, heuristic));
        }
        TwoLower m;
        ReturnResult result = null;
        while (superChildren.size() != 0) {
            m = getTwoLower(superChildren);
            if (m.min1.getF() > fLimit)
                return new ReturnResult(null, m.min1.getF());
            result = recursiveBF(m.min1, Math.min(fLimit, m.min2.getF()), heuristic);
            m.min1.setF(result.f_cost);
            if (result.goal != null)
                break;
        }
        return result;
    }

    private TwoLower getTwoLower(ArrayList<SuperNode> superChildren) {
        TwoLower m = new TwoLower();
        int minF1 = Integer.MAX_VALUE, minF2 = Integer.MAX_VALUE;
        for (SuperNode ch : superChildren) {
            if (ch.getF() <= minF1) {
                minF2 = minF1;
                minF1 = ch.getF();
                m.min2 = m.min1;
                m.min1 = ch;
            } else if (ch.getF() > minF1 && ch.getF() < minF2) {
                minF2 = ch.getF();
                m.min2 = ch;
            }
        }
        return m;
    }

}

class TwoLower {
    SuperNode min1;
    SuperNode min2;
    public TwoLower() {
        min1 = new SuperNode(Integer.MAX_VALUE);
        min2 = new SuperNode(Integer.MAX_VALUE);
    }
}

class ReturnResult {
    int f_cost;
    Node goal;
    public ReturnResult(Node goal, int f_cost) {
        this.goal = goal;
        this.f_cost = f_cost;
    }
}