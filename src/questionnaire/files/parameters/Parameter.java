/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package questionnaire.files.parameters;

import questionnaire.files.DataInTXT;
/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
public class Parameter {
    String[][] dataSplitConstants;
    String[][] dataSplitVariables;
    
    Parameter(String parameterFileName) {
        // Define source folder location
        String sourcePath = System.getProperty("user.dir") 
                + "\\data\\parameters\\";

        // Parameter constants (names, rank lists etc) data from .txt file
        dataSplitConstants = DataInTXT.readTXT(sourcePath + 
                parameterFileName + "_constants.txt");

        // Parameter variables (membership, actual rank etc) data from .txt file
        dataSplitVariables = DataInTXT.readTXT(sourcePath + 
                parameterFileName + "_variables.txt");
    }
    
    /** 
     * Save variables in .txt file
     * @param parameterFileName
     * @param addToFile
     */
    public void saveVariables(String parameterFileName, boolean addToFile) { 
        // Define source folder location
        String sourcePath = System.getProperty("user.dir") 
                + "\\data\\parameters\\";
        System.out.println("parameter.saveVariables: step1");
        DataInTXT.writeTXT(sourcePath + 
                parameterFileName + "_variables.txt", getDataExport(), addToFile);
    }
    
    /**
     * Get String[][] array for writing in .txt file - must overrode!
     * @return 
     */
    String[][] getDataExport() {
        System.out.println("parameter.getDataExport");
        return null;
    }
}
