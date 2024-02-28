package persistance;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import model.Card;
import model.Player;

public class PlayerPersistence {
    public static void savePlayersToFile(List<Player> players) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("playersFile.txt"))) {
            for (Player player : players) {
                writer.write(player.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveCardsToFile(List<Card> cards) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cardsFile.txt"))) {
            for (Card card : cards) {
                writer.write(card.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
