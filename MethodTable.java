import java.util.HashMap;

public class MethodTable extends AbstractTable {
    public String method_name;
    public TypeTable type;
    public HashMap<String, TypeTable> params;
    public HashMap<String, String> locals;
    public String ofClass; //which class the method belongs to
    public String returnType;

    //Constructor
    public MethodTable(){
    }

    public MethodTable(String name, String classLoc, TypeTable typeData){
        MethodName = name;  //Set global
        method_name = name; //Set local
        type = typeData;
        params = new HashMap<String, TypeTable>();
        locals = new HashMap<String, String>();
    }

    public MethodTable(String name, String classLoc, String ret_type,TypeTable typeData){
        MethodName = name;  //Set global
        method_name = name; //Set local
        type = typeData;
        params = new HashMap<String, TypeTable>();
        locals = new HashMap<String, String>();
        returnType = ret_type;
    }


    public void addParam(String name, TypeTable typeData){
        params.put(name, typeData);
    }

    public void addLocal(String name, TypeTable typeData){
        locals.put(name, typeData.dataType);
    }



}
