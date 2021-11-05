import java.util.HashMap;

public class AllVTables {
    public HashMap<String, VTable> vtables;

    public AllVTables(){
        vtables = new HashMap<String,VTable>();
    }

    public void printTables(){
<<<<<<< Updated upstream
        for (String currTable: vtables.keySet()) {
            VTable tempTable = vtables.get(currTable);
            tempTable.printVTable();
=======
        //Print VTables in order
        for (Integer i=0 ; i < vtables.size() ; i++) {

            for (String currTable: vtables.keySet()) {
                VTable tempTable = vtables.get(currTable);
                //Print VTables in order
                if(tempTable.index == i) tempTable.printVTable();
            }

>>>>>>> Stashed changes
        } 
    }
}
