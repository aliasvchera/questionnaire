
package questionnaire.gui;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import static java.lang.Math.max;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import questionnaire.CoreFX;


/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class RightPaneProfileController {    
    // Right pane (profile) controls
    @FXML
    private Pane pProfile;
    
    @FXML
    private Label lTitle;
    
    @FXML
    private Label lName;
    
    @FXML
    private Label lNameText;
    
    @FXML
    private Line lineName;
    
    @FXML
    private Label lSurname;
    
    @FXML
    private Label lSurnameText;
    
    @FXML
    private Line lineSurname;
    
    @FXML
    private Label lYearOfBirth;
    
    @FXML
    private Label lYearOfBirthText;
    
    @FXML
    private Line lineYearOfBirth;
    
    @FXML
    private Label lOccupation;
    
    @FXML
    private Label lOccupationText;
    
    @FXML
    private Line lineOccupation;
    
    @FXML
    private Label lPoliticalViews;
    
    @FXML
    private Label lPoliticalViewsText;
    
    @FXML
    private Line linePoliticalViews;
    
    @FXML
    private Label lKampfbund;
    
    @FXML
    private Label lKampfbundText;
    
    @FXML
    private Line lineKampfbund;
    
    @FXML
    private Label lKampfbundRank;
    
    @FXML
    private Label lKampfbundRankText;
    
    @FXML
    private Line lineKampfbundRank;
    
    @FXML
    private Label lCV;
    
    @FXML
    private Label lCVText;
    
    @FXML
    private Line lineCV00;
    
    @FXML
    private Line lineCV01;
    
    @FXML
    private Line lineCV02;
    
    @FXML
    private Line lineCV03;
    
    @FXML
    private Line lineCV04;
    
    @FXML
    private Line lineCV05;
    
    @FXML
    ImageView ivPortrait;
    
    @FXML
    ImageView ivPhotoFrame;
    
    @FXML
    ImageView ivStampPhoto;
    
    @FXML
    private Rectangle rPhotoFrame;
    
    @FXML
    private Label lStamp;
    
    @FXML
    private ImageView ivStampBottom;
    
    @FXML
    private Label lSignature;
    
    @FXML
    private ImageView ivSignature;
    
    // Semi-transparent stamp layer
    private ImageView ivStampPhotoSemi;
    
    // Link on CoreFX
    private CoreFX coreFX;
    
    
    public RightPaneProfileController() {
        
    }
    
    @FXML
    private void initialize() {
        // Create & add semi-transparent stamp layer
        ivStampPhotoSemi = new ImageView();        
        ivStampPhotoSemi.setOpacity(0.5);
        ivStampPhotoSemi.setPreserveRatio(ivStampPhoto.isPreserveRatio());
        ivStampPhotoSemi.setSmooth(ivStampPhoto.isSmooth());
        ivStampPhotoSemi.setLayoutX(ivStampPhoto.getLayoutX());
        ivStampPhotoSemi.setLayoutY(ivStampPhoto.getLayoutY());
        ivStampPhotoSemi.setFitWidth(ivStampPhoto.getFitWidth());
        ivStampPhotoSemi.setFitHeight(ivStampPhoto.getFitHeight());
        ivStampPhotoSemi.setRotate(ivStampPhoto.getRotate());
        pProfile.getChildren().add(ivStampPhotoSemi);
    }
    
    /**
     * Called by CoreFX that gives a link on itself
     * 
     * @param coreFX
     */
    public void setCoreFX(CoreFX coreFX) {
        this.coreFX = coreFX;
        
        lTitle.setText("ДОСЬЕ");
        // Name
        lName.setText("Имя:");
        lNameText.setText(coreFX.getPersonalityData().name);
        // lNameText.setText("Курт"); // tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Surname
        lSurname.setText("Фамилия:");
        lSurnameText.setText(coreFX.getPersonalityData().surname);
        // lSurnameText.setText("Шумахер"); // tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Year of birth
        lYearOfBirth.setText("Год рождения:");
        lYearOfBirthText.setText(coreFX.getPersonalityData().yearOfBirth);
        // lYearOfBirthText.setText("1898"); // tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Occupation
        lOccupation.setText("Род занятий:");
        // lOccupationText.setText("служащий"); /// ?????????????????????????????????????
        lOccupationText.setText(coreFX.getCVData().getProfession());
        // Political views
        lPoliticalViews.setText("Политические взгляды:");
        lPoliticalViewsText.setText(
                coreFX.getPoliticalData().currentPoliticalViews);
        // lPoliticalViewsText.setText("убеждённый правый социал-демократ"); // tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Kampfbund membership
        lKampfbund.setText("Принадлежность к политической организации:");
        // lKampfbundText.setText("Стальной шлем, Союз солдат-фронтовиков");
        lKampfbundText.setText(coreFX.getKampfbundData().currentName);
        // lKampfbundText.setText("Союз имперского флага (Рейхсбаннер)"); // tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Kampfbund rank
        lKampfbundRank.setText("Должность/звание:");
        //lKampfbundRankText.setText("верманн (рядовой)");
        lKampfbundRankText.setText(coreFX.getKampfbundData().currentRank);
        // lKampfbundRankText.setText("манн (рядовой)"); // tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Other CV
        lCV.setText("Прочее:");
        /* lCVText.setText("ветеран мировой войны и фрайкоров; " + 
                "воевал против польских инсургентов в Верхней Силезии в 1921 году; " + 
                "по имеющимся сведениям, совершал диверсии против оккупантов в Руре в 1923 году; " + 
                "активный участник уличных столкновений с политическими противниками"); // ?????????????????????????????????????????????
        */
        lCVText.setText(coreFX.getCVData().getCVText());
         
        
        lStamp.setText("Печать:");
        
        lSignature.setText("Подпись:");
        
        // Graphics:
        // a. stamps
        ivStampPhoto.setImage(new Image("file:data/graphics/seal.png"));
        ivStampPhotoSemi.setImage(ivStampPhoto.getImage());
        ivStampBottom.setImage(ivStampPhoto.getImage());
        
        // b. signature
        ivSignature.setImage(new Image("file:data/graphics/sign.png"));
        
        // c. portrait
        if(coreFX.getPersonalityData().portrait != null) {  
System.out.println(coreFX.getPersonalityData().portrait);
        // set portrait image & frame
            ivPortrait.setImage(new Image("file:data/portraits/" + 
                    coreFX.getPersonalityData().portrait)); 
                    // "017.jpg"));  tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            ivPhotoFrame.setImage(new Image("file:data/graphics/user200.png"));            
        } else {
System.out.println("Personality is not defyned yet!");
        // remove portrait image & frame
            ivPortrait.setImage(null);
            ivPhotoFrame.setImage(null);
            
            // hide signature & stamps
            ivStampPhoto.setVisible(false);
            ivStampPhotoSemi.setVisible(false);
            ivStampBottom.setVisible(false);
            ivSignature.setVisible(false);
            
            // set no text for info labels
            lPoliticalViewsText.setText("");
        }
    }
    
    
    /**
     * Placing and sizing controls for optimal space usage and good look.
     * Item labels ("Name", "Surname" etc) have maximal priority.
     * 
     */
    public void fitControls() {   
System.out.println("Right pane: fit controls");
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();     
        
        // Fit upper (to left of photo frame) part of CV
        Label[] labels = 
            {lName, lSurname, lYearOfBirth, lOccupation};
        Label[] labelTexts = 
            {lNameText, lSurnameText, lYearOfBirthText, lOccupationText};
        Line[] labelLines = 
            {lineName, lineSurname, lineYearOfBirth, lineOccupation};        
        for(int i = 0; i < labels.length; i++) {            
            labelTexts[i].setLayoutX(labels[i].getLayoutX() + 
                    labels[i].prefWidth(-1) + 10);
            labelTexts[i].setPrefWidth(rPhotoFrame.getLayoutX() - 
                    labelTexts[i].getLayoutX() - 30);
            labelLines[i].setLayoutX(labelTexts[i].getLayoutX());
            labelLines[i].setEndX(labelTexts[i].getPrefWidth());
        }        
                
        // Fit middle & lower part of CV        
        Label[] labelsMiddle = 
            {lPoliticalViews, lKampfbund, lKampfbundRank, lCV};
        boolean[] wrapLabelsMiddle = {false, true, false, false};
        Label[] labelTextsMiddle = 
            {lPoliticalViewsText, lKampfbundText, lKampfbundRankText, lCVText};
        Line[] labelLinesMiddle = 
            {linePoliticalViews, lineKampfbund, lineKampfbundRank, lineCV00};           
        for(int i = 0; i < labelsMiddle.length; i++) {
            // If CV item labels must be wrapped (placed on 2+ strings) 
            if(wrapLabelsMiddle[i]){
                double maxWordWidth = 0;
                String[] labelWords = labelsMiddle[i].getText().split(" ");
                for(String labelWord : labelWords) {
                    maxWordWidth = max(maxWordWidth, 
                            fontLoader.computeStringWidth(labelWord, 
                            labelsMiddle[i].getFont()));
                }
                // Set pref width = the longest word width
                labelsMiddle[i].setPrefWidth(maxWordWidth);
            }
            labelTextsMiddle[i].setLayoutX(labelsMiddle[i].getLayoutX() + 
                    labelsMiddle[i].prefWidth(-1) + 10);
            labelTextsMiddle[i].setPrefWidth(lTitle.getLayoutX() + 
                    lTitle.getPrefWidth() - labelTextsMiddle[i].getLayoutX());
            labelLinesMiddle[i].setLayoutX(labelTextsMiddle[i].getLayoutX());
            labelLinesMiddle[i].setEndX(labelTextsMiddle[i].getPrefWidth());
        }
        
        // Fit all CV ('other') lines to first line
        Line[] labelLinesCV = {lineCV01, lineCV02, lineCV03, lineCV04, lineCV05};
        for(Line lineCV : labelLinesCV) {
            lineCV.setLayoutX(lineCV00.getLayoutX());
            lineCV.setEndX(lineCV00.getEndX());
        }
        
        // Fit signature
        ivSignature.setFitWidth(ivSignature.getImage().getWidth());
        ivSignature.setFitHeight(ivSignature.getImage().getHeight());
        
        // Fit photo frame to photo portrait
        Image photo = ivPortrait.getImage(); 
        // If only photo portrait has photo
        if(photo != null) {
            double leftFrame = 2;
            double rightFrame = 4;
            double topFrame = 2;
            double bottomFrame = 4;
            double imageWidth, imageHeight, verticalRatio, horizontalRatio;        
            if(photo.getWidth() / photo.getHeight() > 
                    ivPortrait.getFitWidth() / ivPortrait.getFitHeight()) {
                imageWidth = ivPortrait.getFitWidth();
                imageHeight = imageWidth / (photo.getWidth() / photo.getHeight());            
            } else {
                imageHeight = ivPortrait.getFitHeight();
                imageWidth = imageHeight * (photo.getWidth() / photo.getHeight()); 
            }
            // Define ratios "fit portrait size vs inner frame image size"
            verticalRatio = imageHeight / (ivPhotoFrame.getImage().getHeight()
                    - (topFrame - 1) - (bottomFrame - 1)); 
            horizontalRatio = imageWidth / (ivPhotoFrame.getImage().getWidth()
                    - (leftFrame - 1) - (rightFrame - 1));
            // Set X,Y layout & fit width, height
            ivPhotoFrame.setLayoutX(ivPortrait.getLayoutX() - 
                    leftFrame * verticalRatio + 1);
            ivPhotoFrame.setLayoutY(ivPortrait.getLayoutY() - 
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
    }
}
