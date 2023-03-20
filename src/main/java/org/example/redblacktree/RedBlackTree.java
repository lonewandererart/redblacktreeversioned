package org.example.redblacktree;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.logging.Logger;

public class RedBlackTree<T extends Comparable<T>> implements Iterable<T> {
    private final Stack<Node<T>> history = new Stack<>();

    private static final Logger logger = Logger.getLogger(RedBlackTree.class.getName());

    public RedBlackTree() {}

    public RedBlackTree(Node<T> rootNode) {
        history.add(rootNode);
    }



    public void insert(T value) {
        logger.info("insert " + value);
        Node<T> toInsert = new Node<>(value);

        Node<T> newRoot;

        if (getRoot() == null) {
            logger.info("tree is empty, insert root");
            newRoot = new Node<>(value, NodeColour.BLACK);
        } else {
            if(toInsert.compareTo(getRoot()) < 0) {
                newRoot = new Node<>(getRoot().getValue(), NodeColour.BLACK, insertNode(getRoot().getChildLeft(), toInsert), getRoot().getChildRight());
            } else if (toInsert.compareTo(getRoot()) > 0) {
                newRoot = new Node<>(getRoot().getValue(), NodeColour.BLACK, getRoot().getChildLeft(), insertNode(getRoot().getChildRight(), toInsert));
            } else {
                throw new IllegalArgumentException("Node with value " + value + " already exists");
            }

        }
        newRoot = rebalanceAfterInsert(newRoot, toInsert);
        history.push(newRoot);
    }

    public void delete(T value) {
        logger.info("delete " + value);
        history.push(deleteValue(value));
    }


    private Node<T> insertNode(Node<T> current, Node<T> toInsert) {
        if(current == null) {
            return toInsert;
        } else if (toInsert.compareTo(current) < 0) {
            return new Node<>(current.getValue(), current.getColour(), insertNode(current.getChildLeft(), toInsert), current.getChildRight());
        } else if (toInsert.compareTo(current) > 0) {
            return new Node<>(current.getValue(), current.getColour(), current.getChildLeft(), insertNode(current.getChildRight(), toInsert));
        } else {
            throw new IllegalArgumentException("Node with value " + toInsert.getValue() + " already exists");
        }
    }

    private Node<T> reInsertNode(Node<T> current, T oldNodeValue, Node<T> toInsert) {
        if(current.getValue().equals(oldNodeValue)) {
            return toInsert;
        } else if (oldNodeValue.compareTo(current.getValue()) < 0) {
            return new Node<>(current.getValue(), current.getColour(), reInsertNode(current.getChildLeft(), oldNodeValue, toInsert), current.getChildRight());
        } else {
            return new Node<>(current.getValue(), current.getColour(), current.getChildLeft(), reInsertNode(current.getChildRight(), oldNodeValue, toInsert));
        }
    }

    private Node<T> rebalanceAfterInsert(Node<T> root, Node<T> node) {

        if(history.isEmpty()){
            return root;
        }

        if(node.equals(root)) {
            return new Node<>(node.getValue(), NodeColour.BLACK, node.getChildLeft(), node.getChildRight());
        }

        Node<T> parent = findParent(root, node.getValue());

        if (parent.isBlack()) {
            return root;
        }

        Node<T> grandparent = findParent(root, parent.getValue());
        Node<T> grandparentOld = grandparent;
        Node<T> uncle = parent.getSibling(grandparent);

        if(uncle != null && uncle.isRed()) {
            Node<T> newGrandparent;
            if(parent.equals(grandparent.getChildLeft())) {
                newGrandparent = new Node<>(grandparent.getValue(), grandparent.getColour(), parent.recolour(), uncle.recolour()).recolour();
            } else {
                newGrandparent = new Node<>(grandparent.getValue(), grandparent.getColour(), uncle.recolour(), parent.recolour()).recolour();
            }
            if(newGrandparent.equals(root)) {
                root = rebalanceAfterInsert(root, newGrandparent);
            } else {
                Node<T> newRoot = new Node<>(root.getValue(), root.getColour(), root.getChildLeft(), root.getChildRight());
                root = reInsertNode(newRoot, grandparentOld.getValue(), newGrandparent);
                root = rebalanceAfterInsert(root, findNode(root, newGrandparent.getValue()));
            }
            
        } else if(parent.equals(grandparent.getChildLeft())) {
            if (node.equals(parent.getChildRight())) {
                parent = rotateLeft(parent).recolour();
                grandparent = new Node<>(grandparent.getValue(), grandparent.getColour(), parent, grandparent.getChildRight());

            } else {
                parent = parent.recolour();
            }
            grandparent = new Node<>(grandparent.getValue(), grandparent.getColour().switchColour(), parent, grandparent.getChildRight());
            grandparent = rotateRight(grandparent);

            if(grandparentOld.equals(root)) {
                return grandparent;
            }
            Node<T> newRoot = new Node<>(root.getValue(), root.getColour(), root.getChildLeft(), root.getChildRight());
            root = reInsertNode(newRoot, grandparentOld.getValue(), grandparent);
            root = rebalanceAfterInsert(root, findNode(root, grandparentOld.getValue()));
        } else {
            if (node.equals(parent.getChildLeft())) {
                parent = rotateRight(parent).recolour();
                grandparent = new Node<>(grandparent.getValue(), grandparent.getColour(), grandparent.getChildLeft(), parent);

            } else {
                parent = parent.recolour();
            }
            grandparent = new Node<>(grandparent.getValue(), grandparent.getColour().switchColour(),  grandparent.getChildLeft(), parent);
            grandparent = rotateLeft(grandparent);

            if(grandparentOld.equals(root)) {
                return grandparent;
            }
            Node<T> newRoot = new Node<>(root.getValue(), root.getColour(), root.getChildLeft(), root.getChildRight());
            root = reInsertNode(newRoot, grandparentOld.getValue(), grandparent);
            root = rebalanceAfterInsert(root, findNode(root, grandparent.getValue()));
        }
        return root;

    }


