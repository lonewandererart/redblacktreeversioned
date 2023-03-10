package org.example.redblacktree;

import org.example.redblacktree.Node;
import org.example.redblacktree.RedBlackTree;

import java.util.*;

public class RedBlackTreeHistory<T extends Comparable<T>> implements Node.NodeChangeObserver<T>, RedBlackTree.RootChangeObserver<T> {
    private final Stack<Version<T>> versions = new Stack<>();
    private Version<T> currentRecording = null;

    @Override
    public void rootChanged(Node<T> oldRoot) {
        if (currentRecording == null) {
            throw new IllegalStateException("no recording has started!");
        }
        currentRecording.setOldRoot(oldRoot);
    }

    @Override
    public void onChange(Node.NodeChangeEvent<T> event) {
        if (currentRecording == null) {
            throw new IllegalStateException("no recording has started!");
        }
        currentRecording.addMemento(event.getPreviousState());
    }

    public void startRecording() {
        currentRecording = new Version<>();
    }

    public void stopRecording() {
        versions.push(currentRecording);
        currentRecording = null;
    }

    public Version<T> getLatestVersionChanges() {
        return versions.pop();
    }

    static class Version<T extends Comparable<T>> {
        private final Set<Node.Memento<T>> history = new HashSet<>();
        private Node<T> oldRoot;
        private boolean rootChanged = false;

        public void setOldRoot(Node<T> node) {
            if (!rootChanged) {
                rootChanged = true;
                oldRoot = node;
            }
        }

        public boolean isRootChanged() {
            return rootChanged;
        }

        public Node<T> getOldRoot() {
            return oldRoot;
        }

        public void addMemento(Node.Memento<T> value) {
            history.add(value);
        }

        public void restoreNodes() {
            history.forEach(Node.Memento::restore);
        }
    }
}
