package org.example.redblacktree;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class RedBlackTree<T extends Comparable<T>> implements Iterable<T>, Cloneable {

    private Node<T> root;

    private final RootChangeObserver<T> rootChangeObserver;

    private static final Logger logger = Logger.getLogger(RedBlackTree.class.getName());

    private final RedBlackTreeHistory<T> history = new RedBlackTreeHistory<>();

    public RedBlackTree() {
        this.rootChangeObserver = history;
    }

    public RedBlackTree(T rootValue) {
        this.rootChangeObserver = history;
        history.startRecording();
        setRoot(createNode(rootValue));
        root.markBlack();
        history.stopRecording();
    }

    private Node<T> createNode(T value) {
        Node<T> node = new Node<>(value);
        node.setChangeObserver(history);
        return node;
    }

    public Node<T> getRoot() {
        return root;
    }

    private void setRoot(Node<T> root) {
        this.rootChangeObserver.rootChanged(this.root);
        this.root = root;
    }

    public Node<T> searchNode(T value) {
        return searchNode(value, root);
    }

    public void insertNode(T value) {
        history.startRecording();
        Node<T> newNode = createNode(value);
        setRoot(insertNode(root, newNode));
        reColourAndReBalance(newNode);
        history.stopRecording();
    }


    public void restoreToPrevious() {
        RedBlackTreeHistory.Version<T> latestVersionChanges = history.getLatestVersionChanges();
        if (latestVersionChanges.isRootChanged()) {
            this.root = latestVersionChanges.getOldRoot();
        }
        latestVersionChanges.restoreNodes();
    }

    public void deleteNode(T value) {
        history.startRecording();
        deleteNode(value, root);
        history.stopRecording();
    }

    private Node<T> insertNode(Node<T> node, Node<T> newNode) {
        if (node == null) {
            return newNode;
        }
        if (newNode.compareTo(node) < 0) {
            node.setChildLeft(insertNode(node.getChildLeft(), newNode));
            node.getChildLeft().setParent(node);
        } else if (newNode.compareTo(node) > 0) {
            node.setChildRight(insertNode(node.getChildRight(), newNode));
            node.getChildRight().setParent(node);
        } else {
            throw new IllegalArgumentException("Duplicate node insert attempted. Node value: " + newNode.getValue());
        }
        return node;
    }

    private void deleteNode(T value, Node<T> localRoot)  {
        Node<T> node = searchNode(value, localRoot);
        if (node == null) {
            return;
        }

        NodeColour deletedNodeColor = node.getColour();
        Node<T> movedUpNode;

        if (node.getChildLeft() == null || node.getChildRight() == null) {
            movedUpNode = deleteNodeWithLessThan2Children(node);
            if(root == null) {
                return;
            }
        } else {

            Node<T> minNode = node.getChildRight();
            while (minNode.getChildLeft() != null) {
                minNode = minNode.getChildLeft();
            }

            T minNodeValue = minNode.getValue();
            deletedNodeColor = minNode.getColour();
            movedUpNode = deleteNodeWithLessThan2Children(minNode);
            node.setValue(minNodeValue);
        }
        if (deletedNodeColor == NodeColour.BLACK && movedUpNode != null) {
            fixRedBlackPropertiesAfterDelete(movedUpNode);
        }

    }


    private Node<T> deleteNodeWithLessThan2Children(Node<T> node) {
        if (node.getParent() == null) {
            setRoot(null);
            return null;
        }
        if (node.isLeaf()) {
            Node<T> newChild = null;
            updateParentsChildren(node, newChild);
            return newChild;
        }else if (node.hasEmptyLeftChild()) {
            updateParentsChildren(node, node.getChildRight());
            return node.getChildRight();
        } else {
            updateParentsChildren(node, node.getChildLeft());
            return node.getChildLeft();
        }

    }

    private Node<T> searchNode(T value, Node<T> localRoot) {
        if (localRoot == null) {
            return null;
        }
        if (value.compareTo(localRoot.getValue()) == 0) {
            return localRoot;
        } else if (value.compareTo(localRoot.getValue()) < 0) {
            return searchNode(value, localRoot.getChildLeft());
        } else {
            return searchNode(value, localRoot.getChildRight());
        }
    }

    private void reColourAndReBalance(Node<T> node) {
        Node<T> parent = node.getParent();

        if (parent == null) {
            node.markBlack();
            return;
        }

        if (parent.isBlack()) {
            return;
        }

        Node<T> grandparent = parent.getParent();
        Node<T> uncle = getUncle(parent);

        if (uncle != null && uncle.isRed()) {
            parent.switchColour();
            grandparent.switchColour();
            uncle.switchColour();
            reColourAndReBalance(grandparent);

        } else if (parent.isChildLeft()) {
            if (node.isChildRight()) {
                rotateLeft(parent);
                parent = node;
            }
            rotateRight(grandparent);

            parent.switchColour();
            grandparent.switchColour();

        } else {
            if (node.isChildLeft()) {
                rotateRight(parent);
                parent = node;
            }
            rotateLeft(grandparent);

            parent.switchColour();
            grandparent.switchColour();
        }
    }



    private void rotateRight(Node<T> node) {
        Node<T> leftChild = node.getChildLeft();

        node.setChildLeft(leftChild.getChildRight());
        if (leftChild.getChildRight() != null) {
            leftChild.getChildRight().setParent(node);
        }

        leftChild.setChildRight(node);
        leftChild.setParent(node.getParent());
        updateParentsChildren(node, leftChild);
        node.setParent(leftChild);
    }



    private void rotateLeft(Node<T> node) {
        Node<T> rightChild = node.getChildRight();

        node.setChildRight(rightChild.getChildLeft());
        if (rightChild.getChildLeft() != null) {
            rightChild.getChildLeft().setParent(node);
        }

        rightChild.setChildLeft(node);
        rightChild.setParent(node.getParent());
        updateParentsChildren(node, rightChild);
        node.setParent(rightChild);
    }

    private void updateParentsChildren(Node<T> oldChild, Node<T> newChild){
        Node<T> parent = oldChild.getParent();
        if(parent == null) {
            setRoot(newChild);
            return;
        } else if(oldChild.isChildLeft()) {
            parent.setChildLeft(newChild);
        } else if(oldChild.isChildRight()) {
            parent.setChildRight(newChild);
        }
        if(newChild != null) {
            newChild.setParent(parent);
        }

    }


    private Node<T> getUncle(Node<T> parent) {
        Node<T> grandparent = parent.getParent();
        return parent.isChildLeft() ?
                grandparent.getChildRight() :
                grandparent.getChildLeft();
    }

    private void fixRedBlackPropertiesAfterDelete(Node<T> node) {

        if (node == root) {
            node.markBlack();
            return;
        }

        Node<T> sibling = node.getSibling();

        if (sibling.isRed()) {
            sibling.switchColour();
            node.getParent().markRed();

            if (node.isChildLeft()) {
                rotateLeft(node.getParent());
            } else {
                rotateRight(node.getParent());
            }
            sibling = node.getSibling();
        }

        if ((sibling.getChildLeft() == null || sibling.getChildLeft().isBlack()) &&
                (sibling.getChildRight() == null || sibling.getChildRight().isBlack())) {
            sibling.markRed();

            if (node.getParent().isRed()) {
                node.getParent().switchColour();
            } else {
                fixRedBlackPropertiesAfterDelete(node.getParent());
            }
        } else {
            handleBlackSiblingWithAtLeastOneRedChild(node, sibling);
        }
    }

    private void handleBlackSiblingWithAtLeastOneRedChild(Node<T> node, Node<T> sibling) {
        if (node.isChildLeft() && (sibling.getChildRight() == null || sibling.getChildRight().isBlack())) {
            sibling.getChildLeft().switchColour();
            sibling.switchColour();
            rotateRight(sibling);
            sibling = node.getParent().getChildRight();
        } else if (node.isChildRight() && (sibling.getChildLeft() == null || sibling.getChildLeft().isBlack())) {

            sibling.getChildRight().switchColour();

            sibling.switchColour();
            rotateLeft(sibling);
            sibling = node.getParent().getChildLeft();
        }

        sibling.setColour(node.getParent().getColour());
        node.getParent().markBlack();
        if (node.isChildLeft()) {
            sibling.getChildRight().switchColour();
            rotateLeft(node.getParent());
        } else {
            sibling.getChildLeft().switchColour();
            rotateRight(node.getParent());
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeIterator(root);
    }

    private class TreeIterator implements Iterator<T> {
        private Node<T> next;

        public TreeIterator(Node<T> root) {
            next = root;
            if(next == null)
                return;

            while (!next.hasEmptyLeftChild())
                next = next.getChildLeft();
        }

        public boolean hasNext(){
            return next != null;
        }

        public T next(){
            if(!hasNext()) throw new NoSuchElementException();
            Node<T> r = next;

            if(next.getChildRight() != null) {
                next = next.getChildRight();
                while (next.getChildLeft() != null)
                    next = next.getChildLeft();
                return r.getValue();
            }

            while(true) {
                if(next.getParent() == null) {
                    next = null;
                    return r.getValue();
                }
                if(next.getParent().getChildLeft() == next) {
                    next = next.getParent();
                    return r.getValue();
                }
                next = next.getParent();
            }
        }
    }


    @Override
    public String toString() {
        return root == null ? null : "RedBlackTree={" +
                root +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        RedBlackTree<T> cloned = (RedBlackTree<T>) super.clone();
        if (root != null) {
            cloned.root = (Node<T>)root.clone();
        }
        return cloned;
    }

    boolean validateRedBlackProperties() {
        //1. The root is black.
        if(root.isRed()) {
            logger.warning("The root is not black");
            return false;
        }
        List<String> allPaths = collectAllPaths();
        String onePath = allPaths.get(0);
        int count = StringUtils.countMatches(onePath, "b");
        for(String path : allPaths) {
            //2. A red node must not have red children.
            if(path.contains("rr")) {
                logger.warning("In path [" + path + "] a red node has red child");
                return false;
            }
            //3.  All paths from a node to the leaves below contain the same number of black nodes.
            if(StringUtils.countMatches(path, "b") != count) {
                logger.warning("path [" + onePath + "] and [" + path + "] have different number of black nodes.");
                return false;
            }
        }
        return true;
    }

    private List<String> collectAllPaths() {
        return collectAllPaths(root, "", new ArrayList<>());
    }

    private List<String> collectAllPaths(Node<T> node, String curPath, List<String> allPaths) {
        if(node == null) {
            return allPaths;
        }
        curPath = curPath.concat(node.getColour().getColourValue());
        if(node.isLeaf()) {
            allPaths.add(curPath);
            return allPaths;
        }
        allPaths = collectAllPaths(node.getChildLeft(), curPath, allPaths);
        allPaths = collectAllPaths(node.getChildRight(), curPath, allPaths);
        return allPaths;
    }

    public interface RootChangeObserver<T extends Comparable<T>> {
        void rootChanged(Node<T> oldRoot);
    }
}
