import java.util.HashMap; // system library

//This is just for testing at the moment, not actually setup yet
//Best attempt, doesn't work

public class SymbolTable {
   /*
    HashMap<String, String> tempTable = new HashMap<String, String>();

    public SymbolTable(String name, String type) {
        tempTable.put(name, type);
        System.out.println(tempTable);
    }
    */
    private String name;
    private static Map<String, SymbolTable> map = new HashMap<>;
    //.put found in scope.java
    private SymbolTable(String s) {
        name = n;
    }

    public String getName(){
        return name;
    }

    public SymbolTable getTable(String s){
        return map.compute(s);
    }


}