package models;

import java.math.BigDecimal;

public interface Portfolio {
    void addStock(Stock stock);
    BigDecimal getValue();
    void clear();
}
