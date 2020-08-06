package ru.job4j.lsp.food;

import java.util.Collection;

public abstract class FoodSupplier {
    /**
     * Из-за того, что чекстайл непобедим даже под саппресом, тут private
     * вместо protected
     */
    private Collection<Food> foodCollection;

    public FoodSupplier(Collection<Food> foodCollection) {
        this.foodCollection = foodCollection;
    }

    /**
     * Добавление еды в целом будет одинаковым, где надо - переопределим
     */
    public boolean addFood(Food food) {
        if (!isSuitableFood(food)) {
            return false;
        } else {
            foodCollection.add(food);
            return true;
        }
    }

    /**
     * Для удобства, ведь методы коллекций нам не подойдут
     * return добавилось всё/ не всё
     */
    public boolean addAll(Collection<Food> foodCollection) {
        boolean result = true;
        for (Food food : foodCollection) {
            result = addFood(food) && (result);
        }
        return result;
    }

    /**
     * Проверяет, можно ли добавить продукт согласно критериям
     */
    public abstract boolean isSuitableFood(Food food);

    public Collection<Food> getAllFood() {
        return foodCollection;
    }

    /**
     * Да, уже есть метод геттер getAllFood - но у него другая логика,
     * которую можно развить. Иными словами - разные задачи у методов
     */
    protected Collection<Food> getFoodCollection() {
        return foodCollection;
    }
}
