/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.parameters;

import java.util.ArrayList;
import questionnaire.files.DataInTXT;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class Political extends Parameter {
	
    // list of Political items with all parameters (constants & variables):
    politicalItem[] item;
    final float ACTIVISM_RATIO_REQUIREMENT;
    final float MIN_RATIO;
    private final String[] POLITICAL_VIEWS;
    public boolean activist = false;    
    public String currentPoliticalViews;

    public Political (String parameterFileName) { // "Political"
        super(parameterFileName);

        String[] POLITICAL_NAMES;

        /* From .txt files:
            Political constants (names, Political ratio requirements etc) data
            Political variables (activism, activism requirement etc) data 
        */
        
        byte lineActivismRatioRequirement = -1;
        byte lineMinRatio = -1;
        byte linePoliticalNames = -1;
        for(byte i = 0; i < dataSplitConstants[0].length; i++) {			
            switch (dataSplitConstants[0][i]) {
                case "ACTIVISM_RATIO_REQUIREMENT": 
                    lineActivismRatioRequirement = i;
                    break;
                case "MIN_RATIO": 
                    lineMinRatio = i;
                    break;
                case "POLITICAL_NAMES": 
                    linePoliticalNames = i;
                    break;
            }
        }
        if(lineActivismRatioRequirement != -1) {
            ACTIVISM_RATIO_REQUIREMENT = 
                    Float.parseFloat(
                            dataSplitConstants[1][lineActivismRatioRequirement]);            
        } else {
            ACTIVISM_RATIO_REQUIREMENT = -1;  // change by log entry!!!
        }
        if(lineMinRatio != -1) {
            MIN_RATIO = Float.parseFloat(dataSplitConstants[1][lineMinRatio]);                    
        } else { 
            MIN_RATIO = -1;  // change by log entry!!!
        }
        if(linePoliticalNames != -1) {
            POLITICAL_NAMES = 
                    dataSplitConstants[1][linePoliticalNames].split("#");                    
        } else {
            POLITICAL_NAMES = null; // change by log entry!!!
        }

        // list of Political items with all parameters (constants & variables)
        item = new politicalItem[POLITICAL_NAMES.length];
        for(byte j = 0; j < item.length; j++) {
            // if political_variables.txt is absent or contains only headers
            if(dataSplitVariables == null || dataSplitVariables.length == 1) {
                item[j] = new politicalItem(POLITICAL_NAMES[j]);
            } else {
                for(byte i = 1; i < dataSplitVariables.length; i++) {
                    if(dataSplitVariables[i][0].equals(POLITICAL_NAMES[j])) {
                        item[j] = new politicalItem(POLITICAL_NAMES[j], 
                                dataSplitVariables[0], dataSplitVariables[i]);
                    }
                }
            }
        }


        
         // Special terms for each configuration of Political views
        int dimension = 1;
        for(String p : POLITICAL_NAMES) {
            dimension = (dimension << 1) + 1; // 1 => 11 => 111 => ...
        }
        POLITICAL_VIEWS = new String[dimension + 1];
        
        // Define source folder location
        String sourcePath = System.getProperty("user.dir") 
                + "\\data\\parameters\\"; // change by variable ????????????????????????????????????

        // List of polical view names (reference book)
        String[][] dataSplitPoliticalViews = DataInTXT.readTXT(sourcePath + 
                "political_views_list.txt");
        /* File structure:
        non-activist | "wert" | "wasd" | ...
        activist | "very wert" | "very wasd" | ...
        socialist | 0 | 0 | ...
        republican | 0 | 0 | ...
        conservative | 1 | 1 | ...
        nationalist | 0 | 1 | ...
        */
        
        
        // Define position of political name in political_views_list.txt
        byte[] PoliticalNamePosition = new byte[POLITICAL_NAMES.length]; 
        for(byte i = 0; i < POLITICAL_NAMES.length; i++) {
            for(byte k = 2; k < dataSplitPoliticalViews.length; k++) {
                if(dataSplitPoliticalViews[k][0].equals(POLITICAL_NAMES[i])) {
                    PoliticalNamePosition[i] = k;
                }
            }
        }

        // Transform multi-dimensional matrix to one-dimensional array
        for(byte i = 1; i < dataSplitPoliticalViews[0].length; i++) {
            byte[] politicalValue = new byte[PoliticalNamePosition.length];
            for(byte k = 0; k < PoliticalNamePosition.length; k++) {
                politicalValue[k] = Byte.parseByte(
                        dataSplitPoliticalViews[PoliticalNamePosition[k]][i]);
            }

            for(byte j = 0; j <= 1; j++) {	
                int position = j;
                for (byte p : politicalValue) {
                    position = (position << 1) + p; // e.g. 10 => 101
                }                
                POLITICAL_VIEWS[position] = dataSplitPoliticalViews[j][i];
            }
        }

        // Defining the person activism and Political views
        this.renewParameters();
    }	

    /**
     *  Defining the person activism and Political views
     */
    public void renewParameters() {
        int activismTotal = 0;
        int activismRequirementTotal = 0;
        float ratio;

        for(politicalItem p : item) {
            activismTotal += p.activism;
            // activismRequirementTotal += p.activismRequirement;
        }
        
        
        // Defining partisans for each Political views
        for(byte i = 0; i < item.length; i++) {
            if(activismTotal != 0) {
                ratio = (float) item[i].activism / activismTotal;
                if(ratio >= MIN_RATIO) {
                    item[i].isPartisan = true;
                    // Sum activism requirement only if isPartisan = true
                    activismRequirementTotal += item[i].activismRequirement;
                }
            } else {
                item[i].isPartisan = false;
            }
        }
        
        // System.out.println("activismTotal = " + activismTotal + " / activismRequirementTotal = " + activismRequirementTotal); // tmp!!!!!
        // System.out.println("activismTotal / activismRequirementTotal = " + ((float) activismTotal / activismRequirementTotal)); // tmp!!!!
        // Activism - common for all Political views
        if((activismRequirementTotal != 0) && ((float) activismTotal / 
                activismRequirementTotal >= ACTIVISM_RATIO_REQUIREMENT)) {		
            activist = true;
        } else {
            activist = false;
        }

        
        // Defining current Political views
        byte current_activism = 0;
        if(this.activist) current_activism = 1;
        int position = current_activism;
        byte isPartisanNmb;  
        for (politicalItem p : item) {
            if(p.isPartisan) {
                isPartisanNmb = 1;
            } else {
                isPartisanNmb = 0;
            }
            position = (position << 1) + isPartisanNmb;
        }
        currentPoliticalViews = POLITICAL_VIEWS[position];
    }
    
    /** 
     * Return list of Political views belonged to the protagonist
     * @return 
     */
    public String[] politicalViewsList() {    
        ArrayList<String> politicalNames = new ArrayList<String>();
        for(politicalItem pItem : item) {
            if(pItem.isPartisan) politicalNames.add(pItem.NAME);
        }
        return politicalNames.toArray(new String[politicalNames.size()]);
    }
    
    /**
     * Assign starting values to variables (new game) 
     */
    public void renewVariables() {    
        for(politicalItem itm : item) {
            itm.activism = 0;
            itm.activismRequirement = 0; 
            itm.isPartisan = false;
        }    
        activist = false;    
        currentPoliticalViews = null;
    }
    
    /**
     * 
     * @param selectedEffects
     * @param totalEffects 
     */
    public void updateActivism(String[] selectedEffects, String[] totalEffects) {
        String politicalPlus, politicalMinus;
        for(politicalItem itm : item) {     
            System.out.println("Political effects (before): " + itm.NAME + "/" 
                    + itm.activism + "/" + itm.activismRequirement); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            
            for(String effect : selectedEffects) {
                // Positive effects, e.g. 'socialist+2'
                politicalPlus = itm.NAME + "+";
                if(effect.startsWith(politicalPlus)) {
                // If 'socialist+2' begins with 'socialist+' add '2' to activism
                    itm.activism += Integer.parseInt(effect.substring(
                            politicalPlus.length()));
                }
                
                // Positive negative, e.g. 'socialist-2'
                politicalMinus = itm.NAME + "-";
                if(effect.startsWith(politicalMinus)) {
                // If 'socialist-2' begins with 'socialist-' add '2' to activism
                    itm.activism -= Integer.parseInt(effect.substring(
                            politicalMinus.length()));
                }
            }
            
            int maxEffect = 0;
            int currentEffect = 0;
            for(String effect : totalEffects) {
                // Positive effects, e.g. 'socialist+2'
                politicalPlus = itm.NAME + "+";
                if(effect.startsWith(politicalPlus)) {
                // If 'socialist+2' begins with 'socialist+' add '2' to activism
                    currentEffect = Integer.parseInt(effect.substring(
                            politicalPlus.length()));
                    if(maxEffect < currentEffect) maxEffect = currentEffect;
                }
            }
            itm.activismRequirement += maxEffect;
            
            System.out.println("Political effects (after): " + itm.NAME + "/" 
                    + itm.activism + "/" + itm.activismRequirement); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
        }
        // Renew the person activism and political views
        this.renewParameters();
    }
    
    /**
     * Get String[][] array for writing in .txt file
     * @return 
     */
    @Override
    String[][] getDataExport() {
        System.out.println("political.getDataExport: step1"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        String[][] dataExport = new String[item.length + 1][3];
        dataExport[0][0] = "NAME";
        dataExport[0][1] = "activism";
        dataExport[0][2] = "activismRequirement";        
        for(int i = 0; i < item.length; i++) {
            dataExport[i + 1][0] = item[i].NAME;
            dataExport[i + 1][1] = Integer.toString(item[i].activism);
            dataExport[i + 1][2] = Integer.toString(item[i].activismRequirement);
        }  
        System.out.println("political.getDataExport: step2"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        return dataExport;
    } 
}

class politicalItem {
    public final String NAME;
    int activism = 0;
    /* Maximal potential activism (if the person in all cases acts as staunch 
       supporter of this Political views): */
    int activismRequirement = 0; 
    public boolean isPartisan = false; // Does the person support this Political views?

    politicalItem(String NAME, String[] headerVariables, String[] dataVariables) { 
        this.NAME = NAME;

        // If variable strings are not empty
        if(headerVariables != null && dataVariables != null) {
            for(int i = 0; i < headerVariables.length; i++) {
                switch (headerVariables[i]) {
                    case "activism": activism = Integer.parseInt(dataVariables[i]);
                        break;
                    case "activismRequirement": activismRequirement = 
                            Integer.parseInt(dataVariables[i]);
                        break;
                }
            }
        } 
    }

    // In case 'political_variables.txt' is absent
    politicalItem(String NAME) {
            this(NAME, null, null);
    }	
    
    
}