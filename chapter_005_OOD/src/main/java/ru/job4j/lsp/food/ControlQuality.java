package ru.job4j.lsp.food;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

public class ControlQuality {
    /**
     * логгер
     */
    public static final Logger LOG = LoggerFactory.getLogger(
            ControlQuality.class.getName());

    /**
     * Определяет качество еды (обратно свежести) как отношение разниц
     * fullTime и nowTime, где
     * fullTime - продолжительность между созданием и сроком годности
     * nowTime - продолжительность между созданием и текущим временем
     * Здесь качество определяется как 0 - идеально, 1 - ужас
     */
    public static double consumeQuality(Food food) {
        Duration fullTime = Duration.between(food.getCreateDate(),
                                             food.getExpiredDate());
        Duration nowTime = Duration.between(food.getCreateDate(),
                                            LocalDateTime.now());

        return BigDecimal.valueOf(nowTime.toDays() / (double) fullTime.toDays())
                         .setScale(2, RoundingMode.HALF_UP)
                         .doubleValue();
    }

    /**
     * Распределяет еду из коллекции по магазинам, УДАЛЯЯ те, что добавили из
     * этой коллекции. Может быть так, что коллекция не поддерживает
     * удаление, в таком случае в новую коллекцию будет добавлена запись, а
     * из старой удалена не будет - можно сделать как угодно, хоть в начале
     * метода проверять на возможность удалять коллекциями, другой вопрос -
     * как лучше
     *
     * @return вся ли еда была распределена
     */
    public static boolean splitFoodRemove(Collection<Food> foods,
                                          FoodSupplier... foodSuppliers) {
        boolean addResult;
        Iterator<Food> iterator = foods.iterator();
        while (iterator.hasNext()) {
            Food currentFood = iterator.next();
            for (FoodSupplier foodSupplier : foodSuppliers) {
                addResult = foodSupplier.addFood(currentFood);
                if (addResult) {
                    try {
                        iterator.remove();
                        break;
                    } catch (UnsupportedOperationException e) {
                        LOG.error("Неподдерживаемая операция удаления в {} у "
                                          + "итератора {}, "
                                          + "продукт {} не был удалён "
                                          + "корректно", foods.getClass(),
                                  iterator.getClass(), currentFood, e);
                    }
                }
            }
        }
        return foods.isEmpty();
    }

    /**
     * Распределяет еду между foodSupplier'ами, не изменяя изначальную коллекцию
     *
     * @return вся ли еда была распределена
     */
    public static boolean splitFoodSoft(Collection<Food> foods,
                                        FoodSupplier... foodSuppliers) {
        boolean result = true;
        Iterator<Food> iterator = foods.iterator();
        while (iterator.hasNext()) {
            Food currentFood = iterator.next();
            for (FoodSupplier foodSupplier : foodSuppliers) {
                result = foodSupplier.addFood(currentFood) && (result);
            }
        }
        return result;
    }
}
