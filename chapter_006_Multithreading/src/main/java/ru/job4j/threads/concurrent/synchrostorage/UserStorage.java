package ru.job4j.threads.concurrent.synchrostorage;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Потокобезопасный класс для работы с User
 */
@ThreadSafe
public class UserStorage {
    /**
     * Коллекция пользователей - обычная мапа, а не ConcurrentHashMap исходя
     * из того, что синхронизацию мы обеспечиваем методами, а не на уровне
     * коллекции (не до конца понятно, правильно ли это)
     */
    @GuardedBy("this")
    private Map<Integer, User> usersMap = new HashMap<>();

    public synchronized boolean add(User user) {
        return usersMap.putIfAbsent(user.getId(), user) == null;
    }

    /**
     * Логика апдейта должна работать ТОЛЬКО если пользователь уже есть в
     * системе
     */
    public synchronized boolean update(User user) {
        return usersMap.containsValue(user) && add(user);
    }

    public synchronized boolean delete(User user) {
        return usersMap.remove(user.getId()) != null;
    }

    /**
     * Перевод денег между счетами по id, но, при этом сами объекты User не
     * синронятся.
     */
    public synchronized void transfer(int sourceId, int destId, int amount)
            throws Exception {
        User source = usersMap.get(sourceId);
        User dest = usersMap.get(destId);
        Objects.requireNonNull(source);
        Objects.requireNonNull(dest);
        if (source.getAmount() < 0 && dest.getAmount() < 0) {
            throw new Exception("amount < 0");
        }
        if (!source.equals(dest) && source.getAmount() > amount) {
            updateAmounts(source, dest, amount);
        }
    }

    /**
     * Приватный метод апдейта денег в transfer. Считается, что все входные
     * параметры уже проверены в другом месте (например в @method transfer)
     * Делать его синхронным - нет смысла из-за приватности
     *
     * @param source источник
     * @param dest   назначение
     * @param amount количество
     */
    //Unsafe
    private void updateAmounts(User source, User dest, int amount) {
        dest.setAmount(dest.getAmount() + amount);
        source.setAmount(source.getAmount() - amount);
    }
}
