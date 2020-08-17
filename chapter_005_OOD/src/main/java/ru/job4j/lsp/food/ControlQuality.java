package ru.job4j.lsp.food;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ControlQuality {
    /**
     * логгер
     */
    public static final Logger LOG = LoggerFactory.getLogger(
            ControlQuality.class.getName());
    /**
     * Список доступных нам foodsupplier'ов
     */
    private List<FoodSupplier> shopList = new ArrayList<>();

    public ControlQuality(List<FoodSupplier> shopList) {
        this.shopList = shopList;
    }

    public ControlQuality() {
    }

    /**
     * Раз есть лист саплаеров, то, очевидно, нужны и методы изменения коллекции
     *
     * @param foodSupplier
     */
    public void addStore(FoodSupplier foodSupplier) {
        shopList.add(foodSupplier);
    }

    public void removeStore(FoodSupplier foodSupplier) {
        shopList.remove(foodSupplier);
    }

    /**
     * Определяет качество еды (обратно свежести) как отношение разниц
     * fullTime и nowTime, где
     * fullTime - продолжительность между созданием и сроком годности
     * nowTime - продолжительность между созданием и текущим временем
     * Здесь качество определяется как 0 - идеально, 1 - ужас
     */
    public double consumeQuality(Food food) {
        Duration fullTime = Duration.between(food.getCreateDate(),
                                             food.getExpiredDate());
        Duration nowTime = Duration.between(food.getCreateDate(),
                                            LocalDateTime.now());

        return BigDecimal.valueOf(nowTime.toDays() / (double) fullTime.toDays())
                         .setScale(2, RoundingMode.HALF_UP)
                         .doubleValue();
    }

    /**
     * Распределяет еду между foodSupplier'ами, не изменяя изначальную коллекцию
     *
     * @return вся ли еда была распределена
     */
    public boolean splitFoodSoft(Collection<Food> foods) {
        boolean result = true;
        Iterator<Food> iterator = foods.iterator();
        while (iterator.hasNext()) {
            Food currentFood = iterator.next();
            for (FoodSupplier foodSupplier : shopList) {
                result = foodSupplier.addFood(currentFood) && (result);
            }
        }
        return result;
    }

    /**
     * Динамическое распределение еды по текущим foodSupplier'ам
     */
    public void resort() {
        var allFood = shopList.stream()
                              .flatMap(s -> s.getAllFood()
                                             .stream())
                              .collect(Collectors.toList());
        shopList.forEach(e -> e.getFoodCollection()
                               .clear());
        splitFoodSoft(allFood);
    }
}
