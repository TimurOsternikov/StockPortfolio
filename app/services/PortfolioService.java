package services;

import Util.Actions;
import Util.Response;
import models.Portfolio;
import models.RequestUnit;
import models.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import javax.inject.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PortfolioService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final WSClient ws;
    private final IEXCloudService IEXCloudService;

    @Inject
    public PortfolioService(WSClient ws, services.IEXCloudService IEXCloudService) {
        this.ws = ws;
        this.IEXCloudService = IEXCloudService;
    }

    public CompletableFuture<Portfolio> evaluatePortfolio(Portfolio portfolio, List<RequestUnit> list) {
        List<CompletableFuture> cfList = new ArrayList<>();

        list.forEach(requestUnit -> {
            CompletableFuture future = executeRequest(Actions.LATEST_PRICE, requestUnit).thenCombine(
                    executeRequest(Actions.COMPANY, requestUnit),
                    (resp1, resp2) -> {
                        try {
                            Stock stock = new Stock(requestUnit);

                            BigDecimal price = resp1.getPrice();
                            logger.info(String.format("Price for Stock '%s' has been recieved - %s", stock.getSymbol(), price));
                            stock.setPrice(price);

                            String sector = resp2.getSector();
                            logger.info(String.format("Sector for Stock '%s' has been recieved - %s", stock.getSymbol(), sector));
                            stock.setSector(sector);

                            return stock;

                        } catch (IllegalArgumentException e) {
                            String msg = String.format("Error occured during request (Symbol: '%s'). Error msg: %s",
                                    requestUnit.getSymbol(), e.getLocalizedMessage());
                            throw new IllegalArgumentException(msg);
                        }
                    })
                    .thenAccept(portfolio::addStock)
                    .toCompletableFuture();

            cfList.add(future);
        });
        return CompletableFuture
                .allOf(cfList.toArray(new CompletableFuture[cfList.size()]))
                .thenApply(dummy -> portfolio);
    }

    private CompletionStage<Response> executeRequest(Actions action, RequestUnit requestUnit) {
        String url = IEXCloudService.getUrl(requestUnit.getSymbol(), action).concat("?token=").concat(IEXCloudService.getToken());
        WSRequest req = ws.url(url);
        return req.get().thenApply(Response::new);
    }
}
