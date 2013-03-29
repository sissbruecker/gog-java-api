package org.lazydevs.api.gog.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lazydevs.api.gog.model.GogGame;
import org.lazydevs.api.gog.util.Asserts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 02.02.13
 * Time: 21:47
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class GogAccountPage {

    private Document document;

    public GogAccountPage(String htmlContent) {
        this.document = Jsoup.parse(htmlContent);
    }

    public List<GogGame> listGames() {

        List<GogGame> games = new ArrayList<GogGame>();

        // Select all game shelf divs
        Element shelfElement = document.select("#shelfGamesListAll").first();

        Asserts.assertNotNull(shelfElement, "Could not find shelf element: #shelfGamesListAll");

        Elements gameElements = shelfElement.select("div.shelf_game");

        for (Element gameElement : gameElements) {

            GogGame game = new GogGame();

            Asserts.assertTrue(gameElement.hasAttr("data-gameid"), "Shelf game element has no id attribute: data-gameid");
            Asserts.assertTrue(gameElement.hasAttr("data-gameindex"), "Shelf game element has no index attribute: data-gameindex");

            game.setId(gameElement.attr("data-gameid"));
            game.setKey(gameElement.attr("data-gameindex"));

            Element coverImageElement = gameElement.select("img.shelf_game_box").first();

            if (coverImageElement != null) {

                game.setCoverUrl("http://gog.com" + coverImageElement.attr("src"));
            }

            games.add(game);
        }

        return games;
    }
}
