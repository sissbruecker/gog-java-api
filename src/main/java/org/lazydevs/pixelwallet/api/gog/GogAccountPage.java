package org.lazydevs.pixelwallet.api.gog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sasch_000
 * Date: 02.02.13
 * Time: 21:47
 * To change this template use File | Settings | File Templates.
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

        if(shelfElement == null)
            throw new GogApiException("Could not find shelf element. selector=div.shelfGamesListAll");

        Elements gameElements = shelfElement.select("div.shelf_game");

        for (Element gameElement : gameElements) {

            GogGame game = new GogGame();

            game.setId(gameElement.attr("data-gameid"));
            game.setKey(gameElement.attr("data-gameindex"));
            game.setTitle(game.getKey());

            Element coverImageElement = gameElement.select("img.shelf_game_box").first();

            if (coverImageElement != null) {

                game.setCoverUrl("http://gog.com" + coverImageElement.attr("src"));
            }

            games.add(game);
        }

        return games;
    }
}
