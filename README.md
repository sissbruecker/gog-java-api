Description
===========

A java API for loading user data from http://gog.com

- Account info
- Game list

Usage
=====

Create a new API instance

    GogApi api = new GogApi();

Login with your username/password

    api.login("username", "password");

Load user account

    GogUser user = api.loadUser();

Load game list

    List<GogGame> games = api.listGames();