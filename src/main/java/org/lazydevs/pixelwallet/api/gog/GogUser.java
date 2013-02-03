package org.lazydevs.pixelwallet.api.gog;

import org.codehaus.jackson.JsonNode;

/**
 * Created with IntelliJ IDEA.
 * User: sasch_000
 * Date: 03.02.13
 * Time: 08:40
 * To change this template use File | Settings | File Templates.
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
