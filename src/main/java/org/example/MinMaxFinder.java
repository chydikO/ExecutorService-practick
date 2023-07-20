package org.example;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Знайти найменше і найбільше число в масиві на 100_000 елементів заповнений випадковими числами.
 * Для прискорення пошуку "розділити" масив на 4 частини і виконувати пошук в 4-х окремих потоках.
 * Використовувати ExecutorService для виконання задачі в потоках.
 * Результат вивести на консоль.
 */

public class MinMaxFinder {
    public static void main(String[] args) {

        int[] array = new int[100_000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }

        int partSize = array.length / 4;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Callable<Integer> task1 = new MinMaxTask(array, 0, partSize);
        Callable<Integer> task2 = new MinMaxTask(array, partSize, 2 * partSize);
        Callable<Integer> task3 = new MinMaxTask(array, 2 * partSize, 3 * partSize);
        Callable<Integer> task4 = new MinMaxTask(array, 3 * partSize, array.length);

        Future<Integer> future1 = executorService.submit(task1);
        Future<Integer> future2 = executorService.submit(task2);
        Future<Integer> future3 = executorService.submit(task3);
        Future<Integer> future4 = executorService.submit(task4);

        try {
            // Получаем результаты выполнения задач
            int min1 = future1.get();
            int min2 = future2.get();
            int min3 = future3.get();
            int min4 = future4.get();

            // Находим минимальное число из частей массива
            int min = Math.min(Math.min(min1, min2), Math.min(min3, min4));

            // Находим максимальное число из всего массива
            int max = Arrays.stream(array).max().getAsInt();

            // Выводим результат на консоль
            System.out.println("Минимальное число: " + min);
            System.out.println("Максимальное число: " + max);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Завершаем работу ExecutorService
            executorService.shutdown();
        }
    }

    private static class MinMaxTask implements Callable<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        public MinMaxTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer call() throws Exception {
            int min = Integer.MAX_VALUE;
            for (int i = start; i < end; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
            }
            return min;
        }
    }
}
