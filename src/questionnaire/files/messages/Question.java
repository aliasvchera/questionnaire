/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.messages;

import java.util.Random;


/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */


public class Question extends Message {
// Question object with all parameters (text, conditions, answers etc)
    // String sourcePath; // ????????????????????????????????????????????????????
        
    MessageItem answerText;
    MessageItem answerConditionPolitical;
    MessageItem answerConditionKampfbund;
    MessageItem effectPolitical;
    MessageItem effectKampfbund;
    MessageItem effectMortality;
    // MessageItem effectCustody;
    // MessageItem effectCustodyTerm;
    MessageItem effectKampfbundJoinOrLeft;
    // MessageItem effectOccasion;
    
    public Question(String sourcePath, String keyText) {
        super(sourcePath, keyText);
                
        answerText = new MessageItem(dataSplit, "answerText");
        answerConditionPolitical = new MessageItem(dataSplit, 
                "answerConditionPolitical");
        answerConditionKampfbund = new MessageItem(dataSplit, 
                "answerConditionKampfbund");
        effectPolitical = new MessageItem(dataSplit, 
                "effectPolitical");
        effectKampfbund = new MessageItem(dataSplit, 
                "effectKampfbund");
        effectMortality = new MessageItem(dataSplit, 
                "effectMortality");	
        // effectCustody = new MessageItem(dataSplit, "effectCustody");
        // effectCustodyTerm = new MessageItem(dataSplit, 
        //        "effectCustodyTerm");
        effectKampfbundJoinOrLeft = new MessageItem(dataSplit, 
                "effectKampfbundJoinOrLeft");        
        // effectOccasion = new MessageItem(dataSplit, "effectOccasion");
    }

    /* public boolean obeyConditions(String activismName, String[] politicalName,  
            String kampfbundName, String deathName) {
    // Check event condition compliance with actual parameter values  
        return super.obeyConditions(activismName, politicalName, kampfbundName,
                deathName);
    } */
    
    
    /**
     * Get political effects for defined answer
     * @param answerPosition
     * @return 
     */
    @Override
    public String[] getPoliticalEffects(int answerPosition) {
        String effectString = "";
        // Only if effects exist for this answer
        if(effectPolitical.item != null && 
                effectPolitical.item.length > answerPosition) {
            int firstI = answerPosition;
            int lastI = answerPosition;// In case of all answer effects
            if(answerPosition == -1) {
                firstI = 0;
                lastI = effectPolitical.item.length - 1;            
            }
            for(int i = firstI; i <= lastI; i++) {
                effectString += " " + effectPolitical.item[i];
            }
            // "socialist+4 nationalist-2 conservative-2"
            effectString = effectString.substring(1);
        }
        // {"socialist+4", "nationalist-2", "conservative-2"}
        return effectString.split(" ");
    }
    
    /**
     * Get kampfbund effects for defined answer
     * @param answerPosition
     * @return 
     */
    @Override
    public String[] getKampfbundEffects(int answerPosition) {
        String effectString = null;
        if(effectKampfbund.item != null && 
                effectKampfbund.item.length > answerPosition) {
            // "STA+4 RFB-2 SA-2"
            effectString = effectKampfbund.item[answerPosition];
        }
        
        if(effectString != null) {
        // {"STA+4", "RFB-2", "SA-2"}
        return effectString.split(" ");
        } else {
            String[] dummy = {""};
            return dummy;
        }
    }
    
    /**
     * Get kampfbund membership effects for defined answer
     * @param answerPosition
     * @return 
     */
    @Override
    public String[] getKampfbundMembershipEffects(int answerPosition) {
        String effectString = null;
        if(effectKampfbundJoinOrLeft.item != null && 
                effectKampfbundJoinOrLeft.item.length > answerPosition) {
            // "STA+ RFB- SA-"
            effectString = effectKampfbundJoinOrLeft.item[answerPosition];
        }
        
        if(effectString != null) {
        // {"STA+", "RFB-", "SA-"}
        return effectString.split(" ");
        } else {
            String[] dummy = {""};
            return dummy;
        }
    }
    
    
    /**
     * Get status effects for defined answer
     * @param answerPosition
     * @return 
     */
    @Override
    public String[] getStatusEffects(int answerPosition) {        
        String effectString = null;
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
     * Get mortality (death or life) effect for defined answer
     * @param answerPosition
     * @return 
     */
    public boolean getMortalityEffects(int answerPosition) {
        boolean isDead = false;
        Random random = new Random();
        if(effectMortality.item != null && 
                effectMortality.item.length > answerPosition && 
                !effectMortality.item[answerPosition].trim().equals("")) {
            float deathProbability = Float.parseFloat(
                    effectMortality.item[answerPosition].replace(",", "."));
            // death probability = 0.3, if 'fortune choice' <= 0.3  =>  is dead
            if(deathProbability >= (float) random.nextFloat()) isDead = true;
        }
        return isDead;
    }
    
    
    
    @Override
    public MessageView getMessageView(String dateText) {
        String captionText = dateText;
        if(this.captionText.item != null) 
            captionText = this.captionText.item[0];
        String pictureName = null;
        if(this.pictureName.item != null) 
            pictureName = this.pictureName.item[0];
        String mainText = "";
        if(this.mainText.item != null) 
            mainText = this.mainText.item[0].replaceAll("@@@", "\n");
        String[] answerText = {null, null, null, null, null};
        if(this.answerText.item != null)
            answerText = this.answerText.item;
        String quotationAuthor = "";
        boolean theEndTrigger = false;
        /*
        System.out.println("captionText " + captionText + "\n" + "pictureName " + 
            pictureName + "\n" + "mainText " + mainText + "\n" + "answerText " + 
            answerText + "\n" + "quotationAuthor " + quotationAuthor);  // !!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!  
        */
        
        return new MessageView("question", captionText, pictureName, mainText, 
                answerText, quotationAuthor, theEndTrigger);             
    }
}