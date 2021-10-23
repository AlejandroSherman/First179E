//TypeTable indentifies: class or method (and typetable?)
import java.util.HashMap;

public class TypeTable extends AbstractTable{

    public HashMap<String, String> vars; //name, type

    //new TypeTable("main", classID)
    //new TypeTable("main", classID, argTable.Global);
    //new TypeTable(n.f0.f0.which, argTable.MethodName, argTable.ClassName, argTable.Global);

    TypeTable(String methodName, String className){
        MethodName = methodName;
        ClassName = className;

    }

    TypeTable(String methodName, String className, SymbolTable Global){
        MethodName = methodName;
        ClassName = className;
    }

    TypeTable(Integer grammerChoice, String methodName, String className, SymbolTable Global){
        MethodName = methodName;
        ClassName = className;
    }

    public void addVar(String name, String type){
        vars.put(name, type);
    }
}