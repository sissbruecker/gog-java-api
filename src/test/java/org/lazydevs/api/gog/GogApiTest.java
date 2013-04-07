package org.lazydevs.api.gog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lazydevs.api.gog.model.DetailedGogGame;
import org.lazydevs.api.gog.model.GogDownload;
import org.lazydevs.api.gog.model.GogGame;
import org.lazydevs.api.gog.model.GogGameBonus;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 02.02.13
 * Time: 17:57
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class GogApiTest {

    private GogApi api;

    @Before
    public void setUp() throws Exception {

        api = new GogApi();

        api.login(System.getProperty("gog-user"), System.getProperty("gog-password"));
    }

    @Test
    public void testLogin() throws Exception {

        Assert.assertTrue(api.isLoggedIn());
    }

    @Test
    public void testListGames() throws Exception {

        List<GogGame> games = api.listGames();

        Assert.assertTrue(games.size() > 0);
    }

    @Test
    public void testLoadDetails() throws Exception {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);

        Assert.assertNotNull(details);
    }

    @Test
    public void testTitle() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);

        Assert.assertEquals("Divinity 2: Developer's Cut", details.getTitle());
    }

    @Test
    public void testStoreLink() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);

        Assert.assertEquals("http://www.gog.com/gamecard/divinity_2_developers_cut", details.getStoreUrl());
    }

    @Test
    public void testForumLink() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);

        Assert.assertEquals("http://www.gog.com/forum/divine_divinity_series", details.getForumUrl());
    }

    @Test
    public void testSupportLink() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);

        Assert.assertEquals("http://www.gog.com/support/divinity_2_developers_cut", details.getSupportUrl());
    }

    @Test
    public void testDownloaderDownloads() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);
        Assert.assertEquals(5, details.getDownloaderDownloads().size());
        Assert.assertEquals("Windows installer, English", details.getDownloaderDownloads().get(0).getTitle());
        Assert.assertEquals("en", details.getDownloaderDownloads().get(0).getLanguage());
        Assert.assertEquals("gogdownloader://divinity_2_developers_cut/installer_win_en", details.getDownloaderDownloads().get(0).getUrl());
        Assert.assertEquals("5.9 GB", details.getDownloaderDownloads().get(0).getSize());
        Assert.assertEquals("1.1.0", details.getDownloaderDownloads().get(0).getVersion());
        Assert.assertEquals(GogDownload.DownloadOS.Win, details.getDownloaderDownloads().get(0).getOs());
    }

    @Test
    public void testFileDownloads() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);
        Assert.assertEquals(21, details.getFileDownloads().size());
        Assert.assertEquals("Windows installer, English (part 1 of 5)", details.getFileDownloads().get(0).getTitle());
        Assert.assertEquals("en", details.getFileDownloads().get(0).getLanguage());
        Assert.assertEquals("https://secure.gog.com/downlink/divinity_2_developers_cut/en1installer0", details.getFileDownloads().get(0).getUrl());
        Assert.assertEquals("2 MB", details.getFileDownloads().get(0).getSize());
        Assert.assertEquals("1.1.0", details.getFileDownloads().get(0).getVersion());
        Assert.assertEquals(GogDownload.DownloadOS.Win, details.getFileDownloads().get(0).getOs());
    }

    @Test
    public void testBonuses() {

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);
        Assert.assertEquals(13, details.getBonuses().size());

        GogGameBonus bonus;

        bonus = details.getBonusById("5002");
        Assert.assertNotNull(bonus);
        Assert.assertEquals("manual", bonus.getTitle());
        Assert.assertEquals(GogGameBonus.BonusType.Manual, bonus.getType());
        Assert.assertEquals("gogdownloader://divinity_2_developers_cut/5002", bonus.getDownloaderUrl());
        Assert.assertEquals("https://secure.gog.com/downlink/file/divinity_2_developers_cut/5002", bonus.getFileUrl());
        Assert.assertEquals("47 MB", bonus.getSize());
    }

}
