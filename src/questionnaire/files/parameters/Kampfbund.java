/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.parameters;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class Kampfbund extends Parameter {	
    private byte currentID = -1; // no membership by default
    public String currentShortName = "";
    public String currentName = "";
    public String currentRank = "";
    kampfbundItem[] item; // list of kampfbunds with all parameters (constants & variables)
	
    public Kampfbund(String parameterFileName) { // "Kampfbund"
        super(parameterFileName);
        
        // In case kampfbund variable file is absent/empty or has only headers
        if(dataSplitVariables == null || dataSplitVariables.length < 2) {
            dataSplitVariables = defaultVariables(dataSplitConstants);
        }
        // list of kampfbunds with all parameters (constants & variables)
        item = new kampfbundItem[dataSplitConstants.length-1];
        for(int i = 1; i < dataSplitConstants.length; i++) {
            item[i-1] = new kampfbundItem(dataSplitConstants[0], 
                    dataSplitConstants[i], dataSplitVariables[0], 
                    dataSplitVariables[i]);
        }
        // Define variables (actual Kampfbund membership & rank)
        this.defineCurrentKampfbund();
    }

    private void defineCurrentKampfbund() {
    // Define variables (actual Kampfbund membership & rank)
        for(byte i = 0; i < item.length; i++) {
            if(item[i].membership) {				
                currentID = i;
            }
        }
        if(currentID != -1) {
        // Current Kampfbund parameters
            currentShortName = item[currentID].SHORT_NAME;
            currentName = item[currentID].NAME;
            currentRank = item[currentID].RANK_NAME[item[currentID].rank];
        } else {
        // No membership in any Kampfbund
            currentShortName = "";
            currentName = "";
            currentRank = "";
        }
    }
    
    /**
     * Create default data table for 'kampfbund' variables
     * @param dataConstants
     * @return 
     */
    private String[][] defaultVariables(String[][] dataConstants) {
        String[][] dataVariables = new String[dataConstants.length][4];
        int shortNameColumn = -1;
        for(int i = 0; i < dataConstants[0].length; i++) {
            if(dataConstants[0][i].equals("SHORT_NAME")) shortNameColumn = i;
        }
        if(shortNameColumn > -1) {
            dataVariables[0][0] = "SHORT_NAME";
            dataVariables[0][1] = "membership";
            dataVariables[0][2] = "reputation";
            dataVariables[0][3] = "rank";
            for(int i = 1; i < dataVariables.length; i++) {
                dataVariables[i][0] = dataConstants[i][shortNameColumn];
                dataVariables[i][1] = "false";
                dataVariables[i][2] = "0";
                dataVariables[i][3] = "0";
            }
        } else {
            System.err.println("SHORT_NAME column was not found in " + 
                    "kampfbund_constants.txt!");
        }
        return dataVariables;
    }
    
    public void renewVariables() {
    // Assign starting values to variables (new game)
        currentID = -1; // no membership by default
        currentShortName = "";
        currentName = "";
        currentRank = "";
        for(kampfbundItem itm : item) {
            itm.membership = false;
            itm.reputation = 0;
            itm.rank = 0;
        }
    }
    
    /**
     * 
     * @param selectedEffects
     * @param totalEffects 
     */
    public void updateReputation(String[] selectedEffects) {
        String reputationPlus, reputationMinus;
        for(kampfbundItem itm : item) {     
            System.out.println("Kampfbund parameters (before): " + itm.SHORT_NAME
                    + "/" + itm.reputation); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            
            for(String effect : selectedEffects) {
                // Positive effects, e.g. 'STA+2'
                reputationPlus = itm.SHORT_NAME + "+";
                if(effect.startsWith(reputationPlus)) {
                // If 'STA+2' begins with 'STA+' add '2' to reputation
                    itm.reputation += Integer.parseInt(effect.substring(
                            reputationPlus.length()));
                }
                
                // Negative effects, e.g. 'STA-2'
                reputationMinus = itm.SHORT_NAME + "-";
                if(effect.startsWith(reputationMinus)) {
                // If 'STA-2' begins with 'STA-' substract '2' from reputation
                    itm.reputation -= Integer.parseInt(effect.substring(
                            reputationMinus.length()));
                }
            }           
            
            System.out.println("Kampfbund parameters (before): " + 
                    itm.SHORT_NAME + "/" + itm.reputation); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
        }
        // Define variables (actual Kampfbund membership & rank)
        this.defineCurrentKampfbund();
    }
    
    /**
     * 
     * @param selectedEffects
     * @param totalEffects 
     */
    public void updateMembership(String[] selectedEffects) {
        String membershipPlus, membershipMinus;
        for(kampfbundItem itm : item) {     
            System.out.println("Kampfbund membership (before): " + itm.SHORT_NAME
                    + "/" + itm.membership); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
            
            for(String effect : selectedEffects) {
                // Join kampfbund, e.g. 'STA+2'
                membershipPlus = itm.SHORT_NAME + "+";
                if(effect.equals(membershipPlus)) {
                // If 'STA+' equals 'STA+' join kampfbund & left all others
                    for(kampfbundItem kpfb : item) { 
                        kpfb.membership = false;
                    }
                    itm.membership = true;
                }
                
                // Left kampfbund, e.g. 'STA-2'
                membershipMinus = itm.SHORT_NAME + "-";
                if(effect.equals(membershipMinus)) {
                // If 'STA-' equals 'STA-' left kampfbund
                    itm.membership = false;
                }
            }           
            
            System.out.println("Kampfbund membership (before): " + 
                    itm.SHORT_NAME + "/" + itm.membership); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! tmp !!!!!!!!!!!!!!!!!!!!!!!!
        }
        // Define variables (actual Kampfbund membership & rank)
        this.defineCurrentKampfbund();
    }
    
    /**
     * Get String[][] array for writing in .txt file
     * @return 
     */
    @Override
    String[][] getDataExport() {
        System.out.println("kampfbund.getDataExport: step1"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        String[][] dataExport = new String[item.length + 1][4];
        dataExport[0][0] = "SHORT_NAME";
        dataExport[0][1] = "membership";
        dataExport[0][2] = "reputation";
        dataExport[0][3] = "rank";          
        for(int i = 0; i < item.length; i++) {
            dataExport[i + 1][0] = item[i].SHORT_NAME;
            dataExport[i + 1][1] = Boolean.toString(item[i].membership);
            dataExport[i + 1][2] = Integer.toString(item[i].reputation);
            dataExport[i + 1][3] = Integer.toString(item[i].rank);
        }  
        System.out.println("kampfbund.getDataExport: step2"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        return dataExport;
    } 
}


class kampfbundItem {
    final String SHORT_NAME;
    final String NAME;
    final String[] RANK_NAME;
    final String[] RANK_REPUTATION_REQUIREMENT;
    boolean membership;
    byte reputation;
    byte rank;
	
    kampfbundItem(String[] headerConstants, String[] dataConstants, 
            String[] headerVariables, String[] dataVariables) {
        // Kampfbund constants (names, ranks etc)
        byte lineShortName =-1;
        byte lineName = -1;
        byte lineRankName = -1;
        byte lineRankReputationRequirement = -1;
        for(byte i = 0; i < headerConstants.length; i++) {
            switch (headerConstants[i]) {
                case "SHORT_NAME": 
                    lineShortName = i;
                    break;
                case "NAME": 
                    lineName = i;
                    break;
                case "RANK_NAME": 
                    lineRankName = i;
                    break;
                case "RANK_REPUTATION_REQUIREMENT": 
                    lineRankReputationRequirement = i;
                    break;
            }
        }
        
	if(lineShortName != -1) {
            SHORT_NAME = dataConstants[lineShortName];
        } else {
            SHORT_NAME = ""; // change by log entry!!!
        }
        if(lineName != -1) {
            NAME = dataConstants[lineName];
        } else {
            NAME = ""; // change by log entry!!!
        }
        if(lineRankName != -1) {
            RANK_NAME = dataConstants[lineRankName].split("#");
        } else {
            RANK_NAME = null; // change by log entry!!!
        }
        if(lineRankReputationRequirement != -1) {
            RANK_REPUTATION_REQUIREMENT = 
                    dataConstants[lineRankReputationRequirement].split("#");
        } else {
            RANK_REPUTATION_REQUIREMENT = null; // change by log entry!!!
        }    
        
        // If variable strings are not empty
        if(headerVariables != null && dataVariables != null) {
            for(int i = 0; i < headerVariables.length; i++) {                
                switch (headerVariables[i]) {
                    case "membership": 
                        if(dataVariables[i].equals("true")) {
                            membership = true;
                        } else {
                            membership = false;
                        } 
                        break;
                    case "reputation": 
                        reputation = Byte.parseByte(dataVariables[i]);
                        break;
                    case "rank": 
                        rank = Byte.parseByte(dataVariables[i]);
                        break;
                }
            }
        }
    }
	
    // In case 'kampfbund_variables.txt' is absent
    kampfbundItem(String[] headerConstants, String[] dataConstants) {
        this(headerConstants, dataConstants, null, null);
    }
}