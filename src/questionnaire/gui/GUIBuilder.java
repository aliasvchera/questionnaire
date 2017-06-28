/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;;

import questionnaire.CoreFX;
import questionnaire.graphics2d.IntroPseudoVideo;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class GUIBuilder {
    // Waiting for animation finishing
    // public static boolean pleaseWait = false; // >>>>>>>>>>>>>>>> ??????????????????????????????????
    
    private static final Dimension SCREEN_SIZE = 
            Toolkit.getDefaultToolkit().getScreenSize();    
    private static final int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();
    private static final int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();
    
    private static CoreFX coreFX;
    private static Stage primaryStageCore;
    private static Scene scene;
    private static StackPane root;
    private static Pane mainDesk;
    private static Pane leftPaneBook;
    
    private static LeftPaneBookMessageController controllerMessage;
    private static LeftPaneBookPersonalityController controllerPersonality;
    private static LeftPaneBookCoverController controllerBookCover;
    private static RightPaneProfileController controllerProfile;
    
    // Label of date in loadscreen (e.g. 'January 1920')
    private static Label lDate;
    private static Pane pLoadscreen;
    private static String dateText = null;
        
    /**
     * Construct root pane
     * @param linkCoreFX
     * @param primaryStage 
     */
    public static void constructRootPane(CoreFX linkCoreFX, Stage primaryStage) {
        coreFX = linkCoreFX;
        primaryStageCore = primaryStage;
        root = new StackPane();
        root.setBackground(Background.EMPTY);
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK);
        primaryStageCore.setScene(scene);
        primaryStageCore.setTitle("Weimarer Republik - Die Kampfbuende");
        getIcon();
        
        // loadscreen elements
        pLoadscreen = new Pane();
        lDate = new Label();        
        pLoadscreen.getChildren().add(lDate);
        lDate.setFont(Font.font("Mistral", 40));
        // lDate.setTextFill(Color.WHITESMOKE);
        lDate.setTextFill(Color.color(230.0/255, 230.0/255, 230.0/255));
        lDate.setPrefWidth(290);
        // lDate.setLayoutY(SCREEN_HEIGHT / 2 + 285); // bottom of the book
        lDate.setLayoutY(SCREEN_HEIGHT / 2 + 175); // bottom of the logo
        lDate.setLayoutX(SCREEN_WIDTH / 2 - lDate.getPrefWidth() / 2);       
        lDate.setAlignment(Pos.CENTER_RIGHT);
        
        scene.setCursor(Cursor.WAIT);
        primaryStageCore.show();            
        primaryStageCore.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStageCore.setFullScreen(true);        
        
        IntroPseudoVideo.startIntro(scene, root);
    }
    
    public static void getIcon() {
        
        try (DirectoryStream<Path> iconDir = 
                    Files.newDirectoryStream(Paths.get("data\\icons"), 
                            "War-Ensign-Of-Germany_32.png");) {
            for(Path iconPNG :  iconDir) {
                Image iconImage = new Image(iconPNG.toUri().toString());
                primaryStageCore.getIcons().add(iconImage);
            }            
        } catch (IOException ex) {
            Logger.getLogger(GUIBuilder.class.getName()).log(Level.SEVERE, null, ex); // ??????
        }        
    }
    
    
    /** 
     * Set background image for root pane
     * @param imageFile 
     */
    static void renewRootBackground(String imageFile) { 
        File fileImage = new File(imageFile); // "data/screens/gamescreen02.jpg"
        String filePath = fileImage.toURI().toString();
        System.out.println("Test URI file path: " + filePath); // >>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<<<<<<<<<<            
        Image backgroundImage = new Image(filePath, 0, 0, false, true);                     
        BackgroundImage myBI= new BackgroundImage(backgroundImage, 
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, 
                BackgroundSize.DEFAULT); 
        root.setBackground(new Background(myBI)); 
    }
    
    public static void constructMainDesk(String subPaneType) {        
        // Null by default
        System.out.println("Construct main desk"); // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< tmp <<<<<<<<<<<<<<<<<<<<<<<<<
        controllerMessage = null;
        controllerPersonality = null;
        controllerBookCover = null;
        
        processTransition("show_cursor", true);
        root.getChildren().clear();
        
        try {            
            // Load root layout from FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CoreFX.class.getResource("gui/MainDesk.fxml"));
            mainDesk = (Pane) loader.load(); 
            
            // root.setOpacity(0); // Unvisible for starting            
            root.getChildren().add(mainDesk);
            
            if(subPaneType == null) {
            // For loadscreen: show only background and label with current date
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            } else {
            // Main game desk with book and profile
                // Left pane: book cover, personality defining or message
                addLeftPane(subPaneType);
                // Right pane: protagonist profile
                addRightPane();

                fitControls();            
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
   
    
    public static void addLeftPane(String subPaneType) {
        try {
            // Load info about persons
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CoreFX.class.getResource("gui/LeftPaneBook.fxml"));
            leftPaneBook = (Pane) loader.load();
            
            // ...
            mainDesk.getChildren().add(leftPaneBook);
            leftPaneBook.setLayoutX(-20); // setLayoutX(SCREEN_WIDTH / 2 - 670);
            leftPaneBook.setLayoutY(50); // setLayoutY(SCREEN_HEIGHT / 2 - 400);
            
            // Give to controller a link on CoreFX
            LeftPaneBookController controller = loader.getController();
            controller.setCoreFX(coreFX);
            
            // Add sub-pane
            switch(subPaneType) {
                case "message":
                    renewRootBackground("data/screens/gamescreen02.jpg");
                    controller.setCustomisationType("opened_book");
                    addLeftPaneMessage();
                    break;
                case "personality":
                    renewRootBackground("data/screens/gamescreen02.jpg");
                    controller.setCustomisationType("opened_book");
                    addLeftPanePersonality();
                    break;
                case "book_cover":
                    renewRootBackground("data/screens/gamescreen01.jpg");
                    controller.setCustomisationType("book_cover");
                    addLeftPaneBookCover();
                    break;
            }            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void addLeftPaneMessage() {
        try {
            // Load info about persons
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CoreFX.class.getResource(
                    "gui/LeftPaneBookMessage.fxml"));
            Pane leftPaneBookMessage = (Pane) loader.load();
            
            // ...
            leftPaneBook.getChildren().add(leftPaneBookMessage);
            leftPaneBookMessage.setLayoutX(125);
            leftPaneBookMessage.setLayoutY(0);
            
            // Give to controller a link on CoreFX
            controllerMessage = loader.getController();
            controllerMessage.setCoreFX(coreFX);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addLeftPanePersonality() {
        try {
            // Load data for personality defining
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CoreFX.class.getResource(
                    "gui/LeftPaneBookPersonality.fxml"));
            Pane leftPaneBookPersonality = (Pane) loader.load();
            
            // ...
            leftPaneBook.getChildren().add(leftPaneBookPersonality);
            leftPaneBookPersonality.setLayoutX(125);
            leftPaneBookPersonality.setLayoutY(0);
            
            // Give to controller a link on CoreFX
            controllerPersonality = loader.getController();
            controllerPersonality.setCoreFX(coreFX);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addLeftPaneBookCover() {
        try {
            // Load data for personality defining
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CoreFX.class.getResource(
                    "gui/LeftPaneBookCover.fxml"));
            Pane leftPaneBookCover = (Pane) loader.load();
            
            // ...
            leftPaneBook.getChildren().add(leftPaneBookCover);
            leftPaneBookCover.setLayoutX(125);
            leftPaneBookCover.setLayoutY(0);
            
            // Give to controller a link on CoreFX
            controllerBookCover = loader.getController();
            controllerBookCover.setCoreFX(coreFX);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addRightPane() {
        try {
            // Load info about persons
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CoreFX.class.getResource(
                    "gui/RightPaneProfile.fxml"));
            Pane RightPaneBook = (Pane) loader.load();
            
            // ...
            mainDesk.getChildren().add(RightPaneBook);
            RightPaneBook.setLayoutX(730);
            RightPaneBook.setLayoutY(35);
                        
            // Give to controller a link on CoreFX
            controllerProfile = loader.getController();
            controllerProfile.setCoreFX(coreFX);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void showLoadscreen(String setDateText) {
        dateText = setDateText;
        if(pLoadscreen.getParent() == null) {
        // If loadscreen is not loaded 
            System.out.println("Show loadscreen"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp <<<<<<<<<<<<<<<<<<<<<<<<<<
            root.getChildren().clear();
                        
            // Set background
            renewRootBackground("data/screens/loadscreen01.jpg");

            // Add auxiliary pane & label of date (e.g. 'June 1919')
            root.getChildren().add(pLoadscreen);  
            
            // Renew label of date on loadscreen (e.g. 'September 1926')
            lDate.setText(dateText); // ????????????????????????????????????????????????
            
            // From transparent to non-transparent & run switcher
            processTransition("renewDateLabel", true);               
        } else {
            // Renew label of date on loadscreen (e.g. 'September 1926')
            renewDateLabel();
        }
    }
    
        
    
    /**
     * Renew label of date on loadscreen (e.g. 'September 1926')
     * 
     */
    public static void renewDateLabel() {
        System.out.println("renewDateLabel: " + dateText); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        lDate.setText(dateText);
        KeyValue dateText0 = new KeyValue(lDate.textProperty(), lDate.getText());
        KeyValue dateText1 = new KeyValue(lDate.textProperty(), dateText);
        
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, dateText0);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.1), dateText1);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1), dateText1);
        
        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1, keyFrame2); 
        timeline.setOnFinished((ActionEvent e) -> {
            dateText = null;
            coreFX.processNext();
        });
        timeline.play(); 
    }
    
    
    public static void fitControls() {        
        KeyValue paneOpacity0 = new KeyValue(root.opacityProperty(), 0);
        KeyValue paneOpacity1 = new KeyValue(root.opacityProperty(), 0);
        
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, paneOpacity0);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.1), paneOpacity1);
        
        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1); 
        timeline.setOnFinished((ActionEvent e) -> {
            // Update layouts for control fitting
            if(controllerMessage != null) controllerMessage.fitControls();            
            controllerProfile.fitControls();
            
            // Main desk pane: from transparent to non-transparent 
            processTransition("show_cursor", true);
        });
        timeline.play(); 
    }    
    
    
    public static void showMainDesk(String procedureName) {
        if(pLoadscreen.getParent() != null) {
        /* 
        If loadscreen is loaded => direct transition (to make root pane 
        transparent = to hide loadscreen) 
        */
            processTransition(procedureName, false);
            
        } else {
        // If loadscreen is not loaded => construct main desk instantly
            constructMainDesk(procedureName);
        }
    }
    
    public static void processTransition(String procedureName, boolean reverseTransition) {  
        // Hide cursor
        scene.setCursor(Cursor.NONE);
        
        KeyValue paneOpacity0;
        KeyValue paneOpacity1;
        if(!reverseTransition) {
            // From non-transparent to transparent
            paneOpacity0 = new KeyValue(root.opacityProperty(), 1);
            paneOpacity1 = new KeyValue(root.opacityProperty(), 0);
        } else {
            // From transparent to non-transparent 
            paneOpacity0 = new KeyValue(root.opacityProperty(), 0);
            paneOpacity1 = new KeyValue(root.opacityProperty(), 1); 
        }
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, paneOpacity0);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.5), paneOpacity1);

        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1); 
        timeline.setOnFinished((ActionEvent e) -> {
            switch(procedureName) {  
                case "show_cursor":
                    scene.setCursor(Cursor.DEFAULT);
                    break;
                case "showLoadscreen":
                    showLoadscreen("");
                    break;
                case "processNext":
                    coreFX.processNext();
                    break;
                case "renewDateLabel":
                    renewDateLabel();
                    break;
                case "book_cover":                    
                    constructMainDesk("book_cover");
                    break;
                case "personality":                    
                    constructMainDesk("personality");
                    break;
                case "message":                    
                    constructMainDesk("message");
                    break;                
            }             
        });
        timeline.play();        
    }
        
}
