package org.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author programacion
 */
public class Principal extends Application {
    public Stage stage;
    private static Principal instancia;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/view/Home.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        this.stage = stage;
        
        stage.setOnCloseRequest(evt -> {
            
            System.exit(0);

        });
        
    }

    public static Principal getInstancia(){ 
        if(instancia == null){ 
            instancia = new Principal();
        } 
        return instancia;
    } 
    public Stage getStage(){
        return  stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
