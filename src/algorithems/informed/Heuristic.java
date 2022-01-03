package algorithems.informed;

import algorithems.uninformed.BFS;
import models.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Heuristic {
    private int wildAnimalsPower = 0, banditPower = 0;
    private int minX, minY, maxX, maxY;
    private final boolean select_WildAnimals;
    private final Memoization memo1;
    private final Memoization memo2;
    BFS bfs = new BFS();

    public Heuristic(Node initialNode) {
        Map map = initialNode.map.copy();
        calculateBorderOfMap(map, initialNode.player);
        select_WildAnimals = (initialNode.player.food - wildAnimalsPower) < (initialNode.player.money - banditPower);
        memo1 = new Memoization();
        memo2 = new Memoization();
    }

    public int h(Node node) {
        int h1 = h1(node);
        int h2 = h2(node);
        // inkar ziad mikne tedad node haro :(
        if (h2 == Integer.MAX_VALUE)
            return h1;
        return Math.max(h1, h2);
    }

    public int h1(Node node) {
        int h = memo1.getH(node);
        if (h != -1)
            return h;
        Map map = node.map.copy();
        Node newNode = new Node(node.player, relaxMap(map), null, null);
        Node goal = bfs.getGoalNode(newNode);
        if (goal != null)
            memo1.add(goal);
        return getDepth(goal);
    }

    // remove Loots & Bandits & WildAnimals
    private Map relaxMap(Map map) {
        BaseEntity entity;
        for (int i = 0; i < map.game.size(); i++) {
            entity = map.game.get(i);
            if (entity instanceof Loot || entity instanceof WildAnimall || entity instanceof Bandit) {
                map.game.set(i, new BaseEntity('G'));
            }
        }
        return map;
    }

    public int h2(Node node) {
        if (!(node.player.i >= minX && node.player.i <= maxX && node.player.j >= minY && node.player.j <= maxY))
            return Integer.MAX_VALUE;
        int h = memo2.getH(node);
        if (h != -1)
            return h;
        Map map = node.map.copy();
        Player player = new Player(node.player.i, node.player.j, node.player.money, node.player.food, node.player.haskey);
        map = cropMap(map, player);
        Node newNode = new Node(player, relaxMap2(map), null, null);
        Node goal = bfs.getGoalNode(newNode);
        if (goal != null)
            memo2.add(goal);
        return getDepth(goal);
    }

    private void calculateBorderOfMap(Map map, Player player) {
        int castleX = 0, castleY = 0, keyX = 0, keyY = 0;
        BaseEntity entity;
        for (int i = 0; i < map.game.size(); i++) {
            entity = map.game.get(i);
            if (entity.name == 'C') {
                castleX = i / map.cols;
                castleY = i % map.cols;
            }
            else if (entity.name == 'K') {
                keyX = i / map.cols;
                keyY = i % map.cols;
            }
        }
        int agentX = player.i, agentY = player.j;
        minX = Math.min(Math.min(agentX, castleX), keyX);
        maxX = Math.max(Math.max(agentX, castleX), keyX);
        minY = Math.min(Math.min(agentY, castleY), keyY);
        maxY = Math.max(Math.max(agentY, castleY), keyY);
        int x, y;
        for (int i = 0; i < map.game.size(); i++) {
            x = i/map.cols;
            y = i%map.cols;
            if (x >= minX && x <= maxX && y >= minY && y <= maxY)
                continue;
            entity = map.game.get(i);
            if (entity instanceof Loot) {
                if (x > maxX)
                    maxX = x;
                if (y > maxY)
                    maxY = y;
                if (x < minX)
                    minX = x;
                if (y < minY)
                    minY = y;
            }
        }
        for (int i = 0; i < map.game.size(); i++) {
            entity = map.game.get(i);
            if (entity instanceof WildAnimall)
                wildAnimalsPower += ((WildAnimall) entity).power;
            else if (entity instanceof Bandit)
                banditPower += ((Bandit) entity).power;
        }
    }

    private Map cropMap(Map map, Player player) {
        int cols = maxY - minY + 1, rows = maxX - minX + 1;
        ArrayList<BaseEntity> game = new ArrayList<>();
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                 game.add(map.at(i, j));
            }
        }
        player.i = player.i - minX;
        player.j = player.j - minY;
        return new Map(rows, cols, game);
    }

    // remove swamp & bridge & ((food - wildAnimalsPower) < (money - banditPower) ? : bandit : wildAnimals)
    private Map relaxMap2(Map map) {
        BaseEntity entity;
        if (select_WildAnimals) {
            // remove swamp & bridge & bandit
            for (int i = 0; i < map.game.size(); i++) {
                entity = map.game.get(i);
                if (entity instanceof Bandit || entity instanceof Bridge || entity.name == 'S') {
                    map.game.set(i, new BaseEntity('G'));
                }
            }
        } else {
            // remove swamp & bridge & wildAnimals
            for (int i = 0; i < map.game.size(); i++) {
                entity = map.game.get(i);
                if (entity instanceof WildAnimall || entity instanceof Bridge || entity.name == 'S') {
                    map.game.set(i, new BaseEntity('G'));
                }
            }
        }
        return map;
    }

    public int getDepth(Node node) {
        int depth = 0;
        if (node == null)
            return Integer.MAX_VALUE;
        while (true) {
            if (node.parentNode == null) {
                break;
            } else {
                node = node.parentNode;
                depth++;
            }
        }
        return depth;
    }
}

class Memoization{
    Hashtable<String, Integer> mem;
    public Memoization() {
        mem = new Hashtable<>();
    }
    public void add(Node goal) {
        int depth = goal.getDepth();
        Node tempNode = goal;
        Stack<Node> nodes = new Stack<>();
        while (true) {
            nodes.push(tempNode);
            if (tempNode.parentNode == null) {
                break;
            } else {
                tempNode = tempNode.parentNode;
            }
        }
        while (!nodes.empty()) {
            tempNode = nodes.pop();
            mem.put(tempNode.hash(), depth--);
        }
    }
    public int getH (Node node) {
        int h = -1; // not found
        if (mem.containsKey(node.hash())) {
            h = mem.get(node.hash());
        }
        return h;
    }
}
