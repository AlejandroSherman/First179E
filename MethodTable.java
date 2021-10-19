import java.util.HashMap;
import java.util.Map;


public class MethodTable {
    public String method_name;
    public String return_type;
    public Map<String, String> params;
    public Map<String, String> locals;

    //Constructor
    public MethodTable(){
        method_name = null;
        return_type = null;
        params = new HashMap<String, String>();
        locals = new HashMap<String, String>();
    }
    public MethodTable(String name, String ret_type){
        method_name = name;
        return_type = ret_type;
        params = new HashMap<String, String>();
        locals = new HashMap<String, String>();
    }

}
