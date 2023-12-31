package com.bancoImobiliario.spring.controller;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

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
    private GameBoard[] gameBoard = new GameBoard[20];

    // private static int round = 0;
    // TODO: terminar a partida se atingir 1000 rodadas

    @GetMapping("/jogo/simular")
    public ResponseEntity<String> WinnerPlayer() {
        StartGame();
        return ResponseEntity.status(HttpStatus.OK).body("teste");
    }

    public void StartGame() {
        CreatePlayers();
        CreateGameBoard();
    }

    public void CreatePlayers() {
        Player newPlayer = new Player(300, 1, "impulsivo");
        players.add(newPlayer);
        newPlayer = new Player(300, 2, "exigente");
        players.add(newPlayer);
        newPlayer = new Player(300, 3, "cauteloso");
        players.add(newPlayer);
        newPlayer = new Player(300, 4, "aleatorio");
        players.add(newPlayer);
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
        for (int i = 0; i < gameBoard.length; i++) {
            System.out.println(gameBoard[i].getHouse().getPrice());
        }
    }
}
