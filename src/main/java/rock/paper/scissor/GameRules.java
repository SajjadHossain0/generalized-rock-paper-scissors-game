package rock.paper.scissor;

import java.util.List;

public class GameRules {
    public static String determineWinner(List<String> moves, int userMove, int computerMove) {
        int half = moves.size() / 2;
        if (userMove == computerMove) {
            return "Draw";
        } else if ((userMove > computerMove && userMove <= computerMove + half) ||
                (userMove < computerMove && userMove + moves.size() <= computerMove + half)) {
            return "Computer wins";
        } else {
            return "You win";
        }
    }
}

