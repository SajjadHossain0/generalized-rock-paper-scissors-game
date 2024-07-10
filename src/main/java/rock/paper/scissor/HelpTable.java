package rock.paper.scissor;
import de.vandermeer.asciitable.AsciiTable;

import java.util.List;

class HelpTable {
    public static void displayHelp(List<String> moves) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        String[] header = new String[moves.size() + 1];
        header[0] = "";
        for (int i = 0; i < moves.size(); i++) {
            header[i + 1] = moves.get(i);
        }
        at.addRow((Object[]) header);
        at.addRule();

        for (int i = 0; i < moves.size(); i++) {
            String[] row = new String[moves.size() + 1];
            row[0] = moves.get(i);
            for (int j = 0; j < moves.size(); j++) {
                if (i == j) {
                    row[j + 1] = "Draw";
                } else {
                    int half = moves.size() / 2;
                    if ((i > j && i <= j + half) || (i < j && i + moves.size() <= j + half)) {
                        row[j + 1] = "Lose";
                    } else {
                        row[j + 1] = "Win";
                    }
                }
            }
            at.addRow((Object[]) row);
            at.addRule();
        }
        System.out.println(at.render());
    }
}
