package task10;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AsyncAggregator {
    private final ExecutorService executor;

    public AsyncAggregator(ExecutorService executor) {
        this.executor = executor;
    }

    public List<CurrencyInfo> fetchAllDataAsync(List<String> ids) {
        List<CompletableFuture<CurrencyInfo>> futures = ids.stream()
                .map(this::fetchOne)
                .collect(Collectors.toList());

        CompletableFuture<Void> all = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        return all.thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(info -> info != null)
                        .collect(Collectors.toList()))
                .join();
    }

    private CompletableFuture<CurrencyInfo> fetchOne(String id) {
        String thread = Thread.currentThread().getName();
        System.out.printf("[%s] scheduling %s%n", thread, id);

        CompletableFuture<Double> rateFuture = CompletableFuture.supplyAsync(
                () -> DataSources.fetchRate(id), executor);

        CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(
                () -> DataSources.fetchName(id), executor);

        return rateFuture.thenCombineAsync(
                        nameFuture,
                        (rate, name) -> new CurrencyInfo(id, rate, name),
                        executor)
                .thenApplyAsync(info -> {
                    System.out.printf("[%s] built %s%n",
                            Thread.currentThread().getName(), info.getCode());
                    return info;
                }, executor)
                .handle((info, ex) -> {
                    if (ex != null) {
                        System.out.printf("[%s] FAILED %s: %s%n",
                                Thread.currentThread().getName(),
                                id, ex.getCause().getMessage());
                        return null;
                    }
                    return info;
                });
    }
}