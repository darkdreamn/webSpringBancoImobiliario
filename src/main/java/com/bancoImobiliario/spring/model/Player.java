package com.bancoImobiliario.spring.model;

import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private int money;
    private int idPlayer;
    private String profile;

    public boolean ActionBuy(Player currentPlayer, House currentHouse) {
        switch (currentPlayer.profile) {
            case "impulsivo":
                if (currentPlayer.money > currentHouse.getPrice()) {
                    currentPlayer.money -= currentHouse.getPrice();
                    return true;
                } else
                    return false;

            case "exigente":
                if (currentPlayer.money > currentHouse.getPrice() && currentHouse.getRent() > 50) {
                    currentPlayer.money -= currentHouse.getPrice();
                    return true;
                } else
                    return false;

            case "cauteloso":
                if (currentPlayer.money > currentHouse.getPrice()
                        && (currentPlayer.money - currentHouse.getPrice() > 80)) {
                    currentPlayer.money -= currentHouse.getPrice();
                    return true;
                } else
                    return false;

            case "aleatorio":
                if (currentPlayer.money > currentHouse.getPrice()) {
                    Random random = new Random();
                    boolean randomValue = random.nextBoolean();
                    if (randomValue) {
                        currentPlayer.money -= currentHouse.getPrice();
                        return true;
                    } else
                        return false;
                }
        }
        return false;
    }

    public boolean ActionRent(Player currentPlayer, House currentHouse) {
        currentPlayer.money -= currentHouse.getRent();
        if (currentPlayer.money > 0)
            return true;
        else
            return false;
    }

    public int RollDice() {
        Random r = new Random();
        return (r.nextInt(6) + 1);
    }
}
