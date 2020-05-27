package ru.job4j.collection;

import java.util.*;
import java.util.function.Predicate;

/**
 * Упрощенная Map
 */
public class SimpleHashMap<K, V> implements Iterable<SimpleHashMap.Node<K, V>> {
    /**
     * Версия коллекции, для правильной работы итератора с выбросом ConcurrentModificationException
     */
    private int versionOfCollection;

    /**
     * Размер table по умолчанию, а вообще он должен быть строго кратен 2 для корректной работы хэширования
     */
    public static final int DEFAULT_SIZE = 16;
    /**
     * Процент заполнения по умолчанию
     */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /**
     * Массив односвязных списков - парлия на table из HashMap
     */
    private Node<K, V>[] table;
    /**
     * Процент "заполнения" table. При достижении определенного уровня - нужно расширение таблицы и перехешировка
     * элементов, потому что она будет зависеть от её(table) размера
     */
    private float loadFactor;

    /**
     * Количество элементов в table, учитывая ВСЕ ВНУТРЕННИЕ связные списки
     */
    private int countOfElements;

    /**
     * Количество задействованных "корзин", то есть ячеек в table
     */
    private int countOfUsedBaskets = 0;

    /**
     * Конструктор c произвольным процентом заполнения и с дефолтным
     * Без валидации входных данных
     */
    public SimpleHashMap(float loadFactor) {
	table = new Node[DEFAULT_SIZE];
	this.loadFactor = loadFactor;
    }

    public SimpleHashMap() {
	this(DEFAULT_LOAD_FACTOR);
    }

    /**
     * Геттер для countOfElements
     */
    public int getSize() {
	return countOfElements;
    }

    /**
     * Вставка элемента
     * deepNode - если у нас по адресу в таблице уже что-то лежит, мы рассматриваем это сразу как связный список
     * <p>
     * Если в таблице по индесу ничего нет - значит мы впервые её задействуем (нужно для countOfBaskets), иначе ищем последний элемент в списке
     * с проверкой на совпадение с добавляемым
     * Если в if с предикатом мы нашли совпадение (true), то значит такой элемент в связном списке уже есть и возвращаем false
     * <p>
     * Если перед добавлением, происходит переполнение loadFactor - расширяем таблицу и перехэшируем элементы
     */
    public boolean insert(K key, V value) {
	if (table.length * loadFactor <= countOfUsedBaskets) {
	    resizeAndReHashTable();
	}
	int hash = key.hashCode();
	int indexOfTable = getIndexByHash(hash);
	Node<K, V> newNode = new Node<>(key, hash, value, null);
	if (table[indexOfTable] != null) {
	    Node<K, V> deepNode = table[indexOfTable];
	    if (checkRuleInInnerLinkedList(deepNode.iterator(), o -> o.hash == newNode.hash && o.key.equals(newNode.key)) != null) {
		return false;
	    } else {
		getLastNode(table[indexOfTable]).next = newNode;
	    }
	} else {
	    countOfUsedBaskets++;
	    table[indexOfTable] = newNode;
	}
	countOfElements++;
	versionOfCollection++;
	return true;
    }

    /**
     * Возвращает элемент, если такой есть. Если там список, то бежим по нему в поисках
     */
    public V get(K key) {
	int hash = key.hashCode();
	int index = getIndexByHash(hash);
	if (table[index] != null) {
	    Iterator<Node<K, V>> nodeIterator = table[index].iterator();
	    Node<K, V> nodeInLinkedList = checkRuleInInnerLinkedList(nodeIterator, o -> o.hash == hash && o.key.equals(key));
	    if (nodeInLinkedList != null) {
		return nodeInLinkedList.value;
	    }
	}
	return null;
    }

