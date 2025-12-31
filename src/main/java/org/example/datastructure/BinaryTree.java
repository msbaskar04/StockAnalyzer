package org.example.datastructure;

public class BinaryTree {

    BinaryNode binaryTreeRootNode;

    void insertNode(int key, int value) {
        if(binaryTreeRootNode==null){
            binaryTreeRootNode = new BinaryNode();
            binaryTreeRootNode.setKey(key);
            binaryTreeRootNode.setValue(value);
        } else {
            binaryTreeRootNode.insert(key, value);
        }
    }

    void findNode(int key) {
        if(binaryTreeRootNode == null) {
            System.out.println(String.format("Key %d is not found in binary tree", key));
        } else {
            int value = binaryTreeRootNode.findValue(key);
            if(value == -1) {
                System.out.println(String.format("Key %d is not found in binary tree", key));
            } else {
                System.out.println(String.format("%d = %d", key, value));
            }
        }
    }

    public static void main(String args[]) {
        BinaryTree binaryTree = new BinaryTree();
        binaryTree.insertNode(2, 20);
        binaryTree.insertNode(3, 30);
        binaryTree.insertNode(1, 10);
        binaryTree.insertNode(5, 50);
        binaryTree.insertNode(4, 40);
        binaryTree.insertNode(6, 60);
        binaryTree.findNode(4);
    }

}
