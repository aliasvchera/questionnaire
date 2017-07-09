
package questionnaire.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static java.lang.Math.max;
import static java.lang.Math.min;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import questionnaire.CoreFX;
import questionnaire.files.DataInTXT;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class LeftPaneBookMessageController {
    // Left pane (opened book) controls
    
    @FXML
    private Pane pBookMessage;
    
    @FXML
    private Label lHeader;
    
    @FXML
    private ImageView ivPicture;
    
    @FXML
    private ImageView ivFrame;
    
    @FXML
    private Label lText;
    
    @FXML
    private RadioButton rbAnswer0;    
    
    @FXML
    private RadioButton rbAnswer1;
    
    @FXML
    private RadioButton rbAnswer2;
    
    @FXML
    private RadioButton rbAnswer3;
    
    @FXML
    private RadioButton rbAnswer4;    
    
    @FXML
    private Label lQuoteAuthor;
    
    @FXML
    private Label lNext;
    
    
    // Radio button group
    final ToggleGroup answers = new ToggleGroup();
    private RadioButton[] rbAnswer = new RadioButton[5];
    
    // Link on CoreFX
    private CoreFX coreFX;
    
    private Image pictureImage;
    
    
    public LeftPaneBookMessageController() {
        
    }
    
    @FXML
    private void initialize() {
        rbAnswer[0] = rbAnswer0;
        rbAnswer[1] = rbAnswer1;
        rbAnswer[2] = rbAnswer2;
        rbAnswer[3] = rbAnswer3;
        rbAnswer[4] = rbAnswer4;  
        Image imageNonSelected = 
                new Image("file:data/graphics/radio_non-selected.png");
        Image imageSelected = 
                new Image("file:data/graphics/radio_selected.png");        
        for(RadioButton rb : rbAnswer) {
            rb.setToggleGroup(answers);
            // Remove radio button dot
            rb.getStyleClass().remove("radio-button");
            rb.getStyleClass().add("label");                    
            // Set custom image of radio button dot
            rb.setGraphic(new ImageView(imageNonSelected));
            rb.setContentDisplay(ContentDisplay.LEFT);
        }
        answers.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle old_toggle, Toggle new_toggle) {
                    if (answers.getSelectedToggle() != null) {
                        // Show 'Next' button for question messages
                        lNext.setVisible(true);
                        
                        // Renew custom radio button dots
                        for(RadioButton rb : rbAnswer) {
                            if(rb.isSelected()) {
                                rb.setGraphic(new ImageView(imageSelected));
                            } else {
                                rb.setGraphic(new ImageView(imageNonSelected));
                            }
                        }
                    }                
                }
        });
    }
    
    /**
     * Called by CoreFX that gives a link on itself
     * 
     * @param coreFX
     */
    public void setCoreFX(CoreFX coreFX) {
        this.coreFX = coreFX;    
        
        // Set title
        lHeader.setText(coreFX.getMessageViewData().getCaptionText());
        
        // Set picture image
        if(coreFX.getMessageViewData().getPictureName() != null) {
            pictureImage = 
                    new Image("file:data/images/" + 
                            coreFX.getMessageViewData().getPictureName());
            ivPicture.setImage(pictureImage);
        } else {
            pBookMessage.getChildren().remove(ivPicture);
            pBookMessage.getChildren().remove(ivFrame);
        }
        
        // Set custom font for text & answers
        try { 
            // final 
            Font customFont = Font.loadFont(new FileInputStream(
                    new File("data/fonts/b52.ttf")), 17);
            customFont = Font.font("Book Antiqua", 15); // <<<<<<<<<<<<<<<< ?????????? >>>>>>>>>>>>>>>>>>>>>>>>
            lText.setFont(customFont);
            for(RadioButton rb : rbAnswer) rb.setFont(customFont);            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Set specific font for event stories
        if(coreFX.getMessageViewData().getMessageType().equals("eventStory")) {
            lText.setFont(Font.font("Franklin Gothic Medium", 14));
        } 
        
        // Set main text of event/question
        lText.setText(coreFX.getMessageViewData().getMainText());
        
        // Set answers 
        boolean noAnswers = true;
        String[] answerText = coreFX.getMessageViewData().getAnswerText();
        for(int i = 0; i < rbAnswer.length; i++) {
            if(answerText[i] != null && !answerText[i].trim().equals("")) {
                rbAnswer[i].setText(answerText[i]);
                noAnswers = false;
            } else {
                pBookMessage.getChildren().remove(rbAnswer[i]);
            }
        }     
        
        if(!noAnswers) {
            // Hide 'Next' button for question messages
            lNext.setVisible(false);
        } else {
            // Hide 'Next' button for events with 'The-End' trigger
            if(coreFX.getMessageViewData().getTheEndTrigger()) 
                lNext.setVisible(false);    
        }
        
        // Set quotation author
        if(coreFX.getMessageViewData().getQuotationAuthor() != null && 
                !coreFX.getMessageViewData().getQuotationAuthor().trim().equals("")) {
            // System.out.println("Set quotationAuthor: (" + coreFX.getMessageViewData().getQuotationAuthor() + ")");
            lQuoteAuthor.setText(coreFX.getMessageViewData().getQuotationAuthor());
        } else {
            // System.out.println("Remove quotationAuthor"); 
            pBookMessage.getChildren().remove(lQuoteAuthor);
        } 
        
        lNext.setText("Далее");
        
        DataInTXT.writeInLog("Left pane book (message) loaded");
    }
    
        
    /**
     * Placing and sizing controls for optimal space usage.
     * Text controls (header, questions etc) have maximal priority vs picture.
     * 
     */
    public void fitControls () {
        double auxLayoutY = lNext.getLayoutY();
        // Fit quotation author label
        if(lQuoteAuthor.getParent() != null) {
            auxLayoutY -= lQuoteAuthor.prefHeight(lQuoteAuthor.getPrefWidth()) 
                    + 8;
            lQuoteAuthor.setLayoutY(auxLayoutY);
        }        
        
        // Fit answers
        for(int i = rbAnswer.length - 1; i >= 0; i--) {
            if(rbAnswer[i].getParent() != null) {
                auxLayoutY -= rbAnswer[i].prefHeight(rbAnswer[i].getPrefWidth()) 
                        + 8;
                rbAnswer[i].setLayoutY(auxLayoutY);
            }  
        }        
        
        if(pictureImage == null) {
        // In case of message without question - align to top side:    
            auxLayoutY -= lText.prefHeight(lText.getPrefWidth()) + 8;
            
            // Fit main text
            lText.setLayoutY(lHeader.getLayoutY() + lHeader.getHeight() + 8);            
            
            // Calculate shift to top side
            auxLayoutY -= lText.getLayoutY();
            
            // Fit answers
            for(int i = rbAnswer.length - 1; i >= 0; i--) {
                if(rbAnswer[i].getParent() != null) {                
                    rbAnswer[i].setLayoutY(rbAnswer[i].getLayoutY() - auxLayoutY);
                }  
            } 
            
            // Fit quotation author label
            if(lQuoteAuthor.getParent() != null) {
                lQuoteAuthor.setLayoutY(lQuoteAuthor.getLayoutY() - auxLayoutY);
            } 
        } else {
            double imageWidth = pictureImage.getWidth();
            double imageHeight = pictureImage.getHeight();
            // System.out.println("Image w/h: " + imageWidth + "/" + imageHeight);

            // Fit main text
            if(lText.getParent() != null) {
                if(imageHeight != 0 && imageWidth / imageHeight > 1 ) {
                    /* 
                    For 'horizontal' image case: place the main text in rectangle 
                    between picture and 1st question or quotation
                    author label 
                    */
                    auxLayoutY -= lText.prefHeight(lText.getPrefWidth()) + 8;
                    lText.setLayoutY(auxLayoutY);
                } else {
                    /* 
                    For 'vertical' image case: place the main text in right 
                    part of rectangle between header and 1st question or quotation
                    author label 
                    */
                    lText.setLayoutY(lHeader.getLayoutY() + lHeader.getHeight() + 8);
                    double tmpImageWidth = (auxLayoutY - lText.getLayoutY() - 8) 
                            * imageWidth / imageHeight; // <<<<<<<<<<<<<<<<<<<<<<<<<< is OK with this ??????????????????????
                    // Avoid negative values
                    if(lHeader.getWidth() - tmpImageWidth - 48 > 150) { // ???????????????????????????????????????????
                        lText.setPrefWidth(lHeader.getWidth() - tmpImageWidth 
                                - 48); // -48 = -8 (space between text & image) -20*2 (space for frame)
                    } else {
                        lText.setPrefWidth(150);
                    }
                    while(lText.getPrefWidth() < lHeader.getWidth() && 
                            lText.prefHeight(lText.getPrefWidth()) >= 
                            (auxLayoutY - lText.getLayoutY() - 8)) {
                        lText.setPrefWidth(lText.getPrefWidth() + 5);
                    }             
                    lText.setLayoutX(lHeader.getWidth() - lText.getPrefWidth());
                    lText.setPrefHeight(auxLayoutY - lText.getLayoutY() - 8); 
                    lText.setAlignment(Pos.CENTER_LEFT); 
                }
            }

            // Fit image        
            if(ivPicture.getParent() != null) {
                // Frame variables
                Image frameImage;
                double leftFrame;
                double rightFrame;
                double topFrame;
                double bottomFrame;
                double horizontalInnerFrame;
                double verticalInnerFrame;
                
                if(imageHeight != 0 && imageWidth / imageHeight > 1 ) {
                    /* 
                    Horizontal alignment and placement for 'horizontal' picture: 
                    set in center of rectangle between header and main text 
                    */
                    frameImage = new Image("file:data/graphics/wide550.png");
                    leftFrame = 20;
                    rightFrame = 25;
                    topFrame = 20;
                    bottomFrame = 22;
                    horizontalInnerFrame = (frameImage.getWidth() - 
                            leftFrame - rightFrame);
                    verticalInnerFrame = (frameImage.getHeight() - 
                            topFrame - bottomFrame);
                    ivPicture.setFitHeight((auxLayoutY - ivPicture.getLayoutY() - 
                            8) * verticalInnerFrame / frameImage.getHeight());  
                    ivPicture.setFitWidth(ivPicture.getFitHeight() * 
                            imageWidth / imageHeight);
                    // Image cannot be wider than lHeader
                    if(ivPicture.getFitWidth() > lHeader.getPrefWidth()) {
                        ivPicture.setFitWidth(lHeader.getPrefWidth());
                        ivPicture.setFitHeight(ivPicture.getFitWidth() * 
                            imageHeight / imageWidth);
                    }                    
                    if(pictureImage.getHeight() != 0) {
                        ivPicture.setLayoutX(lHeader.getLayoutX() + 
                            max(0, (lHeader.getWidth() -
                            (ivPicture.getFitHeight() / pictureImage.getHeight()) * 
                            pictureImage.getWidth()) / 2));
                        ivPicture.setLayoutY(lHeader.getLayoutY() + 
                                lHeader.getHeight() + 8 + topFrame * 
                                ivPicture.getFitHeight() / verticalInnerFrame);                        
                    } 
                } else {
                    /*
                    Vertical alignment and placement for 'vertical' picture:
                    set in left (horizont alignment) and center (vertical alignment)
                    part of rectangle between header, main text and 1st question 
                    or quotation author label
                    */
                    frameImage = new Image("file:data/graphics/tall400.png");
                    leftFrame = 19;
                    rightFrame = 23;
                    topFrame = 20;
                    bottomFrame = 21;
                    horizontalInnerFrame = (frameImage.getWidth() - 
                            leftFrame - rightFrame);
                    verticalInnerFrame = (frameImage.getHeight() - 
                            topFrame - bottomFrame);
                    double freeSpaceWidth = lText.getLayoutX() - 
                            lHeader.getLayoutX() - 8;
                    double freeSpaceHeight = auxLayoutY - 
                            (lHeader.getLayoutY() + lHeader.getHeight()) - 8 * 2;
                    
                    if(freeSpaceWidth > 0 && freeSpaceHeight > 0) {                            
                        ivPicture.setFitHeight(min(freeSpaceHeight, 
                                freeSpaceWidth * imageHeight / imageWidth) * 
                                horizontalInnerFrame / frameImage.getWidth()); // verticalInnerFrame / frameImage.getHeight()  ???
                        ivPicture.setFitWidth(min(freeSpaceWidth, 
                                freeSpaceHeight * imageWidth / imageHeight) * 
                                horizontalInnerFrame / frameImage.getWidth());
                        
                        ivPicture.setLayoutX(lHeader.getLayoutX() + leftFrame * 
                                ivPicture.getFitWidth() / horizontalInnerFrame);
                        // Vertical alignment: place in center
                        ivPicture.setLayoutY(lHeader.getLayoutY() + 
                                lHeader.getHeight() + 8 +(freeSpaceHeight - 
                                ivPicture.getFitHeight()) / 2 );  
                    }
                }
                
                // Set frame
                ivFrame.setLayoutX(ivPicture.getLayoutX() - leftFrame * 
                        ivPicture.getFitWidth() / horizontalInnerFrame);
                ivFrame.setLayoutY(ivPicture.getLayoutY() - topFrame * 
                        ivPicture.getFitHeight() / verticalInnerFrame);
                ivFrame.setFitWidth(ivPicture.getFitWidth() * 
                        frameImage.getWidth() / horizontalInnerFrame);
                ivFrame.setFitHeight(ivPicture.getFitHeight() * 
                        frameImage.getHeight() / verticalInnerFrame);
                ivFrame.setImage(frameImage);                
            }
        }
    }
    
        
    /**
     * "Next" click : send data to CoreFX & close the pane
     */
    @FXML
    private void clickNext() {
        byte selectedAnswer = -1;        
        for(int i = rbAnswer.length - 1; i >= 0; i--) {
            if(rbAnswer[i].getParent() != null && rbAnswer[i].isSelected()) {                
                selectedAnswer = (byte) i;
            }  
        } 
        coreFX.getAnswerFromController(selectedAnswer);
        // From non-transparent to transparent + processNext() method
        // GUIBuilder.processTransition("processNext", false);        
    } 
    
}
