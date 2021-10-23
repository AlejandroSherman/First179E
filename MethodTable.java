import java.util.HashMap;

public class MethodTable extends AbstractTable {
    public String method_name;
    public TypeTable type;
    public HashMap<String, String> params;
    public HashMap<String, String> locals;
    public String ofClass; //which class the method belongs to

    //Constructor
    public MethodTable(){
        //error if this is called
    }

    //new MethodTable("main", classID, new TypeTable("main", classID);
    public MethodTable(String name, String classLoc, TypeTable typeData){
        MethodName = name;  //Set global
        method_name = name; //Set local
        type = typeData;
        params = new HashMap<String, String>();
        locals = new HashMap<String, String>();
    }

    public void addParam(String name, String typeData){
        params.put(name, typeData);
    }

    public void addLocal(String name, String typeData){
        //System.out.println("Method Local: I am inserting " + name + "from " + typeData.ClassName +  "->" + typeData.MethodName);
        locals.put(name, typeData);
    }



}
