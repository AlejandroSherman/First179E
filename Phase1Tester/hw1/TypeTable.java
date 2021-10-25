import java.util.HashMap;

public class TypeTable extends AbstractTable{
    
    public String dataType;

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
