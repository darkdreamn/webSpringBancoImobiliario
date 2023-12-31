package com.bancoImobiliario.spring.controller;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.web.bind.annotation.RestController;

import com.bancoImobiliario.spring.model.Player;

@RestController
public class GameController {
    private Queue<Player> players = new LinkedList<Player>();

    public void StartGame() {
        Player newPlayer = new Player(300, 1, "impulsivo");
        players.add(newPlayer);
        newPlayer = new Player(300, 2, "exigente");
        players.add(newPlayer);
        newPlayer = new Player(300, 3, "cauteloso");
        players.add(newPlayer);
        newPlayer = new Player(300, 4, "aleatorio");
        players.add(newPlayer);
    }

}