    /**
     * Удаление элемента путём использования замудренного remove в итераторе, который вряд ли вообще заработает
     * Используется костыль: Если элемент в списке был изначально один, то remove у итератора в Node не сможет удалить сам себя
     * Связано это с тем, что у нас нет "фейковых" стартовых Node для удобной навигации по cвязным спискам.
     * Поэтому, если возникает подобная ситуация (удаление через remove самого себя), элементу в методе remove заNullяются ключ и значение, что является
     * маркером для удаления всего списка уже непосредственно в методе delete
     */
    public boolean delete(K key) {
	int hash = key.hashCode();
	int index = getIndexByHash(hash);
	if (table[index] != null) {
	    Iterator<Node<K, V>> nodeIterator = table[index].iterator();
	    Node<K, V> nodeRoadToDelete = checkRuleInInnerLinkedList(nodeIterator, o -> o.hash == hash && o.key.equals(key));
	    if (nodeRoadToDelete != null) {
		nodeIterator.remove();
		if (table[index].key == null && table[index].value == null) { //костыль
		    table[index] = null;
		    countOfUsedBaskets--;
		}
		countOfElements--;
		versionOfCollection++;
		return true;
	    }
	}
	return false;
    }

    /**
     * Погружаемся в глубину связного списка в table и возвращаем последнюю ноду
     */
    private Node<K, V> getLastNode(Node<K, V> deepNode) {
	var nodeIterator = deepNode.iterator();
	Node<K, V> result = null;
	while (nodeIterator.hasNext()) {
	    result = nodeIterator.next();
	}
	return result;
    }

    /**
     * Проверка связного списка на условие
     * Возвращает первый Node, которое выполнило условие предиката или null, если никто не выполнил
     */
    private Node<K, V> checkRuleInInnerLinkedList(Iterator<Node<K, V>> iterator, Predicate<Node<K, V>> rule) {
	Node<K, V> problemNode;
	while (iterator.hasNext()) {
	    problemNode = iterator.next();
	    if (rule.test(problemNode)) {
		return problemNode;
	    }
	}
	return null;
    }

    /**
     * Преобразование хэшкода для определения его позиции в table
     * table.length - 1, видимо из-за того, что в 0 принято хранить null ключи и фактический размер table для "true" элементов на один меньше
     */
    private int getIndexByHash(int hash) {
	return hash & table.length - 1;
    }

