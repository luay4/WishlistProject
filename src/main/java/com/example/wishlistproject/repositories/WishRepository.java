package com.example.wishlistproject.repositories;

import com.example.wishlistproject.models.User;

import java.sql.*;

public class WishRepository {

    public WishRepository() {
        connectToDB();
        createTables();
    }

    private Connection con;
    private String sqlString;
    private Statement stmt;
    private ResultSet rs;

    public void connectToDB() {
        try {
            String url = "jdbc:mysql://wishlist-fsp.mysql.database.azure.com:3306/wishlist?useSSL=true&requireSSL=false";
            con = DriverManager.getConnection(url, "dbadmin@wishlist-fsp", "Keadbkode123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            sqlString = "CREATE TABLE users " +
                    "(user_id INT NOT NULL AUTO_INCREMENT, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(50) NOT NULL, " +
                    "PRIMARY KEY (user_id))";
            stmt.execute(sqlString);

            sqlString = "CREATE TABLE wishes " +
                    "(user_id INT NOT NULL," +
                    "item_sequence INT NOT NULL, " +
                    "item_name VARCHAR(50) NOT NULL, " +
                    "FOREIGN KEY (user_id) REFERENCES users(user_id))";
            stmt.execute(sqlString);

        } catch (SQLException e) {
            System.out.println("Tables already exist in database");
        }
    }

    public void createUser(String username, String password) {
        try {
            sqlString = "INSERT INTO users (username, password) " +
                    "VALUES('" + username + "', '" + password + "')";
            stmt.execute(sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWish(String username, String wishName) {
        try {
            sqlString = "SELECT user_id FROM users WHERE username = '" + username + "'";
            rs = stmt.executeQuery(sqlString);
            rs.next();
            int id = rs.getInt("user_id");

            sqlString = "SELECT COUNT(user_id) FROM wishes WHERE user_id = " + id + "";
            rs = stmt.executeQuery(sqlString);
            rs.next();
            int seq = rs.getInt(1) + 1;

            sqlString = "INSERT INTO wishes (user_id, item_sequence, item_name) " +
                    "VALUES(" + id + ", " + seq + ",'" + wishName + "')";
            stmt.execute(sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesUserExist(String username, String password) {
        try {
            sqlString = "SELECT * FROM users " +
                    "WHERE username = '" + username + "' AND password = '" + password + "'";
            rs = stmt.executeQuery(sqlString);
            boolean result = rs.next();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isUsernameTaken(String username) {
        //https://stackoverflow.com/questions/46061989/jdbc-check-if-entry-exists kode inspiration :)
        try {
            sqlString = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sqlString);
            boolean result = rs.next();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getUserIDFromDB(String username) {
        try {
            sqlString = "SELECT user_id FROM users WHERE username = '" + username + "'";
            rs = stmt.executeQuery(sqlString);
            rs.next();
            return rs.getInt("user_id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateWishesFromUser(User user) {
        try {
            user.clearWishlist();

            sqlString = "SELECT item_name FROM wishes WHERE user_id = " + user.getUserID() + "";
            rs = stmt.executeQuery(sqlString);
            String itemName;
            while (rs.next()) {
                itemName = rs.getString("item_name");
                user.addToWishlist(itemName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WishRepository wr = new WishRepository();
        System.out.println(wr.getUserIDFromDB("ben"));
    }
}
