import java.util.HashMap;

public class OneMethod extends AbstractTable {
    public String methodName;
    public HashMap<String, OneVar> params;
    public HashMap<String, OneVar> locals;
    public String inClass;  //class the method belongs to
    public String returnType;

    //Constructor
    public OneMethod(){
        methodName = "none";
        inClass = "none";
        params = new HashMap<String, OneVar>();
        locals = new HashMap<String, OneVar>();
        returnType = "none";
    }

    public OneMethod(String name, String retType, String classLoc){
        methodName = name;
        inClass = classLoc;
        params = new HashMap<String, OneVar>();
        locals = new HashMap<String, OneVar>();
        returnType = retType;
    }

    public void addParam(String name, OneVar varData){
        params.put(name, varData);
    }

    public void addLocal(String name, OneVar varData){
        locals.put(name, varData);
    }
}
