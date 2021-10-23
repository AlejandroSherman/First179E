import java.util.HashMap;

public class MethodTable extends AbstractTable {
    public String method_name;
    public TypeTable type;
    public HashMap<String, TypeTable> params;
    public HashMap<String, TypeTable> locals;
    public String ofClass; //which class the method belongs to

    //Constructor
    public MethodTable(){
        //error if this is called
    }

    //new MethodTable("main", classID, new TypeTable("main", classID);
    public MethodTable(String name, String classLoc, TypeTable typeData){
        method_name = name;
        type = typeData;
        params = new HashMap<String, TypeTable>();
        locals = new HashMap<String, TypeTable>();
    }

    public void addParam(String name, TypeTable type){
        params.put(name, type);
    }

    public void addLocal(String name, TypeTable type){
        locals.put(name, type);
    }

}
