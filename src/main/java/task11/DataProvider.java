package task11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataProvider {
    private static final Random RANDOM = new Random();
    public static List<String> generateAggregatedItems(int count) {
        List<String> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            if (RANDOM.nextInt(100) < 5) {
                result.add(null);
            } else {
                result.add("AGG-" + i);
            }
        }
        return result;
    }
}
