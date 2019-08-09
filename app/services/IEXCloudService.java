package services;

import Util.Actions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class IEXCloudService {

    private Config cfg = ConfigFactory.load();

    public String getUrl(String symbol, Actions action) {
        return getHost().concat(symbol).concat(action.getRoute());
    }

    public String getHost() {
        return cfg.getString("IEXCloud.url");
    }

    public String getToken() {
        return cfg.getString("IEXCloud.token");
    }
}
