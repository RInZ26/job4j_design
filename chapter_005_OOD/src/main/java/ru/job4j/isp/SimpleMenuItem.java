package ru.job4j.isp;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Подобие Node по идее (есть дети, есть родитель)
 */
public class SimpleMenuItem implements MenuItem<SimpleMenuItem> {
    public static final SimpleMenuItem EMPTY_ROOT = new SimpleMenuItem(
            "EMPTY_ROOT");
    private String name;
    private Collection<SimpleMenuItem> children;
    private SimpleMenuItem parent;

    public SimpleMenuItem() {
        this.children = new HashSet<>();
        parent = EMPTY_ROOT;
    }

    public SimpleMenuItem(String name) {
        this();
        this.name = name;
    }

    @Override
    public SimpleMenuItem getParent() {
        return parent;
    }

    @Override
    public Collection<SimpleMenuItem> getChildren() {
        return children;
    }

    @Override
    public boolean isRoot() {
        return !Objects.isNull(parent) && parent.equals(EMPTY_ROOT);
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void addChild(List<SimpleMenuItem> children) {
        children.forEach((child) -> child.setParent(this));
        children.addAll(children);
    }

    @Override
    public void addChild(SimpleMenuItem child) {
        child.setParent(this);
        children.add(child);
    }

    @Override
    public void setParent(SimpleMenuItem parent) {
        this.parent = parent;
    }

    @Override
    public void setChildren(Collection<SimpleMenuItem> collection) {
        this.children = collection;
    }

    @Override
    public void removeChild(SimpleMenuItem child) {
        children.remove(child);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void runAction() {
        //todo
    }

    @Override
    public void display() {
        var builder = new StringBuilder();
        int deep = getDeep();
        for (int c = 0; c < deep; c++) {
            builder.append("***");
        }
        System.out.println(builder.append(" ")
                                  .append(name));
    }

    /**
     * Определение глубины элемента
     *
     * @return
     */
    @Override
    public int getDeep() {
        int deep = 0;
        SimpleMenuItem highest = this;
        while (!highest.isRoot()) {
            deep++;
            highest = highest.parent;
        }
        return deep;
    }
}
