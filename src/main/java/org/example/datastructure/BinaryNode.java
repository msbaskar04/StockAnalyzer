package org.example.datastructure;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BinaryNode {
    BinaryNode left;
    BinaryNode right;
    Integer key;
    Integer value;

    void insert(int key, int value) {
        if(this.key == null) {
            this.key = key;
            this.value = value;
        } else if(key < this.key) {
            if(left == null) {
                left = new BinaryNode();
                left.key=key;
                left.value = value;
            } else {
                left.insert(key, value);
            }
        } else if(key > this.key) {
            if(right == null) {
                right = new BinaryNode();
                right.key=key;
                right.value = value;
            } else {
                right.insert(key, value);
            }
        }
    }

    int findValue(int key) {
        if(key == this. key) {
            return this.value;
        } else if(key<this.key && left!=null) {
            return left.findValue(key);
        } else if(key>this.key && right!=null) {
            return right.findValue(key);
        }
        return -1;
    }

}