    private Node<T> deleteValue(T value) {

        Node<T> root = getRoot();
        Node<T> parent = findParent(root, value);

        Node<T> toDelete = findNode(parent, value);

        if(toDelete == root && toDelete.isLeaf()) {
            return null;
        }

        T parentValue = parent == null ? null : parent.getValue();
        boolean isToDeleteLeftChild = toDelete.compareTo(parent) < 0;

        Node<T> movedUpNode;
        Node<T> movedUpNodeParent;
        NodeColour deletedNodeColour = toDelete.getColour();

        if(toDelete.hasEmptyLeftChild() || toDelete.hasEmptyRightChild()) {
            movedUpNode = deleteNodeWithZeroOrOneChild(toDelete);
            if(parent == null) {
                return movedUpNode;
            }
            parent = updateParentsChildren(parent, movedUpNode, isToDeleteLeftChild);
            movedUpNodeParent = parent;

        } else {
            Node<T> youngestRightNode = findYoungestNodeValue(toDelete.getChildRight());
            Node<T> youngestRightNodeParent = findParent(toDelete, youngestRightNode.getValue());

            movedUpNode = deleteNodeWithZeroOrOneChild(youngestRightNode);
            deletedNodeColour = youngestRightNode.getColour();

            youngestRightNodeParent = updateParentsChildren(youngestRightNodeParent, movedUpNode, youngestRightNodeParent.getChildLeft() == youngestRightNode);
            toDelete = reInsertNode(toDelete, youngestRightNodeParent.getValue(), youngestRightNodeParent);
            toDelete = new Node<>(youngestRightNode.getValue(), toDelete.getColour(), toDelete.getChildLeft(), toDelete.getChildRight());
            movedUpNodeParent = toDelete;
            if(parent == null) {
                root = toDelete;
            } else {
                parent = updateParentsChildren(parent, toDelete, isToDeleteLeftChild);
            }

        }

        if(deletedNodeColour == NodeColour.BLACK) {
            root = reInsertNode(root, parentValue, parent);
            root = recolourAndRebalanceAfterDelete(root, movedUpNodeParent, movedUpNode);
            if (movedUpNode.getClass() == NilNode.class) {
                Node<T> oldParent = findNode(root, movedUpNodeParent.getValue());
                if(movedUpNode == oldParent.getChildLeft()){

                oldParent = updateParentsChildren(oldParent, null, true);
                } else {
                    oldParent = updateParentsChildren(oldParent, null, false);

                }
                return reInsertNode(root, oldParent.getValue(), oldParent);
            }
            return root;
        }
        if(parent == null) {
            return root;
        }
        return reInsertNode(root, parentValue, parent);
    }


