package org.lazydevs.pixelwallet.api.gog;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sasch_000
 * Date: 02.02.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class GogApiTest {

    @Test
    public void testLoginAndList() throws Exception {

        GogApi api = new GogApi();

        api.login("your-username", "your-password");

        Assert.assertTrue(api.isLoggedIn());

        List<GogGame> games = api.listGames();

        Assert.assertTrue(games.size() > 0);
    }
}
