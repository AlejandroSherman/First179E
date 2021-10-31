import java.util.HashMap;

public class AllVTables {
    public HashMap<String, VTable> vtables;

    public AllVTables(){
        vtables = new HashMap<String,VTable>();
    }

    public void printTables(){
        for (String currTable: vtables.keySet()) {
            VTable tempTable = vtables.get(currTable);
            tempTable.printVTable();
        } 
    }
}