    private Node<T> recolourAndRebalanceAfterDelete(Node<T> root, Node<T> parent, Node<T> node){
        if(node.equals(root)){
            return reInsertNode(root, parent.getValue(), parent);
        }

        T parentValue = parent.getValue();
        boolean isNodeLeftChild = parent.getChildLeft() == node;
        Node<T> sibling = node.getSibling(parent);
        if(sibling.isRed()) {
            sibling = sibling.recolour();
            if(isNodeLeftChild) {
                parent = new Node<>(parent.getValue(), NodeColour.RED, node, sibling);
                if(parent.equals(root)) {
                    root = rotateLeft(parent);
                } else {
                    parent = rotateLeft(parent);
                    root = reInsertNode(root, parentValue, parent);
                }
            } else {
                parent = new Node<>(parent.getValue(), NodeColour.RED, sibling, node);
                if(parent.equals(root)) {
                    root = rotateRight(parent);
                } else {
                    parent = rotateRight(parent);
                    root = reInsertNode(root, parentValue, parent);
                }
            }

            parent = findNode(root, parentValue);
            sibling = node.getSibling(parent);
        }

        if ((sibling.getChildLeft() == null || sibling.getChildLeft().isBlack()) &&
                (sibling.getChildRight() == null || sibling.getChildRight().isBlack())) {

            sibling = sibling.recolour(NodeColour.RED);
            parent = updateParentsChildren(parent, sibling, !isNodeLeftChild);

            if (parent.isRed()) {
                parent = parent.recolour();
                return reInsertNode(root, parent.getValue(), parent);
            } else {
                Node<T> grandparent = findParent(root, parent.getValue());
                grandparent = updateParentsChildren(grandparent, parent, grandparent.getChildLeft().equals(parent));

                return recolourAndRebalanceAfterDelete(root, grandparent, parent);
            }
        } else {
            return reInsertNode(root, parent.getValue(), handleBlackSiblingWithAtLeastOneRedChild(parent, node, sibling));
        }
    }

    private Node<T> handleBlackSiblingWithAtLeastOneRedChild(Node<T> parent, Node<T> node, Node<T> sibling) {
        boolean nodeIsLeftChild = parent.getChildLeft().equals(node);

        if (nodeIsLeftChild && (sibling.getChildRight() == null || sibling.getChildRight().isBlack())) {
            Node<T> siblingChildLeft = sibling.getChildLeft().recolour(NodeColour.BLACK);
            sibling = new Node<>(sibling.getValue(), sibling.getColour().switchColour(), siblingChildLeft, sibling.getChildRight());
            sibling = rotateRight(sibling);
            parent = new Node<>(parent.getValue(), parent.getColour(), parent.getChildLeft(), sibling);
        } else if (!nodeIsLeftChild && (sibling.getChildLeft() == null || sibling.getChildLeft().isBlack())) {
            Node<T> siblingChildRight = sibling.getChildRight().recolour(NodeColour.BLACK);
            sibling = new Node<>(sibling.getValue(), sibling.getColour().switchColour(), sibling.getChildLeft(), siblingChildRight);
            sibling = rotateLeft(sibling);
            parent = new Node<>(parent.getValue(), parent.getColour(), sibling, parent.getChildRight());
        }

        if (nodeIsLeftChild) {
            Node<T> siblingChildRight = sibling.getChildRight().recolour(NodeColour.BLACK);
            sibling = new Node<>(sibling.getValue(), parent.getColour(), sibling.getChildLeft(), siblingChildRight);
            parent = new Node<>(parent.getValue(), NodeColour.BLACK, parent.getChildLeft(), sibling);
            parent = rotateLeft(parent);
        } else {
            Node<T> siblingChildLeft = sibling.getChildLeft().recolour(NodeColour.BLACK);
            sibling = new Node<>(sibling.getValue(), parent.getColour(), siblingChildLeft, sibling.getChildRight());
            parent = new Node<>(parent.getValue(), NodeColour.BLACK, sibling, parent.getChildRight());
            parent = rotateRight(parent);
        }
        return parent;
    }

    private Node<T> findYoungestNodeValue(Node<T> current) {
        if (current.getChildLeft() == null) {
            return current;
        }
        return findYoungestNodeValue(current.getChildLeft());
    }

    
    private Node<T> deleteNodeWithZeroOrOneChild(Node<T> node) {
        if(node.getChildLeft() != null){
            return node.getChildLeft();
        } else if(node.getChildRight() != null){
            return node.getChildRight();
        } else {
            return node.getColour() == NodeColour.BLACK ? new NilNode<>() : null;
        }
    }

    private Node<T> updateParentsChildren(Node<T> parent, Node<T> newChild, boolean updateLeft) {
        return updateLeft ?
                new Node<>(parent.getValue(), parent.getColour(), newChild, parent.getChildRight()) :
                new Node<>(parent.getValue(), parent.getColour(), parent.getChildLeft(), newChild);
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> leftChild = node.getChildLeft();
        Node<T> newNode = new Node<>(node.getValue(), node.getColour(), leftChild.getChildRight(), node.getChildRight());
        return new Node<>(leftChild.getValue(), leftChild.getColour(), leftChild.getChildLeft(), newNode);
    }


