package Util;

import play.libs.ws.WSResponse;
import java.math.BigDecimal;

public class Response {

    private final WSResponse wsResponse;

    public Response(WSResponse wsResponse) {
        this.wsResponse = wsResponse;
    }

    public WSResponse getWsResponse() {
        return wsResponse;
    }

    public BigDecimal getPrice() {
        String price = this.wsResponse.getBody();
        try {
            return new BigDecimal(price);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(String.format("Wrong price request, response body: '%s'", price));
        }
    }

    public String getSector() {
        String sector = this.wsResponse.asJson().path("sector").asText();
        if (sector.isEmpty()) throw new IllegalArgumentException("Response has no information about stock sector");
        return sector;
    }
}
