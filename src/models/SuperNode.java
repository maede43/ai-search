package models;

import algorithems.informed.Heuristic;

public class SuperNode implements Comparable<SuperNode> {
    private Heuristic heuristic;
    private Node node;
    private int f;
    private int g;
    private int h;
    public SuperNode(Node node, int g, Heuristic heuristic) {
        this.node = node;
        this.g = g;
        h = heuristic.h(node);
        // be goal nmirese
        if (h == Integer.MAX_VALUE)
            f = Integer.MAX_VALUE;
        else
            f =  g + h;
    }

    public SuperNode(int f) {
        this.f = f;
    }

    public Node getNode() {
        return this.node;
    }

    public int getG() {
        return g;
    }


    public int getF() {
        return f;
    }

    public int getH() {
        return h;
    }

    public void setG(int g) {
        this.g = g;
        f = g + h;
    }

    public void setF(int f) {
        this.f = f;
    }

    @Override
    public int compareTo(SuperNode sn) {
        return Double.compare(this.f, sn.f);
    }

}
