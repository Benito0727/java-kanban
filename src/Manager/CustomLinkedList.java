package Manager;

import Tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CustomLinkedList {
    private Node head;
    private Node tail;

    Map<Integer, Node> nodeHashMap = new HashMap<>();

    public Node getTail() {
        return tail;
    }


    public void linkLast(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
            tail = node;
            removeNode(task.getIndex());
        } else {
            node.setPreviousNode(tail);
            tail.setNextNode(node);
            tail = node;
            removeNode(task.getIndex());
        }
        nodeHashMap.put(task.getIndex(), node);
    }

                  


    public void removeNode(int id) {
        if (nodeHashMap.get(id) != null){
            Node oldNode = nodeHashMap.get(id);
            if (nodeHashMap.get(id).equals(head)){
                oldNode.getNextNode().setPreviousNode(null);
                head = oldNode.getNextNode();
            } else oldNode.getPreviousNode().setNextNode(oldNode.getNextNode());
            if (nodeHashMap.get(id).equals(tail)){
                oldNode.getPreviousNode().setNextNode(null);
                tail = oldNode.getPreviousNode();
            } else oldNode.getNextNode().setPreviousNode(oldNode.getPreviousNode());
            nodeHashMap.remove(id);
        }
    }


}




class Node{
    final Task values;
    private Node previousNode;
    private Node nextNode;

    public Node(Task values) {
        this.values = values;
    }

    public Node getPreviousNode(){
        return previousNode;
    }

    public void setPreviousNode(Node node){
        previousNode = node;
    }

    public Node getNextNode(){
        return nextNode;
    }

    public void setNextNode(Node node){
        nextNode = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(values, node.values) && Objects.equals(previousNode, node.previousNode) && Objects.equals(nextNode, node.nextNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, previousNode, nextNode);
    }
}