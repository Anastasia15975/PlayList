package com.example.playlist;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloController {
    @FXML
    private TableView<Music> TableMusic;

    @FXML
    private TableColumn<Music, String> albumColumn;

    @FXML
    private TableColumn<Music, Integer> idColumn;

    @FXML
    private TableColumn<Music, String> nameColumn;

    @FXML
    private TableColumn<Music, String> singerColumn;

    @FXML
    private TextField albumField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField singerField;

    @FXML
    private Button btnAdd;
    Connection connection;

    public void initialize() throws SQLException, ClassNotFoundException {
        DBConnector connector = new DBConnector();
        connection = connector.getConnection();
        TableMusic.setItems(connector.getMusic("SELECT music.id, music.name, singer.name, album.name FROM playlist.music JOIN singer ON music.singerId = singer.id JOIN album ON music.albumId = album.id"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        singerColumn.setCellValueFactory(new PropertyValueFactory<>("singer"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
    }

    public void addMusic() throws SQLException, ClassNotFoundException {
        String name, singer, album;

        name = nameField.getText();
        singer = singerField.getText();
        album = albumField.getText();

        DBConnector connector = new DBConnector();
        connection = connector.getConnection();
        Statement statement = connection.createStatement();

        //добавление исполнителя
        statement.executeUpdate("INSERT INTO singer(singer.name) VALUES ('" + singer + "')");
        //берём его id
        ResultSet setSinger = statement.executeQuery("SELECT singer.id FROM singer WHERE singer.name = '" + singer + "'");
        setSinger.next();
        int idSinger = setSinger.getInt("singer.id");

        //добавление альбома
        statement.executeUpdate("INSERT INTO album(album.name) VALUES ('" + album + "')");
        //берём его id
        ResultSet setAlbum = statement.executeQuery("SELECT album.id FROM album WHERE album.name = '" + album + "'");
        setAlbum.next();
        int idAlbum = setAlbum.getInt("album.id");

        //добавление и вывод новой песни
        statement.executeUpdate("INSERT INTO music(music.name, music.singerId, music.albumId) VALUES ('" + name + "'," + idSinger + "," + idAlbum + ")");
        initialize();
    }
}