
package questionnaire.files.messages;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class MessageView {
    private final StringProperty messageType;
    private final StringProperty captionText;
    private final StringProperty pictureName;
    private final StringProperty mainText;
    private final StringProperty[] answerText;
    private final StringProperty quotationAuthor;
    private final BooleanProperty theEndTrigger;
    
    /**
     * Default constructor
     */
    public MessageView() {
        this(null, null, null, null, null, null, false);
    }
    
    public MessageView(String messageType, String captionText, 
            String pictureName, String mainText, String[] answerText, 
            String quotationAuthor, boolean theEndTrigger) {
        this.messageType = new SimpleStringProperty(messageType);
        this.captionText = new SimpleStringProperty(captionText);
        this.pictureName = new SimpleStringProperty(pictureName);
        this.mainText = new SimpleStringProperty(mainText);        
        this.answerText = new SimpleStringProperty[5];
        for(int i = 0; i < 5; i++) {
            if(i < answerText.length) {
                this.answerText[i] = new SimpleStringProperty(answerText[i]);
            } else {
                // Set default empty value
                this.answerText[i] = new SimpleStringProperty("");
            }
        }
        this.quotationAuthor = new SimpleStringProperty(quotationAuthor); 
        this.theEndTrigger = new SimpleBooleanProperty(theEndTrigger);
// System.out.println(captionText + "/" + pictureName + "/" + mainText + "/" + quotationAuthor); // >>>>>>>>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<
// System.out.println(this.captionText + "/" + this.pictureName + "/" + this.mainText + "/" + this.quotationAuthor); // >>>>>>> delete <<<<<<<<<
    }
    
    public String getMessageType() {
        return messageType.get();
    }
    
    public void setMessageType(String messageType) {
        this.messageType.set(messageType);
    }
    
    public String getCaptionText() {
        return captionText.get();
    }
    
    public void setCaptionText(String captionText) {
        this.captionText.set(captionText);
    }
    
    public String getPictureName() {
        return pictureName.get();
    }
    
    public void setPictureName(String pictureName) {
        this.pictureName.set(pictureName);
    }
    
    public String getMainText() {
        return mainText.get();
    }
    
    public void setMainText(String mainText) {
        this.mainText.set(mainText);
    }
    
    public String[] getAnswerText() {
        String[] answerTextArray = new String[answerText.length];
        // System.out.println("before return: answerText.length = " + answerText.length + "/ answerTextArray.length = " + answerTextArray.length);
        for(int i = 0; i < answerText.length; i++) {
            if(answerText[i] == null) System.out.println("i = " + i + " " + answerText[i].toString());
            answerTextArray[i] = answerText[i].get();
        }        
        return answerTextArray;
    }
    
    public void setAnswerText(String[] answerText) {        
        for(int i = 0; i < answerText.length; i++) {
            this.answerText[i].set(answerText[i]);
        }
    }
    
    public String getQuotationAuthor() {
        return quotationAuthor.get();
    }
    
    public void setQuotationAuthor(String quotationAuthor) {
        this.quotationAuthor.set(quotationAuthor);
    }    
    
    public Boolean getTheEndTrigger() {
        return theEndTrigger.get();
    }
    
    public void setTheEndTrigger(Boolean theEndTrigger) {
        this.theEndTrigger.set(theEndTrigger);
    }
}
