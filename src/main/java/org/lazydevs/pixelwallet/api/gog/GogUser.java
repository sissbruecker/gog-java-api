package org.lazydevs.pixelwallet.api.gog;

import org.codehaus.jackson.JsonNode;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 03.02.13
 * Time: 08:40
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class GogUser {

    private String id;

    private String email;

    private String name;

    private String avatarUrl;

    private String buk;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBuk() {
        return buk;
    }

    public void setBuk(String buk) {
        this.buk = buk;
    }

    public GogUser(JsonNode jsonResponse) {

        this.buk = jsonResponse.get("buk").asText();

        if(jsonResponse.has("user")) {

            JsonNode jsonUser = jsonResponse.get("user");

            this.id = jsonUser.get("id").asText();
            this.email = jsonUser.get("email").asText();
            this.name = jsonUser.get("xywka").asText();
            this.avatarUrl = "http://gog.com" + jsonUser.get("avatar_small").asText();
        }
    }
}
