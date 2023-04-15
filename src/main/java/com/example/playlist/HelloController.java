package com.example.playlist;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.SQLException;

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
}