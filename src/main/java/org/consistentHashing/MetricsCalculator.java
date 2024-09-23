package org.consistentHashing;

import java.util.Map;

public class MetricsCalculator {

    /**
     * Method to calculate the population and sample standard deviation of the values assigned to servers.
     *
     * @param serverData A map containing server names as keys and their assigned data count as values
     * @return The population standard deviation of the values
     */
    public static double getPopulationStandardDeviation(Map<String, Integer> serverData) {
        // Step 1: Calculate the mean
        int totalServers = serverData.size();
        double sum = 0.0;

        for (int count : serverData.values()) {
            sum += count;
        }

        double mean = sum / totalServers;

        // Step 2: Calculate the variance
        double varianceSum = 0.0;
        for (int count : serverData.values()) {
            varianceSum += Math.pow(count - mean, 2);
        }

        double variance = varianceSum / totalServers; // Dividing by the total number of servers

        // Step 3: Calculate the standard deviation
        return Math.sqrt(variance);
    }

    /**
     * Method to calculate the sample standard deviation.
     *
     * @param serverData A map containing server names as keys and their assigned data count as values
     * @return The sample standard deviation of the values
     */
    public static double getSampleStandardDeviation(Map<String, Integer> serverData) {
        // Step 1: Calculate the mean
        int totalServers = serverData.size();
        double sum = 0.0;

        for (int count : serverData.values()) {
            sum += count;
        }

        double mean = sum / totalServers;

        // Step 2: Calculate the variance
        double varianceSum = 0.0;
        for (int count : serverData.values()) {
            varianceSum += Math.pow(count - mean, 2);
        }

        double variance = varianceSum / (totalServers - 1); // Dividing by totalServers - 1

        // Step 3: Calculate the standard deviation
        return Math.sqrt(variance);
    }

    // Test the getMetrics methods
    public static void main(String[] args) {
        Map<String, Integer> serverData = Map.of(
                "Server_1", 501,
                "Server_2", 1167,
                "Server_3", 834,
                "Server_4", 332,
                "Server_5", 200,
                "Server_6", 120,
                "Server_7", 190,
                "Server_8", 145,
                "Server_9", 300,
                "Server_10", 211
        );

        double populationSD = getPopulationStandardDeviation(serverData);
        double sampleSD = getSampleStandardDeviation(serverData);

        System.out.println("Population Standard Deviation of server data counts: " + populationSD);
        System.out.println("Sample Standard Deviation of server data counts: " + sampleSD);
    }
}