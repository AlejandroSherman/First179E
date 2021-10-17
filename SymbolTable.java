import java.util.HashMap; // system library

//Abstract class, other tables inherit from this

//argu.Global.addClass(n.f1.f0.tokenImage, classtable);
//.isClassType())

public class SymbolTable extends GlobalTable {
   
    public HashMap<String, String> table = new HashMap<String, String>();


    public SymbolTable() {
        //System.out.println("Got called in Symbol table.");
    }

    public SymbolTable(String name, String type) {

    }

    public void addVariable(String name, TypeTable typeTable) {
        String type;
        type = typeTable.Type;
        table.put(name, type);
    }

    public boolean isClassType(){
        return false;
    }

    public void addClass(String name, ClassTable classtable){

    }


}