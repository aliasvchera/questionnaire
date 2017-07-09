
package questionnaire.files.messages;

import questionnaire.files.DataInTXT;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class Message {
// Superclass for events & questions    
    String[][] dataSplit;
    
    MessageItem conditionTrigger;
    MessageItem conditionDate;
    MessageItem conditionPeriod; // e.g. {"1920-01", "1921-01", "y"}
    // MessageItem conditionOccasion;
    MessageItem conditionStatus;
    MessageItem conditionKampfbund;
    MessageItem conditionPolitical;
    MessageItem conditionActivism;    
    
    MessageItem usedMax;
    
    MessageItem captionText;
    MessageItem dateText;
    MessageItem mainText;
    MessageItem pictureName;
    
    // MessageItem effectMoney;
    // MessageItem effectJob;
    MessageItem effectStatus;
    MessageItem effectTrigger;
    
    Message(String sourcePath, String keyText) {
        System.out.println("sourcePath: " + sourcePath); // >>>>>>>>>>>>>>>>>>>>> tmp >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        dataSplit = DataInTXT.readTXT(sourcePath);
        
        conditionTrigger = new MessageItem(dataSplit, "conditionTrigger");
        conditionDate = new MessageItem(dataSplit, "conditiondDate");
        conditionPeriod = new MessageItem(dataSplit, "conditionPeriod");
        // conditionOccasion = new MessageItem(dataSplit, "conditionOccasion");
        conditionStatus = new MessageItem(dataSplit, "conditionStatus");
        conditionKampfbund = new MessageItem(dataSplit, "conditionKampfbund");
        conditionPolitical = new MessageItem(dataSplit, "conditionPolitical");
        conditionActivism = new MessageItem(dataSplit, "conditionActivism");
                
        usedMax = new MessageItem(dataSplit, "usedMax");
        
        captionText = new MessageItem(dataSplit, "captionText");
        dateText = new MessageItem(dataSplit, "dateText");
        mainText = new MessageItem(dataSplit, "mainText");
        pictureName = new MessageItem(dataSplit, "pictureName");
        
        // effectMoney = new MessageItem(dataSplit, "effectMoney");
        // effectJob = new MessageItem(dataSplit, "effectJob");
        effectStatus = new MessageItem(dataSplit, "effectStatus");
        effectTrigger = new MessageItem(dataSplit, "effectTrigger");
    }
    
    public boolean checkKey(String keyText) {
    // Check specific condition (Date, Period, Trigger)
        System.out.println("check key - step0: " + keyText); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<<<<  
        boolean conditionResult = false;
        int first_, second_;
        /* Define position of delimiter symbols "_", e.g. in 
           "eventStory_Date_1932-06" first_ = 11, second_ = 16 */
        first_ = keyText.indexOf("_");
        second_ = keyText.indexOf("_", first_ + 1);
        System.out.println("check key - step1: " + keyText.substring(first_ + 1, second_)); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<<<<  
        // Check condition depending to message type (Date, Period, Trigger)
        switch(keyText.substring(first_ + 1, second_)) {
            case "Date":
                conditionResult = 
                        conditionDate.item[0].equals(keyText.substring(second_ 
                                + 1));
                break;
            case "Period":                
                // Period date1
                String[] auxDate = conditionPeriod.item[0].split("-");                
                int date1 = Integer.parseInt(auxDate[0]) * 12 + 
                        Integer.parseInt(auxDate[1]);
                // Period date2
                String[] auxDate1 = conditionPeriod.item[1].split("-");
                int date2 = Integer.parseInt(auxDate1[0]) * 12 + 
                        Integer.parseInt(auxDate1[1]);
                // Actual date
                auxDate1 = (keyText.substring(second_ + 1)).split("-");
                int date0 = Integer.parseInt(auxDate1[0]) * 12 + 
                        Integer.parseInt(auxDate1[1]);
                // If actual date is within period limits
                conditionResult = (date1 <= date0 && date0 <= date2);
                if(conditionPeriod.item[2].equals("y")) {
                /* For monthly events check month (month of actual date must be
                   equal to that of period date1) */
                    conditionResult = (Integer.parseInt(auxDate[1]) == 
                            Integer.parseInt(auxDate1[1]));
                }
                break;
            case "Trigger":
                System.out.println("check key - step1.1: Trigger"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<<<<
                conditionResult = 
                        conditionTrigger.item[0].equals(keyText.substring(second_ 
                                + 1));
                break;
        }
        System.out.println("check key - step2: " + conditionResult); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<<<<        
        return conditionResult;
    }
    
    
    /**
     * Check event condition compliance with actual parameter values 
     * @param fileName
     * @param activismName
     * @param politicalName
     * @param kampfbundName
     * @param deadOrAliveName
     * @param statusName
     * @return 
     */
    public boolean obeyConditions(String fileName, String activismName, String[] politicalName,  
            String kampfbundName, String deadOrAliveName, String[] statusName) {
        byte usageStatistics = 0;
        byte activismIs = 0;
        byte activismIsNot = 1;        
        byte kampfbundIs = 0;
        byte kampfbundIsNot = 1;
        byte politicalIs = 0;
        byte politicalIsNot = 1;
        byte statusIs = 0;
        byte statusIsNot = 1;
        
        // Check usage statistics vs maximal usage limit
        if(MessageDB.checkMessageUsageStatistics(fileName, 
                Byte.parseByte(usedMax.item[0]))) {
            usageStatistics = 1;
        }
        
        // ??????????????????????????????????????????????????????????????????????????? check ??????????????????
        if (conditionActivism.item != null && 
                !conditionActivism.item[0].trim().equals("")) {            
            if (conditionActivism.item[0].trim().equals(activismName + "+")) 
                activismIs = 1;
            if (conditionActivism.item[0].trim().equals(activismName + "-")) 
                activismIsNot = 0;            
        } else {
            activismIs = 1;
        }        
        
        if (conditionKampfbund.item != null && 
                !conditionKampfbund.item[0].trim().equals("")) {
            String[] conditionKampfbundArray = 
                    conditionKampfbund.item[0].trim().split(" ");
            // auxiliary variable: if there is any "+" membership condition
            boolean auxKampfbundIs = false;
            for(byte i = 0; i < conditionKampfbundArray.length; i++) {
                if(conditionKampfbundArray[i].endsWith("+")) 
                    auxKampfbundIs = true;
                if (conditionKampfbundArray[i].equals(kampfbundName + "+")) 
                    kampfbundIs = 1;
                if (conditionKampfbundArray[i].equals(kampfbundName + "-")) 
                    kampfbundIsNot = 0;
            }
            // If there are not any "+" membership condition
            if(!auxKampfbundIs) kampfbundIs = 1;
        } else {
            kampfbundIs = 1;
        }

        if(conditionPolitical.item != null && 
                !conditionPolitical.item[0].trim().equals("") && 
                politicalName != null) {
            String[] conditionPoliticalArray = 
                    conditionPolitical.item[0].trim().split(" ");
            // auxiliary variable: if there is any "+" condition
            boolean auxPoliticalIs = false;
            for(byte i = 0; i < politicalName.length; i++) {
                for(byte j = 0; j < conditionPoliticalArray.length; j++) {
                    if(conditionPoliticalArray[j].endsWith("+")) 
                        auxPoliticalIs = true;
                    if (conditionPoliticalArray[j].equals(politicalName[i] + "+")) 
                        politicalIs = 1;
                    if (conditionPoliticalArray[j].equals(politicalName[i] + "-")) 
                        politicalIsNot = 0;
                }
            }
            // If there are not any "+" condition
            if(!auxPoliticalIs) politicalIs = 1;
        } else {
            politicalIs = 1;
        }
        
        if(conditionStatus.item != null && 
                !conditionStatus.item[0].trim().equals("") && 
                statusName != null) {
            String[] conditionStatusArray = 
                    conditionStatus.item[0].trim().split(" ");
            // auxiliary variable: if there is any "+" condition
            boolean auxStatusIs = false;
            for(byte i = 0; i < statusName.length; i++) {
                for(byte j = 0; j < conditionStatusArray.length; j++) {
                    if(conditionStatusArray[j].endsWith("+")) 
                        auxStatusIs = true;
                    if (conditionStatusArray[j].equals(statusName[i] + "+")) 
                        statusIs = 1;
                    if (conditionStatusArray[j].equals(statusName[i] + "-")) 
                        statusIsNot = 0;
                }
            }
            // If there are not any "+" condition
            if(!auxStatusIs) statusIs = 1;
        } else {
            statusIs = 1;
        }         
        
        System.out.println("usageStatistics = " + usageStatistics + "\n" +
                "activismIs = " + activismIs + "\n" +
                "activismIsNot = " + activismIsNot + "\n" +
                "kampfbundIs = " + kampfbundIs + "\n" + 
                "kampfbundIsNot = " + kampfbundIsNot + "\n" +
                "politicalIs = " + politicalIs + "\n" + 
                "politicalIsNot = " + politicalIsNot + "\n" +
                "statusIs = " + statusIs + "\n" +
                "statusIsNot = " + statusIsNot);
        System.out.println("Obey conditions (message): " + usageStatistics * activismIs * activismIsNot * kampfbundIs * 
                kampfbundIsNot * politicalIs * politicalIsNot * statusIs * statusIsNot); // !!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!
        // TRUE if obeying condition, FALSE if not 
        return(usageStatistics * activismIs * activismIsNot * kampfbundIs * 
                kampfbundIsNot * politicalIs * politicalIsNot * statusIs * 
                statusIsNot == 1);
    }
    
    
    /**
     * For events optionNumber = 0, for question optionNumber = answer number.
     * @param triggerNumber
     * @return 
     */
    public String getTrigger(byte triggerNumber) {
        if(effectTrigger.item == null || 
                effectTrigger.item.length <= triggerNumber) {
            return null;
        } else {
            // System.out.println("Trigger array length " + effectTrigger.item.length); // <<<<<< tmp >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            return effectTrigger.item[triggerNumber];
        }
    }
    
    public MessageView getMessageView(String dateText) {        
        return null;
    }
    
    /**
     * Get political effects for all answers
     * @param answerPosition
     * @return 
     */
    public String[] getPoliticalEffects() {
        return getPoliticalEffects(-1);
    }
    
    /**
     * Get political effects for defined answer - must be overrode !!!
     * @param answerPosition
     * @return 
     */
    public String[] getPoliticalEffects(int answerPosition) {        
        return null;
    }    
    
    /**
     * Get kampfbund reputation effects for defined answer - must be overrode !!!
     * @param answerPosition
     * @return 
     */
    public String[] getKampfbundEffects(int answerPosition) {        
        return null;
    }
    
    /**
     * Get kampfbund membership effects for defined answer - must be overrode !!!
     * @param answerPosition
     * @return 
     */
    public String[] getKampfbundMembershipEffects(int answerPosition) {        
        return null;
    }
    
    /**
     * Get status effects for defined answer - must be overrode !!!
     * @param answerPosition
     * @return 
     */
    public String[] getStatusEffects(int answerPosition) {        
        String effectString = null;
        
        // For events
        if(answerPosition == -1) answerPosition = 0;
        
        if(effectStatus.item != null && 
                effectStatus.item.length > answerPosition) {
            // "Freikorps+ Baltikum-"
            effectString = effectStatus.item[answerPosition];
        }
        
        if(effectString != null) {
        // {"Freikorps+", "Baltikum-"}
        return effectString.split(" ");
        } else {
            String[] dummy = {""};
            return dummy;
        }
    }
    
    /**
     * Get mortality (death or life) effect for defined answer - must de overrode !!!
     * @param answerPosition
     * @return 
     */
    public boolean getMortalityEffects(int answerPosition) {
        return false;
    }
}
