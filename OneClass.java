import java.util.*;


public class OneClass extends AbstractTable {
    public String className;
    public String superClass;
    public List<String> children;
    public HashMap<String, OneVar> vars;
    public HashMap<String, OneMethod> methods;

    //Constructor
    public OneClass(){
        className = "none";
        superClass = "none";
        children = new LinkedList<String>();
        vars = new HashMap<String, OneVar>();
        methods = new HashMap<String, OneMethod>();
    }

    public OneClass(String name){
        className = name;
        superClass = "none";
        children = new LinkedList<String>();
        vars = new HashMap<String, OneVar>();
        methods = new HashMap<String, OneMethod>();
    }

    public void addMethod(String name, OneMethod mthd){
        methods.put(name, mthd);
    }

    public void addVar(String name, OneVar varData){
        vars.put(name, varData);
    }
}
