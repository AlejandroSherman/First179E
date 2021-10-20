import java.util.HashMap;

public class ClassTable extends AbstractTable {
    public String class_name;
    public String super_class;  //which class this class derives from
    public HashMap<String, TypeTable> vars;
    public HashMap<String,MethodTable> methods;

    //Constructor
    public ClassTable(){
        //error if this is called
    }

    public ClassTable(String className, String superClass, SymbolTable Global){
        class_name = className;
        super_class = superClass;
        vars = new HashMap<String, TypeTable>();
        methods = new HashMap<String, MethodTable>();
        //not sure what to do with global yet
    }
    
    //needs an addMethod
    public void addMethod(String name, MethodTable methodData){
        methods.put(name, methodData);
    }

    public void addVar(String name, TypeTable typeData){
        vars.put(name, typeData);
    }
}
