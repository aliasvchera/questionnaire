
package questionnaire.files.parameters;

import java.util.ArrayList;
import questionnaire.files.DataInTXT;


/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
// Current game moment parameters (month, year, dead or alive, in custody etc)
public class CurrentMoment extends Parameter {
    public final int START_YEAR;
    public final int START_MONTH;
    public final int FINAL_YEAR;
    public final int FINAL_MONTH;
    
    private final String YEAR_TEXT;
    private final String[] MONTH_TEXT;
    
    public int currentYear;
    public int currentMonth;
    public String currentDateText;
    
    // Current status
    public boolean isDead = false;
    public boolean inCustody = false;
    /* Current event/question full path in 'data' folder, 
    e.g. "...\event\message\date\1932-06_Preussenschlag.txt" = 
    {"\\event\\message\\date", "1932-06_Preussenschlag.txt"} */
    public String[] currentMessage = new String[2]; 
    /* last time the game was finished on event, question etc 
    ("book cover" by default == -2) */
    public byte gameMoment = -2;
    // Actual trigger list:
    // a. trigger key value
    public ArrayList<String> triggerName = new ArrayList<String> ();
    // b. trigger game moment value
    public ArrayList<Byte> triggerGameMoment = new ArrayList<Byte> ();
    
    public CurrentMoment(String parameterFileName) { // "current_moment"
        super(parameterFileName);
        
        // Personality features (constants)
        byte lineStartYear = -1;
        byte lineStartMonth = -1;
        byte lineFinalYear = -1;
        byte lineFinalMonth = -1;

        for(byte i = 0; i < dataSplitConstants.length; i++) {
            switch (dataSplitConstants[i][0]) {
                case "START_YEAR": 
                    lineStartYear = i;
                    break;
                case "START_MONTH": 
                    lineStartMonth = i;
                    break;
                case "FINAL_YEAR": 
                    lineFinalYear = i;
                    break;
                case "FINAL_MONTH": 
                    lineFinalMonth = i;
                    break;
            }
        }
        if(lineStartYear != -1) {
            START_YEAR = Integer.parseInt(dataSplitConstants[lineStartYear][1]);
        } else {
            START_YEAR = -1; // change by log entry!!!
        }
        if(lineStartMonth != -1) {
            START_MONTH = Integer.parseInt(dataSplitConstants[lineStartMonth][1]);
        } else {
            START_MONTH = -1; // change by log entry!!!
        }
        if(lineFinalYear != -1) {
            FINAL_YEAR = Integer.parseInt(dataSplitConstants[lineFinalYear][1]);
        } else {
            FINAL_YEAR = -1; // change by log entry!!!
        }
        if(lineFinalMonth != -1) {
            FINAL_MONTH = Integer.parseInt(dataSplitConstants[lineFinalMonth][1]);
        } else {
            FINAL_MONTH = -1; // change by log entry!!!
        }
        
        // Personality features (variables)        
        currentYear = START_YEAR;
        currentMonth = START_MONTH;
        for(byte i = 0; i < dataSplitVariables.length; i++) {
            if(dataSplitVariables[i].length > 1) {
                switch (dataSplitVariables[i][0]) {
                    case "currentYear":                     
                        currentYear = 
                                Integer.parseInt(dataSplitVariables[i][1]);
                        break;
                    case "currentMonth": 
                        currentMonth = 
                                Integer.parseInt(dataSplitVariables[i][1]);
                        break;
                    case "isDead": 
                        if(dataSplitVariables[i][1].equals("true")) 
                            isDead = true;
                        break;
                    case "inCustody": 
                        if(dataSplitVariables[i][1].equals("true")) 
                            inCustody = true;
                        break;
                    case "currentMessage": 
                        if(!dataSplitVariables[i][1].equals("")) 
                            currentMessage = 
                                    dataSplitVariables[i][1].split("#");
                        break;
                    case "gameMoment": 
                        if(!dataSplitVariables[i][1].equals("")) 
                            gameMoment = 
                                    Byte.parseByte(dataSplitVariables[i][1]);
                        break;
                    case "triggerName": 
                        if(!dataSplitVariables[i][1].equals("")) {
                            String[] tmpTriggerName = 
                                    dataSplitVariables[i][1].split("#");
                            for(int j = 0; j < tmpTriggerName.length; j++)
                                triggerName.add(tmpTriggerName[j]);
                        }
                        break;
                    case "triggerGameMoment": 
                        if(!dataSplitVariables[i][1].equals("")) {
                            String[] tmpTriggerGameMoment = 
                                    dataSplitVariables[i][1].split("#");
                            for(int j = 0; j < tmpTriggerGameMoment.length; j++)
                                triggerGameMoment.add(Byte.
                                        parseByte(tmpTriggerGameMoment[j]));
                        }
                        break;
                }
            }
        }
        
        
        // Text constants for month names & year
        // Define source folder location
        String sourcePath = System.getProperty("user.dir") 
                + "\\data\\parameters\\";

        // Parameter constants (names, rank lists etc) data from .txt file
        String[][] dataSplit = DataInTXT.readTXT(sourcePath + "dates.txt");
        
        int lineYearText = -1;
        int lineMonthText = -1;
        for(byte i = 0; i < dataSplit.length; i++) {
            switch (dataSplit[i][0]) {
                case "YEAR_TEXT": 
                    lineYearText = i;
                    break;
                case "MONTH_TEXT": 
                    lineMonthText = i;
                    break;                
            }
        }
        if(lineStartYear != -1) {
            YEAR_TEXT = dataSplit[lineYearText][1];
        } else {
            YEAR_TEXT = null; // change by log entry!!!
        }
        if(lineStartMonth != -1) {
            MONTH_TEXT = dataSplit[lineMonthText][1].split("#");
        } else {
            MONTH_TEXT = null; // change by log entry!!!
        }
    }
    
