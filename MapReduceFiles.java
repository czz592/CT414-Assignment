import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapReduceFiles {

    public static void main(String[] args) {
        Map<String, String> input = new HashMap<String, String>();
        int mapPoolSize = Integer.parseInt(args[0]);
        int reducePoolSize = Integer.parseInt(args[1]);
        // remove the first two arguments from the list
        String[] new_args = new String[args.length - 2];
        System.arraycopy(args, 2, new_args, 0, new_args.length);
        try {
            for (String file : new_args) {
                input.put(file, readFile(file));
            }
        } catch (IOException ex) {
            System.err.println("Error reading files...\n" + ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }

        // measure time for each approach
        long start = System.currentTimeMillis();
        bruteForce(input);
        System.out.println("Brute force: " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        mapReduce(input);
        System.out.println("MapReduce: " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        distributed_mapReduce(input);
        System.out.println("Distributed MapReduce: " + (System.currentTimeMillis() - start) + "ms");

        start = System.currentTimeMillis();
        thread_pool_mapReduce(input, mapPoolSize, reducePoolSize);
        System.out.println("Thread Pool MapReduce: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println(
                "Thread Pool MapReduce - Map Pool Size: " + mapPoolSize + " - Reduce Pool Size: " + reducePoolSize);
    }

    // APPROACH #1: Brute force
    private static void bruteForce(Map<String, String> input) {
        Map<String, Map<String, Integer>> output = new HashMap<String, Map<String, Integer>>();
        Iterator<Map.Entry<String, String>> inputIter = input.entrySet().iterator();
        while (inputIter.hasNext()) {
            Map.Entry<String, String> entry = inputIter.next();
            String file = entry.getKey();
            String contents = entry.getValue();

            String[] words = contents.trim().split("\\s+");

            for (String word : words) {
                Map<String, Integer> files = output.get(word);
                if (files == null) {
                    files = new HashMap<String, Integer>();
                    output.put(word, files);
                }
                Integer occurrences = files.remove(file);
                if (occurrences == null) {
                    files.put(file, 1);
                } else {
                    files.put(file, occurrences.intValue() + 1);
                }
            }
        }
    }

    // APPROACH #2: MapReduce
    private static void mapReduce(Map<String, String> input) {
        Map<String, Map<String, Integer>> output = new HashMap<String, Map<String, Integer>>();

        // mapping phase
        long start = System.currentTimeMillis();
        List<MappedItem> mappedItems = new LinkedList<MappedItem>();
        Iterator<Map.Entry<String, String>> inputIter = input.entrySet().iterator();
        while (inputIter.hasNext()) {
            Map.Entry<String, String> entry = inputIter.next();
            String file = entry.getKey();
            String contents = entry.getValue();

            map(file, contents, mappedItems);
        }
        System.out.println("MapRedue - Mapping phase: " + (System.currentTimeMillis() - start) + "ms");

        // grouping phase
        start = System.currentTimeMillis();
        Map<String, List<String>> groupedItems = new HashMap<String, List<String>>();
        Iterator<MappedItem> mappedIter = mappedItems.iterator();
        while (mappedIter.hasNext()) {
            MappedItem item = mappedIter.next();
            String word = item.getWord();
            String file = item.getFile();
            List<String> list = groupedItems.get(word);
            if (list == null) {
                list = new LinkedList<String>();
                groupedItems.put(word, list);
            }
            list.add(file);
        }
        System.out.println("MapRedue - Grouping phase: " + (System.currentTimeMillis() - start) + "ms");

        // reducing phase
        start = System.currentTimeMillis();
        Iterator<Map.Entry<String, List<String>>> groupedIter = groupedItems.entrySet().iterator();
        while (groupedIter.hasNext()) {
            Map.Entry<String, List<String>> entry = groupedIter.next();
            String word = entry.getKey();
            List<String> list = entry.getValue();

            reduce(word, list, output);
        }
        System.out.println("MapRedue - Reducing phase: " + (System.currentTimeMillis() - start) + "ms");
    }

    // APPROACH #3: Distributed MapReduce
    private static void distributed_mapReduce(Map<String, String> input) {
        final Map<String, Map<String, Integer>> output = new HashMap<String, Map<String, Integer>>();

        // mapping phase
        long start = System.currentTimeMillis();
        final List<MappedItem> mappedItems = new LinkedList<MappedItem>();
        final MapCallback<String, MappedItem> mapCallback = new MapCallback<String, MappedItem>() {
            @Override
            public synchronized void mapDone(String file, List<MappedItem> results) {
                mappedItems.addAll(results);
            }
        };

        List<Thread> mapCluster = new ArrayList<Thread>(input.size());

        Iterator<Map.Entry<String, String>> inputIter = input.entrySet().iterator();
        while (inputIter.hasNext()) {
            Map.Entry<String, String> entry = inputIter.next();
            final String file = entry.getKey();
            final String contents = entry.getValue();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    map(file, contents, mapCallback);
                }
            });
            mapCluster.add(t);
            t.start();
        }

        // wait for mapping phase to be over:
        for (Thread t : mapCluster) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Distributed MapRedue - Mapping phase: " + (System.currentTimeMillis() - start) + "ms");

        // grouping phase
        start = System.currentTimeMillis();
        Map<String, List<String>> groupedItems = new HashMap<String, List<String>>();
        Iterator<MappedItem> mappedIter = mappedItems.iterator();
        while (mappedIter.hasNext()) {
            MappedItem item = mappedIter.next();
            String word = item.getWord();
            String file = item.getFile();
            List<String> list = groupedItems.get(word);
            if (list == null) {
                list = new LinkedList<String>();
                groupedItems.put(word, list);
            }
            list.add(file);
        }
        System.out.println("Distributed MapRedue - Grouping phase: " + (System.currentTimeMillis() - start) + "ms");

        // reducing phase
        start = System.currentTimeMillis();
        final ReduceCallback<String, String, Integer> reduceCallback = new ReduceCallback<String, String, Integer>() {
            @Override
            public synchronized void reduceDone(String k, Map<String, Integer> v) {
                output.put(k, v);
            }
        };
        List<Thread> reduceCluster = new ArrayList<Thread>(groupedItems.size());
        Iterator<Map.Entry<String, List<String>>> groupedIter = groupedItems.entrySet().iterator();
        while (groupedIter.hasNext()) {
            Map.Entry<String, List<String>> entry = groupedIter.next();
            final String word = entry.getKey();
            final List<String> list = entry.getValue();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    reduce(word, list, reduceCallback);
                }
            });
            reduceCluster.add(t);
            t.start();
        }
        // wait for reducing phase to be over:
        for (Thread t : reduceCluster) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Distributed MapRedue - Reducing phase: " + (System.currentTimeMillis() - start) + "ms");
    }

    // APPROACH #4: Thread Pool MapReduce
    private static void thread_pool_mapReduce(Map<String, String> input, int mapPoolSize, int reducePoolSize) {
        final Map<String, Map<String, Integer>> output = new HashMap<String, Map<String, Integer>>();

        // mapping phase
        long start = System.currentTimeMillis();
        final List<MappedItem> mappedItems = new LinkedList<MappedItem>();
        final MapCallback<String, MappedItem> mapCallback = new MapCallback<String, MappedItem>() {
            @Override
            public synchronized void mapDone(String file, List<MappedItem> results) {
                mappedItems.addAll(results);
            }
        };

        // modified mapping phase to use thread pool
        // create thread pool of poolsize
        ExecutorService executor = Executors.newFixedThreadPool(mapPoolSize);
        Iterator<Map.Entry<String, String>> inputIter = input.entrySet().iterator();

        while (inputIter.hasNext()) {
            Map.Entry<String, String> entry = inputIter.next();
            final String file = entry.getKey();
            final String contents = entry.getValue();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    map(file, contents, mapCallback);
                }
            });
        }

        // shutdown the executor to finish the mapping phase
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Thread Pool MapRedue - Mapping phase: " + (System.currentTimeMillis() - start) + "ms");

        // grouping phase
        start = System.currentTimeMillis();
        Map<String, List<String>> groupedItems = new HashMap<String, List<String>>();
        Iterator<MappedItem> mappedIter = mappedItems.iterator();
        while (mappedIter.hasNext()) {
            MappedItem item = mappedIter.next();
            String word = item.getWord();
            String file = item.getFile();
            List<String> list = groupedItems.get(word);
            if (list == null) {
                list = new LinkedList<String>();
                groupedItems.put(word, list);
            }
            list.add(file);
        }
        System.out.println("Thread Pool MapRedue - Grouping phase: " + (System.currentTimeMillis() - start) + "ms");

        // reducing phase
        start = System.currentTimeMillis();
        final ReduceCallback<String, String, Integer> reduceCallback = new ReduceCallback<String, String, Integer>() {
            @Override
            public synchronized void reduceDone(String k, Map<String, Integer> v) {
                output.put(k, v);
            }
        };

        // modified reducing phase to use thread pool
        executor = Executors.newFixedThreadPool(reducePoolSize);
        Iterator<Map.Entry<String, List<String>>> groupedIter = groupedItems.entrySet().iterator();
        while (groupedIter.hasNext()) {
            Map.Entry<String, List<String>> entry = groupedIter.next();
            final String word = entry.getKey();
            final List<String> list = entry.getValue();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    reduce(word, list, reduceCallback);
                }
            });
        }

        // shutdown the executor to finish the reducing phase
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Thread Pool MapRedue - Reducing phase: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void map(String file, String contents, List<MappedItem> mappedItems) {
        String[] words = contents.trim().split("\\s+");
        for (String word : words) {
            mappedItems.add(new MappedItem(word, file));
        }
    }

    public static void reduce(String word, List<String> list, Map<String, Map<String, Integer>> output) {
        Map<String, Integer> reducedList = new HashMap<String, Integer>();
        for (String file : list) {
            Integer occurrences = reducedList.get(file);
            if (occurrences == null) {
                reducedList.put(file, 1);
            } else {
                reducedList.put(file, occurrences.intValue() + 1);
            }
        }
        output.put(word, reducedList);
    }

    public static interface MapCallback<E, V> {

        public void mapDone(E key, List<V> values);
    }

    public static void map(String file, String contents, MapCallback<String, MappedItem> callback) {
        String[] words = contents.trim().split("\\s+");
        List<MappedItem> results = new ArrayList<MappedItem>(words.length);
        for (String word : words) {
            results.add(new MappedItem(word, file));
        }
        callback.mapDone(file, results);
    }

    public static interface ReduceCallback<E, K, V> {

        public void reduceDone(E e, Map<K, V> results);
    }

    public static void reduce(String word, List<String> list, ReduceCallback<String, String, Integer> callback) {

        Map<String, Integer> reducedList = new HashMap<String, Integer>();
        for (String file : list) {
            Integer occurrences = reducedList.get(file);
            if (occurrences == null) {
                reducedList.put(file, 1);
            } else {
                reducedList.put(file, occurrences.intValue() + 1);
            }
        }
        callback.reduceDone(word, reducedList);
    }

    private static class MappedItem {

        private final String word;
        private final String file;

        public MappedItem(String word, String file) {
            this.word = word;
            this.file = file;
        }

        public String getWord() {
            return word;
        }

        public String getFile() {
            return file;
        }

        @Override
        public String toString() {
            return "[\"" + word + "\",\"" + file + "\"]";
        }
    }

    private static String readFile(String pathname) throws IOException {
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
        String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase();
                line = line.replaceAll("[^a-zA-Z\\s]", "");
                fileContents.append(line).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

}
