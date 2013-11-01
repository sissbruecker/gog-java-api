package org.lazydevs.api.gog.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lazydevs.api.gog.model.DetailedGogGame;
import org.lazydevs.api.gog.model.GogDownload;
import org.lazydevs.api.gog.model.GogGame;
import org.lazydevs.api.gog.model.GogGameBonus;
import org.lazydevs.api.gog.util.Asserts;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 29.03.13
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class GogGameShelfDetailsPage {

    private static final String SELECTOR_TITLE_LINK = "div.shelf_det_top > h2 > a";

    private static final String SELECTOR_STORE_LINK = "div.shelf_det_bottom > div.list_det_links > a.list_link.reviews";
    private static final String SELECTOR_FORUM_LINK = "div.shelf_det_bottom > div.list_det_links > a.list_link.forum";
    private static final String SELECTOR_SUPPORT_LINK = "div.shelf_det_bottom > div.list_det_links > a.list_link.support";

    private static final String SELECTOR_DOWNLOADER_LIST = "div.list_down_loader";
    private static final String SELECTOR_FILE_LIST = "div.list_down_browser";

    private static final String SELECTOR_WIN_DOWNLOADS = "div.win-download";
    private static final String SELECTOR_MAC_DOWNLOADS = "div.mac-download";

    private static final String SELECTOR_DOWNLOAD = "div.lang-item";
    private static final String SELECTOR_DOWNLOAD_PART = "a.list_game_item";
    private static final String SELECTOR_DOWNLOAD_TITLE_DOWNLOADER = "span.text.normal";
    private static final String SELECTOR_DOWNLOAD_TITLE_FILE = "span.details-underline";
    private static final String SELECTOR_DOWNLOAD_SIZE = "span.size";
    private static final String SELECTOR_DOWNLOAD_VERSION = "span.version";

    private static final String SELECTOR_BONUS_DOWNLOADER_LIST = "div.shelf_det_right > div.bonus_content_list.downloader";
    private static final String SELECTOR_BONUS_FILE_LIST = "div.shelf_det_right > div.bonus_content_list.browser";

    private static final String SELECTOR_DOWNLOADER_BONUS_NAME = "span.details-underline";
    private static final String SELECTOR_FILE_BONUS_NAME = "span.details-underline";
    private static final String SELECTOR_FILE_BONUS_SIZE = "span.size";

    private Document document;

    public GogGameShelfDetailsPage(String htmlContent) {
        this.document = Jsoup.parse(htmlContent);
    }

    public DetailedGogGame getDetails(GogGame base) {

        DetailedGogGame details = new DetailedGogGame(base);

        // Title
        Element titleLinkElement = document.select(SELECTOR_TITLE_LINK).first();

        Asserts.assertNotNull(titleLinkElement, "Can not find title link element: " + SELECTOR_TITLE_LINK);

        details.setTitle(titleLinkElement.text().trim());

        // Store url
        Element storeLinkElement = document.select(SELECTOR_STORE_LINK).first();

        Asserts.assertNotNull(storeLinkElement, "Can not find store link element: " + SELECTOR_STORE_LINK);

        String storeUrl = storeLinkElement.attr("href");

        if (storeUrl.contains("#")) {
            storeUrl = storeUrl.substring(0, storeUrl.indexOf("#"));
        }

        details.setStoreUrl(storeUrl);

        // Forum url
        Element forumLinkElement = document.select(SELECTOR_FORUM_LINK).first();

        Asserts.assertNotNull(forumLinkElement, "Can not find forum link element: " + SELECTOR_FORUM_LINK);

        details.setForumUrl(forumLinkElement.attr("href"));

        // Support url
        Element supportLinkElement = document.select(SELECTOR_SUPPORT_LINK).first();

        Asserts.assertNotNull(supportLinkElement, "Can not find support link element: " + SELECTOR_SUPPORT_LINK);

        details.setSupportUrl(supportLinkElement.attr("href"));

        // Downloader downloads
        Element downloaderListElement = document.select(SELECTOR_DOWNLOADER_LIST).first();

        Asserts.assertNotNull(downloaderListElement, "Can not find downloader list element: " + SELECTOR_DOWNLOADER_LIST);

        Element downloaderWinDownloadElement = downloaderListElement.select(SELECTOR_WIN_DOWNLOADS).first();

        Asserts.assertNotNull(downloaderListElement, "Can not find downloader win element: " + SELECTOR_WIN_DOWNLOADS);

        parseDownloaderDownloadsByOs(details, downloaderWinDownloadElement, GogDownload.DownloadOS.Win);

        Element downloaderMacDownloadElement = downloaderListElement.select(SELECTOR_MAC_DOWNLOADS).first();

        Asserts.assertNotNull(downloaderListElement, "Can not find downloader mac element: " + SELECTOR_MAC_DOWNLOADS);

        parseDownloaderDownloadsByOs(details, downloaderMacDownloadElement, GogDownload.DownloadOS.Mac);

        // File downloads
        Element fileListElement = document.select(SELECTOR_FILE_LIST).first();

        Asserts.assertNotNull(fileListElement, "Can not find file list element: " + SELECTOR_FILE_LIST);

        Element fileWinDownloadElement = fileListElement.select(SELECTOR_WIN_DOWNLOADS).first();

        Asserts.assertNotNull(fileWinDownloadElement, "Can not find file win element: " + SELECTOR_WIN_DOWNLOADS);

        parseFileDownloadsByOs(details, fileWinDownloadElement, GogDownload.DownloadOS.Win);

        Element fileMacDownloadElement = fileListElement.select(SELECTOR_MAC_DOWNLOADS).first();

        Asserts.assertNotNull(fileMacDownloadElement, "Can not find file mac element: " + SELECTOR_MAC_DOWNLOADS);

        parseFileDownloadsByOs(details, fileMacDownloadElement, GogDownload.DownloadOS.Mac);

        // Downloader bonuses
        Element downloaderBonusList = document.select(SELECTOR_BONUS_DOWNLOADER_LIST).first();

        Asserts.assertNotNull(downloaderBonusList, "Can not find downloader bonus list: " + SELECTOR_BONUS_DOWNLOADER_LIST);

        parseDownloaderBonuses(details, downloaderBonusList);

        // File bonuses
        Element fileBonusList = document.select(SELECTOR_BONUS_FILE_LIST).first();

        Asserts.assertNotNull(fileBonusList, "Can not find file bonus list: " + SELECTOR_BONUS_FILE_LIST);

        parseFileBonuses(details, fileBonusList);

        return details;
    }

    private void parseDownloaderDownloadsByOs(DetailedGogGame details, Element osDownloadsElement, GogDownload.DownloadOS os) {

        Elements downloadElements = osDownloadsElement.select(SELECTOR_DOWNLOAD);

        for (Element downloadElement : downloadElements) {

            details.getDownloaderDownloads().add(parseDownloaderDownload(downloadElement, os));
        }
    }

    private GogDownload parseDownloaderDownload(Element downloadElement, GogDownload.DownloadOS os) {

        GogDownload download = new GogDownload();

        download.setOs(os);

        // Language
        download.setLanguage(parseLanguage(downloadElement.classNames()));

        // Title
        Element downloadTitleElement = downloadElement.select(SELECTOR_DOWNLOAD_TITLE_DOWNLOADER).first();

        Asserts.assertNotNull(downloadTitleElement, "Can not find download title element: " + SELECTOR_DOWNLOAD_TITLE_DOWNLOADER);

        download.setTitle(downloadTitleElement.text().trim());

        // Url
        Element linkElement = downloadElement.select("a").first();

        Asserts.assertNotNull(linkElement, "Can not find download link element.");

        download.setUrl(linkElement.attr("href"));

        // Size
        Element sizeElement = downloadElement.select(SELECTOR_DOWNLOAD_SIZE).first();

        Asserts.assertNotNull(sizeElement, "Can not find download size element: " + SELECTOR_DOWNLOAD_SIZE);

        download.setSize(sizeElement.text());

        // Version
        Element versionElement = downloadElement.select(SELECTOR_DOWNLOAD_VERSION).first();

        Asserts.assertNotNull(versionElement, "Can not find download version element: " + SELECTOR_DOWNLOAD_VERSION);

        download.setVersion(versionElement.text());

        return download;
    }

    private void parseFileDownloadsByOs(DetailedGogGame details, Element osDownloadsElement, GogDownload.DownloadOS os) {

        Elements downloadElements = osDownloadsElement.select(SELECTOR_DOWNLOAD);

        for (Element fileDownloadElement : downloadElements) {

            // Language
            String language = parseLanguage(fileDownloadElement.classNames());

            // Parts
            Elements fileDownloadParts = fileDownloadElement.select(SELECTOR_DOWNLOAD_PART);

            for (Element fileDownloadPart : fileDownloadParts)
                details.getFileDownloads().add(parseFileDownload(fileDownloadPart, language, os));
        }
    }

    private GogDownload parseFileDownload(Element downloadElement, String language, GogDownload.DownloadOS os) {

        GogDownload download = new GogDownload();

        download.setLanguage(language);
        download.setOs(os);

        // Title
        Element downloadTitleElement = downloadElement.select(SELECTOR_DOWNLOAD_TITLE_FILE).first();

        Asserts.assertNotNull(downloadTitleElement, "Can not find download title element: " + SELECTOR_DOWNLOAD_TITLE_FILE);

        download.setTitle(downloadTitleElement.text().trim());

        // Url
        download.setUrl(downloadElement.attr("href"));

        // Size
        Element sizeElement = downloadElement.select(SELECTOR_DOWNLOAD_SIZE).first();

        Asserts.assertNotNull(sizeElement, "Can not find download size element: " + SELECTOR_DOWNLOAD_SIZE);

        download.setSize(sizeElement.text());

        // Version
        Element versionElement = downloadElement.select(SELECTOR_DOWNLOAD_VERSION).first();

        Asserts.assertNotNull(versionElement, "Can not find download version element: " + SELECTOR_DOWNLOAD_VERSION);

        download.setVersion(versionElement.text());

        return download;
    }

    private void parseDownloaderBonuses(DetailedGogGame details, Element listElement) {

        Elements extraElements = listElement.select("a");

        for (Element bonusElement : extraElements) {

            String url = bonusElement.attr("href");
            String id = url.substring(url.lastIndexOf("/") + 1, url.length());
            GogGameBonus bonus = details.getBonusById(id);

            if (bonus == null) {

                bonus = new GogGameBonus();
                bonus.setId(id);

                // Title
                Element titleElement = bonusElement.select(SELECTOR_DOWNLOADER_BONUS_NAME).first();

                Asserts.assertNotNull(titleElement, "Can not find title element: " + SELECTOR_DOWNLOADER_BONUS_NAME);

                bonus.setTitle(titleElement.text());

                // Type
                bonus.setType(parseBonusType(bonusElement.classNames()));

                details.getBonuses().add(bonus);
            }

            // Downloader URL
            bonus.setDownloaderUrl(url);
        }
    }

    private void parseFileBonuses(DetailedGogGame details, Element listElement) {

        Elements extraElements = listElement.select("a");

        for (Element bonusElement : extraElements) {

            String url = bonusElement.attr("href");
            String id = url.substring(url.lastIndexOf("/") + 1, url.length());
            GogGameBonus bonus = details.getBonusById(id);

            if (bonus == null) {

                bonus = new GogGameBonus();
                bonus.setId(id);

                // Title
                Element titleElement = bonusElement.select(SELECTOR_FILE_BONUS_NAME).first();

                Asserts.assertNotNull(titleElement, "Can not find title element: " + SELECTOR_FILE_BONUS_NAME);

                bonus.setTitle(titleElement.text());

                // Type
                bonus.setType(parseBonusType(bonusElement.classNames()));

                details.getBonuses().add(bonus);
            }

            // Size
            Element sizeElement = bonusElement.select(SELECTOR_FILE_BONUS_SIZE).first();

            Asserts.assertNotNull(sizeElement, "Can not find type element: " + SELECTOR_FILE_BONUS_SIZE);

            bonus.setSize(sizeElement.text());

            // File URL
            bonus.setFileUrl(url);
        }
    }

    private String parseLanguage(Set<String> classNames) {

        String language = "";

        for (String className : classNames) {
            if (!className.equalsIgnoreCase("lang-item") && className.startsWith("lang")) {
                language = className.substring(5);
                break;
            }
        }

        return language;
    }

    private GogGameBonus.BonusType parseBonusType(Set<String> classNames) {

        if (classNames.contains("imanual"))
            return GogGameBonus.BonusType.Manual;

        if (classNames.contains("iwallpaper"))
            return GogGameBonus.BonusType.Wallpaper;

        if (classNames.contains("iguides"))
            return GogGameBonus.BonusType.Guide;

        if (classNames.contains("imovies"))
            return GogGameBonus.BonusType.Video;

        if (classNames.contains("itunes"))
            return GogGameBonus.BonusType.Audio;

        if (classNames.contains("iarts"))
            return GogGameBonus.BonusType.Artwork;

        return GogGameBonus.BonusType.Other;
    }
}
