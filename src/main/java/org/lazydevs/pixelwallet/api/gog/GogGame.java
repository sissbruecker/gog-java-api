package org.lazydevs.pixelwallet.api.gog;

/**
 * Created with IntelliJ IDEA.
 * User: sasch_000
 * Date: 03.02.13
 * Time: 09:07
 * To change this template use File | Settings | File Templates.
 */
public class GogGame {

    private String id;

    private String key;

    private String title;

    private String coverUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
