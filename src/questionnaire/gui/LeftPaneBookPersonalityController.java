/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import questionnaire.CoreFX;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class LeftPaneBookPersonalityController {
    
    @FXML
    private Pane pPersonality;
    
    @FXML
    private Label lHeader;
    
    @FXML
    private Label lPreviousPhoto;
    
    @FXML
    private Label lNextPhoto;
    
    @FXML
    private ImageView ivPhotoFrame;
    
    @FXML
    private ImageView ivPortraitSelect;
    
    @FXML
    private Label lName;
    
    @FXML 
    private ComboBox cbName;
    
    @FXML
    private Line lineName;
            
    @FXML
    private Label lSecondName;
    
    @FXML
    private Label lSecondNameAux;
    
    @FXML 
    private ComboBox cbSecondName;
    
    @FXML
    private Line lineSecondName;
            
    @FXML
    private Label lSurname;
    
    @FXML 
    private ComboBox cbSurname;
    
    @FXML
    private Line lineSurname;
    
    @FXML
    private Label lYearOfBirth;
    
    @FXML 
    private ComboBox cbYearOfBirth;
    
    @FXML
    private Line lineYearOfBirth;
    
    @FXML
    private Label lNationality;
    
    @FXML
    private ComboBox cbNationality;
    
    @FXML
    private Line lineNationality;
    
    @FXML
    private Label lNext;
    
    // Link on CoreFX
    private CoreFX coreFX;
    
    // Position of the current selected portrait in portrait list
    private int portraitNumber;
    
    private ArrayList<String> portraitNames;
    
    public LeftPaneBookPersonalityController() {
        
    }
        
    @FXML
    private void initialize() {
        Random rand = new Random();
        
        // Disabled in this version
        lNationality.setVisible(false);
        cbNationality.setVisible(false);
        lineNationality.setVisible(false);
        lHeader.setVisible(false); // ???
        
        lNext.setVisible(false);
        
        ivPhotoFrame.setImage(new Image("file:data/graphics/user200.png"));
        
        // Add portrait names in array list
        portraitNames = new ArrayList<String>();
        File messageDir = new File(CoreFX.GAME_DIR + "/data/portraits");
        if(!messageDir.isDirectory()) System.out.println(messageDir.toString() +  
                    " isDirectory: " + messageDir.isDirectory()); 
            
        for(String DirItem : messageDir.list()) {
            portraitNames.add(DirItem);    
            // System.out.println(DirItem);
        }
        
        portraitNumber = rand.nextInt(portraitNames.size() - 1);
        selectPortrait(false);
        
        /*
        cbName.getItems().add("Kolomann");
        cbSurname.getItems().add("von Gogenzollern");
        cbYearOfBirth.getItems().add("1890");
        */
        
        // Set combo box font, text alignment (in center) & background colour
        ComboBox[] comboBoxes = 
                {cbName, cbSecondName, cbSurname, cbYearOfBirth, cbNationality};
        for(ComboBox comboBox : comboBoxes) {
            comboBox.setStyle("-fx-background-color: transparent;" +
                    "\n" +  
                    "-fx-font: 24px \"Mistral\";" + 
                    "\n" +
                    "-fx-text-alignment: CENTER;"); // ??????????????????????????????????????????????????
            
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override 
                public void changed(ObservableValue ov, String t, String t1) {
                    showNextCheckCondition();
                }    
            });
        }
    }
    
    
    /**
     * Called by CoreFX that gives a link on itself
     * 
     * @param coreFX
     */
    public void setCoreFX(CoreFX coreFX) {
        this.coreFX = coreFX;    
        
        // Set label text
        lPreviousPhoto.setText("<< Предыдущий");
        lNextPhoto.setText("Следующий >>");
        lName.setText("Имя");
        lSecondName.setText("Второе имя ");
        lSecondNameAux.setText("(необязательно)");
        lSurname.setText("Фамилия");
        lYearOfBirth.setText("Год рождения");
        // lNationality.setText(???);
        lNext.setText("Далее");
             
        // Add data to combo box lists
        cbName.getItems().addAll(
                (Object[]) this.coreFX.getPersonalityData().NAME);
        cbSecondName.getItems().addAll(
                (Object[]) this.coreFX.getPersonalityData().NAME);
        cbSurname.getItems().addAll(
                (Object[]) this.coreFX.getPersonalityData().SURNAME);
        cbYearOfBirth.getItems().addAll(
                (Object[]) this.coreFX.getPersonalityData().YEAR_OF_BIRTH);
    }
    
    @FXML
    private void clickPreviousPhoto() {
        selectPortrait(true);
    }
    
    @FXML
    private void clickNextPhoto() {
        selectPortrait(false);
    }
    
    private void selectPortrait(boolean reverseOrder) {
        if(reverseOrder) 
            portraitNumber--; // previous portrait
        else
            portraitNumber++; // next portrait
        
        // For infinite scrolling in both direction
        if(portraitNumber < 0) 
            portraitNumber += portraitNames.size();
        if(portraitNumber >= portraitNames.size()) 
            portraitNumber -= portraitNames.size();
        
        // Show new portrait
        ivPortraitSelect.setImage(new Image("file:data/portraits/" + 
                portraitNames.get(portraitNumber)));
        
        fitControls();
    }
    
    
    private void fitControls() {
        // Fit photo frame to photo portrait
        Image photo = ivPortraitSelect.getImage();        
        double leftFrame = 2;
        double rightFrame = 4;
        double topFrame = 2;
        double bottomFrame = 4;
        double imageWidth, imageHeight, verticalRatio, horizontalRatio;        
        if(photo.getWidth() / photo.getHeight() > 
                ivPortraitSelect.getFitWidth() / 
                ivPortraitSelect.getFitHeight()) {
            imageWidth = ivPortraitSelect.getFitWidth();
            imageHeight = imageWidth / (photo.getWidth() / photo.getHeight());            
        } else {
            imageHeight = ivPortraitSelect.getFitHeight();
            imageWidth = imageHeight * (photo.getWidth() / photo.getHeight()); 
        }
        // Define ratios "fit portrait size vs inner frame image size"
        verticalRatio = imageHeight / (ivPhotoFrame.getImage().getHeight()
                - (topFrame - 1) - (bottomFrame - 1)); 
        horizontalRatio = imageWidth / (ivPhotoFrame.getImage().getWidth()
                - (leftFrame - 1) - (rightFrame - 1));
        // Set X,Y layout & fit width, height
        ivPhotoFrame.setLayoutX(ivPortraitSelect.getLayoutX() - 
                leftFrame * verticalRatio + 1);
        ivPhotoFrame.setLayoutY(ivPortraitSelect.getLayoutY() - 
                topFrame * verticalRatio + 1);
        ivPhotoFrame.setFitWidth(ivPhotoFrame.getImage().getWidth() * 
                horizontalRatio);
        ivPhotoFrame.setFitHeight(ivPhotoFrame.getImage().getHeight() * 
                verticalRatio);
        /* 
        NB. '-1' of frames (top, bottom, left, right) means that frame should 
        recover 1 pixel of image from each side
        */
    }
    
    /**
     * Show 'Next' label/button if all obligatory field are filled
     */
    private void showNextCheckCondition() {
        if(cbName.getValue() != null && cbSurname.getValue() != null && 
                cbYearOfBirth.getValue() != null) {
            lNext.setVisible(true);
        }
    }
    
    /**
     * "Next" click : send data to CoreFX & close the pane
     */
    @FXML
    private void clickNext() {
        String complexName = (String) cbName.getValue();
        if(cbSecondName.getValue()!= null) {
            complexName += " " + cbSecondName.getValue();
        }
        coreFX.getPersonalityInfoFromController(
                portraitNames.get(portraitNumber), complexName, 
                (String) cbSurname.getValue(), 
                (String) cbYearOfBirth.getValue(), "");
        // From non-transparent to transparent + processNext() method
        // GUIBuilder.processTransition("processNext", false);
    }
}
