package ru.job4j.collection;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.collection.SimpleHashMap.Node;

import java.util.Iterator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleHashMapNodeTest {
    String nastya = "Nastya", ivan = "Ivan", matvey = "Matvey", baka = "baka",
	    prince = "Prince", balda = "balda";
    Node<String, String> cursedNode;
    Iterator<Node<String, String>> cursedIterator;

    @Before
    public void setUp() {
	cursedNode = new Node<String, String>(nastya, nastya.hashCode(), baka,
					      new Node<String, String>(ivan,
								       ivan.hashCode(),
								       prince,
								       new Node<String, String>(
									       matvey,
									       matvey.hashCode(),
									       balda,
									       null)));
	cursedIterator = cursedNode.iterator();
    }

    @Test
    public void iteratorHasNext() {
	assertTrue(cursedIterator.hasNext());
	cursedIterator.next();
	cursedIterator.next();
	cursedIterator.next();
	assertFalse(cursedIterator.hasNext());
    }

    @Test
    public void iteratorRemoveFirst() {
	cursedIterator.next();
	cursedIterator.remove();
	assertThat(cursedNode.key, is(ivan));
	assertThat(cursedNode.next.key, is(matvey));
    }

    @Test
    public void iteratorRemoveLast() {
	cursedIterator.next();
	cursedIterator.next();
	cursedIterator.next();
	cursedIterator.remove();
	assertNull(cursedNode.next.next);
	assertFalse(cursedIterator.hasNext());
    }

    @Test
    public void iteratorRemoveBetweenElements() {
	cursedIterator.next();
	cursedIterator.next();
	cursedIterator.remove();
	assertThat(cursedNode.next.key, is(matvey));
	assertTrue(cursedIterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveAfterRemove() {
	cursedIterator.next();
	cursedIterator.remove();
	cursedIterator.remove();
    }
}
