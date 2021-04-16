package org.ludo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.ludo.App;
import org.ludo.gameLogic.FXMLElements;
import org.ludo.gameLogic.Game;
import org.ludo.utils.gameSaving.GameSaveHandler;
import org.ludo.utils.gameSaving.FileHandler;
import org.ludo.utils.gameSaving.SerializedGameState;

import java.io.IOException;

public class LoadGameSceneController {

    @FXML
    private VBox savedGamesVBox;

    @FXML
    private void initialize() {
        FileHandler fileHandler = new GameSaveHandler();
        fileHandler.getSavedGames().forEach(game -> {
            Button saveButton = new Button(game.substring(0, game.length() - 5));
            saveButton.setOnAction(event -> {
                try {
                    loadGame(fileHandler.loadGameSave(game));
                } catch (IOException e) {
                    //TODO: give feedback to user that error occured
                    System.out.println("error loading game");
                    e.printStackTrace();
                }
            });
            savedGamesVBox.getChildren().add(saveButton);
        });
    }

    public void back() throws IOException {
        App.setRoot("startScene");
    }

    private void loadGame(SerializedGameState serializedGameState) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/gameScene.fxml"));

        Scene activeScene = App.getScene();
        activeScene.setRoot(loader.load());


        var game = new Game();
        game.initState(serializedGameState);
        game.initGraphics();
        game.start();

        GameSceneController controller = loader.getController();
        controller.setGameState(game);
    }
}
