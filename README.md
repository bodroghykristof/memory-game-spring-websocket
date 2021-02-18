# memory-game-spring-websocket

## Intro

In this project I am creating a multiplayer memory-game playable for 2 players

## Core feature

The application currently consists of 2 pages. On the landing page we can see the rooms currently in operation. Here we can create or join rooms.
When the owner of the room presses the 'Start' button players are navigated to the game page automatically.

Here they are able to play a round-based memory game. The goal of the game is to find matching pairs of cards which are displayed in a table format with the
icon on them hidden. By clicking on any of the cards its icon is revealed. A round consists of two card-reveals. If the two revelad icons match the guessing player
is awarded a point and can keep his turn. Otherwise the other player  can guess in the next round. The game ends when all of the cards are revealed and the player
with the higher score wins.

Each card turrning is visible to both players so not only the guessing player but also the opponent is aware of the currently turned cards. This way they have double
chance of memorizing which card hides which icon.

## Used technologies

  - Spring Boot
  - Spring Websocket
  - H2 database
  - Stomp JS
  - HTML, CSS, vanilla JS

## Further security and UX features

The solution (the mapping between cards and their corresponding icons) is stored on the server in database. Each turn requires a request towards the server.
Actual icons are not stored on client side in any form so by no means can users solve the game by cheating.

Users are free to refresh the page at any time, the whole game state is presisted and reloaded at will. On the other hand, if any of the playing parties wiches to
leave the page, the other player gets a notificatio after a given (currently 5 sec) waiting time and is free to return to the room page. In this case every data
in connection with the given game is removed from the database.

Room owners are able to choose from 4 different board sizes. Boards are generated randomly on server-side.

## Features yet to be implemented

  - informing players about started/finished game on room page
  - preventing joining to multiple rooms at the same time on room page
  - removing data from localStorage before leaving game
  - choosing username and storing user data on server side
  - making websocketConnectionHandler waiting specific to user ID and not game ID
  - designing cards and UI
