/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.graphics2d;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.nio.file.*;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;

import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.effect.GaussianBlur;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javafx.scene.input.KeyCombination;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javafx.scene.media.*;

import javafx.scene.paint.Color;

import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import javafx.util.Duration;
import questionnaire.CoreFX;


public class IntroPseudoVideo {
    
    private static float blurRadius0;
    private static float blurRadius1;
    private static double shiftPositionX0;
    private static double shiftPositionY0;
    private static double shiftPositionX1;
    private static double shiftPositionY1;
    
    private static Image fullImage;
    private static int shiftRange;
    private static int fullWidth;
    private static int fullHeight;
    private static int frameWidth;
    private static int frameHeight;  
    
    private static StackPane root;
    private static Pane introFrame;
    private static Scene scene;
    private static ImageView whiteNoizeFilter;
        
    private static MediaPlayer player;
    
    private static DisplacementMap displacementMap;
    private static GaussianBlur blur;
    private static ColorAdjust bw;
    
    private static String filePath0;
    private static String filePath1;
    private static String filePath2;
    private static String filePath3;
    private static String filePath4;
    private static String filePath5;
    private static String filePath6;
    private static String filePath7;
    private static String currentImage;
    
    private static final Dimension SCREEN_SIZE = 
            Toolkit.getDefaultToolkit().getScreenSize();    
    private static final int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();
    private static final int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();
    
    // Artifacts    
    private static Line artifactLine[];
    private static Polygon artifactPolygon[];
    private static Line artifactShortLine[];
    
    
    public static void startIntro(Scene mainScene, StackPane rootPane) {
       
        // Testing animated intro
        filePath0 = "file:data/screens/loadscreen_adel-studio.png";
        filePath1 = "file:data/screens/loadscreen_RFB.png";
        filePath2 = "file:data/screens/loadscreen_RB.png";
        filePath3 = "file:data/screens/loadscreen_STA.png";
        filePath4 = "file:data/screens/loadscreen_SA.png";
        filePath5 = "file:data/screens/loadscreen_im.png";
        filePath6 = "file:data/screens/loadscreen_logo.png";
        filePath7 = "file:data/screens/loadscreen01.jpg";
                
        fullImage = new Image(filePath0);
        currentImage = "image0";
        shiftRange = 10;        
        fullWidth = (int) fullImage.getWidth();
        fullHeight = (int) fullImage.getHeight();
        frameWidth = 1024; // 1280; // 800;
        frameHeight = 768; // 600;
                
        root = rootPane;
        introFrame = new Pane();
        introFrame.setMaxSize(frameWidth, frameHeight);        
        root.getChildren().add(introFrame);
        
        
        introFrame.setBackground(processImage(fullImage, shiftRange)); 
        
        // Create and set white noize filter image
        whiteNoizeFilter = new ImageView(whiteNoize(frameWidth, frameHeight));
        whiteNoizeFilter.setOpacity(0.25); // find the best value
        introFrame.getChildren().add(whiteNoizeFilter);        
        
        // Create pseudo-video artifacts
        createAtrifacts();
        // Initialize artifacts by random values
        renewArtifacts();
        
        scene = mainScene;
        
        // Hide cursor
        scene.setCursor(Cursor.NONE);
                  
        // Black-and-white (grayscale)
        bw = new ColorAdjust();
        bw.setSaturation(-1);
        bw.setBrightness(-0.25); // find the best value
               
        // Blur
        blur = new GaussianBlur(); // MotionBlur?
        blur.setRadius(2);
        blur.setInput(bw);
        root.setEffect(blur);        
        
        // Displacement map
        FloatMap floatMap = new FloatMap();
        floatMap.setWidth(frameWidth);
        floatMap.setHeight(frameHeight);
        for (int i = 0; i < frameWidth; i++) {
            for (int j = 0; j < frameHeight; j++) {
                 floatMap.setSamples(i, j, 0.0f, 0.0f);
            }
        } 
        displacementMap = new DisplacementMap();
        displacementMap.setMapData(floatMap);
        displacementMap.setWrap(true);
        whiteNoizeFilter.setEffect(displacementMap);
                
        Path path = Paths.get("data\\music\\German Military -3R- Hakenkreuz am Stahlhelm (Stahlhelmkapelle LV) -n15.mp3");
        String location = path.toUri().toString();
        Media media = new Media(location);
        player = new MediaPlayer(media);
        player.setAutoPlay(true);
        MediaView view = new MediaView(player);
        view.setOnError(e -> System.out.println(e));
        player.setVolume(0.2);    
        
        // Run animation
        blurRadius0 = -1;
        playPart1();        
        
        // primaryStage.show();
        // primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // primaryStage.setFullScreen(true);
    }
    
