package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Card;
import model.Player;

public class DAOimpl implements Dao {

    private Connection connection;

    public void connect() {
        try {
            // Establecer la conexión a la base de datos
        	String url = "jdbc:mysql://127.0.0.1:3306/unobbdd?serverTimezone=UTC&useSSL=false";

            String user = "root";
            String password = "1234";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            // Cerrar la conexión
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLastIdCard(int playerId) throws SQLException {
        int lastId = 0;
        String query = "SELECT IFNULL(MAX(id_card), 0) + 1 AS last_id FROM unocard WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    lastId = resultSet.getInt("last_id");
                }
            }
        }
        return lastId;
    }

    @Override
    public Card getLastCard() throws SQLException {
        Card lastCard = null;
        String query = "SELECT id_card, number, color, id_player FROM unocard ORDER BY id_card DESC LIMIT 1";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id_card");
                String number = resultSet.getString("number");
                String color = resultSet.getString("color");
                int playerId = resultSet.getInt("id_player");
                lastCard = new Card(id, number, color, playerId);
            }
        }
        return lastCard;
    }


    @Override
    public Player getPlayer(String user, String pass) throws SQLException {
        Player player = null;
        String query = "SELECT * FROM unoplayer WHERE user = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id_player");
                    String name = resultSet.getString("name");
                    int games = resultSet.getInt("games");
                    int victories = resultSet.getInt("victories");
                    player = new Player(id, name, games, victories);
                }
            }
        }
        return player;
    }

    @Override
    public ArrayList<Card> getCards(int playerId) throws SQLException {
        ArrayList<Card> cards = new ArrayList<>();
        String query = "SELECT id_card, number, color FROM unocard WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id_card");
                    String number = resultSet.getString("number");
                    String color = resultSet.getString("color");
                    cards.add(new Card(id, number, color, playerId));
                }
            }
        }
        return cards;
    }

    @Override
    public Card getCard(int cardId) throws SQLException {
        Card card = null;
        String query = "SELECT number, color, id_player FROM unocard WHERE id_card = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, cardId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String number = resultSet.getString("number");
                    String color = resultSet.getString("color");
                    int playerId = resultSet.getInt("id_player");
                    card = new Card(cardId, number, color, playerId);
                }
            }
        }
        return card;
    }

    @Override
    public void saveGame(Card card) throws SQLException {
        String query = "INSERT INTO unogame (id_card) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, card.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void saveCard(Card card) throws SQLException {
        String query = "INSERT INTO unocard (id_card, number, color, id_player) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, card.getId());
            preparedStatement.setString(2, card.getNumber());
            preparedStatement.setString(3, card.getColor());
            preparedStatement.setInt(4, card.getPlayerId());
            preparedStatement.executeUpdate();
        }
    }


    @Override
    public void deleteCard(Card card) throws SQLException {
        String query = "DELETE FROM unogame WHERE id_card = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, card.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void clearDeck(int playerId) throws SQLException {
        String query = "DELETE FROM unocard WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void addVictories(int playerId) throws SQLException {
        String query = "UPDATE unoplayer SET victories = victories + 1 WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void addGames(int playerId) throws SQLException {
        String query = "UPDATE unoplayer SET games = games + 1 WHERE id_player = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.executeUpdate();
        }
    }
}
