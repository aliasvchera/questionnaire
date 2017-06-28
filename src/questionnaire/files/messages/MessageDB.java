/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.messages;

import java.io.File;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import questionnaire.files.DataInTXT;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class MessageDB {
    /* Hash map for
        1. events:
        1.I. story
        1.I.a. date, e.g. key = "eventStory_Date_1920-01"    
        1.I.b. period, e.g. key = "eventStory_Period_1920-01"
        1.I.c. trigger, e.g. key = "eventStory_Trigger_Kapp-Luetwitz"
        1.II. message
        1.II.a. date, e.g. key = "eventMessage_Date_1920-01"    
        1.II.b. period, e.g. key = "eventMessage_Period_1920-01"
        1.II.c. trigger, e.g. key = "eventMessage_Trigger_Kapp-Luetwitz"
        2. questions:
        2.a. date, e.g. key = "question_Date_1920-01"
        2.b. period, e.g. key = "question_Period_1920-01"
        2.c. trigger, e.g. key = "question_Trigger_Kapp-Luetwitz" */
    private static HashMap<String, ArrayList<String>> messageHashMap = 
            new HashMap<String, ArrayList<String>>();
    
    /* Hash map for message usage statistics. As of some messages (period
        messages) can be called more than once, but no more than X times, there 
        is necessity to save and check this statistics
    */
    private static HashMap<String, Byte> messageUsageHashMap = 
            new HashMap<String, Byte>();
    
    // {Path, Key1, Key2}
    private static final String[][] MESSAGE_FILES = 
        { {"\\events\\story\\date", "eventStory", "Date"},
            {"\\events\\story\\period", "eventStory", "Period"},
            {"\\events\\story\\trigger", "eventStory", "Trigger"},
            {"\\events\\message\\date", "eventMessage", "Date"},
            {"\\events\\message\\period", "eventMessage", "Period"},
            {"\\events\\message\\trigger", "eventMessage", "Trigger"},
            {"\\questions\\date", "question", "Date"},
            {"\\questions\\period", "question", "Period"},
            {"\\questions\\trigger", "question", "Trigger"}};
    
    private static final String messageUsageFilePath = 
            "\\parameters\\message_usage_statistics.txt";
    
    public static void importMessageUsageStatistics(String dataFolderPath) {
        // Clear hash map
        messageUsageHashMap.clear();
                
        // Import data from the file
        String[][] auxData = 
                DataInTXT.readTXT(dataFolderPath + messageUsageFilePath);
        if(auxData != null) {
            // if file with previous statistics exists
            for(String[] dataItem : auxData) {
                if(!dataItem[0].equals("FILE_NAME")) { // exclude header string
                    messageUsageHashMap.put(dataItem[0], 
                            Byte.parseByte(dataItem[1]));
                }
            }
        }
    }
    
    /**
     * Clear message usage statistics
     */
    public static void renewMessageUsageStatistics() {
        messageUsageHashMap.clear();
    }
    
    /**
     * +1 to the key (defined event/question) usage statistics
     * @param fileName 
     */
    public static void addMessageUsageStatistics(String fileName) {
        byte usageStat = 0;
        if(messageUsageHashMap.get(fileName) != null) {
            usageStat = messageUsageHashMap.get(fileName);
        }
        messageUsageHashMap.put(fileName, ++usageStat);        
    }
    
    /**
     * Check could the message be used once again? (usageStat vs maxUsage)
     * @param fileName
     * @param maxUsage
     * @return 
     */
    public static boolean checkMessageUsageStatistics(String fileName, 
            byte maxUsage) {       
        // boolean checkingResult = false;
        byte usageStat = 0;
        if(messageUsageHashMap.get(fileName) != null) {
            usageStat = messageUsageHashMap.get(fileName);
        }        
        // if(usageStat < maxUsage) checkingResult = true;
        
        return (usageStat < maxUsage);        
    }
    
    /**
     * Save data with actual file usage statistics in file
     * @param dataFolderPath 
     */
    public static void saveMessageUsageStatistics(String dataFolderPath) {    
        String[][] messageUsageArray = 
                new String[messageUsageHashMap.size() + 1][2];
        Set<Map.Entry<String, Byte>> messageUsageSet = 
                messageUsageHashMap.entrySet(); 
        // Header values
        messageUsageArray[0][0] = "FILE_NAME";
        messageUsageArray[0][1] = "used";
        int i = 1;
        for(Map.Entry<String, Byte> usageEntry : messageUsageSet) {
            messageUsageArray[i][0] = usageEntry.getKey();
            messageUsageArray[i][1] = usageEntry.getValue().toString();
            i++;
        }
        DataInTXT.writeTXT(dataFolderPath + messageUsageFilePath, 
                messageUsageArray, Boolean.FALSE);
    }
    
    /**
     * Create or re-create DB with all message (events&questions) names 
     * marked by specific keys
     * @param dataFolderPath 
     */
    public static void renewMessageDB(String dataFolderPath) {        
        File messageDir;
        String keyText;
        int auxYear1, auxMonth1, auxYear2, auxMonth2;
        String period, auxZero;
        
        // Clear hash map
        messageHashMap.clear();
        
        for(String[] msgFiles : MESSAGE_FILES) {
            messageDir = new File(dataFolderPath + msgFiles[0]);
            
            // Check existing of directory (folder)
            if(!messageDir.isDirectory()) System.out.println(msgFiles[0] + " " 
                    +"isDirectory: " + messageDir.isDirectory()); // delete ??????????????????????????????
            
            for(String DirItem : messageDir.list()) {
                System.out.println("File name: " + DirItem);
                switch(msgFiles[2]) {
                    case "Date": 
                        // Add new filename for the date key (e.g. "*_1920-01")
                        // Filename: e.g. "1932-06_Preussenschlag.txt"
                        keyText = msgFiles[1] + "_" + msgFiles[2] 
                                + "_" + DirItem.substring(0, 7);
                        addMessageFile(keyText, DirItem);                        
                        break;
                    case "Period": 
                        // Add new filename for several date key 
                        // (e.g. "*_1920-01", "*_1920-02", "*_1920-03" ...)
                        // Filename: e.g. "1918-12_1919-12_m_Freikorps.txt"
                        auxYear1 = Integer.parseInt(DirItem.substring(0, 4));
                        auxMonth1 = Integer.parseInt(DirItem.substring(5, 7));
                        auxYear2 = Integer.parseInt(DirItem.substring(8, 12));
                        auxMonth2 = Integer.parseInt(DirItem.substring(13, 15));
                        period = DirItem.substring(16, 18);
                        
                        do {
                            auxZero = "";
                            if (auxMonth1 < 10) auxZero = "0";
                            keyText = msgFiles[1] + "_" + msgFiles[2] 
                                + "_" + auxYear1 + "-" +  auxZero + auxMonth1;   
                            addMessageFile(keyText, DirItem);
                            
                            if(period.equals("y")) {
                                // yearly events (e.g. "Deutsche Tag")
                                auxMonth1 += 12;
                            } else {
                                // monthly events (e.g. "join to Sta")
                                auxMonth1++;
                            }
                            
                            if (auxMonth1 > 12) {
                                auxYear1++;
                                auxMonth1 -= 12;
                            }
                        } while(auxYear1 * 12 + auxMonth1 
                                <= auxYear2 * 12 + auxMonth2);
                        break;        
                    case "Trigger": 
                        // Add new filename for the date key (e.g. "*_Versaille")
                        // Filename: e.g. "Blutmai_RFB.txt"
                        keyText = msgFiles[1] + "_" + msgFiles[2] 
                                + "_" + DirItem.substring(0, 
                                        DirItem.indexOf("_"));
                        addMessageFile(keyText, DirItem);
                        break;
                }                
            } 
        }
    }
    
    /**
     * Add new file name in list of the specific key, 
     * e.g. "eventStory_Date_1920-01"
     * @param keyText
     * @param fileName 
     */
    private static void addMessageFile(String keyText, String fileName) {
        System.out.println("addMessageFile: " + keyText + "/" + fileName); // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        ArrayList<String> fileNames = new ArrayList<String> ();
        if(messageHashMap.get(keyText) != null) {
            System.out.println("messageHashMap: " + keyText); // !!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            fileNames = messageHashMap.get(keyText);            
        }
        fileNames.add(fileName);
        messageHashMap.put(keyText, fileNames);
    }
    
    /**
     * Get filename (e.g. "1932-06_Preussenschlag.txt") for specific key 
     * (e.g. "eventStory_Date_1932-06")
     * @param keyText
     * @return 
     */
    public static String[] getMessageFile(String keyText) {    
        String[] messageFileNames = null;                        
        ArrayList<String> fileNames = messageHashMap.get(keyText);
        if(fileNames != null) {
            // If the key exists in the list
            Collections.shuffle(fileNames); // for obtaining random order 
            if (fileNames.get(0) != null) {
                // If item exists define folder path and filename
                messageFileNames = new String[fileNames.size()];
                fileNames.toArray(messageFileNames);
            } 
        }        
        return messageFileNames;
    }
    
    /**
     * Get path (e.g. "\\event\\message\\date") for specific key 
     * (e.g. "eventStory_Date_1932-06")
     * @param keyText
     * @return 
     */
    public static String getFolderPath (String keyText) {
        String folderPath = "";
        for(String[] msgFiles : MESSAGE_FILES) {
            if(keyText.startsWith(msgFiles[1] + "_" + msgFiles[2])) {
                folderPath = msgFiles[0] + "\\";
                break; 
            }
        }        
        return folderPath;
    }
}
