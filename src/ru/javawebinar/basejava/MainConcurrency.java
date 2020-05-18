package ru.javawebinar.basejava;

public class MainConcurrency {

    Object lock = new Object();

    public static void main(String[] args) {
        MainConcurrency deadlock = new MainConcurrency();
        for (int i = 0; i < 100; i++) {
            new Thread(deadlock::method1).start();
            new Thread(deadlock::method2).start();
        }
    }

    public void method1() {
        synchronized (lock) {
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " executing method1");
            }
        }
    }

    public void method2() {
        synchronized (this) {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + " executing method2");
            }
        }
    }
}
