/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.engine;

import java.util.Random;
import questionnaire.CoreFX;
import questionnaire.files.DataInTXT;
import questionnaire.files.messages.*;
import questionnaire.files.parameters.*;
import questionnaire.gui.GUIBuilder;
import static questionnaire.gui.GUIBuilder.constructMainDesk;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class DeusExMachina {
    static boolean randomMessage;
    static CurrentMoment currentMoment;
    static Kampfbund kampfbund;
    static Political political;
    static CurriculumVitae curriculumVitae;
    static Message currentMessage = null;
    static String keyText = null;
    static String messageType = null;
    
    public static void renewDataLinks() {
        currentMoment = CoreFX.getInstance().getCurrentMomentData();
        kampfbund = CoreFX.getInstance().getKampfbundData();
        political = CoreFX.getInstance().getPoliticalData();
        curriculumVitae = CoreFX.getInstance().getCVData();
    }
    
    /**
     * Main switcher-controller for game moments (message showing, personality 
     * defining etc)
     */
    public static void switcher() {
        // Clear variables
        keyText = null;
        messageType = null;
        
        System.out.println("Current moment: " + currentMoment.gameMoment); // !!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // Process current message (actual on the moment of last game exiting)
        // ... >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // return;
        
        // Process triggers
        int currentTrigger = currentMoment.triggerName.size() - 1;
        /* 
        if trigger game moment value == maximal value => delete the trigger  
        and process next in list
        */ 
        if(currentTrigger >= 0 && 
                currentMoment.triggerGameMoment.get(currentTrigger) >= 5) {
            System.out.println("switcher: remove trigger"); // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            currentMoment.triggerName.remove(currentTrigger);
            currentMoment.triggerGameMoment.remove(currentTrigger);
            currentTrigger--;
        }
        
        if(currentTrigger >= 0) {
            // ...
            keyText = currentMoment.triggerName.get(currentTrigger);
            messageType = "Trigger";
            System.out.println("switcher: process trigger: " + currentTrigger + "/" +
                    keyText + "/" + currentMoment.triggerGameMoment.get(currentTrigger)); // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            nextCycle(keyText, messageType, 
                    currentMoment.triggerGameMoment.get(currentTrigger));            
        } else {
            System.out.println("switcher: date"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            /* 
            If all game moments for current date & triggers are passed/realized
            start new month        
            */
            // Stop if game over
            // if(currentMoment.gameOver()) return; // ????????????????????????????? delete ????????????????????????
            // Start new month if cycle is passed
            if(currentMoment.gameMoment >= 5) {
                nextMonth();
            } else {
                System.out.println("switcher: process date");  // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // "Date" cycle
                keyText = currentMoment.currentYear + "-";
                messageType = "Date";
                if(currentMoment.currentMonth < 10) keyText += "0";
                keyText += currentMoment.currentMonth;
                // Run stack of events, questions etc
                nextCycle(keyText, messageType, currentMoment.gameMoment); 
            }
        }        
    }
    
    
    static void nextMonth() {        
        Random rnd = new Random();
        /* Allow / not allow to find random message ("period" message) for
            current time period */
        randomMessage = (rnd.nextInt(3) == 2); // ??? change by variable ?????????
        // System.out.println("randomMessage = " + randomMessage + "/" + rnd.nextInt(3));
        
        // Start next cycle from video/movie (if exists)
        currentMoment.gameMoment = 1;
        // Add new month & new year in case of January
        if(++currentMoment.currentMonth > 12) {
            currentMoment.currentMonth -= 12;
            currentMoment.currentYear += 1;
        }
        
        // Show loadscreen (if not showed yet) & renew date label on loadscreen
        System.out.println(currentMoment.currentYear + "/" + currentMoment.currentMonth);  // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!
        GUIBuilder.showLoadscreen(currentMoment.getDateText());
    }

    
    static void changeGameMoment(int gameMoment) {
        if(messageType.equals("Trigger")) {
            currentMoment.triggerGameMoment.set(
                    currentMoment.triggerName.size() - 1, (byte) gameMoment);
        } else {
            currentMoment.gameMoment = (byte) gameMoment; // ??? byte ???
            System.out.println("Change current moment to : " + currentMoment.gameMoment); // !!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
    }
    
    /**
     * Change game moment after 'Next' clicked on message form
     */
    public static void changeGameMoment() {
        int gameMoment;
        if(messageType.equals("Trigger")) {
            gameMoment = currentMoment.triggerGameMoment.get(
                    currentMoment.triggerName.size() - 1);
        } else {
            gameMoment = currentMoment.gameMoment;
        }
        System.out.println("Change game moment after 'Next' clicked"); // !!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!
        changeGameMoment(++gameMoment);
    }
    
    
    static void nextCycle(String keyText, String messageType, int gameMoment) {
        // System.out.println("Next cycle - current moment: " + gameMoment);  // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                
        // new game: show book cover
        if(gameMoment <= -2) {
            gameMoment = -2;
            changeGameMoment(gameMoment);
            // Show book cover
            System.out.println("Book cover");
            GUIBuilder.showMainDesk("book_cover");
            return;
        }

        // Remarque quote aka "1914-1918"
        if(gameMoment <= -1) {
            gameMoment = -1;
            changeGameMoment(gameMoment);
            currentMessage = cycleItem("eventStory_Trigger_epigraph");
            if(currentMessage != null) {
                System.out.println("1914-1918 epigraph");
                CoreFX.messageView = 
                        currentMessage.getMessageView(currentMoment.getDateText());
                GUIBuilder.showMainDesk("message");
            } else {
                System.err.println("1914-1918 epigraph was not found!!!");
            }
            return;
        }

        // Define name, date of birth etc
        if(gameMoment <= 0) {
            gameMoment = 0;
            changeGameMoment(gameMoment);
            //...
            System.out.println("Personality defining");
            GUIBuilder.showMainDesk("personality");
            return;
        }

        if(gameMoment <= 1) {
            gameMoment = 1;
            changeGameMoment(gameMoment);
            // find appropriate movie
            //...
            /* while ((trigger = this.movieShow(???)) != null) {
                    // ???;
            } */
        }
        if(gameMoment <= 2) {
            gameMoment = 2;
            changeGameMoment(gameMoment);
            // find appropriate event story
            currentMessage = 
                    cycleItem("eventStory_" + messageType + "_" + keyText);
            if(currentMessage != null) {
                CoreFX.messageView = 
                        currentMessage.getMessageView(currentMoment.getDateText());
                GUIBuilder.showMainDesk("message");
                return;
            }
        }
        if(gameMoment <= 3) {
            gameMoment = 3;
            changeGameMoment(gameMoment);
            // find appropriate event message
            currentMessage = 
                    cycleItem("eventMessage_" + messageType + "_" + keyText);
            if(currentMessage != null) {
                CoreFX.messageView = 
                        currentMessage.getMessageView(currentMoment.getDateText());
                GUIBuilder.showMainDesk("message");
                return;
            }
        }
        if(gameMoment <= 4) {
            gameMoment = 4;
            changeGameMoment(gameMoment);
            // find appropriate question
            currentMessage = 
                    cycleItem("question_" + messageType + "_" + keyText);
            if(currentMessage != null) {
                // System.out.println("question was found!"); // !!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                CoreFX.messageView = 
                        currentMessage.getMessageView(currentMoment.getDateText());
                GUIBuilder.showMainDesk("message");
                return;
            }
        }
        if(gameMoment <= 5) {
            gameMoment = 5;
            changeGameMoment(gameMoment);
            // end cycle & re-start switcher
            switcher();
        }
    }

    static Message cycleItem(String keyText) {
    // Find & process appropriate message (event/question)
        currentMessage = null;
        
        if(currentMoment.currentMessage != null && 
                currentMoment.currentMessage[0] != null && 
                currentMoment.currentMessage[1] != null) { 
        // In case of previously interrupted (and saved) game continuing
            String messagePath = currentMoment.currentMessage[0] + "\\" + 
                    currentMoment.currentMessage[1];            
            if(keyText.startsWith("event")) {
                currentMessage = new Event(messagePath, keyText);
            } else if(keyText.startsWith("question")) {
                currentMessage = new Question(messagePath, keyText);
            }
            
        } else {
        // Search event/question by keyText 
            /* Get filename with path (e.g.
            "\\event\\message\\date\\1932-06_Preussenschlag.txt") for specific
            key (e.g. "eventStory_Date_1932-06") */
            // System.out.println("KeyText: " + keyText); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            currentMessage = findMessage(keyText);
            
            // System.out.println("currentMessage = " + currentMessage); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            // System.out.println("keyText.contains(\"_Date_\") = " + keyText.contains("_Date_")); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            // System.out.println("randomMessage = " + randomMessage); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            if(currentMessage == null && keyText.contains("_Date_") && 
                    randomMessage) {
            /* If there is no message for the date and random message is allowed
            try to find "period" message (with not strict date) */       
                System.out.println("Try to find period message..."); // !!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!
                keyText = keyText.replaceFirst("_Date_", "_Period_");
                currentMessage = findMessage(keyText);
            }
        }               
        
        // Return message searching result
        if(currentMessage != null) { 
            // System.out.println("cycleItem return: not null!"); // !!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!
            return currentMessage; 
        } else {          
            // System.out.println("cycleItem return: null!"); // !!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!
            return null;
        }        
    }
    
    static Message findMessage(String keyText) {
    // find message obeying requirements (parameter values)    
        // System.out.println("Find message: step 1"); // !!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!
        String messageFolder = CoreFX.DATA_DIR + MessageDB.getFolderPath(keyText);
        String[] messageList = MessageDB.getMessageFile(keyText); 
        Message currentMessage = null;
        // System.out.println("Find message: step 2");  // !!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!

        if(messageList != null) {
            // System.out.println("Find message: step 3");  // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!
            String activist = "";
            if(political.activist) activist = "activist";
            String death = "";
            if(currentMoment.isDead) death = "death";

            for(String msg : messageList) {
                // System.out.println("Find message: step 4");  // !!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!
                if(keyText.startsWith("event")) {
                    // System.out.println("Find message: step 4.1 " + msg);  // !!!!!!!! tmp !!!!!!!!!!!!!!!!               
                    currentMessage = new Event(messageFolder + msg, keyText);
                } else if(keyText.startsWith("question")) {                    
                    currentMessage = new Question(messageFolder + msg, keyText);
                } else {
                    // ???????????????????????????????????????????????????????????????
                }
                
                // System.out.println("Find message: step 5"); // !!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!          
                if(currentMessage.checkKey(keyText) && 
                        currentMessage.obeyConditions(msg, activist, 
                        political.politicalViewsList(), 
                        kampfbund.currentShortName, death, 
                        curriculumVitae.getStatusList())) {
                    // System.out.println("Find message: obey conditions = true"); // !!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!
                    
                    // Renew current moment variable for message (current message)
                    currentMoment.currentMessage[0] = messageFolder; 
                    currentMoment.currentMessage[1] = msg;
                    DataInTXT.writeInLog("Current message: " + messageFolder + msg); // log
                    // +1 to message usage statistics
                    MessageDB.addMessageUsageStatistics(msg);
                    break;
                } else {
                    currentMessage = null;
                    // System.out.println("Find message: obey conditions = false"); // !!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!
                }
            }
        }
        // System.out.println("Find message: ");   // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // if(currentMessage == null) System.out.println("Find message: null!!!");   // !!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return currentMessage;
    }
    
    /**
     * Process selected answer: political/kampfbund effects, triggers etc.
     * @param selectedAnswer 
     */
    public static void processMessageEffects(byte selectedAnswer) {
        String newTrigger = null;        
        // System.out.println("processMessageEffect - step1"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if(selectedAnswer == -1) {
        // Process events
            newTrigger = currentMessage.getTrigger((byte) 0);
        } else {
        // Process questions
            newTrigger = currentMessage.getTrigger(selectedAnswer);
            // Other effects
            political.updateActivism(
                    currentMessage.getPoliticalEffects(selectedAnswer), 
                    currentMessage.getPoliticalEffects());
            kampfbund.updateReputation(
                    currentMessage.getKampfbundEffects(selectedAnswer));
            kampfbund.updateMembership(
                    currentMessage.getKampfbundMembershipEffects(selectedAnswer));
            curriculumVitae.updateStatus(
                    currentMessage.getStatusEffects(selectedAnswer));
            // occasion
            // custody
            currentMoment.isDead = 
                    currentMessage.getMortalityEffects(selectedAnswer);
        }
        
        // Add new trigger if exists
        if(newTrigger != null && !newTrigger.equals("")) {
            currentMoment.triggerName.add(newTrigger);
            currentMoment.triggerGameMoment.add((byte) 1);
        }
        
        currentMessage = null;
    }
}
