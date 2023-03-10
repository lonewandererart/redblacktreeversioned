package org.example.redblacktree;

import org.example.redblacktree.Node;
import org.example.redblacktree.RedBlackTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedBlackTreeTest {

    @org.junit.jupiter.api.Test
    void rebalanceInsertTest() {

        List<Integer> listWithUncle = List.of(5, 4, 7, 6, 8, 9);
        RedBlackTree<Integer> tree = createDummyTreeFromArray(listWithUncle);
        assertTrue(tree.validateRedBlackProperties());

        List<Integer> sortedTreeList = listWithUncle.stream().sorted().toList();
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(sortedTreeList, treeElems);


        List<Integer> listRightHeavy1 = List.of(5, 4, 7, 9, 8);
        RedBlackTree<Integer> treeRightHeavy1 = createDummyTreeFromArray(listRightHeavy1);
        assertTrue(treeRightHeavy1.validateRedBlackProperties());

        List<Integer> sortedTreeRightHeavy1List = listRightHeavy1.stream().sorted().toList();
        List<Integer> treeRightHeavy1Elems = new ArrayList<>();
        treeRightHeavy1.forEach(treeRightHeavy1Elems::add);
        assertEquals(sortedTreeRightHeavy1List, treeRightHeavy1Elems);


        List<Integer> listRightHeavy2 = List.of(5, 4, 7, 9, 10);
        RedBlackTree<Integer> treeRightHeavy2 = createDummyTreeFromArray(listRightHeavy2);
        assertTrue(treeRightHeavy2.validateRedBlackProperties());

        List<Integer> sortedTreeRightHeavy2List = listRightHeavy2.stream().sorted().toList();
        List<Integer> treeRightHeavy2Elems = new ArrayList<>();
        treeRightHeavy2.forEach(treeRightHeavy2Elems::add);
        assertEquals(sortedTreeRightHeavy2List, treeRightHeavy2Elems);

    }

    @org.junit.jupiter.api.Test
    void insertNode() {
        Integer someValue = 4;
        RedBlackTree<Integer> tree = createDummyTreeFromArray(List.of(someValue));
        assertTrue(tree.validateRedBlackProperties());
        Node<Integer> root = tree.getRoot();

        assertNotNull(root);
        assertEquals(root.getValue(), someValue);
        assertNull(root.getParent());
        assertNull(root.getChildLeft());
        assertNull(root.getChildRight());

        Node<Integer> found = tree.searchNode(someValue);
        assertEquals(root, found);

        tree.deleteNode(someValue);
        assertNull(tree.getRoot());

        assertThrows(IllegalArgumentException.class, () -> createDummyTreeFromArray(List.of(1, 2, 3, 2)));

        List<Integer> treeList = generateRandomList(100);
        RedBlackTree<Integer> tree2 = createDummyTreeFromArray(treeList);
        assertTrue(tree2.validateRedBlackProperties());
        Node<Integer> someNode = tree2.searchNode(treeList.get(5));
        assertNotNull(someNode);
        assertEquals(treeList.get(5), someNode.getValue());

        List<Integer> sortedTreeList = treeList.stream().sorted().toList();
        List<Integer> tree2Elems = new ArrayList<>();
        tree2.forEach(tree2Elems::add);
        assertEquals(sortedTreeList, tree2Elems);

        tree2.deleteNode(treeList.get(5));
        assertNull(tree2.searchNode(treeList.get(5)));
        assertTrue(tree2.validateRedBlackProperties());

    }

    @org.junit.jupiter.api.Test
    void rebalanceAfterDeleteTest() {

        RedBlackTree<Integer> tree3 = createDummyTreeFromArray(List.of(2, 4, 1, 5, 3));
        assertTrue(tree3.validateRedBlackProperties());

        tree3.deleteNode(4);
        assertTrue(tree3.validateRedBlackProperties());
        List<Integer> tree3Elems = new ArrayList<>();
        tree3.forEach(tree3Elems::add);
        assertEquals(4, tree3Elems.size());

        RedBlackTree<Integer> tree4 = createDummyTreeFromArray(List.of(6, 3, 7, 4, 8, 1));
        assertTrue(tree4.validateRedBlackProperties());

        tree4.deleteNode(3);
        assertTrue(tree4.validateRedBlackProperties());
        List<Integer> tree4Elems = new ArrayList<>();
        tree4.forEach(tree4Elems::add);
        assertEquals(5, tree4Elems.size());

        tree4.deleteNode(tree4.getRoot().getValue());
        assertTrue(tree4.validateRedBlackProperties());
        tree4Elems = new ArrayList<>();
        tree4.forEach(tree4Elems::add);
        assertEquals(4, tree4Elems.size());


        RedBlackTree<Integer> tree5 = createDummyTreeFromArray(List.of(10, 5, 15, 6, 13, 18, 12, 14, 19));
        assertTrue(tree5.validateRedBlackProperties());

        tree5.deleteNode(15);
        assertTrue(tree5.validateRedBlackProperties());

        final List<Integer> tree6List = List.of(3, 1, 5, 2, 4, 6, 7);
        RedBlackTree<Integer> tree6 = createDummyTreeFromArray(tree6List);
        assertTrue(tree6.validateRedBlackProperties());

        tree6.deleteNode(1);
        assertTrue(tree6.validateRedBlackProperties());
        List<Integer> tree6Elems = new ArrayList<>();
        tree6.forEach(tree6Elems::add);
        assertEquals(tree6List.size() - 1, tree6Elems.size());

        final List<Integer> tree7List = List.of(8, 4, 9, 2, 5, 10, 6);
        RedBlackTree<Integer> tree7 = createDummyTreeFromArray(tree7List);
        assertTrue(tree7.validateRedBlackProperties());

        tree7.deleteNode(9);
        assertTrue(tree7.validateRedBlackProperties());
        List<Integer> tree7Elems = new ArrayList<>();
        tree7.forEach(tree7Elems::add);
        assertEquals(tree7List.size() - 1, tree7Elems.size());

        final List<Integer> tree8List = List.of(2, 1, 5, 4, 7, 6, 8);
        RedBlackTree<Integer> tree8 = createDummyTreeFromArray(tree8List);

        tree8.deleteNode(4);
        assertTrue(tree8.validateRedBlackProperties());
        List<Integer> tree8Elems = new ArrayList<>();
        tree8.forEach(tree8Elems::add);
        assertEquals(tree8List.size() - 1, tree8Elems.size());


        final List<Integer> tree9List = List.of(2, 1, 5, 4, 7, 6, 8, 10);
        RedBlackTree<Integer> tree9 = createDummyTreeFromArray(tree9List);
        assertTrue(tree9.validateRedBlackProperties());

        tree9.deleteNode(4);
        assertTrue(tree9.validateRedBlackProperties());
        List<Integer> tree9Elems = new ArrayList<>();
        tree9.forEach(tree9Elems::add);
        assertEquals(tree9List.size() - 1, tree9Elems.size());

        tree9.deleteNode(10);
        assertTrue(tree9.validateRedBlackProperties());

        tree9.deleteNode(4);
        assertTrue(tree9.validateRedBlackProperties());

        tree9.deleteNode(2);
        assertTrue(tree9.validateRedBlackProperties());

        final List<Integer> tree10List = List.of(10, 5, 15, 4);
        RedBlackTree<Integer> tree10 = createDummyTreeFromArray(tree10List);

        tree10.deleteNode(5);
        assertTrue(tree10.validateRedBlackProperties());

        RedBlackTree<Integer> tree11 = createDummyTreeFromArray(tree10List);

        tree10.deleteNode(15);
        assertTrue(tree11.validateRedBlackProperties());

        final List<Integer> tree12List = List.of(2, 1, 5, 4, 8, 3, 7);
        RedBlackTree<Integer> tree12 = createDummyTreeFromArray(tree12List);

        tree12.deleteNode(4);
        assertTrue(tree12.validateRedBlackProperties());


    }

    /**
     * Deep structure of trees is compared with toString method.
     * The reason for it lies in the implementation of delete method which uses
     * .setValue on the node. Overriding .equals method brings a lot of mess.
     *
     * @throws CloneNotSupportedException
     */
    @org.junit.jupiter.api.Test
    void restorePreviousTreeVersionTest() throws CloneNotSupportedException {

        List<Integer> arrayList = generateRandomList(100);
        RedBlackTree<Integer> tree = createDummyTreeFromArray(arrayList);
        RedBlackTree<Integer> treeCopy = (RedBlackTree<Integer>) tree.clone();

        assertEquals(tree.toString(), treeCopy.toString());

        Node<Integer> root = tree.getRoot();
        tree.deleteNode(root.getValue());
        assertNotEquals(tree.toString(), treeCopy.toString());
        tree.restoreToPrevious();
        assertEquals(treeCopy.toString(), tree.toString());

        int toDelete = arrayList.get(new Random().nextInt(0, 99));
        tree.deleteNode(toDelete);
        assertNull(tree.searchNode(toDelete));
        assertNotEquals(tree.toString(), treeCopy.toString());
        tree.restoreToPrevious();
        assertEquals(treeCopy.toString(), tree.toString());

        Integer insertValue = new Random().nextInt(1, 1000000);
        tree.insertNode(insertValue);
        assertEquals(insertValue, tree.searchNode(insertValue).getValue());
        assertNotEquals(tree.toString(), treeCopy.toString());
        tree.restoreToPrevious();
        assertEquals(treeCopy.toString(), tree.toString());

    }

    private RedBlackTree<Integer> createDummyTreeFromArray(Collection<Integer> arrayList) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        arrayList.forEach(tree::insertNode);
        return tree;
    }

    private List<Integer> generateRandomList(int listSize) {
        int bound = listSize * 1000;
        return new Random().ints(listSize, 1, bound)
                .boxed().collect(Collectors.toSet()).stream().toList();
    }


}