package symboltable;

import syntaxtree.Type;

import java.util.HashMap;
import java.util.Map;

public class ClassTable {
    public String class_name;
    public Map<String, Type> vars;
    public Map<String,MethodTable> methods;

    //Constructor
    public ClassTable(){
        class_name = null;
        vars = new HashMap<String, Type>();
        methods = new HashMap<String, MethodTable>();
    }

    public ClassTable(String clazz, Map class_fields, Map class_methods){
        class_name = clazz;
        vars = new HashMap<String, Type>(class_fields);
        methods = new HashMap<String, MethodTable>(class_methods);
    }
}
