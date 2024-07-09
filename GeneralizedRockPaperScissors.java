import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GeneralizedRockPaperScissors {
    public static void main(String[] args) {
        if (args.length < 3 || args.length % 2 == 0 || Arrays.stream(args).distinct().count() != args.length) {
            System.out.println("Error: Invalid arguments");
            System.out.println("Usage: java GeneralizedRockPaperScissors move1 move2 move3 ...");
            return;
        }

        List<String> moves = Arrays.asList(args); // for command line arguments

        Game game = new Game(moves);
        game.play();
    }
}

class CryptoUtils {
    public static byte[] generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        return key;
    }

    public static String calculateHMAC(byte[] key, String message) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmac = mac.doFinal(message.getBytes());
        return bytesToHex(hmac);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

class GameRules {
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

class HelpTable {
    public static void displayHelp(List<String> moves) {
        System.out.printf("%-10s", "");
        for (String move : moves) {
            System.out.printf("%-10s", move);
        }
        System.out.println();
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("%-10s", moves.get(i));
            for (int j = 0; j < moves.size(); j++) {
                if (i == j) {
                    System.out.printf("%-10s", "Draw");
                } else {
                    int half = moves.size() / 2;
                    if ((i > j && i <= j + half) || (i < j && i + moves.size() <= j + half)) {
                        System.out.printf("%-10s", "Lose");
                    } else {
                        System.out.printf("%-10s", "Win");
                    }
                }
            }
            System.out.println();
        }
    }
}

class Game {
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

/*
=> java GeneralizedRockPaperScissors rock paper scissor

HMAC: 5f035cd1f98a249aa6115295de9e30bf4416434783e49188a858f21a03959876
Available moves:
1 - rock
2 - paper
3 - scissor
0 - Exit
? - Help
Enter your move: 1
Your move: rock
Computer move: paper
You win
HMAC key: 5f409313d86bdff67df8c563e8b789c9fa2cb933dbdceb98a564e77f9f58f3d0
 */
