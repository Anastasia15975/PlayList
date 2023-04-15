package com.example.playlist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnector {
    private Connection connection;

    DBConnector() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost/playlist", "root", "root");
    }

    public ObservableList<Music> getMusic(String selectRequest) throws SQLException {
        ObservableList<Music> res = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(selectRequest);

        int id;
        String name, album, singer;
        while (set.next()) {
            id = set.getInt("music.id");
            name = set.getString("music.name");
            singer = set.getString("singer.name");
            album = set.getString("album.name");
            res.add(new Music(id, name, singer, album));
        }
        return res;
    }

    public Connection getConnection() {
        return connection;
    }
}
