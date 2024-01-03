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

        int[] playersRandom = { 1, 2, 3, 4 };
        Random random = new Random();
        for (int i = playersRandom.length - 1; i > 0; i--) {
            int randomI = random.nextInt(i + 1);
            int temp = playersRandom[i];
            playersRandom[i] = playersRandom[randomI];
            playersRandom[randomI] = temp;
        }
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
        House[] typeHouse = new House[5];
        House houseType1 = new House(20, 40, 0);
        House houseType2 = new House(30, 50, 0);
        House houseType3 = new House(50, 80, 0);
        House houseType4 = new House(70, 100, 0);
        House houseType5 = new House(90, 110, 0);

        typeHouse[0] = houseType1;
        typeHouse[1] = houseType2;
        typeHouse[2] = houseType3;
        typeHouse[3] = houseType4;
        typeHouse[4] = houseType5;

        Random rand = new Random();
        int randInt;

        for (int i = 0; i < gameBoard.length; i++) {
            randInt = rand.nextInt(5);
            switch (randInt) {
                case 0:
                    gameBoard[i] = new GameBoard(typeHouse[0]);
                    break;
                case 1:
                    gameBoard[i] = new GameBoard(typeHouse[1]);
                    break;
                case 2:
                    gameBoard[i] = new GameBoard(typeHouse[2]);
                    break;
                case 3:
                    gameBoard[i] = new GameBoard(typeHouse[3]);
                    break;
                case 4:
                    gameBoard[i] = new GameBoard(typeHouse[4]);
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

        HashMap<String, Object> result = new HashMap<String, Object>();

        for (int i = 0; i < players.size(); i++) {
            finalPlayers.add(players.peek());
        }
        Collections.sort(finalPlayers, new Comparator<Player>() {
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getMoney(), p2.getMoney());
            }
        });

        String[] orderPlayers = new String[finalPlayers.size()];
        for (int i = 0; i < finalPlayers.size(); i++) {
            orderPlayers[i] = finalPlayers.get(i).getProfile();
        }
        result.put("vencedor:", currentPlayer.getProfile());
        result.put("jogadores:", orderPlayers);
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
