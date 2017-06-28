/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class CurriculumVitae extends Parameter {
    // List of all possible CV cases (key & text)
    private HashMap<String, String> cvTextHashMap = 
            new HashMap<String, String>();
    
    private ArrayList<cvItem> cvStatusList = new ArrayList<cvItem>();
    
    String professionKey = "profession_";
            

    public CurriculumVitae(String parameterFileName) { // "curriculum_vitae"
        super(parameterFileName);
              
        // CV constants (event/status names, text for CV) data from .txt file        
        byte columnStatusName = -1;
        byte columnTextForCV = -1;
        byte columnIsActual = -1;
        System.out.println("CV - step1"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp >>>>>>>>>>>>>>>>>>>>>>>>
        for(byte i = 0;  i < dataSplitConstants[0].length; i++) {
            switch(dataSplitConstants[0][i]) {
                case "STATUS_NAME":
                    columnStatusName = i;
                    break;
                case "TEXT_FOR_CV":
                    columnTextForCV = i;
                    break;
            }
        }
        System.out.println("CV - step2"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp >>>>>>>>>>>>>>>>>>>>>>>>
        if(columnStatusName == -1 || columnTextForCV == -1) {
            System.out.println("CV constant columns are not defined!"); // change by log entry!!!
        } else {        
            // Add items (key & text) to CV hash map (list of all possible CV cases)
            for(int i = 1; i < dataSplitConstants.length; i++) {
                cvTextHashMap.put(dataSplitConstants[i][columnStatusName], 
                        dataSplitConstants[i][columnTextForCV]);
            }
        }
        
        System.out.println("CV - step3"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp >>>>>>>>>>>>>>>>>>>>>>>>
        // CV variables (event participation / status) data from .txt file
        for(byte i = 0;  i < dataSplitVariables[0].length; i++) {
            switch(dataSplitVariables[0][i]) {
                case "isActual":
                    columnIsActual = i;
                    // System.out.println("columnIsActual: " + columnIsActual); // >>> tmp <<<<<<<<<<<<<<<<<<
                    break;                    
            }
        }
        if(columnIsActual  == -1 ) {
            System.out.println("CV variable columns are not defined!"); // change by log entry!!!
        }
        
        System.out.println("CV - step4"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp >>>>>>>>>>>>>>>>>>>>>>>>
        for(int i = 1; i < dataSplitVariables.length; i++) {
            // System.out.println("i = " + i); // >>> tmp <<<<<<<<<<<<<<<<<<            
            cvStatusList.add(new cvItem(dataSplitVariables[i][0], 
                    Boolean.parseBoolean(dataSplitVariables[i][columnIsActual])));
            // System.out.println("status: " + dataSplitVariables[i][columnIsActual]); // >>> tmp <<<<<<<<<<<<<<<<<<
        }
        System.out.println("CV - step5"); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> tmp >>>>>>>>>>>>>>>>>>>>>>>>        
    }
    
    /**
     * Assign starting values to variables (new game)
     */
    public void renewVariables() {
        cvStatusList.clear();        
    }
    
    /**
     * Get actual status list
     * @return 
     */
    public String[] getStatusList() {
        System.out.println("getStatusList - step1"); // >>>>>>>>>>>>>>>>>>>>>>>>>>> tmp <<<<<<<<<<<<<<<
        ArrayList<String> cvStatusNameList = new ArrayList<String>();        
        cvStatusList.forEach((itm) -> {
            cvStatusNameList.add(itm.STATUS_NAME);
        }); 
        
        System.out.println("getStatusList - step2"); // >>>>>>>>>>>>>>>>>>>>>>>>>>> tmp <<<<<<<<<<<<<<<        
        return cvStatusNameList.toArray(new String[cvStatusNameList.size()]);        
    }
    
    /**
     * Return text string with CV text (for all statuses - actual or not)
     * @return 
     */
    public String getCVText() {
        String cvText = "";
        if(cvStatusList.size() > 0) {
            for(cvItem itm : cvStatusList){
                String cvTextItem = cvTextHashMap.get(itm.STATUS_NAME);
                if(cvTextItem != null && 
                        !itm.STATUS_NAME.startsWith(professionKey)) 
                    cvText += cvTextItem + " ";
            }
        }
        if(cvText.length() > 2) cvText = cvText.substring(0, cvText.length() - 2);
        return cvText;
    }
    
    /**
     * Return text string with actual profession name
     * @return 
     */
    public String getProfession() {
        String profession = "";        
        for(cvItem itm : cvStatusList){
            if(itm.STATUS_NAME.startsWith(professionKey) && itm.isActual) {
                profession = cvTextHashMap.get(itm.STATUS_NAME);
                break;
            }
        }
        return profession;
    }
    
    public void updateStatus(String[] selectedEffects) {        
        Boolean isActual = null;
        if(selectedEffects != null) {
            for(String effect : selectedEffects) {
                if(!effect.trim().equals("")) {
                    System.out.println("Status effect :" + effect);
                    switch(effect.substring(effect.length() - 1)) {
                        case "+":
                            isActual = true;
                            break;
                        case "-":
                            isActual = false;
                            break;
                        default:
                            System.err.println("Error in effectStatus!");
                    }    
                    
                    int indexI = -1;
                    for(int i = 0; i < cvStatusList.size(); i++) {
                        if(cvStatusList.get(i).STATUS_NAME.equals(
                                effect.substring(0, effect.length() - 1))) {
                            indexI = i;
                        }
                    }
                    
                    // If status exists already in the list
                    if(indexI != -1) {
                        // update isActual
                        cvStatusList.get(indexI).isActual = isActual;
                    } else {
                        // add new item
                        cvStatusList.add(new cvItem(effect.substring(0, 
                                effect.length() - 1), isActual));
                    }
                }
            }
        }
    }
    
    /**
     * Get String[][] array for writing in .txt file
     * @return 
     */
    @Override
    String[][] getDataExport() {
        System.out.println("CV.getDataExport: step1"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        String[][] dataExport = new String[cvStatusList.size() + 1][2];
        // System.out.println("cvStatusHashMap.size() = " + cvStatusHashMap.size());
        dataExport[0][0] = "STATUS_NAME";
        dataExport[0][1] = "isActual";  
        if(cvStatusList.size() > 0) {        
            // Add statuses to the list
            for(int i = 0; i < cvStatusList.size(); i++) {            
                dataExport[i + 1][0] = cvStatusList.get(i).STATUS_NAME;
                dataExport[i + 1][1] = Boolean.toString(cvStatusList.get(i).isActual);
            }
        }
        System.out.println("CV.getDataExport: step2"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        return dataExport;
    } 
}


class cvItem {
    final String STATUS_NAME;	
    // final String TEXT_FOR_CV;
    boolean isActual;

    cvItem(String eventName, boolean isActualValue) {
        STATUS_NAME = eventName;
        // TEXT_FOR_CV = textForCV;
        isActual = isActualValue;
    }    
}
