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
        } else {
            node.setPreviousNode(tail);
            tail.setNextNode(node);
        }
        tail = node;
        tail.setNextNode(null);
        removeNode(task.getIndex());
        nodeHashMap.put(task.getIndex(), node);
    }

                  


    public void removeNode(int id) {
        if (nodeHashMap.get(id) != null){
            Node nodeToRemove = nodeHashMap.get(id);
            if (nodeToRemove.equals(head)){
                if (nodeToRemove.getNextNode() != null) nodeToRemove.getNextNode().setPreviousNode(null);
                head = nodeToRemove.getNextNode();
            } else nodeToRemove.getPreviousNode().setNextNode(nodeToRemove.getNextNode());
            if (nodeToRemove.equals(tail)){
                if(nodeToRemove.getPreviousNode() != null) nodeToRemove.getPreviousNode().setNextNode(null);
                tail = nodeToRemove.getPreviousNode();
            } else {
               if (nodeToRemove.getNextNode() != null) {
                   nodeToRemove.getNextNode().setPreviousNode(nodeToRemove.getPreviousNode());
               }
            }
            nodeHashMap.remove(id);
        }
    }


}




class Node{
    final Task content;
    private Node previousNode;
    private Node nextNode;

    public Node(Task content) {
        this.content = content;
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
        return Objects.equals(content, node.content) && Objects.equals(previousNode, node.previousNode) && Objects.equals(nextNode, node.nextNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, previousNode, nextNode);
    }
}