import java.util.HashMap; // system library

//This is just for testing at the moment, not actually setup yet

public class SymbolTable {
   
    HashMap<String, String> tempTable = new HashMap<String, String>();

    public SymbolTable(String name, String type) {
        tempTable.put(name, type);
        System.out.println(tempTable);
    }


}