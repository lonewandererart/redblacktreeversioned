package org.example.redblacktree;

import java.util.Objects;

class Node<T extends Comparable<T>> implements Cloneable {
    private T value;
    private NodeColour colour = NodeColour.RED;


    private Node<T> childLeft;
    private Node<T> childRight;
    private Node<T> parent;

    private NodeChangeObserver<T> changeObserver = null;

    public Node(T value) {
        this.value = value;
    }


    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        fireChangeEvent();
        this.value = value;
    }

    public boolean isChildLeft() {
        return parent != null && this.compareTo(parent.getChildLeft()) == 0;
    }

    public boolean isChildRight() {
        return parent != null && this.compareTo(parent.getChildRight()) == 0;
    }

    public Node<T> getChildLeft() {
        return childLeft;
    }

    public void setChildLeft(Node<T> childLeft) {
        fireChangeEvent();
        this.childLeft = childLeft;
    }

    public Node<T> getChildRight() {
        return childRight;
    }

    public void setChildRight(Node<T> childRight) {
        fireChangeEvent();
        this.childRight = childRight;
    }

    public Node<T> getParent() {
        return parent;
    }

    public void setParent(Node<T> parent) {
        fireChangeEvent();
        this.parent = parent;
    }

    public NodeColour getColour() {
        return colour;
    }

    public void setColour(NodeColour colour) {
        if (this.colour != colour) {
            fireChangeEvent();
            this.colour = colour;
        }
    }

    public void switchColour() {
        setColour(this.isRed() ? NodeColour.BLACK : NodeColour.RED);
    }

    public void markRed() {
        setColour(NodeColour.RED);
    }

    public void markBlack() {
        setColour(NodeColour.BLACK);
    }

    public boolean isBlack() {
        return colour == NodeColour.BLACK;
    }

    public boolean isRed() {
        return colour == NodeColour.RED;
    }

    public boolean hasEmptyLeftChild() {
        return childLeft == null;
    }

    public boolean hasEmptyRightChild() {
        return childRight == null;
    }

    public void setChangeObserver(NodeChangeObserver<T> changeObserver) {
        this.changeObserver = changeObserver;
    }

    private void fireChangeEvent() {
        if (this.changeObserver != null) {
            this.changeObserver.onChange(new NodeChangeEvent<>(createMemento()));
        }
    }

    /**
     * null safe node comparison
     *
     * @param node
     * @return
     */
    public int compareTo(Node<T> node) {
        if (node == null) {
            return -1;
        }

        if (node.getValue() == null && this.getValue() == null) {
            return 0;
        }
        if (this.getValue() == null) {
            return -1;
        }
        if (node.getValue() == null) {
            return 1;
        }
        return this.getValue().compareTo(node.getValue());
    }

    protected Node<T> getSibling() {
        Node<T> parent = this.getParent();
        if (this.isChildLeft()) {
            return parent.getChildRight();
        } else if (this.isChildRight()) {
            return parent.getChildLeft();
        } else {
            throw new IllegalStateException("Parent is not a child of its grandparent");
        }
    }

    public boolean isLeaf() {
        return hasEmptyLeftChild() && hasEmptyRightChild();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(value.toString()).append(colour.getColourValue()).append("[");
        deepFirstConcatTreeString(childLeft, builder);
        builder.append(",");
        deepFirstConcatTreeString(childRight, builder);
        builder.append("]");
        return builder.toString();
    }

    private void deepFirstConcatTreeString(Node<T> node, StringBuilder builder) {
        if (node == null) {
            builder.append("null");
            return;
        }

        builder
                .append(node.getValue())
                .append(node.getColour().getColourValue())
                .append("[");
        deepFirstConcatTreeString(node.getChildLeft(), builder);
        builder.append(",");
        deepFirstConcatTreeString(node.getChildRight(), builder);
        builder.append("]");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Node<T> cloned = (Node<T>) super.clone();

        if (cloned.childLeft != null) {
            cloned.childLeft = (Node<T>) childLeft.clone();
            cloned.childLeft.parent = cloned;
        }
        if (cloned.childRight != null) {
            cloned.childRight = (Node<T>) childRight.clone();
            cloned.childRight.parent = cloned;
        }

        return cloned;
    }

    public Memento<T> createMemento() {
        return new Memento<>(this);
    }

    public static class NodeChangeEvent<T extends Comparable<T>> {
        private final Memento<T> previousState;

        public NodeChangeEvent(Memento<T> previousState) {
            this.previousState = previousState;
        }

        public Memento<T> getPreviousState() {
            return previousState;
        }
    }

    @FunctionalInterface
    public interface NodeChangeObserver<T extends Comparable<T>> {
        void onChange(NodeChangeEvent<T> event);
    }


    public static class Memento<T extends Comparable<T>> {
        private final T value;
        private final NodeColour colour;
        private final Node<T> childLeft;
        private final Node<T> childRight;
        private final Node<T> parent;
        private final Node<T> mementoOf;


        protected Memento(Node<T> node) {
            this.mementoOf = node;
            this.value = node.value;
            this.colour = node.colour;
            this.childLeft = node.childLeft;
            this.childRight = node.childRight;
            this.parent = node.parent;
        }

        protected void restore() {
            mementoOf.value = this.value;
            mementoOf.colour = this.colour;
            mementoOf.childLeft = this.childLeft;
            mementoOf.childRight = this.childRight;
            mementoOf.parent = this.parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Memento<?> memento = (Memento<?>) o;

            return Objects.equals(mementoOf, memento.mementoOf);
        }

        @Override
        public int hashCode() {
            return mementoOf.hashCode();
        }
    }
}
