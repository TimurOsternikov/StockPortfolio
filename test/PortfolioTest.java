import models.CustomPortfolio;
import models.Portfolio;
import models.Stock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PortfolioTest extends Assert {

    private Stock createStock(String symbol, long volume, double price, String sector) {
        return new Stock(symbol, new BigDecimal(volume), sector, new BigDecimal(price));
    }

    @Test
    public void addStockTest() throws InterruptedException {
        Portfolio portfolio = new CustomPortfolio();
        Stock stock1 = createStock("AAPL", 10, 1, "Technology Services");
        Stock stock2 = createStock("HOG", 20, 2, "Technology Services");
        Stock stock3 = createStock("IDRA", 1, 3, "Health Technology");

        ExecutorService service = Executors.newFixedThreadPool(3);

        for(int i= 0; i < 100; i++) {
            service.submit(() -> portfolio.addStock(stock1));
            service.submit(() -> portfolio.addStock(stock2));
            service.submit(() -> portfolio.addStock(stock3));
            service.awaitTermination(10, TimeUnit.MILLISECONDS);
            assertEquals(53, portfolio.getValue().intValue());
            portfolio.clear();
        }

    }
}
