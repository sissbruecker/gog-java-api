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

    private List<GogDownload> fileUrls;

    private List<GogDownload> downloaderUrls;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GogDownload> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<GogDownload> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public List<GogDownload> getDownloaderUrls() {
        return downloaderUrls;
    }

    public void setDownloaderUrls(List<GogDownload> downloaderUrls) {
        this.downloaderUrls = downloaderUrls;
    }

    public DetailedGogGame(GogGame base) {
        this.setId(base.getId());
        this.setKey(base.getKey());
        this.setCoverUrl(base.getCoverUrl());
        this.fileUrls = new ArrayList<GogDownload>();
        this.downloaderUrls = new ArrayList<GogDownload>();
    }
}
