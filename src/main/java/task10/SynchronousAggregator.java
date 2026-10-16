package task10;

import java.util.ArrayList;
import java.util.List;

public class SynchronousAggregator {
    public List<CurrencyInfo> fetchAllDataSync(List<String> ids) {
        List<CurrencyInfo> results = new ArrayList<>();
        for (String id : ids) {
            try {
                double rate = DataSources.fetchRate(id);
                String name = DataSources.fetchName(id);
                results.add(new CurrencyInfo(id, rate, name));
            } catch (Exception e) {
                System.out.printf("FAILED %s: %s%n", id, e.getMessage());
            }
        }
        return results;
    }
}
