package rock.paper.scissor;

import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<String> moves;
    private byte[] key;
    private int computerMoveIndex;
    private String computerMove;

    public Game(List<String> moves) {
        this.moves = moves;
        this.key = CryptoUtils.generateKey();
        this.computerMoveIndex = new SecureRandom().nextInt(moves.size());
        this.computerMove = moves.get(computerMoveIndex);
    }

    public void play() {

        try {

            String hmac = CryptoUtils.calculateHMAC(key, computerMove);
            System.out.println("HMAC: " + hmac);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Available moves:");
                for (int i = 0; i < moves.size(); i++) {
                    System.out.printf("%d - %s\n", i + 1, moves.get(i));
                }

                System.out.println("0 - Exit");
                System.out.println("? - Help");
                System.out.print("Enter your move: ");
                String input = scanner.nextLine();

                if (input.equals("0")) {
                    break;
                } else if (input.equals("?")) {
                    HelpTable.displayHelp(moves);
                } else {
                    try {
                        int userMoveIndex = Integer.parseInt(input) - 1;
                        if (userMoveIndex >= 0 && userMoveIndex < moves.size()) {
                            String result = GameRules.determineWinner(moves, userMoveIndex, computerMoveIndex);
                            System.out.println("Your move: " + moves.get(userMoveIndex));
                            System.out.println("Computer move: " + computerMove);
                            System.out.println(result);
                            System.out.println("HMAC key: " + CryptoUtils.bytesToHex(key));
                            break;
                        } else {
                            System.out.println("Invalid move. Try again.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Try again.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

