package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Simple benchmark for the Red-Black Tree implementation.
 * 
 * @author Jacob Cobler
 */
public class TreeBenchmark {
    private static final int[] SIZES = { 1_000, 2_500, 5_000, 10_000 };
    private static final int WARMUP_ROUNDS = 4;
    private static final int MEASURED_ROUNDS = 9;
    private static final long SEED = 2331L;

    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);
        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark_results.csv"))) {
            writer.println("structure,distribution,n,insert_ms,search_ms,delete_ms,height_after_insert");

            for (String distribution : List.of("ascending", "random")) {
                for (int n : SIZES) {
                    runBenchmark(writer, distribution, n);
                }
            }
        }

        System.out.println("Benchmark complete. Results written to benchmark_results.csv");
    }

    private static void runBenchmark(PrintWriter writer, String distribution, int n) {
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            measureOnce(distribution, n, i, false);
        }

        double[] insertTimes = new double[MEASURED_ROUNDS];
        double[] searchTimes = new double[MEASURED_ROUNDS];
        double[] deleteTimes = new double[MEASURED_ROUNDS];
        int height = 0;

        for (int i = 0; i < MEASURED_ROUNDS; i++) {
            Result result = measureOnce(distribution, n, i, true);
            insertTimes[i] = result.insertMs;
            searchTimes[i] = result.searchMs;
            deleteTimes[i] = result.deleteMs;
            height += result.heightAfterInsert;
        }

        double insertMs = median(insertTimes);
        double searchMs = median(searchTimes);
        double deleteMs = median(deleteTimes);
        height /= MEASURED_ROUNDS;

        writer.printf("RedBlackTree,%s,%d,%.3f,%.3f,%.3f,%d%n",
                distribution, n, insertMs, searchMs, deleteMs, height);
        writer.flush();

        System.out.printf("%-16s %-10s n=%6d insert=%8.3f ms search=%8.3f ms delete=%8.3f ms height=%5d%n",
                "RedBlackTree", distribution, n, insertMs, searchMs, deleteMs, height);
    }

    private static Result measureOnce(String distribution, int n, int round, boolean measured) {
        List<Integer> insertKeys = keys(n, distribution, SEED + round);
        List<Integer> searchKeys = new ArrayList<>(insertKeys);
        Collections.shuffle(searchKeys, new Random(SEED * 3 + round));
        List<Integer> deleteKeys = new ArrayList<>(insertKeys);
        Collections.shuffle(deleteKeys, new Random(SEED * 5 + round));

        RedBlackTree<Integer, Integer> tree = new RedBlackTree<>();

        long startInsert = System.nanoTime();
        for (Integer key : insertKeys) {
            tree.insert(key, key);
        }
        long endInsert = System.nanoTime();

        int heightAfterInsert = tree.height();
        if (measured && !tree.validateRedBlackProperties()) {
            throw new IllegalStateException("RedBlackTree failed validation during benchmark");
        }

        long startSearch = System.nanoTime();
        for (Integer key : searchKeys) {
            if (tree.search(key) == null) {
                throw new IllegalStateException("missing key " + key);
            }
        }
        long endSearch = System.nanoTime();

        long startDelete = System.nanoTime();
        for (Integer key : deleteKeys) {
            tree.delete(key);
        }
        long endDelete = System.nanoTime();

        if (measured && !tree.isEmpty()) {
            throw new IllegalStateException("RedBlackTree should be empty after benchmark deletions");
        }

        return new Result(toMs(endInsert - startInsert), toMs(endSearch - startSearch),
                toMs(endDelete - startDelete), heightAfterInsert);
    }

    private static List<Integer> keys(int n, String distribution, long seed) {
        List<Integer> keys = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            keys.add(i);
        }
        if (distribution.equals("random")) {
            Collections.shuffle(keys, new Random(seed));
        }
        return keys;
    }

    private static double toMs(long nanos) {
        return nanos / 1_000_000.0;
    }

    private static double median(double[] values) {
        double[] copy = values.clone();
        java.util.Arrays.sort(copy);
        return copy[copy.length / 2];
    }

    private record Result(double insertMs, double searchMs, double deleteMs, int heightAfterInsert) {
    }
}
