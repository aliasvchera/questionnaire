/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import questionnaire.CoreFX;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class LeftPaneBookController {
    // Left pane (book) controls
        
    @FXML
    private Label lNewGame;
    
    @FXML
    private Label lExitGame;
        
    // Link on CoreFX
    private CoreFX coreFX;
    
    
    public LeftPaneBookController() {
        
    }
    
    @FXML
    private void initialize() {
        lNewGame.setText("Заново"); // change to variable ???????????????????????????????????????        
        lNewGame.setLayoutY(723);
        
        lExitGame.setText("Закрыть"); // change to variable ???????????????????????????????????????
        lExitGame.setLayoutY(723);
    }
    
    /**
     * Define layout parameters (X, Y, rotation angle) for different main desks
     */
    public void setCustomisationType(String mainDeskType) {
        switch(mainDeskType) {
            case "book_cover":
                lNewGame.setLayoutX(147);
                lNewGame.setRotate(-89);
                
                lExitGame.setLayoutX(300);
                lExitGame.setRotate(-97);
                break;
            case "opened_book":
                lNewGame.setLayoutX(-14);
                lNewGame.setRotate(-90);
                
                lExitGame.setLayoutX(309);
                lExitGame.setRotate(-96);
                break;
        }
    }
    
    /**
     * Called by CoreFX that gives a link on itself
     * 
     * @param coreFX
     */
    public void setCoreFX(CoreFX coreFX) {
        this.coreFX = coreFX;   
    }
    
    /**
     * Start new game
     */
    @FXML
    private void newGame() {
        System.out.println("New game - step1");
        CoreFX.getInstance().newGame();
    } 
    
    /**
     * Exit application
     */
    @FXML
    private void exitGame() {
        System.out.println("Exit game - step1");
        CoreFX.saveGame();
        Platform.exit();
    }   
   
}
