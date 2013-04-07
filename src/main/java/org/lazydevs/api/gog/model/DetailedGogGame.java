package org.lazydevs.api.gog.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 29.03.13
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class DetailedGogGame extends GogGame {

    private String title;

    private String storeUrl;

    private String forumUrl;

    private String supportUrl;

    private List<GogDownload> fileDownloads;

    private List<GogDownload> downloaderDownloads;

    private List<GogGameBonus> bonuses;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getForumUrl() {
        return forumUrl;
    }

    public void setForumUrl(String forumUrl) {
        this.forumUrl = forumUrl;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(String supportUrl) {
        this.supportUrl = supportUrl;
    }

    public List<GogDownload> getFileDownloads() {
        return fileDownloads;
    }

    public void setFileDownloads(List<GogDownload> fileDownloads) {
        this.fileDownloads = fileDownloads;
    }

    public List<GogDownload> getDownloaderDownloads() {
        return downloaderDownloads;
    }

    public void setDownloaderDownloads(List<GogDownload> downloaderDownloads) {
        this.downloaderDownloads = downloaderDownloads;
    }

    public List<GogGameBonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<GogGameBonus> bonuses) {
        this.bonuses = bonuses;
    }

    public DetailedGogGame(GogGame base) {
        this.setId(base.getId());
        this.setKey(base.getKey());
        this.setCoverUrl(base.getCoverUrl());
        this.fileDownloads = new ArrayList<GogDownload>();
        this.downloaderDownloads = new ArrayList<GogDownload>();
        this.bonuses = new ArrayList<GogGameBonus>();
    }

    public GogGameBonus getBonusById(String id) {

        for(GogGameBonus bonus : this.bonuses) {
            if(bonus.getId().equalsIgnoreCase(id))
                return bonus;
        }

        return null;
    }
}
