package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MinMaxFindInArr {
    public static void main(String[] args) {
        int[] array = new int[100_000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }

        int partSize = array.length / 4;

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Future<ArrayList<Integer>> future1 = executorService.submit(() -> findMinMaxInRange(array, 0, partSize));
        Future<ArrayList<Integer>> future2 = executorService.submit(() -> findMinMaxInRange(array, partSize, 2 * partSize));
        Future<ArrayList<Integer>> future3 = executorService.submit(() -> findMinMaxInRange(array, 2 * partSize, 3 * partSize));
        Future<ArrayList<Integer>> future4 = executorService.submit(() -> findMinMaxInRange(array, 3 * partSize, array.length));

        try {
            ArrayList<Integer> min1 = future1.get();
            ArrayList<Integer> min2 = future2.get();
            ArrayList<Integer> min3 = future3.get();
            ArrayList<Integer> min4 = future4.get();

            int min = Math.min(Math.min(min1.get(0), min2.get(0)), Math.min(min3.get(0), min4.get(0)));
            int max = Math.min(Math.min(min1.get(1), min2.get(1)), Math.min(min3.get(1), min4.get(1)));

            System.out.println("Минимальное число: " + min);
            System.out.println("Максимальное число: " + max);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static ArrayList<Integer> findMinMaxInRange(int[] array, int start, int end) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (array[i] < min) {
                min = array[i];
            }
            if (array[i] > max) {
                max = array[i];
            }
        }
        return new ArrayList<>(Arrays.asList(min, max));
    }
}
