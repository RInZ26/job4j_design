package ru.job4j.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class TempClass {
    public static void main(String[] args) {
        ReferenceQueue<User> referenceQueue = new ReferenceQueue<>();
        User u = new User();
        PhantomReference<User> phantomReference = new PhantomReference<>(u,
                                                                         referenceQueue);
        System.out.println(phantomReference.get());
        do {
            var a = referenceQueue.poll();
            System.out.println(a);
            if (a != null) {
                System.out.println(a.get());
            }
        } while (u != null);
    }

    static class User {

    }
}

