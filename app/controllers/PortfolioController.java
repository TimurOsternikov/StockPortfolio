package controllers;

import javax.inject.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.CustomPortfolio;
import models.Portfolio;
import models.RequestUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.FormFactory;
import play.mvc.*;
import services.PortfolioService;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.List;

public class PortfolioController extends Controller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private FormFactory formFactory;
    private PortfolioService portfolioService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public PortfolioController(FormFactory formFactory, PortfolioService portfolioService) {
        this.formFactory = formFactory;
        this.portfolioService = portfolioService;
    }

    public CompletionStage<Result> getValue(Http.Request request) {
        return CompletableFuture.supplyAsync(() -> getRequestUnitList(request))
                .thenCompose(list -> getPortfolio(list))
                .handle((portfolio, err) -> {
                    if (portfolio != null) return ok(portfolio.toString());
                    else {
                        logger.error(err.getLocalizedMessage());
                        return badRequest(err.getLocalizedMessage());
                    }
                });
    }

    private List<RequestUnit> getRequestUnitList(Http.Request request) {
        logger.info("Request recieved, body: " + formFactory.form().bindFromRequest(request).rawData());

        JsonNode body = request.body().asJson();
        if (body == null) throw new IllegalArgumentException("Wrong request body");

        String stocks = body.get("stocks").toString();
        try {
            return mapper.readValue(stocks, new TypeReference<List<RequestUnit>>() {});
        } catch (IOException e) {
            String msg = String.format("IOException has been thrown during json deserializing. Cause-param: %s. Msg: %s",
                    stocks,
                    e.getLocalizedMessage());
            throw new RuntimeException(msg, e);
        }
    }

    private CompletionStage<Portfolio> getPortfolio(List<RequestUnit> list) {
        Portfolio portfolio = new CustomPortfolio();
        return portfolioService.evaluatePortfolio(portfolio, list);
    }
}
