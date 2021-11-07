public class OneVar extends AbstractTable{ 
    public String varName;
    public String varType;
    public String inClass;
    public String inMethod;

    OneVar(){
        varName = "none";
        varType = "none";
        inMethod = "none";
        inClass = "none";
    }

    OneVar(String name, String type, String classLoc, String methodLoc){
        varName = name;
        varType = type;
        inMethod = methodLoc;
        inClass = classLoc;
    }
}
