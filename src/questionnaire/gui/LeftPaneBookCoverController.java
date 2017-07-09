
package questionnaire.gui;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import questionnaire.CoreFX;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class LeftPaneBookCoverController {
    // Left pane (book cover) controls
    
    @FXML
    private Pane pBookCover;
    
    @FXML
    private Label lPublisher;
    
    @FXML
    private Label lStart;
    
    
    // Link on CoreFX
    private CoreFX coreFX;
    
    
    public LeftPaneBookCoverController() {
        
    }
    
    @FXML
    private void initialize() {
        
    }
    
    /**
     * Called by CoreFX that gives a link on itself
     * 
     * @param coreFX
     */
    public void setCoreFX(CoreFX coreFX) {
        this.coreFX = coreFX;    
        
        // Set 'publisher' (studio) name
        lPublisher.setText("Adel-Verlag");
        lPublisher.setVisible(false); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>> ???? >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        
        lStart.setText("Начать");
    }    
     
        
    /**
     * 'Start' click : close the pane
     */
    @FXML
    private void clickStart() {
        System.out.println("'Start' is clicked!");
        coreFX.startNewGame();
    } 
    
}
