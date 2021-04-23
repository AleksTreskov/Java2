package ru.geekbrains.domashnee_zadanie;

public class Main {
   private static final int size = 1000000;
   private static final int h = size / 2;

    public static void main(String[] args) {
        createAndStartThread1();
        createAndStartThread2();


    }

    public static void createAndStartThread1() {
        Thread thread1 = new Thread(() -> {
            float[] arr = new float[size];
            for (float arr2 : arr
            ) {
                arr2 = 1;

            }
            long a = System.currentTimeMillis();
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));

            }
            System.out.println(System.currentTimeMillis() - a + " - Thread1.");
        });
        thread1.start();
    }

    public static void createAndStartThread2() {
        Thread thread2 = new Thread(() -> {
            float[] arr = new float[size];

            for (float arr2 : arr
            ) {
                arr2 = 1;

            }
            long a = System.currentTimeMillis();
            float[] halfArr = new float[h];
            System.arraycopy(arr, 0, halfArr, 0, h);
            for (int i = 0; i < halfArr.length; i++) {
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                halfArr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
            System.arraycopy(halfArr, 0, arr, h, h);
            System.out.println(System.currentTimeMillis() - a + " - Thread2.");
        });
        thread2.start();
    }
}

