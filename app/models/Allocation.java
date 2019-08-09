package models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

public class Allocation {
    private final String sector;
    private BigDecimal assetValue;
    private BigDecimal proportion;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Allocation(String sector) {
        this.sector = sector;
        this.assetValue = new BigDecimal(0);
        this.proportion = new BigDecimal(0);
    }

    public String getSector() {
        return sector;
    }

    public BigDecimal getAssetValue() {
        return assetValue;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void addAssetValue(BigDecimal newAssetValue) {
        this.assetValue = this.assetValue.add(newAssetValue).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    @Override
    public String toString() {
        return String.format("{\"sector\":\"%s\", \"assetValue\":%s, \"proportion\":%s}",
                sector,
                assetValue.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString(),
                proportion.setScale(3, BigDecimal.ROUND_HALF_UP).toPlainString());
    }
}