    public Node<T> rotateLeft(Node<T> node){
        Node<T> childRight = node.getChildRight();
        Node<T> newNode = new Node<>(node.getValue(), node.getColour(), node.getChildLeft(), childRight.getChildLeft());
        return new Node<>(childRight.getValue(), childRight.getColour(), newNode, childRight.getChildRight());
    }
    
    
    public List<Node<T>> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public RedBlackTree<T> getVersion(int version) {
        return new RedBlackTree<>(history.get(version));
    }

    public RedBlackTree<T> getPreviousVersion(){
        return getVersion(history.size()-2);
    }

    public Node<T> getRoot() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    public Node<T> findNode(T value) {
        return findNode(getRoot(), value);
    }

    private Node<T> findNode(Node<T> current, T value) {
        if(current == null){
            return getRoot();
        }
        if (current.getValue().compareTo(value) > 0) {
            if (current.getChildLeft() != null) {
                return findNode(current.getChildLeft(), value);
            } else {
                throw new NoSuchElementException("tree has no element with value " + value);
            }
        } else if (current.getValue().compareTo(value) < 0) {
            if (current.getChildRight() != null) {
                return findNode(current.getChildRight(), value);
            } else {
                throw new NoSuchElementException("tree has no element with value " + value);
            }
        }
        return current;
    }
    

    private Node<T> findParent(Node<T> currentNode, T value) {
        if(value.equals(currentNode.getValue())){
            return null;
        }
        if (currentNode.getValue().compareTo(value) < 0 && currentNode.getChildRight() != null && currentNode.getChildRight().getValue() != value) {
            return findParent(currentNode.getChildRight(), value);
        } else if (currentNode.getValue().compareTo(value) > 0 && currentNode.getChildLeft() != null && currentNode.getChildLeft().getValue() != value) {
            return findParent(currentNode.getChildLeft(), value);
        }
        return currentNode;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator(getRoot());
    }

    private class TreeIterator implements Iterator<T> {
        private Node<T> next;
        private final Stack<Node<T>> cursor = new Stack<>();

        public TreeIterator(Node<T> root) {
            next = root;

            if(next == null) {
                return;
            }

            while (!next.hasEmptyLeftChild()) {
                cursor.push(next);
                next = next.getChildLeft();
            }
        }

        public boolean hasNext(){
            return next != null;
        }

        public T next(){

            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<T> r = next;
            if(next.getChildRight() != null) {
                cursor.push(next);
                next = next.getChildRight();
                while (next.getChildLeft() != null) {
                    cursor.push(next);
                    next = next.getChildLeft();
                }
                return r.getValue();
            }

            while(true) {
                if(cursor.isEmpty()) {
                    next = null;
                    return r.getValue();
                }
                if(cursor.peek().getChildLeft() == next) {
                    next = cursor.pop();
                    return r.getValue();
                }
                next = cursor.pop();
            }
        }
    }


    boolean validateRedBlackProperties() {
        //1. The root is black.
        if (getRoot().isRed()) {
            logger.warning("The root is not black");
            return false;
        }
        List<String> allPaths = collectAllPaths();
        String onePath = allPaths.get(0);
        int count = StringUtils.countMatches(onePath, "b");
        for (String path : allPaths) {
            //2. A red node must not have red children.
            if (path.contains("rr")) {
                logger.warning("In path [" + path + "] a red node has red child");
                return false;
            }
            //3.  All paths from a node to the leaves below contain the same number of black nodes.
            if (StringUtils.countMatches(path, "b") != count) {
                logger.warning("path [" + onePath + "] and [" + path + "] have different number of black nodes.");
                return false;
            }
        }
        return true;
    }

    private List<String> collectAllPaths() {
        return collectAllPaths(getRoot(), "", new ArrayList<>());
    }

    private List<String> collectAllPaths(Node<T> node, String curPath, List<String> allPaths) {
        if (node == null) {
            return allPaths;
        }
        curPath = curPath.concat(node.getColour().getColourValue());
        if (node.isLeaf()) {
            allPaths.add(curPath);
            return allPaths;
        }
        allPaths = collectAllPaths(node.getChildLeft(), curPath, allPaths);
        allPaths = collectAllPaths(node.getChildRight(), curPath, allPaths);
        return allPaths;
    }

    private static class NilNode<T extends Comparable<T>> extends Node<T> {
        private NilNode() {
            super();
        }
    }

    @Override
    public String toString() {
        return getRoot() == null ? null : "RedBlackTree={" +
                getRoot() +
                '}';
    }
}
