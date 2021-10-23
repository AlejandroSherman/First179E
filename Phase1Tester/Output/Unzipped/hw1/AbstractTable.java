//This allows us to pass in other Objects other than SymbolTable in the visitors

public class AbstractTable {
    public SymbolTable Global;
    public String MethodName; //no idea how this works either
    public String ClassName;  //no idea how this works either

    // testing
    public void prntTable() {
        System.out.println(Global);
    }
}