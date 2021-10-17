import java.util.*;

public class MethodTable extends SymbolTable {
    //MethodTable methodtable = new MethodTable("main", n.f1.f0.tokenImage, new TypeTable("main", n.f1.f0.tokenImage);
    //methodtable.addParameter(n.f11.f0.tokenImage, type);
    //.addvariable(n.f1.f0.tokenImage, type);

    /*
    public Type returnType;
    public Id name;
    public List<Parameter> parameters;
    public List<VarDecl> varDecls;
    public List<Statement> statements;
    */

    public HashMap<String, String> params = new HashMap<String, String>();
    public HashMap<String, String> localVars = new HashMap<String, String>(); 

    public MethodTable(String classHub, String name, TypeTable tTable) {
        //System.out.println("Got called in Method table.");
    }

    public void addParameter(String name, TypeTable type) {

    }

    @Override
    public void addVariable(String name, TypeTable typeTable) {
        String type;
        type = typeTable.Type;
        localVars.put(name, type);
    }
}
