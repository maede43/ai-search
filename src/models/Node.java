package models;

import java.util.ArrayList;
import java.util.Hashtable;

public class Node{
    public Map map;
    public Player player;
    public Node parentNode;
    public String previousAction;

    public Node(Player player, Map map, Node parentNode, String previousAction) {
        this.map = map.copy();
        this.player = new Player(player.i, player.j, player.money, player.food, player.haskey);
        this.parentNode = parentNode;
        this.previousAction = previousAction;
    }
    public boolean isGoal() {
        return map.at(player.i, player.j).name == 'C';
    }

    public int getDepth() {
        int depth = 0;
        Node node = this;
        while (true){
            if(node.parentNode == null){
                break;
            }
            else {
                node = node.parentNode;
                depth++;
            }
        }
        return depth;
    }

    // from current node to initial node
    public Hashtable<String, Boolean> getCurrentPath() {
        Hashtable<String, Boolean> nodes = new Hashtable<>();
        Node temp = this;
        while (true) {
            nodes.put(temp.hash(), true);
            if (temp.parentNode == null) {
                break;
            } else {
                temp = temp.parentNode;
            }
        }
        return nodes;
    }

    public String hash() {
        int key = player.haskey ? 1 : 0;
        StringBuilder result = new StringBuilder(player.i + "," + player.j + "," + player.money + "," + player.food + "," + key);
        int size = map.game.size();
        for (int i = 0; i < size; i++) {
            if (map.game.get(i) instanceof Bridge) {
                key = ((Bridge) map.game.get(i)).traveresd ? 1 : 0;
                result.append(key);
            } else if (map.game.get(i) instanceof Loot) {
                key = ((Loot) map.game.get(i)).used ? 1 : 0;
                result.append(key);
            }
        }
        return result.toString();
    }

