package org.lazydevs.api.gog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.lazydevs.api.gog.model.DetailedGogGame;
import org.lazydevs.api.gog.model.GogGame;
import org.lazydevs.api.gog.model.GogUser;
import org.lazydevs.api.gog.parsing.GogAccountPage;
import org.lazydevs.api.gog.parsing.GogGameShelfDetailsPage;
import org.lazydevs.api.gog.util.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 02.02.13
 * Time: 17:37
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class GogApi {

    private static final String URL_LOGIN = "https://secure.gog.com/login";
    private static final String PARAM_USER = "log_email";
    private static final String PARAM_PASSWORD = "log_password";
    private static final String PARAM_UNLOCK_SETTINGS = "unlockSettings";
    private static final String PARAM_BUK = "buk";

    private static final String URL_USER_JSON = "http://www.gog.com/user/ajax/?a=get";

    private static final String URL_ACCOUNT = "https://secure.gog.com/account/games/shelf";

    private static final String URL_GAME_SHELF_DETAILS = "https://secure.gog.com/account/ajax?a=gamesShelfDetails&g=%s";

    private DefaultHttpClient client;
    private HttpContext context;
    private CookieStore cookies;

    private boolean loggedIn;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public GogApi() {

        this.client = new DefaultHttpClient();
        this.context = new BasicHttpContext();
        this.cookies = new BasicCookieStore();

        this.context.setAttribute(ClientContext.COOKIE_STORE, this.cookies);

        client.setRedirectStrategy(new LaxRedirectStrategy());

        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

        /* For capturing traffic with Fiddler
        HttpClientUtil.initHttpsNoCertNoHostVerification(client, 443);

        HttpHost proxy = new HttpHost("localhost", 8888);

        client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
         */
    }

    public void login(String username, String password) {

        // Retrieve 'buk' - this param needs to be in the login request to succeed
        GogUser user = loadUser();

        // Execute login request
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair(PARAM_USER, username));
        params.add(new BasicNameValuePair(PARAM_PASSWORD, password));
        params.add(new BasicNameValuePair(PARAM_UNLOCK_SETTINGS, "1"));
        params.add(new BasicNameValuePair(PARAM_BUK, user.getBuk()));

        HttpResponse response = HttpClientUtil.post(client, context, URL_LOGIN, params);

        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            throw new GogApiException("Error closing login request.", e);
        }

        // Load user again to check if we are logged in
        user = loadUser();

        loggedIn = username.equalsIgnoreCase(user.getEmail());
    }

    public GogUser loadUser() {

        String responseBody;
        GogUser user;

        try {
            responseBody = HttpClientUtil.getBody(client, context, URL_USER_JSON);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonUser = mapper.readTree(responseBody);

            user = new GogUser(jsonUser);

        } catch (IOException e) {
            throw new GogApiException("Error loading user JSON", e);
        }

        return user;
    }

    public List<GogGame> listGames() {

        // Load account page
        String responseBody = HttpClientUtil.getBody(client, context, URL_ACCOUNT);

        // Parse game list
        List<GogGame> games = new GogAccountPage(responseBody).listGames();

        return games;
    }

    public DetailedGogGame loadDetails(GogGame game) {

        // Load game shelf details
        String gameDetailUrl = String.format(URL_GAME_SHELF_DETAILS, game.getId());
        String responseBody = HttpClientUtil.getBody(client, context, gameDetailUrl);

        // Parse game details
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonGameDetails;

        try {
            jsonGameDetails = mapper.readTree(responseBody);
        } catch (IOException e) {
            throw new GogApiException("Error parsing game shelf details.", e);
        }

        String html = jsonGameDetails.get("details").get("html").getTextValue();
        DetailedGogGame details = new GogGameShelfDetailsPage(html).getDetails(game);

        return details;
    }


}
