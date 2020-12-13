package advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Day13A {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("feed values:");

        String readFirstLine = bufferedReader.readLine();
        int currentTime = Integer.parseInt(readFirstLine.trim());

        ArrayList<Integer> buses = new ArrayList<>();
        String readLine;
        do {
            readLine = bufferedReader.readLine();
            if (readLine != null && !readLine.isEmpty()) {

                for (String bus : readLine.trim().split(",")) {
                    if (!bus.equals("x")) {
                        buses.add(Integer.parseInt(bus));
                    }
                }
            } else {
                break;
            }
        } while (true);

        bufferedReader.close();

        System.out.println("Got " + buses.size() + " buses");

        final Instant start = Instant.now();

        int closestBus = 0;
        double closestValue = 0;

        for (Integer bus : buses) {
            double value = (((double) currentTime) / ((double) bus)) - (currentTime / bus);
//            System.out.println("bus " + bus + " value " + value + " = " + (((double) currentTime) / ((double) bus)) + " - " + (currentTime / bus));
            if (closestValue < value) {
                closestBus = bus;
                closestValue = value;
            }
        }


        final Instant finish = Instant.now();
        final long timeElapsed = Duration.between(start, finish).toMillis();  //in millis

        System.out.println("closestBus " + closestBus + " wait " + (closestBus - (currentTime % closestBus)) + " yields " + (closestBus * (closestBus - (currentTime % closestBus))) + " in " + timeElapsed + "ms");
    }
}
