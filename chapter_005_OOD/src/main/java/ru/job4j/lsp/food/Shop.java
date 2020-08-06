package ru.job4j.lsp.food;

import java.util.LinkedList;

public class Shop extends FoodSupplier {
    /**
     * В Shop есть верхняя граница качества(тухлости), после которой нужно
     * делать скидку
     */
    private double qualityHighBound = 0.75d;
    /**
     * Слишком свежий товар можно и не в магазин
     */
    private double qualityLowBound = 0.25d;
    /**
     * Критичная граница, выше уже нельзя
     */
    private double qualityCritical = 1d;

    /**
     * Коллекцтя продуктов в виде LinkedList, потому что мы будем активно и
     * часто удалять из разных мест
     */
    public Shop() {
        super(new LinkedList<>());
    }

    /**
     * Добавление еды с учетом качества текущего наследника FoodSupplier'a
     * Так как у Shop несколько критериев по проходимости, нам нужно
     * оперировать в зависимости от quality, а не только boolean подходит/ не
     * подходит
     *
     * @param food что добавляем
     *
     * @return ? Добавили : не добавили
     */
    @Override
    public boolean addFood(Food food) {
        double quality = ControlQuality.consumeQuality(food);
        if (!isSuitableQuality(quality)) {
            return false;
        }
        if (quality >= qualityCritical || quality <= qualityLowBound) {
            return false;
        }
        if (quality >= qualityHighBound) {
            food.addDiscount(0.2);
        }
        getFoodCollection().add(food);
        return true;
    }

    @Override
    public boolean isSuitableFood(Food food) {
        double quality = ControlQuality.consumeQuality(food);
        return quality < qualityCritical || quality > qualityLowBound;
    }

    /**
     * Чтобы избежать повторого расчёта quality в addFood (при обычной проверке
     * через isSuitableFood) нужен метод, который будет проверять уже кем-то
     * подсчитанное качество, а результат такой же как и в isSuitableFood
     */
    private boolean isSuitableQuality(double quality) {
        return quality < qualityCritical || quality > qualityLowBound;
    }
}
