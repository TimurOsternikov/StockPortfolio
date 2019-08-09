import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import static play.test.Helpers.*;

public class FunctionalPortfolioTest extends Assert {

    @org.junit.Test
    public void portfolioTest() {
        String json = "{ \"stocks\":[ { \"symbol\":\"AAPL\", \"volume\":50 }, { \"symbol\":\"HOG\", \"volume\":10 }, " +
                "{ \"symbol\":\"MDSO\", \"volume\":1 }, { \"symbol\":\"IDRA\", \"volume\":1 }, { \"symbol\":\"MRSN\", \"volume\":1 } ] }";
        JsonNode body = Json.parse(json);

        Http.RequestBuilder req = Helpers.fakeRequest(POST, "/portfolio").bodyJson(body);
        Result result = route(fakeApplication(), req);
        String res = contentAsString(result);

        assertEquals(200, result.status());
        assertThat(res, CoreMatchers.containsString("\"allocations\":[{"));
        assertThat(res, CoreMatchers.containsString("\"Health Technology\""));
        assertThat(res, CoreMatchers.containsString("\"value\""));
    }

    @org.junit.Test
    public void portfolioWrongStockSymbolTest() {
        String json = "{ \"stocks\":[ { \"symbol\":\"KFDSFSD123F\", \"volume\":50 }, { \"symbol\":\"HOG\", \"volume\":10 }]}";
        JsonNode body = Json.parse(json);

        Http.RequestBuilder req = Helpers.fakeRequest(POST, "/portfolio").bodyJson(body);
        Result result = route(fakeApplication(), req);
        String res = contentAsString(result);

        assertEquals(400, result.status());
        assertThat(res, CoreMatchers.containsString("Error occured"));
    }

    @org.junit.Test
    public void portfolioWrongRequest1Test() {
        String text = "sdfsdfsdf";
        Http.RequestBuilder req = Helpers.fakeRequest(POST, "/portfolio").bodyText(text);
        Result result = route(fakeApplication(), req);
        String res = contentAsString(result);

        assertEquals(400, result.status());
        assertThat(res, CoreMatchers.containsString("Wrong request body"));
    }


    @org.junit.Test
    public void portfolioWrongRequest2Test() {
        String text = "{ \"stocks\":[{ \"asd\":\"asd\"}] }";
        JsonNode body = Json.parse(text);

        Http.RequestBuilder req = Helpers.fakeRequest(POST, "/portfolio").bodyJson(body);
        Result result = route(fakeApplication(), req);
        String res = contentAsString(result);

        assertEquals(400, result.status());
        assertThat(res, CoreMatchers.containsString("IOException has been thrown"));
    }


    @org.junit.Test
    public void portfolioWrongRequest3Test() {
        String text = "{ \"stocks\": \"asdasd\"}";
        JsonNode body = Json.parse(text);

        Http.RequestBuilder req = Helpers.fakeRequest(POST, "/portfolio").bodyJson(body);
        Result result = route(fakeApplication(), req);
        String res = contentAsString(result);

        assertEquals(400, result.status());
        assertThat(res, CoreMatchers.containsString("IOException has been thrown"));
    }
}