    private static void playPart1() {
        Random rand = new Random();
        // Initialize parameters:
        if(blurRadius0 == -1) {    
        // 1st cycle
            blurRadius0 = 6;
            shiftPositionX0 = 0;
            shiftPositionY0 = 0;
        } else {      
        // other cycles
            blurRadius0 = blurRadius1;
            shiftPositionX0 = shiftPositionX1;
            shiftPositionY0 = shiftPositionY1;
        }
        
        /*
        // Change studio logo on game logo
        if(currentImage.equals("image0") && 
                player.getCurrentTime().toSeconds() >= 1.8) { // find the best value        
            fullImage = new Image(filePath1);
            currentImage = "image1";
        }  
        */
        
        // Change studio logo on RFB logo
        if(currentImage.equals("image0") && 
                player.getCurrentTime().toSeconds() >= 1.8) { // find the best value        
            fullImage = new Image(filePath1);
            currentImage = "image1";
        } 
        
        // Change RFB logo on RB logo
        if(currentImage.equals("image1") && 
                player.getCurrentTime().toSeconds() >= (1.8 + 2.5)) { // find the best value        
            fullImage = new Image(filePath2);
            currentImage = "image2";
        } 
        
        // Change RB logo on STA logo
        if(currentImage.equals("image2") && 
                player.getCurrentTime().toSeconds() >= (1.8 + 2.5 * 2)) { // find the best value        
            fullImage = new Image(filePath3);
            currentImage = "image3";
        } 
        
        // Change STA logo on SA logo
        if(currentImage.equals("image3") && 
                player.getCurrentTime().toSeconds() >= (1.8 + 2.5 * 3)) { // find the best value        
            fullImage = new Image(filePath4);
            currentImage = "image4";
        } 
        
        // Change SA logo on "im" logo
        if(currentImage.equals("image4") && 
                player.getCurrentTime().toSeconds() >= (1.8 + 2.5 * 4)) { // find the best value        
            fullImage = new Image(filePath5);
            currentImage = "image5";
        } 
        
        // Change "im" logo on game logo
        if(currentImage.equals("image5") && 
                player.getCurrentTime().toSeconds() >= (1.8 + 2.5 * 4 + 1.3)) { // find the best value        
            fullImage = new Image(filePath6);
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            fullWidth = (int) fullImage.getWidth();
            fullHeight = (int) fullImage.getHeight();
            currentImage = "image6";
        } 
        
        blurRadius1 = (float) min(8, max(5, blurRadius0 + rand.nextGaussian()));
        shiftPositionX1 = rand.nextDouble();
        shiftPositionY1 = rand.nextDouble();
           
        // Renew atrifacts
        renewArtifacts();
                
        KeyValue pictureShift0 = new KeyValue(introFrame.backgroundProperty(), 
                introFrame.getBackground());
        KeyValue pictureBlur0 = new KeyValue(blur.radiusProperty(), blurRadius0);
        KeyValue shiftXFilter0 = new KeyValue(displacementMap.offsetXProperty(), 
                shiftPositionX0);
        KeyValue shiftYFilter0 = new KeyValue(displacementMap.offsetYProperty(), 
                shiftPositionY0);
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, pictureShift0, 
                pictureBlur0, shiftXFilter0, shiftYFilter0);
        
