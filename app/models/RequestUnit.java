package models;

import java.math.BigDecimal;

public class RequestUnit {

    private String symbol;
    private BigDecimal volume;

    public RequestUnit(String symbol, BigDecimal volume) {
        this.symbol = symbol;
        this.volume = volume;
    }
    public RequestUnit() {
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = new BigDecimal(volume);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