    public ArrayList<Node> predecessor() {
        ArrayList<Node> result = new ArrayList<>();
        BaseEntity entity = this.map.at(this.player.i, this.player.j);
        if (this.player.j < this.map.cols - 1) {//player can move right
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        temp.player.j++;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j++;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    if (this.player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j++;
                        temp.player.haskey = false;
                        result.add(temp);
                        // agent chandbar rad shode bashe
                        Node temp2 = new Node(this.player, this.map, this, "left");
                        temp2.player.j++;
                        result.add(temp2);
                    }
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j++;
                    bandit.power *= -1;
                    bandit.takeMoney(temp.player);
                    bandit.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j++;
                    wildAnimall.power *= -1;
                    wildAnimall.takeFood(temp.player);
                    wildAnimall.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j++;
                        result.add(temp);
                        if (this.player.money - loot.money > 0) {
                            Node temp1 = new Node(this.player, this.map, this, "left, use money");
                            temp1.player.money -= loot.money;
                            ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).used = false;
                            temp1.player.j++;
                            result.add(temp1);
                        }
                        if (this.player.food - loot.food > 0) {
                            Node temp2 = new Node(this.player, this.map, this, "left, use food");
                            temp2.player.food -= loot.food;
                            ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).used = false;
                            temp2.player.j++;
                            result.add(temp2);
                        }
                    }
                }
            }
        }
        if (this.player.j > 0) {//player can move left
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        temp.player.j--;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j--;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    if (this.player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j--;
                        temp.player.haskey = false;
                        result.add(temp);
                        // agent chandbar rad shode bashe
                        Node temp2 = new Node(this.player, this.map, this, "right");
                        temp2.player.j--;
                        result.add(temp2);
                    }
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j--;
                    bandit.power *= -1;
                    bandit.takeMoney(temp.player);
                    bandit.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j--;
                    wildAnimall.power *= -1;
                    wildAnimall.takeFood(temp.player);
                    wildAnimall.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j--;
                        result.add(temp);
                        if (this.player.money - loot.money > 0) {
                            Node temp1 = new Node(this.player, this.map, this, "right, use money");
                            temp1.player.money -= loot.money;
                            ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).used = false;
                            temp1.player.j--;
                            result.add(temp1);
                        }
                        if (this.player.food - loot.food > 0) {
                            Node temp2 = new Node(this.player, this.map, this, "right, use food");
                            temp2.player.food -= loot.food;
                            ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).used = false;
                            temp2.player.j--;
                            result.add(temp2);
                        }
                    }
                }
            }
        }
        if (this.player.i < this.map.rows - 1) {//player can move down
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        temp.player.i++;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i++;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    if (this.player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i++;
                        temp.player.haskey = false;
                        result.add(temp);
                        // agent chandbar rad shode bashe
                        Node temp2 = new Node(this.player, this.map, this, "up");
                        temp2.player.i++;
                        result.add(temp2);
                    }
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i++;
                    bandit.power *= -1;
                    bandit.takeMoney(temp.player);
                    bandit.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i++;
                    wildAnimall.power *= -1;
                    wildAnimall.takeFood(temp.player);
                    wildAnimall.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i++;
                        result.add(temp);
                        if (this.player.money - loot.money > 0) {
                            Node temp1 = new Node(this.player, this.map, this, "up, use money");
                            temp1.player.money -= loot.money;
                            ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).used = false;
                            temp1.player.i++;
                            result.add(temp1);
                        }
                        if (this.player.food - loot.food > 0) {
                            Node temp2 = new Node(this.player, this.map, this, "up, use food");
                            temp2.player.food -= loot.food;
                            ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).used = false;
                            temp2.player.i++;
                            result.add(temp2);
                        }
                    }
                }
            }
        }
        if (this.player.i > 0) {//player can move up
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = false;
                        temp.player.i--;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i--;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    if (this.player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i--;
                        temp.player.haskey = false;
                        result.add(temp);
                        // agent chandbar rad shodebashe
                        Node temp2 = new Node(this.player, this.map, this, "down");
                        temp2.player.i--;
                        result.add(temp2);
                    }
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i--;
                    bandit.power *= -1;
                    bandit.takeMoney(temp.player);
                    bandit.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i--;
                    wildAnimall.power *= -1;
                    wildAnimall.takeFood(temp.player);
                    wildAnimall.power *= -1;
                    result.add(temp);
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i--;
                        result.add(temp);
                        if (this.player.money - loot.money > 0) {
                            Node temp1 = new Node(this.player, this.map, this, "down, use money");
                            temp1.player.money -= loot.money;
                            ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).used = false;
                            temp1.player.i--;
                            result.add(temp1);
                        }
                        if (this.player.food - loot.food > 0) {
                            Node temp2 = new Node(this.player, this.map, this, "down, use food");
                            temp2.player.food -= loot.food;
                            ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).used = false;
                            temp2.player.i--;
                            result.add(temp2);
                        }
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<Node> successor() {
        ArrayList<Node> result = new ArrayList<>();
        if (this.player.j < this.map.cols - 1) {//player can move right
            BaseEntity entity = this.map.at(this.player.i, this.player.j + 1);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "right");
                    temp.player.j++;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "right");
                        temp.player.j++;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "right, use money");
                        temp1.player.j++;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "right, use food");
                        temp2.player.j++;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        if (this.player.j > 0) {//player can move left
            BaseEntity entity = this.map.at(this.player.i, this.player.j - 1);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "left");
                    temp.player.j--;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "left");
                        temp.player.j--;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "left, use money");
                        temp1.player.j--;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "left, use food");
                        temp2.player.j--;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        if (this.player.i > 0) {//player can move up
            BaseEntity entity = this.map.at(this.player.i - 1, this.player.j);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "up");
                    temp.player.i--;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "up");
                        temp.player.i--;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "up, use money");
                        temp1.player.i--;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "up, use food");
                        temp2.player.i--;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        if (this.player.i < this.map.rows - 1) {//player can move down
            BaseEntity entity = this.map.at(this.player.i + 1, this.player.j);
            if (entity.name != 'S') {
                if (entity.name == 'G') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    result.add(temp);
                } else if (entity.name == 'P') {
                    if (!((Bridge) entity).traveresd) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        ((Bridge) temp.map.at(temp.player.i, temp.player.j)).traveresd = true;
                        result.add(temp);
                    }
                } else if (entity.name == 'C') {
                    if (player.haskey) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        result.add(temp);
                    }
                } else if (entity.name == 'K') {
                    Node temp = new Node(this.player, this.map, this, "down");
                    temp.player.i++;
                    temp.player.haskey = true;
                    result.add(temp);
                } else if (entity.name == 'B') {
                    Bandit bandit = (Bandit) entity;
                    if (this.player.money > bandit.power) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        bandit.takeMoney(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'W') {
                    WildAnimall wildAnimall = (WildAnimall) entity;
                    if (this.player.food > wildAnimall.power) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        wildAnimall.takeFood(temp.player);
                        result.add(temp);
                    }
                } else if (entity.name == 'L') {
                    Loot loot = (Loot) entity;
                    if (loot.used) {
                        Node temp = new Node(this.player, this.map, this, "down");
                        temp.player.i++;
                        result.add(temp);
                    } else {
                        Node temp1 = new Node(this.player, this.map, this, "down, use money");
                        temp1.player.i++;
                        ((Loot) temp1.map.at(temp1.player.i, temp1.player.j)).useMoney(temp1.player);
                        result.add(temp1);
                        Node temp2 = new Node(this.player, this.map, this, "down, use food");
                        temp2.player.i++;
                        ((Loot) temp2.map.at(temp2.player.i, temp2.player.j)).useFood(temp2.player);
                        result.add(temp2);
                    }
                }
            }
        }
        return result;
    }
}