        KeyValue pictureShift1 = new KeyValue(introFrame.backgroundProperty(), 
                processImage(fullImage, shiftRange));     
        KeyValue shiftXFilter1 = new KeyValue(displacementMap.offsetXProperty(), 
                shiftPositionX1);
        KeyValue shiftYFilter1 = new KeyValue(displacementMap.offsetYProperty(), 
                shiftPositionY1);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.1), pictureShift1, 
                shiftXFilter1, shiftYFilter1);
        
        KeyValue pictureBlur1 = new KeyValue(blur.radiusProperty(), blurRadius1);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.2 / 2), pictureBlur1);
        
        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1, keyFrame2); 
        timeline.setOnFinished(e -> {
            // create new animation after this animation finishes
            if(player.getCurrentTime().toSeconds() < 15.7) playPart1(); // find the best value
            else playPart2();
        });
        timeline.play();
    }
    
    
    private static void playPart2() {
        /*
        // Set dark gray background
        introFrame.setBackground(new Background(new BackgroundFill(
                Color.color(51 / 256, 51 / 256, 51 / 256), new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0))));
        */
        
        // Set non-cropped background image
        introFrame.setBackground(processImage(fullImage, 0));
        
        /*
        ImageView logoImage = new ImageView(filePath6);
        introFrame.getChildren().add(logoImage);
        */
        
        // Remove artifacts:
        // a) full-height balck lines
        for(int i = 0; i < artifactLine.length; i++) {
            introFrame.getChildren().remove(artifactLine[i]);
        }
        // b) white polygons
        for(int i = 0; i < artifactPolygon.length; i++) {
            introFrame.getChildren().remove(artifactPolygon[i]);
        }
        // c) short white lines
        for(int i = 0; i < artifactShortLine.length; i++) {
            introFrame.getChildren().remove(artifactShortLine[i]);
        }
        
        KeyValue rootBrightness0 = new KeyValue(bw.brightnessProperty(), 
                bw.getBrightness());
        KeyValue filterOpacity0 = new KeyValue(whiteNoizeFilter.opacityProperty(), 
                whiteNoizeFilter.getOpacity());
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, rootBrightness0, 
                filterOpacity0);
        
        // Set zero opacity
        KeyValue rootBrightness1 = new KeyValue(bw.brightnessProperty(), 0);
        // Set default brightness
        KeyValue filterOpacity1 = new KeyValue(whiteNoizeFilter.opacityProperty(), 0);
        KeyValue pictureBW0 = new KeyValue(bw.saturationProperty(), -1);
        KeyValue pictureBlur0 = new KeyValue(blur.radiusProperty(), blurRadius1);
        KeyValue pictureWidth0 = new KeyValue(introFrame.maxWidthProperty(), 
                frameWidth);
        KeyValue pictureHeight0 = new KeyValue(introFrame.maxHeightProperty(), 
                frameHeight);
        KeyValue audioVolume0 = new KeyValue(player.volumeProperty(), 
                player.getVolume());
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(1), rootBrightness1, 
                filterOpacity1, pictureBW0, pictureBlur0, pictureWidth0, 
                pictureHeight0, audioVolume0);
                
        KeyValue pictureBW1 = new KeyValue(bw.saturationProperty(), 0);
        KeyValue pictureBlur1 = new KeyValue(blur.radiusProperty(), 0);
        KeyValue pictureWidth1 = new KeyValue(introFrame.maxWidthProperty(), 
                SCREEN_WIDTH);
        KeyValue pictureHeight1 = new KeyValue(introFrame.maxHeightProperty(), 
                SCREEN_HEIGHT);
        KeyValue audioVolume1 = new KeyValue(player.volumeProperty(), 0);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(4), pictureBW1, 
                pictureBlur1, pictureWidth1, pictureHeight1, audioVolume1);
          
        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1, keyFrame2); 
        timeline.setOnFinished(e -> playPart3());
        timeline.play();
    }
    
    private static void playPart3() {
        // Delete white noize filter
        introFrame.getChildren().remove(whiteNoizeFilter);
        // Stop player
        player.stop();
        
        scene.setFill(Color.WHITESMOKE);
        
        // Play old photo flash sound
        Path path = Paths.get("data\\sounds\\232130__werra__vintage-camera-flash-powder-and-shutter.wav");
        String location = path.toUri().toString();
        Media media = new Media(location);
        player = new MediaPlayer(media);
        player.setAutoPlay(true);
        
        KeyValue rootOpacity0 = new KeyValue(introFrame.opacityProperty(), 1);
        KeyValue rootOpacity1 = new KeyValue(introFrame.opacityProperty(), 0);
        KeyValue rootOpacity2 = new KeyValue(introFrame.opacityProperty(), 1);
        
        KeyValue changeBackground1 = new KeyValue(introFrame.backgroundProperty(), 
                processImage(new Image(filePath7), 0));
        KeyFrame keyFrame0 = new KeyFrame(Duration.ZERO, rootOpacity0);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.5), rootOpacity1, 
                changeBackground1);
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1.5), rootOpacity2);
        
        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1, keyFrame2); 
        timeline.setOnFinished(e -> {
                scene.setFill(Color.BLACK);
                player.stop();
                
                playPart4();
        });                
        timeline.play();        
    }
    
    private static void playPart4() {
        KeyValue paneOpacity0 = new KeyValue(root.opacityProperty(), 
                root.getOpacity());
        KeyFrame keyFrame0 = new KeyFrame(Duration.seconds(3), paneOpacity0);
        
        KeyValue paneOpacity1 = new KeyValue(root.opacityProperty(), 0);
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(5), paneOpacity1);
        
        Timeline timeline  = new Timeline(); 
        timeline.setCycleCount(1); 
        timeline.setAutoReverse(false); 
        timeline.getKeyFrames().addAll(keyFrame0, keyFrame1);  
        timeline.setOnFinished(e -> {
            // Exit intro
            CoreFX.processNext();
            // Platform.exit(); // ????????????????????????????????????????????????????? delete ????????????????????????
        });
        timeline.play();  
    }
    
    
    private static Background processImage(Image fullImage, int shiftRange) {
        Random rand = new Random();
        // Full-sized non-shifted image by default
        int valueX = 0;
        int valueY = 0;
        
        int shiftX = 0;
        int shiftY = 0;
        
        int cropWidth = fullWidth;
        int cropHeight = fullHeight;
        
        // Use random image shift and cropping only for non-zero shift range
        if(shiftRange > 0) {
            valueX = (fullWidth - frameWidth) / 2 - shiftRange;
            valueY = (fullHeight - frameHeight) / 2 - shiftRange;
            
            shiftX = (int) min(shiftRange, max(-shiftRange, 
                    (rand.nextGaussian() * 2)));            
            shiftY = (int) min(shiftRange, max(-shiftRange, 
                    (rand.nextGaussian() * 2)));
            
            cropWidth = frameWidth + shiftRange * 2;
            cropHeight = frameHeight + shiftRange * 2;
        } 
        
        // Create cropped & shifted image
        PixelReader pixelReader = fullImage.getPixelReader();
        WritableImage croppedImage = new WritableImage(pixelReader, 
                valueX + shiftX, valueY + shiftY, cropWidth, cropHeight);
                
        BackgroundImage backgroundImage= new BackgroundImage(croppedImage, 
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, 
                BackgroundSize.DEFAULT); 
        
        return new Background(backgroundImage); 
    }
    
    // Create white noize image
    private static WritableImage whiteNoize(int width, int height) {
        WritableImage whiteNoizeImage = new WritableImage(width, height);
        Random rand = new Random();
        double colorRed, colorGreen, colorBlue;
        PixelReader pixelReader = whiteNoizeImage.getPixelReader();
        PixelWriter pixelWriter = whiteNoizeImage.getPixelWriter();         
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                double randColor = sqrt(max(0, min(1, rand.nextGaussian() / 6 + 
                        0.5)));                  
                colorRed = randColor; 
                colorGreen = randColor;
                colorBlue = randColor;
                pixelWriter.setColor(i, j, Color.color(colorRed, 
                        colorGreen, colorBlue));
            }
        }
        return whiteNoizeImage;
    }
         
    // Create 3 types of artifacts
    private static void createAtrifacts() {        
        Random rand = new Random();
        
        // Create artifact lines
        artifactLine = new Line[7]; // ?????
        for(int i = 0; i < artifactLine.length; i++) {
            artifactLine[i] = new Line();
            artifactLine[i].setStartX(frameWidth * rand.nextFloat());
            artifactLine[i].setStartY(0);
            artifactLine[i].setEndX(artifactLine[i].getStartX());
            artifactLine[i].setEndY(frameHeight);         
        }        
        introFrame.getChildren().addAll(artifactLine);
        
        // Create artifact polygons
        artifactPolygon = new Polygon[5];
        for(int i = 0; i < artifactPolygon.length; i++) {            
            artifactPolygon[i] = new Polygon();
            artifactPolygon[i].setFill(Color.WHITESMOKE);
        }
        introFrame.getChildren().addAll(artifactPolygon);
        
        // Create artifact short lines
        artifactShortLine = new Line[5]; // ?????
        for(int i = 0; i < artifactShortLine.length; i++) {
            artifactShortLine[i] = new Line();
            artifactShortLine[i].setStroke(Color.WHITESMOKE);
        }        
        introFrame.getChildren().addAll(artifactShortLine);
        
    }
    
    // Renew 3 types of artifacts
    private static void renewArtifacts() {
        Random rand = new Random();
        
        // Full-height black lines
        double newPositionX;
        for(int i = 0; i < artifactLine.length; i++) {
            newPositionX = max(0, min(frameWidth, artifactLine[i].getStartX() +
                    rand.nextGaussian() * 10));
            artifactLine[i].setStartX(newPositionX);
            artifactLine[i].setEndX(newPositionX);
            artifactLine[i].setStrokeWidth(rand.nextDouble() / 2);
            artifactLine[i].setOpacity(rand.nextDouble() * 0.7);
        }  
        
        // White polygons
        for(int i = 0; i < artifactPolygon.length; i++) {
            if(abs(rand.nextGaussian()) > 1) {
                artifactPolygon[i].setVisible(true);
                double startX = frameWidth * rand.nextFloat();
                double startY = frameHeight * rand.nextFloat();

                artifactPolygon[i].getPoints().clear();
                artifactPolygon[i].getPoints().addAll(3 + startX + 
                        rand.nextFloat(), 1 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(4.5 + startX + 
                        rand.nextFloat(), 1.5 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(5 + startX + 
                        rand.nextFloat(), 3 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(4.5 + startX + 
                        rand.nextFloat(), 4.5 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(3 + startX + 
                        rand.nextFloat(), 5 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(1.5 + startX + 
                        rand.nextFloat(), 3.5 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(1 + startX + 
                        rand.nextFloat(), 3 + startY + rand.nextFloat());
                artifactPolygon[i].getPoints().addAll(1.5 + startX + 
                        rand.nextFloat(), 1.5 + startY + rand.nextFloat());
                
                artifactPolygon[i].setScaleX(abs(rand.nextGaussian()) * 2);
                artifactPolygon[i].setScaleY(abs(rand.nextGaussian()) * 2);
                
                artifactPolygon[i].setOpacity(abs(rand.nextGaussian()));
            } else {
                artifactPolygon[i].setVisible(false);
            }
        }
        
        // Short white lines
        for(int i = 0; i < artifactShortLine.length; i++) {            
            artifactShortLine[i].setStartX(frameWidth * rand.nextFloat());
            artifactShortLine[i].setStartY(frameHeight * rand.nextFloat());
            artifactShortLine[i].setEndX(artifactShortLine[i].getStartX());
            artifactShortLine[i].setEndY(min(frameHeight, 
                    artifactShortLine[i].getStartY() + 20 + 
                            10 * rand.nextGaussian()));
            artifactShortLine[i].setStrokeWidth(rand.nextDouble() * 2);
            artifactShortLine[i].setOpacity(rand.nextDouble() * 0.5);  
        }        
    }
}
