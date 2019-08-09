package models;

import java.math.BigDecimal;

public class Stock {
    private final String symbol;
    private final BigDecimal volume;
    private String sector;
    private BigDecimal price;


    public Stock(String symbol, BigDecimal volume, String sector, BigDecimal price) {
        this.symbol = symbol;
        this.volume = volume;
        this.sector = sector;
        this.price = price;
    }

    public Stock(RequestUnit unit) {
        this(unit.getSymbol(), unit.getVolume(), null, new BigDecimal(0));
    }


    public BigDecimal getValue() {
        return price.multiply(volume).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        if (this.sector == null) this.sector = sector;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
