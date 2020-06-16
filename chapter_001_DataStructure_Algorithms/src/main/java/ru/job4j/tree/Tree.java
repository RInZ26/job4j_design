package ru.job4j.tree;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Predicate;

class Tree<E> implements SimpleTree<E> {
    private final Node<E> root;

    Tree(final E root) {
        this.root = new Node<>(root);
    }

    public boolean isBinary() {
        return checkSomething(o -> o.children.size() > 2).isEmpty();
    }

    public Optional<Node<E>> checkSomething(Predicate<Node<E>> rule) {
        Queue<Node<E>> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);
        while (!nodeQueue.isEmpty()) {
            var element = nodeQueue.poll();
            if (rule.test(element)) {
                return Optional.of(element);
            }
            nodeQueue.addAll(element.children);
        }
        return Optional.empty();
    }

    @Override
    public boolean add(E parent, E child) {
        boolean rsl = false;
        var parentOptional = findBy(parent);
        var childOptional = findBy(child);
        if (parentOptional.isPresent() && childOptional.isEmpty()) {
            parentOptional.get().children.add(new Node<E>(child));
            rsl = true;
        }
        return rsl;
    }

    @Override
    public Optional<Node<E>> findBy(E value) {
        return checkSomething(o -> o.value.equals(value));
    }
}