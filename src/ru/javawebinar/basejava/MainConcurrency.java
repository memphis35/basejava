package ru.javawebinar.basejava;

public class MainConcurrency {

    Object lock1 = new Object();
    Object lock2 = new Object();

    public static void main(String[] args) {
        MainConcurrency deadlock = new MainConcurrency();
        for (int i = 0; i < 2; i++) {
            new Thread(deadlock::method1).start();
            new Thread(deadlock::method2).start();
        }
    }

    public void method1() {
        synchronized (lock1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " executing method1");
            }
        }
    }

    public void method2() {
        synchronized (lock2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " executing method2");
            }
        }
    }
}
