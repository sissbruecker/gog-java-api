Description
===========

A java API for loading user data from http://gog.com

- Account info
- Game list
- Details for a game (downloader/browser URLs, bonuses, info URLs)

Usage
=====

Create a new API instance

    GogApi api = new GogApi();

Login with your username/password

    api.login("username", "password");

Load user account

    GogUser user = api.loadUser();

Load list of games in your account

    List<GogGame> games = api.listGames();

Load details for a game

    DetailedGogGame details = api.loadDetails(game);

To see what kind of information is loaded by each API call please have a look at the respective result classes:
https://github.com/sissbruecker/gog-java-api/blob/master/src/main/java/org/lazydevs/api/gog/model/GogGame.java
https://github.com/sissbruecker/gog-java-api/blob/master/src/main/java/org/lazydevs/api/gog/model/DetailedGogGame.java
https://github.com/sissbruecker/gog-java-api/blob/master/src/main/java/org/lazydevs/api/gog/model/GogDownload.java
https://github.com/sissbruecker/gog-java-api/blob/master/src/main/java/org/lazydevs/api/gog/model/GogGameBonus.java