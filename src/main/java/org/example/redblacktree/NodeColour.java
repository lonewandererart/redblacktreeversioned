package org.example.redblacktree;

enum NodeColour {
    RED("r"), BLACK("b");

    private String colourValue;

    NodeColour(String value) {
        this.colourValue = value;
    }

    String getColourValue(){
        return colourValue;
    }

    NodeColour switchColour() {
        return this == RED ? BLACK : RED;
    }
}
