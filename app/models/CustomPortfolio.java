package models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomPortfolio implements Portfolio {
    private List<Allocation> allocations;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomPortfolio() {
        this.allocations = new ArrayList<>();
    }

    public synchronized void addStock(Stock stock) {
        Allocation allocation = allocations.stream()
                .filter(a -> a.getSector().equals(stock.getSector()))
                .findFirst()
                .orElseGet(() -> {
                    Allocation a = new Allocation(stock.getSector());
                    this.allocations.add(a);
                    return a;
                });

        allocation.addAssetValue(stock.getValue());
        recalculateProportions();
    }

    private void recalculateProportions(){
        BigDecimal value = getValue();
        allocations.forEach(a -> {
            BigDecimal proportion = a.getAssetValue()
                                     .divide(value, BigDecimal.ROUND_HALF_UP);
            a.setProportion(proportion);
        });
    }

    public BigDecimal getValue(){
        return allocations.stream()
                .map(Allocation::getAssetValue)
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal(0));
    }

    @Override
    public void clear() {
        allocations.clear();
        recalculateProportions();
    }

    @Override
    public String toString() {
        String allocs = this.allocations.stream().map(Object::toString).collect(Collectors.joining(","));
        String value = getValue().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        String result = String.format("{\"value\": %s, \"allocations\":[%s]}", value, allocs);
        logger.info(result);

        return result;
    }
}
