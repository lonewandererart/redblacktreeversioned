package org.example.redblacktree;

import java.util.Objects;

public class Node <T extends Comparable<T>> {
    private final T value;
    private final NodeColour colour;
    private final Node<T> childLeft;
    private final Node<T> childRight;


    public Node(T value, NodeColour colour, Node<T> childLeft, Node<T> childRight) {
        this.value = value;
        this.colour = colour;
        this.childLeft = childLeft;
        this.childRight = childRight;

    }

    public Node(T value) {
        this(value, NodeColour.RED, null, null);
    }

    public Node(T value, NodeColour colour) {
        this(value, colour, null, null);
    }

    public Node() {
        this(null, NodeColour.BLACK);
    }

    public T getValue() {
        return this.value;
    }

    public NodeColour getColour() {
        return colour;
    }

    public Node<T> getChildLeft() {
        return childLeft;
    }

    public Node<T> getChildRight() {
        return childRight;
    }


    public int compareTo(Node<T> node) {
        if (node == null) {
            return 1;
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
    public Node<T> recolour() {
        return recolour(this.getColour().switchColour());
    }

    public Node<T> recolour(NodeColour newColour) {
        return new Node<>(this.getValue(), newColour, this.getChildLeft(), this.getChildRight());
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

    public boolean isLeaf() {
        return hasEmptyLeftChild() && hasEmptyRightChild();
    }

    public Node<T> getSibling(Node<T> parent) {
        if(parent == null) {
            return null;
        }
        return this.equals(parent.getChildRight()) ? parent.getChildLeft() : parent.getChildRight();
    }

    @Override
    public String toString() {
        String valueString = value == null ? "NIL" : value.toString();
        StringBuilder builder = new StringBuilder(valueString).append(colour.getColourValue()).append("[");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node<?> node = (Node<?>) o;

        return Objects.equals(value, node.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}