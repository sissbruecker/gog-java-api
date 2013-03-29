package org.lazydevs.api.gog;

import org.junit.Assert;
import org.junit.Test;
import org.lazydevs.api.gog.model.DetailedGogGame;
import org.lazydevs.api.gog.model.GogDownload;
import org.lazydevs.api.gog.model.GogGame;

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

    @Test
    public void testLogin() throws Exception {

        GogApi api = new GogApi();

        api.login(System.getProperty("gog-user"), System.getProperty("gog-password"));

        Assert.assertTrue(api.isLoggedIn());
    }

    @Test
    public void testListGames() throws Exception {

        GogApi api = new GogApi();

        api.login(System.getProperty("gog-user"), System.getProperty("gog-password"));

        List<GogGame> games = api.listGames();

        Assert.assertTrue(games.size() > 0);
    }

    @Test
    public void testLoadDetails() throws Exception {

        GogApi api = new GogApi();

        api.login(System.getProperty("gog-user"), System.getProperty("gog-password"));

        GogGame game = new GogGame();
        game.setId("1207659105");
        game.setKey("divinity_2_developers_cut");

        DetailedGogGame details = api.loadDetails(game);

        Assert.assertEquals("Divinity 2: Developer's Cut", details.getTitle());

        Assert.assertEquals(5, details.getDownloaderDownloads().size());
        Assert.assertEquals("Windows installer, English", details.getDownloaderDownloads().get(0).getTitle());
        Assert.assertEquals("en", details.getDownloaderDownloads().get(0).getLanguage());
        Assert.assertEquals("gogdownloader://divinity_2_developers_cut/installer_win_en", details.getDownloaderDownloads().get(0).getUrl());
        Assert.assertEquals("5.9 GB", details.getDownloaderDownloads().get(0).getSize());
        Assert.assertEquals("1.1.0", details.getDownloaderDownloads().get(0).getVersion());
        Assert.assertEquals(GogDownload.DownloadOS.Win, details.getDownloaderDownloads().get(0).getOs());

        Assert.assertEquals(21, details.getFileDownloads().size());
        Assert.assertEquals("Windows installer, English (part 1 of 5)", details.getFileDownloads().get(0).getTitle());
        Assert.assertEquals("en", details.getFileDownloads().get(0).getLanguage());
        Assert.assertEquals("https://secure.gog.com/downlink/divinity_2_developers_cut/en1installer0", details.getFileDownloads().get(0).getUrl());
        Assert.assertEquals("2 MB", details.getFileDownloads().get(0).getSize());
        Assert.assertEquals("1.1.0", details.getFileDownloads().get(0).getVersion());
        Assert.assertEquals(GogDownload.DownloadOS.Win, details.getFileDownloads().get(0).getOs());
    }

}
