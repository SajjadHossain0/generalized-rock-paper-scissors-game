package org.GeneralizedRockPaperScissors;

import rock.paper.scissor.Game;

import java.util.Arrays;
import java.util.List;

public class game {
    public static void main(String[] args) {
        if (args.length < 3 || args.length % 2 == 0 || Arrays.stream(args).distinct().count() != args.length) {
            System.out.println("Error: Invalid arguments");
            System.out.println("Usage: java GeneralizedRockPaperScissors move1 move2 move3 ...");
            return;
        }

        List<String> moves = Arrays.asList(args);

        Game game = new Game(moves);
        game.play();
    }
}