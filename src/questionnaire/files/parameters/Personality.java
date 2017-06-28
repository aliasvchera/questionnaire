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
// Personality features
public class Personality extends Parameter {
    public final String[] NAME;
    public final String[] SURNAME;
    public final String[] YEAR_OF_BIRTH;
    /* public final String NATIONALITY;
    public final String RELIGION; */

    public String name;
    public String surname;
    public String yearOfBirth;
    public String portrait;
    /*public String nationality;
    public String religion;*/	

    public Personality(String parameterFileName) { // "Personality"
        super(parameterFileName);

        // Personality features (constants)
        byte lineFirstName = -1;
        byte lineSecondName = -1;
        byte lineYearOfBirth = -1;
                
        for(byte i = 0; i < dataSplitConstants.length; i++) {
            switch (dataSplitConstants[i][0]) {
                case "NAME": 
                    lineFirstName = i;
                    break;
                case "SURNAME": 
                    lineSecondName = i;
                    break;
                case "YEAR_OF_BIRTH": 
                    lineYearOfBirth = i;
                    break;
            }
        }
        if(lineFirstName != -1) {
            NAME = dataSplitConstants[lineFirstName][1].split("#");
        } else {
            NAME = null; // change by log entry!!!
        }
        if(lineSecondName != -1) {
            SURNAME = dataSplitConstants[lineSecondName][1].split("#");
        } else {
            SURNAME = null; // change by log entry!!!
        }
        if(lineYearOfBirth != -1) {
            YEAR_OF_BIRTH = dataSplitConstants[lineYearOfBirth][1].split("#");            
        } else {
            YEAR_OF_BIRTH = null; // change by log entry!!!
        }

        // Personality features (variables)
        for(byte i = 0; i < dataSplitVariables.length; i++) {
            if(dataSplitVariables[i].length > 1) {
                switch (dataSplitVariables[i][0]) {
                    case "name": 
                        name = dataSplitVariables[i][1];
                        break;
                    case "surname": 
                        surname = dataSplitVariables[i][1];
                        break;
                    case "yearOfBirth": 
                        yearOfBirth = dataSplitVariables[i][1];
                        break;
                    case "portrait": 
                        portrait = dataSplitVariables[i][1];
                        break;
                }
            }
        }		
    }
    
    public void renewVariables() {
    // Assign starting values to variables (new game)
        name = null;
        surname = null;
        yearOfBirth = null;
        portrait = null;
        /* nationality = null;
        religion = null; */
    }
    
    /**
     * Get String[][] array for writing in .txt file
     * @return 
     */
    @Override
    String[][] getDataExport() {
        System.out.println("personality.getDataExport: step1"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        String tmpName = "";
        if(name != null) tmpName = name;
        String tmpSurname = "";
        if(surname != null) tmpSurname = surname;
        String tmpYearOfBirth = "";
        if(yearOfBirth != null) tmpYearOfBirth = yearOfBirth;
        String tmpPortrait = "";
        if(portrait != null) tmpPortrait = portrait;
        String[][] dataExport = {
            {"name", tmpName},
            {"surname", tmpSurname},
            {"yearOfBirth", tmpYearOfBirth},
            {"portrait", tmpPortrait}};
        System.out.println("personality.getDataExport: step2"); // <<<<<<<<<<<<<<<<<<< tmp >>>>>>>>>>>>>>>>>>>>>
        return dataExport;
    }    
}