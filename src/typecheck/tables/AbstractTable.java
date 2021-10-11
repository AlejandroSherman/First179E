package typecheck;

//This file was created based on the missing definition of later instances, at this point
//Im unsure of what declarations/maps can be present in this file
//This class is probably extended by ClassTable and MethodTable
public class AbstractTable {
    
    HashMap<String, String> symbolTable = new HashMap<String, String>();

    // Insert (keys, values)
    // Not sure exactly what we are suppose to map in the symbol table exactly... or where this would go in code
    // I kinda guessed this is where main table class, not positive though
    symbolTable.put("int", "var a?");
    symbolTable.put("int", "var b?");
    symbolTable.put("string", "var name?");
    symbolTable.put("string", "var prompt?");

    //more on hashmaps in java here: https://www.w3schools.com/java/java_hashmap.asp
    
    System.out.println(symbolTable);

    // also needs  .MethodName, .ClassName, and .Global properties (or templates of since this is abstract?)
    //argu.Global.addClass(n.f1.f0.tokenImage, classtable);
    //So needs a Global property this is a reference to a ClassTable?
    
    public ClassTable getClassTable(String) {
        //To do
    }


}