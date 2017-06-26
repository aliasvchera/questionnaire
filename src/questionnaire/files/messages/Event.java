/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.messages;


/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */

// Event object with all parameters (text, conditions, picture etc)
public class Event extends Message {	
    private String eventType;
    
    MessageItem conditionDeath;
    
    MessageItem quotationAuthor;

    public Event(String sourcePath, String keyText) {
        super(sourcePath, keyText);
        
        conditionDeath = new MessageItem(dataSplit, "conditionDeath");
        
        quotationAuthor = new MessageItem(dataSplit, "quotationAuthor");
        
        // Define event type       
        if(keyText.startsWith("eventStory")) 
            eventType = "eventStory";
        if(keyText.startsWith("eventMessage")) 
            eventType = "eventMessage";
    }
    
    
    /**
     * Check event condition compliance with actual parameter values  
     * @param activismName
     * @param politicalName
     * @param kampfbundName
     * @param deathName
     * @param statusName
     * @return 
     */
    @Override
    public boolean obeyConditions(String fileName, String activismName, String[] politicalName,  
            String kampfbundName, String deathName, String[] statusName) {
        boolean auxObeyConditions = super.obeyConditions(fileName, activismName, 
                politicalName, kampfbundName, deathName, statusName);
        byte deathIs = 0;
        byte deathIsNot = 1;

        if (conditionDeath.item != null) {
            if (conditionDeath.item[0].trim().equals(deathName + "+")) 
                deathIs = 1;
            if (conditionDeath.item[0].trim().equals(deathName + "-")) 
                deathIsNot = 0;
        } else {
            deathIs = 1;
        }
        
        System.out.println("Obey conditions (event): " + deathIs * deathIsNot);
        // TRUE if obeying condition, FALSE if not 
        if(!(deathIs * deathIsNot == 1)) auxObeyConditions = false;
        return auxObeyConditions;
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
        String quotationAuthor = "";
        if(this.quotationAuthor.item != null) 
            quotationAuthor = this.quotationAuthor.item[0];
        boolean theEndTrigger = (this.effectTrigger.item != null && 
                this.effectTrigger.item[0].equals("The-End"));
                
        /*
        System.out.println("captionText " + captionText + "\n" + "pictureName " + 
                pictureName + "\n" + "mainText " + mainText + "\n" + "answerText " + 
                answerText + "\n" + "quotationAuthor " + quotationAuthor);
        */
        return new MessageView(eventType, captionText, pictureName, mainText, 
                answerText, quotationAuthor, theEndTrigger);
    }
}
