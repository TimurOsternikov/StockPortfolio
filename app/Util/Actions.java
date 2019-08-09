package Util;

public enum Actions {
    LATEST_PRICE("/quote/latestPrice"),
    COMPANY("/company");

    String route;

    Actions(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }
}
