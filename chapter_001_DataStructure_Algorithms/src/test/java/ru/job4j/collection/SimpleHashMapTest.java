package ru.job4j.collection;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SimpleHashMapTest {
    SimpleHashMap<String, String> blessedMap;
    String nastya = "Nastya", ivan = "Ivan", matvey = "Matvey", baka = "baka", prince = "Prince", balda = "balda";

    @Before
    public void setUp() throws Exception {
	blessedMap = new SimpleHashMap<>();
    }

    @Test
    public void insertOneThenGetOne() {
	blessedMap.insert(nastya, baka);
	assertThat(blessedMap.get(nastya), is(baka));
    }

    @Test
    public void getNonexistentElement() {
	assertNull(blessedMap.get(nastya));
    }

    @Test
    public void insertTwoElementsWithSameKey() {
	blessedMap.insert(nastya, baka);
	blessedMap.insert(nastya, balda);
	assertThat(blessedMap.get(nastya), is(baka));
	assertThat(blessedMap.getSize(), is(1));
    }

    @Test
    public void insertElementsWithSameHashingBySimpleHashMap() {
	blessedMap.insert(nastya, balda);
	blessedMap.insert(ivan, prince);
	blessedMap.insert(matvey, baka); //matvey has same hashing(IN SIMPLEHASHMAP!) like ivan
	assertThat(blessedMap.getSize(), is(3));
	assertThat(blessedMap.get(matvey), is(baka));
	assertThat(blessedMap.get(ivan), is(prince));
    }

    /**
     * Когда у нас создался внутренний linkedList и мы добавляем по тому же ключу
     */
    @Test
    public void insertElementsWithSameKeyWhenInnerLinkedListIsExisting() {
	blessedMap.insert(nastya, balda);
	blessedMap.insert(ivan, prince);
	blessedMap.insert(matvey, baka); //matvey has same hashing(IN SIMPLEHASHMAP!) like ivan
	blessedMap.insert(matvey, balda);
	assertThat(blessedMap.getSize(), is(3));
	assertThat(blessedMap.get(matvey), is(baka));
	assertThat(blessedMap.get(ivan), is(prince));
    }


    @Test
    public void simpleDeleteWhenMapHas1ElementInInnerLinkedList() {
	blessedMap.insert(nastya, balda);
	blessedMap.delete(nastya);
	assertNull(blessedMap.get(nastya));
	assertThat(blessedMap.getSize(), is(0));
    }

    @Test
    public void simpleDeleteWhenMapHasMoreThan1ElementInInnerLinkedList() {
	blessedMap.insert(matvey, baka); //matvey has same hashing(IN SIMPLEHASHMAP!) like ivan
	blessedMap.insert(ivan, prince);
	blessedMap.delete(matvey);
	assertNull(blessedMap.get(matvey));
	assertThat(blessedMap.get(ivan), is(prince));
	assertThat(blessedMap.getSize(), is(1));
    }


    @Test
    public void iteratorHasNext() {
	blessedMap.insert(matvey, baka);
	var mapIterator = blessedMap.iterator();
	assertTrue(mapIterator.hasNext());
	mapIterator.next();
	assertFalse(mapIterator.hasNext());
    }

    @Test
    public void iteratorNextWhenMapHasDifferentCellsInTable() {
	blessedMap.insert(nastya, balda);
	blessedMap.insert(ivan, prince);
	var mapIterator = blessedMap.iterator();
	assertTrue(mapIterator.hasNext());
	mapIterator.next();
	mapIterator.next();
	assertFalse(mapIterator.hasNext());
    }

    @Test
    public void iteratorNextWhenMapHasInnerLinkedList() {
	blessedMap.insert(matvey, balda);
	blessedMap.insert(ivan, prince);
	var mapIterator = blessedMap.iterator();
	assertTrue(mapIterator.hasNext());
	mapIterator.next();
	mapIterator.next();
	assertFalse(mapIterator.hasNext());
    }


}