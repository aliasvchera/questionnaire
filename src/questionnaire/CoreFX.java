/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire;

import javafx.application.Application;
import javafx.stage.Stage;

import questionnaire.engine.*;
import questionnaire.files.DataInTXT;
import questionnaire.files.messages.*;
import questionnaire.files.parameters.*;
import questionnaire.gui.*;


/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class CoreFX extends Application {
    
    // Initialize parameters
    static CurrentMoment currentMoment
            = new CurrentMoment("current_moment");
    static Personality personality
            = new Personality("personality");
    static CurriculumVitae curriculumVitae
            = new CurriculumVitae("curriculum_vitae");
    static Kampfbund kampfbund
            = new Kampfbund("kampfbund");
    static Political political
            = new Political("political");
    
    public static final String GAME_DIR = System.getProperty("user.dir");
    public static final String DATA_DIR = GAME_DIR + "\\data";
    
    
    private static CoreFX instance;
    
    public static MessageView messageView;
    
    public CoreFX() {
        instance = this;
        
        // Renew links in engine class
        DeusExMachina.renewDataLinks();
                
        System.out.println(currentMoment.currentMonth + "/" + currentMoment.currentYear);
        DataInTXT.writeInLog(currentMoment.currentMonth + "/" + currentMoment.currentYear);
        
        /* Create or re-create DB with all message (events & questions) names
            marked by specific keys */
        MessageDB.renewMessageDB(DATA_DIR);

        // Update statistics of message usage (number of times)
        MessageDB.importMessageUsageStatistics(DATA_DIR);
    }
    
   
    @Override
    public void init() {
        
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Create root pane and run game moment switcher
        GUIBuilder.constructRootPane(this, primaryStage);        
    }
        
    
    public static CoreFX getInstance() {
        return instance;
    }
    
    public MessageView getMessageViewData() {
       return messageView; 
    }
    
    public Personality getPersonalityData() {
        return personality;
    }
        
    public Political getPoliticalData() {
        return political;
    }
    
    public Kampfbund getKampfbundData() {
        return kampfbund;
    }
    
    public CurriculumVitae getCVData() {
        return curriculumVitae;
    }
    
    public CurrentMoment getCurrentMomentData() {
        return currentMoment;
    }
    
    /**
     * Click 'Start' on book cover pane
     */
    public void startNewGame() {
        // add +1 to current game moment (trigger or date)
        DeusExMachina.changeGameMoment();
        
        // Make message pane transparent (animation) => black screen
        GUIBuilder.processTransition("processNext", false);
    }
    
    /** 
     * Get name(s), surname, year of birth and nationality from GUI controller
     * @param portraitName
     * @param name
     * @param surname
     * @param yearOfBirth
     * @param nationality 
     */
    public void getPersonalityInfoFromController(String portraitName, 
            String name, String surname, String yearOfBirth, String nationality) {
        // add +1 to current game moment (trigger or date)
        DeusExMachina.changeGameMoment();
        
        personality.portrait = portraitName;
        personality.name = name;
        personality.surname = surname;
        personality.yearOfBirth = yearOfBirth;
        System.out.println("Portrait: " + personality.portrait + "\n" +
                "Name: " + personality.name + "\n" + 
                "Surname: " + personality.surname + "\n" + 
                "Year of birth: " + personality.yearOfBirth);
                
        // Make message pane transparent (animation) => black screen
        GUIBuilder.processTransition("processNext", false);
    }
    
    /**
     * Get selected answer (-1 = event with no answer) from GUI controller
     * @param selectedAnswer 
     */
    public void getAnswerFromController(byte selectedAnswer) {
        // add +1 to current game moment (trigger or date)
        DeusExMachina.changeGameMoment();
        System.out.println("Selected answer: " + selectedAnswer); // !!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!
        DeusExMachina.processMessageEffects((byte) selectedAnswer);
        
        // Clear current message
        currentMoment.currentMessage[0] = null; 
        currentMoment.currentMessage[1] = null;
        
        // Make message pane transparent (animation) => black screen
        GUIBuilder.processTransition("processNext", false);
    }
    
    /**
     * 
     */
    public static void processNext() {
        DeusExMachina.switcher();
    }
    
    public static void newGame() {
        System.out.println("New game - step2");
        // set starting parameters:
        // a. delete all files with parameter variables
        //... ?????????????????????????????????????????????????????????????????????????
        
        // b. re-initialize parameter variables
        currentMoment.renewVariables();
        personality.renewVariables();
        curriculumVitae.renewVariables();
        kampfbund.renewVariables();
        political.renewVariables();        
        MessageDB.renewMessageUsageStatistics();
        
        // c. Re-start from book cover
        /* Make message pane transparent (animation) => black screen & show 
           loadscreen
        */
        GUIBuilder.processTransition("showLoadscreen", false);
    }
    
    /**
     * Save game before exit (only if 'Exit' is clicked)
     */
    public static void saveGame() {        
        // Save current data
        currentMoment.saveVariables("current_moment", false);
        personality.saveVariables("personality", false);
        political.saveVariables("political", false);
        kampfbund.saveVariables("kampfbund", false);
        curriculumVitae.saveVariables("curriculum_vitae", false);
        
        MessageDB.saveMessageUsageStatistics(DATA_DIR);
    }
    
    /**
     * Exit game: save data & close application
     */        
    @Override
    public void stop() {                 
        System.out.println("Exit game - step2");
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
    
}