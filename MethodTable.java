import syntaxtree.Type;

import java.util.HashMap;
import java.util.Map;


public class MethodTable {
    public String method_name;
    public Type return_type;
    public Map<String,Type> params;
    public Map<String,Type> locals;

    //Constructor
    public MethodTable(){
        method_name = null;
        return_type = null;
        params = new HashMap<String, Type>();
        locals = new HashMap<String, Type>();
    }
    public MethodTable(String name, Type ret_type){
        method_name = name;
        return_type = ret_type;
        params = new HashMap<String, Type>();
        locals = new HashMap<String, Type>();
    }

}