    /**
     * Расширяем таблицу, если достигли/привысили loadFactor
     * Расширяем строго по степеням двойки, чтобы правильно хэшировать
     * Затем перехешируем её элементы
     * Учитывается наличие связных списков внутри
     * oldTable - содержит ссылку на старую
     * Table же мы заново запоняем через add, так как там вся логика
     */
    private void resizeAndReHashTable() {
	countOfUsedBaskets = 0;
	countOfElements = 0;
	Node<K, V>[] oldTable = table;
	table = new Node[(table.length << 2)];
	for (Node<K, V> oldNode : oldTable) {
	    if (oldNode != null) {
		Node<K, V> deepNode = oldNode;
		do {
		    insert(deepNode.key, deepNode.value);
		    deepNode = deepNode.next;
		} while (deepNode != null);
	    }
	}
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Node<K, V>> iterator() {

	return new Iterator<Node<K, V>>() {
	    /**
	     * Отслеживание версионности
	     */
	    int currentVersionOfCollection = SimpleHashMap.this.versionOfCollection;
	    /**
	     * Количество элементов, пройденных итератором
	     */
	    int countOfGotElements = 0;
	    /**
	     * Указатель на текущий индекс в таблице
	     */
	    int pointerOnIndexOfTable = 0;
	    /**
	     * Если ячейка в таблице является связным списком, то указывает на следующую ноду
	     */
	    Iterator<Node<K, V>> iteratorOfTheCurrentInnerLinkedList = Collections.emptyIterator();

	    /**
	     * hasNext, который кроме всего остального осуществляет сдвиги поинтеров.
	     * Сначала идёт проверка по таблице, как только обнаружена неNull строка смотрим -  итератор был проинициализирован ранее ?
	     * Если нет - мы встретили строку впервые, следовательно инициализируем итератор, иначе идём по нему дальше (уже в next)
	     */
	    @Override
	    public boolean hasNext() {
		if (currentVersionOfCollection != SimpleHashMap.this.versionOfCollection) {
		    throw new ConcurrentModificationException();
		}
		while ((table[pointerOnIndexOfTable] == null || !iteratorOfTheCurrentInnerLinkedList.hasNext()) && pointerOnIndexOfTable < table.length - 1) {
		    pointerOnIndexOfTable++;
		    iteratorOfTheCurrentInnerLinkedList = table[pointerOnIndexOfTable] == null ? Collections.emptyIterator() : table[pointerOnIndexOfTable].iterator();
		}
		return countOfGotElements < countOfElements;
	    }

	    @Override
	    public Node<K, V> next() {
		if (!hasNext()) {
		    throw new NoSuchElementException();
		}
		countOfGotElements++;
		return iteratorOfTheCurrentInnerLinkedList.next();
	    }
	};

    }

    /**
     * Реализация узла для односвязного списка
     *
     * @param <K> key type
     * @param <V> value type
     */
    public static class Node<K, V> implements Iterable<Node<K, V>> {
	/**
	 * Ключ
	 */
	K key;
	/**
	 * Значение
	 */
	V value;
	/**
	 * Хэш-код ключа
	 */
	int hash;
	/**
	 * Ссылка на следующий узел (реализация односвязного списка для SimpleMap)
	 */
	Node<K, V> next;

	/**
	 * Базовый конструктор
	 */
	Node(K key, int hash, V value, Node<K, V> next) {
	    this.key = key;
	    this.hash = hash;
	    this.value = value;
	    this.next = next;
	}

	/**
	 * Для итератора в большей степени
	 */
	Node(Node<K, V> next) {
	    this.next = next;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
		return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
		return false;
	    }
	    Node<?, ?> node = (Node<?, ?>) o;
	    return hash == node.hash
		    && Objects.equals(key, node.key);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(key, value, hash, next);
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 */
	@Override
	public Iterator<Node<K, V>> iterator() {

	    return new Iterator<Node<K, V>>() {
		/**
		 * Указатель на текущий выданный элемент
		 */
		Node<K, V> pointerCurrentElement = new Node<K, V>(Node.this);
		/**
		 * Указатель на элемент перед pointerCurrentElement, то есть на элемент перед элементом, который нужно удалить
		 */
		Node<K, V> pointerBeforeCurrentElement = new Node<>(pointerCurrentElement);
		/**
		 * Флаг, удаляли ли мы после next или нет. Не допускает повторные вызовы remove без вызова next
		 */
		boolean removed;

		@Override
		public boolean hasNext() {
		    return pointerCurrentElement.next != null;
		}

		@Override
		public Node<K, V> next() {
		    if (!hasNext()) {
			throw new NoSuchElementException();
		    }
		    pointerBeforeCurrentElement = pointerBeforeCurrentElement.next;
		    pointerCurrentElement = pointerCurrentElement.next;
		    removed = false;
		    return pointerCurrentElement;
		}

		/**
		 * Т.к. remove можно вызывать только после Next, проверяем его на Null, а после удаления зануляем.
		 * Сперва перенаправляем основную связь у поинтера некст
		 * Потом уже "ломаем" старую связь через pointerBeforeNext.next.next
		 *
		 * В первом else рассматривается ситуация подробно описанная в SimpleHashMap.delete() - случай, когда
		 * удаляется сама голова списка и больше в списке ничего нет
		 * Если голова не единственный элемент, тогда происходит переприсвоение key и value (if(hasNext()))
		 *
		 * Если вызвано до next или дважды после одного некс - выбросит IllegalStateException
		 */
		@Override
		public void remove() {
		    if (!removed) {
			if (pointerCurrentElement.equals(Node.this)) {
			    if (hasNext()) {
				Node.this.key = pointerCurrentElement.next.key;
				Node.this.value = pointerCurrentElement.next.value;
				Node.this.hash = pointerCurrentElement.next.hash;
				Node.this.next = pointerCurrentElement.next.next;
				pointerCurrentElement = new Node<K, V>(Node.this);
				pointerBeforeCurrentElement = new Node<K, V>(pointerCurrentElement);
			    } else {
				Node.this.value = null;
				Node.this.key = null;
			    }
			    removed = true;
			    return;
			}
			removed = true;
			Node<K, V> nextNodeForCurrentNode = hasNext() ? pointerCurrentElement.next : null;
			pointerBeforeCurrentElement.next.next = null;
			pointerBeforeCurrentElement.next = nextNodeForCurrentNode;
			pointerCurrentElement = pointerBeforeCurrentElement;
		    } else {
			throw new IllegalStateException();
		    }
		}
	    };
	}
    }
}