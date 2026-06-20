public class BankStatementBatchProcessor {

    // FIX: change to AtomicInteger to avoid race conditions
    private java.util.concurrent.atomic.AtomicInteger processedCount = new java.util.concurrent.atomic.AtomicInteger(0);

    public void process(java.util.List<StatementRecord> records) {
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(10);

        for (StatementRecord record : records) {
            executor.submit(() -> {
                processRecord(record);
                // FIX: use atomic increment so multiple threads don't drop counts
                processedCount.incrementAndGet();   // <-- inconsistent in production
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, java.util.concurrent.TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getProcessedCount() {
        // FIX: return the integer value from the atomic counter
        return processedCount.get();
    }
}