    /**
     * Return current date in text format, e.g. 'April 1926'
     * @return 
     */
    public String getDateText() {
        return MONTH_TEXT[currentMonth - 1] + " " + currentYear + " " + YEAR_TEXT;
    }
    
    /**
     * Assign starting values to variables (new game)
     */
    public void renewVariables() {
        currentYear = START_YEAR;
        currentMonth = START_MONTH;
        isDead = false;
        inCustody = false;        
        currentMessage[0] = null;
        currentMessage[1] = null;
        gameMoment = -2;   
        triggerName.clear();
        triggerGameMoment.clear();        
    }
        
    // ???????????????????????????????????????????????????? DELETE ??????????????????????????????????????????????
    /**
     * Check a) is current date exceeds finish date; b) (turned off) is the protagonist dead?
     * @return 
     */
    public boolean gameOver() {
        return(currentYear * 12 + currentMonth > FINAL_YEAR * 12 + FINAL_MONTH); // isDead || ???
    }
    
       
    /**
     * Get String[][] array for writing in .txt file
     * @return 
     */
    @Override
    String[][] getDataExport() {
        System.out.println("currentMoment.getDataExport: step1"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        String tmpCurrentMessage = "";
        if(currentMessage != null) 
            tmpCurrentMessage = currentMessage[0] + "#" + currentMessage[1];
        String tmpTriggerName = "";
        String tmpTriggerGameMoment = "";
        if(triggerName.size() > 0){
            for(int i = 0; i < triggerName.size(); i++) {            
                tmpTriggerName += "#" + triggerName.get(i);
                tmpTriggerGameMoment += "#" + triggerGameMoment.get(i);
            }      
            tmpTriggerName = tmpTriggerName.substring(1);
            tmpTriggerGameMoment = tmpTriggerGameMoment.substring(1);
        }
        String[][] dataExport = {
            {"currentYear", Integer.toString(currentYear)},
            {"currentMonth", Integer.toString(currentMonth)},
            {"isDead", Boolean.toString(isDead)},
            {"inCustody", Boolean.toString(inCustody)},
            {"currentMessage", tmpCurrentMessage},
            {"gameMoment", Byte.toString(gameMoment)},
            {"triggerName", tmpTriggerName},
            {"triggerGameMoment", tmpTriggerGameMoment}};
        System.out.println("currentMoment.getDataExport: step2"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        return dataExport;
    }    
}
