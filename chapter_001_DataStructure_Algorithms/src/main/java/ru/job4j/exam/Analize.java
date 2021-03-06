package ru.job4j.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Analize {

    public static Info diff(List<User> previous, List<User> current) {
        Info infoAboutCollections = new Info();
        Map<Integer, User> previousUserMap = new HashMap<Integer, User>();
        int unchanged = 0;
        for (User user : previous) {
            previousUserMap.put(user.id, user);
        }
        for (User currentUser : current) {
            Optional<User> testedUserFromPreviousMap = Optional.ofNullable(
                    previousUserMap.putIfAbsent(currentUser.id, currentUser));
            if (testedUserFromPreviousMap.isEmpty()) {
                infoAboutCollections.added++;
            } else if (!testedUserFromPreviousMap.get().name.equals(
                    currentUser.name)) {
                infoAboutCollections.changed++;
            } else {
                unchanged++;
            }
        }
        infoAboutCollections.deleted =
                previousUserMap.size() - infoAboutCollections.added
                        - infoAboutCollections.changed - unchanged;
        return infoAboutCollections;
    }

    public static class User {
        private int id;
        private String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class Info {
        private int added;
        private int changed;
        private int deleted;

        public int getAdded() {
            return added;
        }

        public int getChanged() {
            return changed;
        }

        public int getDeleted() {
            return deleted;
        }
    }
}
