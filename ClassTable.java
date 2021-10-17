import java.util.*;

public class ClassTable extends SymbolTable  {
    //ClassTable classtable = new ClassTable(n.f1.f0.tokenImage, "java.lang.Object", argu.Global);
    //classtable.addMethod("main", methodtable);
    //.addVariable(n.f1.f0.tokenImage, type);
    
    public HashMap<String, MethodTable> cTable;
    public HashMap<String, String> localVars = new HashMap<String, String>(); 

    public ClassTable(String name, String superClass, SymbolTable mTable) {
        //System.out.println("Got called in ClassTable.");
    }

    public void addMethod(String classHub, MethodTable methodtable) {
        cTable.put(classHub,methodtable);
    }

    @Override
    public boolean isClassType(){
        return true;
    }

    @Override
    public void addVariable(String name, TypeTable typeTable) {
        String type;
        type = typeTable.Type;
        localVars.put(name, type);
    }
}
