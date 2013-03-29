package org.lazydevs.api.gog.parsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.lazydevs.api.gog.model.DetailedGogGame;
import org.lazydevs.api.gog.model.GogDownload;
import org.lazydevs.api.gog.model.GogGame;
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

    private static final String SELECTOR_DOWNLOADER_LIST = "div.list_down_loader";
    private static final String SELECTOR_FILE_LIST = "div.list_down_browser";

    private static final String SELECTOR_WIN_DOWNLOADS = "div.win-download";
    private static final String SELECTOR_MAC_DOWNLOADS = "div.mac-download";

    private static final String SELECTOR_DOWNLOAD = "div.lang-item";
    private static final String SELECTOR_DOWNLOAD_PART = "a.list_game_item";
    private static final String SELECTOR_DOWNLOAD_TITLE_DOWNLOADER = "span.text.normal";
    private static final String SELECTOR_DOWNLOAD_TITLE_FILE = "span.light_un";
    private static final String SELECTOR_DOWNLOAD_SIZE = "span.size";
    private static final String SELECTOR_DOWNLOAD_VERSION = "span.version";


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
}
