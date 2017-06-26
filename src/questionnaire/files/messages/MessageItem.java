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

// Question/event item constructor
class MessageItem {
    //Separator = #
    private static final String TEXT_SPLIT_BY = "#"; 
    String name;
    String[] item = null;
    
    MessageItem(String[][] dataSplit, String itemName) {
// System.out.println(itemName); // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> delete <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        name = itemName;
        for (int i = 0; i < dataSplit.length; i++) {
// System.out.println("Name: " + dataSplit[i][0].toString());
// System.out.println("Length: " + dataSplit[i].length);
// System.out.println("Value: " + dataSplit[i][1].toString());
            if(dataSplit[i][0].equals(name)) {
// System.out.println("Name: " + dataSplit[i][1].toString());
                /*
                for (byte j = 1; j <= dataSplit[i].length; j++) {
                    item[j - 1] = dataSplit[i][j];                    
                }
                */
                if(dataSplit[i].length >= 2) {
                    item = dataSplit[i][1].split(TEXT_SPLIT_BY);
                }
                return;
            }
        }            
    }
}
