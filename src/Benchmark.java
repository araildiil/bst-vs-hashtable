import java.util.*;

public class Benchmark {
    private static final int[] SIZES = {100, 1_000, 10_000};
    private static final int RUNS = 30;
    private static final int WARMUP = 10;

    public static void main(String[] args) {
        System.out.println("| Dataset | N | Structure | Insert (us) | Search (us) | Delete (us) | BST height |");
        System.out.println("|---------|---|-----------|-------------|-------------|-------------|------------|");
        for (String mode : new String[]{"random", "sorted"}) {
            for (int n : SIZES) {
                int[] keys = generateKeys(n, mode);
                int[] shuffledForDelete = keys.clone();
                shuffle(shuffledForDelete, new Random(7));
                runCase(mode, n, keys, shuffledForDelete);
            }
        }
    }

    private static void runCase(String mode, int n, int[] keys, int[] delOrder) {
        double[] bst = new double[3], ht = new double[3];
        int height = 0;

        for (int r = 0; r < WARMUP + RUNS; r++) {
            BST t = new BST();
            HashTable h = new HashTable();
            double[] tb = new double[3], th = new double[3];

            long s = System.nanoTime();
            for (int k : keys) t.insert(k);
            tb[0] = System.nanoTime() - s;
            s = System.nanoTime();
            for (int k : keys) h.insert(k);
            th[0] = System.nanoTime() - s;

            if (r == 0) height = t.height();

            s = System.nanoTime();
            for (int k : keys) t.search(k);
            tb[1] = System.nanoTime() - s;
            s = System.nanoTime();
            for (int k : keys) h.search(k);
            th[1] = System.nanoTime() - s;

            s = System.nanoTime();
            for (int k : delOrder) t.delete(k);
            tb[2] = System.nanoTime() - s;
            s = System.nanoTime();
            for (int k : delOrder) h.delete(k);
            th[2] = System.nanoTime() - s;

            if (r >= WARMUP) {
                for (int i = 0; i < 3; i++) { bst[i] += tb[i]; ht[i] += th[i]; }
            }
        }
        for (int i = 0; i < 3; i++) { bst[i] /= RUNS * 1000.0; ht[i] /= RUNS * 1000.0; }

        System.out.printf("| %s | %d | BST | %.1f | %.1f | %.1f | %d |%n", mode, n, bst[0], bst[1], bst[2], height);
        System.out.printf("| %s | %d | Hash Table | %.1f | %.1f | %.1f | - |%n", mode, n, ht[0], ht[1], ht[2]);
    }

    private static int[] generateKeys(int n, String mode) {
        int[] a = new int[n];
        if (mode.equals("sorted")) {
            for (int i = 0; i < n; i++) a[i] = i;
        } else {
            Set<Integer> used = new HashSet<>();
            Random rnd = new Random(42);
            int i = 0;
            while (i < n) {
                int v = rnd.nextInt(n * 10);
                if (used.add(v)) a[i++] = v;
            }
        }
        return a;
    }

    private static void shuffle(int[] a, Random rnd) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }
}