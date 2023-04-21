package com.example.playlist;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloController {
    @FXML
    public Button btnDel;
    @FXML
    public Button btnUpd;
    @FXML
    public TextField searchField;
    @FXML
    public Button btnSearch;
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

    public void deleteMusic() throws SQLException, ClassNotFoundException {
        Music music = TableMusic.getSelectionModel().getSelectedItem();

        DBConnector connector = new DBConnector();
        connection = connector.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM music WHERE music.id = " + music.getId());

        initialize();
    }

    public void updateMusic() throws SQLException, ClassNotFoundException {
        Music music = TableMusic.getSelectionModel().getSelectedItem();

        DBConnector connector = new DBConnector();
        connection = connector.getConnection();
        Statement statement = connection.createStatement();

        //поиск исполнителя, если нет в БД - добавляет
        ResultSet setSinger = statement.executeQuery("SELECT singer.id FROM singer WHERE singer.name = '" + singerField.getText() + "'");
        if(!setSinger.next()) {
            statement.executeUpdate("INSERT INTO singer (singer.name) VALUES ('" + singerField.getText() + "')");
        }
        setSinger = statement.executeQuery("SELECT singer.id FROM singer WHERE singer.name = '" + singerField.getText() + "'");
        setSinger.next();
        int singerId = setSinger.getInt("singer.id");

        //поиск альбома, если нет в БД - добавляет
        ResultSet setAlbum = statement.executeQuery("SELECT album.id FROM album WHERE album.name = '" + albumField.getText() + "'");
        if(!setAlbum.next()) {
            statement.executeUpdate("INSERT INTO album (album.name) VALUES ('" + albumField.getText() + "')");
        }
        setAlbum = statement.executeQuery("SELECT album.id FROM album WHERE album.name = '" + albumField.getText() + "'");
        setAlbum.next();
        int albumId = setAlbum.getInt("album.id");

        //обновление музыки
        statement.executeUpdate("UPDATE music SET music.name = '" + nameField.getText() + "', music.singerId = " + singerId + ", music.albumId = " + albumId + " WHERE music.id = " + music.getId());

        initialize();
    }

    public void searchMusic() throws SQLException, ClassNotFoundException {
        String text = searchField.getText();

        DBConnector connector = new DBConnector();
        connection = connector.getConnection();


        TableMusic.setItems(connector.getMusic("SELECT music.id, music.name, singer.name, album.name FROM playlist.music JOIN singer ON music.singerId = singer.id JOIN album ON music.albumId = album.id WHERE music.name = '" + text + "' OR singer.name = '" + text + "' OR album.name = '" + text + "'"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        singerColumn.setCellValueFactory(new PropertyValueFactory<>("singer"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
    }
}