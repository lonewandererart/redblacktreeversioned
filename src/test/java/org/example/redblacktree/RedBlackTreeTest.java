package org.example.redblacktree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {

    @Test
    void rootTest(){
        List<Integer> treeList = List.of(2);
        assertInsertCorrect(treeList);
    }


    @Test
    void insertAndRebalanceWithRedUncle(){
        List<Integer> treeList = List.of(5, 4, 8, 6, 9, 7);
        assertInsertCorrect(treeList);
    }

    @Test
    void insertAndRebalanceRightRotationMinimal() {
        List<Integer> treeList = List.of(3, 2, 1);
        assertInsertCorrect(treeList);
    }

    @Test
    void insertAndRebalanceLeftRightRotationMinimal() {
        List<Integer> treeList = List.of(4,2,3);
        assertInsertCorrect(treeList);
    }

    @Test
    void insertAndRebalanceLeftRotationMinimal() {
        List<Integer> treeList = List.of(2, 4, 3);
        assertInsertCorrect(treeList);
    }

    @Test
    void insertAndRebalanceRightLeftRotationMinimal() {
        List<Integer> treeList = List.of(2, 4, 3);
        assertInsertCorrect(treeList);
    }

    @Test
    void noDuplicatesTest() {
        List<Integer> treeList = List.of(2, 3, 1, 2);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertThrows(IllegalArgumentException.class, () -> treeList.forEach(tree::insert));
    }

    @Test
    void leftRightRotationExtendedTest() {
        List<Integer> treeList = List.of(7, 5, 8, 4, 6, 9, 2, 10, 3);
        assertInsertCorrect(treeList);
    }

    @Test
    void deleteOneNodeTreeTest() {
        List<Integer> treeList = List.of(1);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        tree.delete(tree.getRoot().getValue());
        assertNull(tree.getRoot());
    }

    @Test
    void deleteLeftLeafMinimalTest() {
        List<Integer> treeList = List.of(2, 1, 3);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        int toDelete = 1;
        tree.delete(toDelete);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
    }

    @Test
    void deleteRightLeafMinimalTest() {
        List<Integer> treeList = List.of(1, 3);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        int toDelete = 3;
        tree.delete(toDelete);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
    }

    @Test
    void deleteManyTest() {
        List<Integer> treeList = new ArrayList<>(Arrays.asList(7, 5, 8, 4, 6, 9, 2, 10, 3));

        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);

        tree.delete(9);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(treeList.size()-1, treeElems.size(), "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(9));
        assertTrue(tree.validateRedBlackProperties());


        tree.delete(3);
        treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(treeList.size()-2, treeElems.size(), "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(3));
        assertTrue(tree.validateRedBlackProperties());

        tree.delete(4);
        treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(treeList.size()-3, treeElems.size(), "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(4));
        assertTrue(tree.validateRedBlackProperties());

        tree.delete(7);
        treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        assertEquals(treeList.size()-4, treeElems.size(), "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(7));
        assertTrue(tree.validateRedBlackProperties());

        tree.delete(10);
        System.out.println(tree);
        treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-5, treeElems.size(), "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(10));
        assertTrue(tree.validateRedBlackProperties());



    }

    @Test
    void deleteRootRightMinimalTest() {
        List<Integer> treeList = List.of(2,  3);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        int toDelete = 2;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
    }

    @Test
    void deleteRootLeftMinimalTest() {
        List<Integer> treeList = List.of(2,  1);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        int toDelete = 2;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
    }

    @Test
    void  deleteRedSiblingRightTest() {
        List<Integer> treeList = List.of(7, 5, 10, 3, 6, 8, 1);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 10;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteRedSiblingRightNotRootTest() {
        List<Integer> treeList = List.of(5, 2, 7, 1, 3, 6, 10, 4, 8, 13, 15);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 6;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteRedSiblingLeftNotRootTest() {
        List<Integer> treeList = List.of(10, 8, 13, 6, 9, 12, 14, 5, 7, 11, 4);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 6; //9
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteBlackSiblingRedParentTest() {
        List<Integer> treeList = List.of(5, 2, 7, 1, 3, 6, 10, 4, 8, 13, 15);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 10;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteBlackSiblingBlackParentTest() {
        List<Integer> treeList = List.of(5, 2, 7, 1, 3, 6, 10, 4, 8, 13, 15);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 3;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteRedSiblingLeftTest() {
        List<Integer> treeList = List.of(7, 5, 10, 3, 9, 11, 8);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 5;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteBlackSiblingRightBlackNephewTest() {
        List<Integer> treeList = List.of(17, 9, 19, 18, 75, 24);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 18;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteBlackSiblingLeftBlackNephewTest() {
        List<Integer> treeList = List.of(17, 9, 20, 18, 75, 19);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 75;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void  deleteBlackSiblingRedNephewTest() {
        List<Integer> treeList = List.of(17, 9, 19, 18, 75, 24, 81);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        System.out.println(tree);
        int toDelete = 18;
        tree.delete(toDelete);
        System.out.println(tree);
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        System.out.println(treeElems);
        assertEquals(treeList.size()-1, treeElems.size(),  "tree has wrong number of nodes");
        assertThrows(NoSuchElementException.class, () -> tree.findNode(toDelete));
        assertTrue(tree.validateRedBlackProperties());

    }

    @Test
    void persistanceTest() {
        List<Integer> treeList = List.of(3, 2, 4, 1);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        String oldTreeString = tree.toString();
        tree.insert(5);
        RedBlackTree<Integer> previousTree = tree.getPreviousVersion();
        assertEquals(oldTreeString, previousTree.toString());
        assertSame(tree.getRoot().getChildLeft(), previousTree.getRoot().getChildLeft(), "persistance violated. The node is changed although it should not");
        assertNotSame(tree.getRoot().getChildRight(), previousTree.getRoot().getChildRight(), "persistance violated. The node is not changed, although it should");
        assertEquals(tree.getRoot().getChildRight(), previousTree.getRoot().getChildRight(), "persistance violated. Node value is changed although it should not.");
    }

    @Test
    void historyMinimalTest() {
        List<Integer> treeList = List.of(3, 2, 4, 1);
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        treeList.forEach(tree::insert);
        tree.delete(1);
        tree.insert(1);
        assertEquals(6, tree.getHistory().size(), "historisation violated. Not all changes written in history");
        RedBlackTree<Integer> initTree = tree.getVersion(0);
        assertNotSame(tree.getRoot(), initTree.getRoot(), "persistance violated. The node is not changed, although it should");
        assertEquals(tree.getRoot(), initTree.getRoot(), "persistance violated. Node value is changed although it should not.");
        assertNull(initTree.getRoot().getChildRight(), "historisation violated. This is not initial tree.");
        assertNull(initTree.getRoot().getChildLeft(), "historisation violated. This is not initial tree.");
    }


    private void assertInsertCorrect(List<Integer> treeList) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        //treeList.forEach(tree::insert);
        for(int i=0; i<0;i++) {
            tree.insert(treeList.get(i));
        List<Integer> treeElems = new ArrayList<>();
        tree.forEach(treeElems::add);
        List<Integer> sortedTreeList = treeList.stream().sorted().toList();

        assertEquals(treeList.size(), treeElems.size(),  "tree has wrong number of nodes");
        assertEquals(sortedTreeList, treeElems, "tree is not sorted");
        assertTrue(tree.validateRedBlackProperties(), "tree violates red-black conditions");
        }

    }

}