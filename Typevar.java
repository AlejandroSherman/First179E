import java.util.*;
import syntaxtree.*;

public class Typevar{
    public static String typeName(Node n){
        if(n is instanceof MainClass){
            return "MainClass";
        }
        else if (n is instanceof ClassDeclaration){
            return "ClassDeclaration";
        }
    }


}