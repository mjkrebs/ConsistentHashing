package org.consistentHashing;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsistentHashing {
    // TreeMap to represent the hash ring
    private final TreeMap<Integer, String> hashRing = new TreeMap<>();
    private final int numberOfVirtualNodes;
    private final List<String> servers = new ArrayList<>();

    private final HashGenerator hashGenerator;

    private static ConcurrentHashMap<String, Integer> metrics;

    public ConsistentHashing(List<String> serverList, int virtualNodeCount) {
        this.numberOfVirtualNodes = virtualNodeCount;
        hashGenerator = new HashGenerator();
        metrics = new ConcurrentHashMap<>();

        // Add each server and its virtual nodes to the hash ring
        for (String server : serverList) {
            addServer(server);
        }
    }

    private void addServer(String server) {
        servers.add(server);
        // Create virtual nodes for the server
        for (int i = 0; i < numberOfVirtualNodes; i++) {
            int hash = hashGenerator.createHash(server + "#VN" + i);
            hashRing.put(hash, server);
        }
    }

    public void storeData(String data) {
        int hash = hashGenerator.createHash(data);
        // Find the nearest server node clockwise
        Map.Entry<Integer, String> entry = hashRing.ceilingEntry(hash);
        if (entry == null) {
            entry = hashRing.firstEntry();
        }
        final String assignedServer = entry.getValue();
        metrics.put(assignedServer, metrics.getOrDefault(assignedServer, 0) + 1);
    }

    public static void main(String[] args) {
        List<String> serverList = Arrays.asList("Server1", "Server2", "Server3", "Server 4",
                "Server5", "Server6", "Server7", "Server");
        double minSD = 10000000.0;
        int optimalVN = -1;
        int n = 9999999;

        for(int virtualNodes = 200; virtualNodes < 400; virtualNodes+= 25) {
            double curr = performTest(serverList, virtualNodes, n);
            System.out.println("VN: " + virtualNodes + " SD: " + curr);
            if(curr < minSD) {
                optimalVN = virtualNodes;
                minSD = curr;
            }
            metrics.clear();
        }

        System.out.println("VN: " + optimalVN + " SD: " + minSD);

    }

    public static double performTest(List<String> serverList, int numberOfVirtualNodes, int n) {
        ConsistentHashing consistentHashing = new ConsistentHashing(serverList, numberOfVirtualNodes);

        for (int i = 1; i < n; i++) {
            String data = String.valueOf(i);
            consistentHashing.storeData(data);
        }
        return MetricsCalculator.getSampleStandardDeviation(metrics);
    }

}