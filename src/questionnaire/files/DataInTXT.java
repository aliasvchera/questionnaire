
package questionnaire.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
// import java.io.FileReader;
// import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Igor 'Alias' Shutyi <aliasvchera@gmail.com>
 */
// Process data in TXT files
public class DataInTXT {
    //Separator 'vertical bar with spaces on both sides'
    private static final String SPLIT_BY = " \\| "; 
    private static final String SPLIT_BY_EXPORT = " | "; 
    
    public static String[][] readTXT(String FilePath) {		
        BufferedReader bReader = null;
        String line;
        ArrayList<String[]> lineSplit = new ArrayList<String[]>();
        String [][] dataSplit = null;
                
        try {
            // bReader = new BufferedReader(new FileReader(FilePath));
            bReader = 
                    new BufferedReader(new InputStreamReader(
                            new FileInputStream(FilePath), "windows-1251")); // Change by variable!!!!!!!!!!!!!!!!!
            while ((line = bReader.readLine()) != null) {  	
                // use 'vertical bar with spaces on both sides' as separator
                lineSplit.add(line.split(SPLIT_BY));                
            }
            
            dataSplit = new String[lineSplit.size()][]; // ????? delete ?????
            lineSplit.toArray(dataSplit);
            lineSplit.clear();
            System.gc();

        } catch (FileNotFoundException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        } finally {
            if (bReader != null) {
                try {
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return dataSplit; 
    }

    
    public static void writeTXT(String FilePath, String[][] dataExport, 
            Boolean addToFile) {        
        /* try (BufferedWriter bWriter = 
                new BufferedWriter(new FileWriter(FilePath, addToFile))) { */
        try (BufferedWriter bWriter = 
                    new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(FilePath, addToFile), "windows-1251"))) { // Change by variable!!!!!!!!!!!!!!!!!
            boolean firstLine = true;
            
            for(String[] lineItems : dataExport) {
                if(firstLine) {
                    // start new line if only data is added to existing file
                    if(addToFile) bWriter.newLine();
                    firstLine = false;
                } else {
                    // start new line
                    bWriter.newLine();
                }
                for(byte i = 0; i < lineItems.length; i++) { 
                    if(i > 0) bWriter.write(SPLIT_BY_EXPORT);
                    // change "\n" by "###"
                    // ... !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    bWriter.write(lineItems[i]);
                }
            }
        } catch (IOException e) {
            System.out.println("BufferedWriter error!");
        }
    }
    
    
    public static void writeInLog(String message) {
        Date now = new Date();
        String[][] messageText = new String[1][2];
        messageText[0][0] = now.toString();
        messageText[0][1] = message;
        System.out.println(System.getProperty("user.dir") + "\\log.txt"); // <<<<<<<<<<<<< delete >>>>>>>>>>>>>>>>>>>
        writeTXT(System.getProperty("user.dir") + "\\log.txt", messageText, true);
    }
}
