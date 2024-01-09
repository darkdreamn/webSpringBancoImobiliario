package com.bancoImobiliario.spring.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancoImobiliario.spring.model.GameBoard;
import com.bancoImobiliario.spring.model.House;
import com.bancoImobiliario.spring.model.Player;

@RestController
public class GameController {
    private Queue<Player> players = new LinkedList<Player>();
    private List<Player> finalPlayers = new ArrayList<>();
    private GameBoard[] gameBoard = new GameBoard[20];

    private static int round = 0;

    @GetMapping("/jogo/simular")
    public ResponseEntity<JSONObject> WinnerPlayer() {
        StartGame();
        JSONObject resultJson = new JSONObject(PlayGame());
        return ResponseEntity.status(HttpStatus.OK).body(resultJson);
    }

    public void StartGame() {
        CreatePlayers();
        CreateGameBoard();
    }

    public void CreatePlayers() {
        Player player1 = new Player(300, 1, "impulsivo");
        Player player2 = new Player(300, 2, "exigente");
        Player player3 = new Player(300, 3, "cauteloso");
        Player player4 = new Player(300, 4, "aleatorio");

        int[] playersRandom = { -1, -1, -1, -1 };
        int auxPlayer;
        int count = 0;
        boolean find = false;
        do {
            auxPlayer = (int) (Math.random() * playersRandom.length);
            for (int i : playersRandom) {
                if (i == auxPlayer) {
                    find = true;
                    break;
                } else
                    find = false;
            }
            if (find == false) {
                for (int i = 0; i < playersRandom.length; i++) {
                    if (playersRandom[i] == -1) {
                        playersRandom[i] = auxPlayer;
                        count++;
                        break;
                    }
                }
            }
        } while (count < playersRandom.length);

        for (int i : playersRandom) {
            switch (i) {
                case 0:
                    players.add(player1);
                    break;
                case 1:
                    players.add(player2);
                    break;
                case 2:
                    players.add(player3);
                    break;
                case 3:
                    players.add(player4);
                    break;
            }
        }
    }

    public void CreateGameBoard() {
        Random rand = new Random();
        int randInt;

        for (int i = 0; i < gameBoard.length; i++) {
            randInt = rand.nextInt(5);
            switch (randInt) {
                case 0:
                    House houseType1 = new House(40, 60, 0);
                    gameBoard[i] = new GameBoard(houseType1);
                    break;
                case 1:
                    House houseType2 = new House(50, 90, 0);
                    gameBoard[i] = new GameBoard(houseType2);
                    break;
                case 2:
                    House houseType3 = new House(70, 110, 0);
                    gameBoard[i] = new GameBoard(houseType3);
                    break;
                case 3:
                    House houseType4 = new House(90, 120, 0);
                    gameBoard[i] = new GameBoard(houseType4);
                    break;
                case 4:
                    House houseType5 = new House(100, 130, 0);
                    gameBoard[i] = new GameBoard(houseType5);
                    break;
            }
        }
    }

    public HashMap<String, Object> PlayGame() {
        Player currentPlayer = new Player();
        int currentHouse = 0;
        boolean buyHouse = false;

        do {
            currentPlayer = players.remove();
            currentHouse += currentPlayer.RollDice();
            if (currentHouse > 19)
                currentHouse = currentHouse - 20;

            if (gameBoard[currentHouse].getHouse().getIdHouse() == 0) {
                buyHouse = currentPlayer.ActionBuy(currentPlayer, gameBoard[currentHouse].getHouse());
                if (buyHouse) {
                    gameBoard[currentHouse].getHouse().setIdHouse(currentPlayer.getIdPlayer());
                }
                players.add(currentPlayer);
            } else if (gameBoard[currentHouse].getHouse().getIdHouse() == 1 && 1 != currentPlayer.getIdPlayer()) {
                PlayerRent(currentPlayer, gameBoard[currentHouse].getHouse());
            } else if (gameBoard[currentHouse].getHouse().getIdHouse() == 2 && 2 != currentPlayer.getIdPlayer()) {
                PlayerRent(currentPlayer, gameBoard[currentHouse].getHouse());
            } else if (gameBoard[currentHouse].getHouse().getIdHouse() == 3 && 3 != currentPlayer.getIdPlayer()) {
                PlayerRent(currentPlayer, gameBoard[currentHouse].getHouse());
            } else if (gameBoard[currentHouse].getHouse().getIdHouse() == 4 && 4 != currentPlayer.getIdPlayer()) {
                PlayerRent(currentPlayer, gameBoard[currentHouse].getHouse());
            }
            round++;
        } while (round < 1000 && players.size() > 1);

        // TODO: testa id casas
        // for (int i = 0; i < gameBoard.length; i++) {
        // System.out.println(gameBoard[i].getHouse().getIdHouse());
        // }

        HashMap<String, Object> result = new HashMap<String, Object>();

        for (int i = 0; i < players.size(); i++) {
            finalPlayers.add(players.peek());
        }
        System.out.println(players.size());
        System.out.println(finalPlayers.size());

        Collections.sort(finalPlayers, new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getMoney(), p2.getMoney());
            }
        });

        // TODO: vencedor
        // for (int i = 0; i < players.size(); i++) {
        // System.out.println(players.peek().getProfile());
        // }

        String[] orderPlayers = new String[finalPlayers.size()];
        for (int i = 0; i < finalPlayers.size(); i++) {
            orderPlayers[i] = finalPlayers.get(i).getProfile();
        }
        result.put("vencedor:", orderPlayers[0]);
        result.put("jogadores:", orderPlayers);

        // TODO: mostra a ordem dos jogadores, precisa arrumar
        // for (String i : orderPlayers) {
        // System.out.println(i);
        // }
        // for (int i = 0; i < finalPlayers.size(); i++) {
        // System.out.println(finalPlayers.get(i).getProfile());
        // }
        return result;
    }

    public void PlayerRent(Player currentPlayer, House currentHouse) {
        boolean rentHouse = false;

        rentHouse = currentPlayer.ActionRent(currentPlayer, currentHouse);
        if (rentHouse) {
            players.add(currentPlayer);
        } else {
            for (int i = 0; i < gameBoard.length; i++) {
                if (gameBoard[i].getHouse().getIdHouse() == currentPlayer.getIdPlayer())
                    gameBoard[i].getHouse().setIdHouse(0);
            }
            currentPlayer.setMoney(currentPlayer.getMoney() - currentHouse.getRent());
            finalPlayers.add(currentPlayer);
        }
    }
}
