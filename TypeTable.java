//TypeTable indentifies: class or method (and typetable?)
import java.util.HashMap;

public class TypeTable extends AbstractTable{
    
    public String dataType;
    
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

    TypeTable(String methodName, String className, String dType, SymbolTable Global){
        MethodName = methodName;
        ClassName = className;
        dataType = dType;
    }

    TypeTable(Integer grammerChoice, String methodName, String className, SymbolTable Global){
        MethodName = methodName;
        ClassName = className;
    }

    TypeTable(Integer grammerChoice, String methodName, String className, String dType, SymbolTable Global){
        MethodName = methodName;
        ClassName = className;
        dataType = dType;
    }

}
