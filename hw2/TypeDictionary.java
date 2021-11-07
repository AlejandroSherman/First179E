import java.util.HashMap;

public class TypeDictionary {
    public HashMap<String, String> typeMap;

    TypeDictionary(){
        typeMap = new HashMap<String, String>();
        typeMap.put("IntegerLiteral", "Integer");
        typeMap.put("IntegerType", "Integer");
        typeMap.put("BooleanType", "Boolean");
        typeMap.put("TrueLiteral", "Boolean");
        typeMap.put("FalseLiteral", "Boolean");
        typeMap.put("ArrayAllocationExpression", "Array");
        typeMap.put("ArrayType", "Array");
        typeMap.put("Identifier", "(Class)");
    }

    public String getRealType(String dataType){
        return typeMap.get(dataType);
    }
}
