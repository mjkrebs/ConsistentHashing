package org.consistentHashing;

import java.util.*;

public class VirtualNodeOptimizer {
    private static final int NUM_SERVERS = 10;
    private static final int NUM_KEYS = 1000000; // Number of data entries to hash
    private static final int NUM_ITERATIONS = 100; // Number of iterations for Monte Carlo simulation

    // Main function to find the optimal number of virtual nodes using Monte Carlo simulation
    public void findOptimalVirtualNodes(int minNodes, int maxNodes, int step) {
        double minStandardDeviation = Double.MAX_VALUE;
        int optimalNodes = minNodes;

        for (int virtualNodes = minNodes; virtualNodes <= maxNodes; virtualNodes += step) {
            double totalStandardDeviation = 0.0;

            // Monte Carlo simulation
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                double sd = ConsistentHashing.performTest(createServers(), virtualNodes, NUM_KEYS);
                totalStandardDeviation += sd;
            }

            double averageSD = totalStandardDeviation / NUM_ITERATIONS;
            System.out.printf("Virtual Nodes: %d, Average Standard Deviation: %.4f%n", virtualNodes, averageSD);

            if (averageSD < minStandardDeviation) {
                minStandardDeviation = averageSD;
                optimalNodes = virtualNodes;
            }
        }

        System.out.printf("Optimal Number of Virtual Nodes: %d with SD: %.4f%n", optimalNodes, minStandardDeviation);
    }

    private List<String> createServers() {
        List<String> serverList = new ArrayList<>();
        for(int i = 1; i < NUM_SERVERS; i++) {
            serverList.add("Server" + i);
        }
        return serverList;
    }

    public static void main(String[] args) {
        VirtualNodeOptimizer optimizer = new VirtualNodeOptimizer();
        optimizer.findOptimalVirtualNodes(100, 500, 20); // Test between 50 to 500 virtual nodes in steps of 10
    }
}