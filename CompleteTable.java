//this will store the entire class symbol table for the program
public class CompleteTable extends AbstractTable {
    CompleteTable(){
        Global = new SymbolTable();
        GlobalVTables = new AllVTables();
    }

    //For testing, prints out tables / structure
    public void prntTable() {
        //Print classes of main

        //Print methods of each class
        for (String className: Global.classes.keySet()) { 
            OneClass tempClass = Global.classes.get(className); 
            System.out.println("Class " + className + ": ");
            if(!tempClass.superClass.equals("none")) System.out.println("    Super Class: " + tempClass.superClass);
            if(!tempClass.children.isEmpty()) System.out.println("    Children: " + tempClass.children);
            if(!tempClass.vars.isEmpty()) System.out.println("    Vars: ");
           
            //Print vars of class
            for (String varName: tempClass.vars.keySet()) {
                OneVar tempVar = tempClass.vars.get(varName);
                System.out.println("        " + tempVar.varType +" " + varName);
            } 

            System.out.println("    Methods: ");
            for (String methodName: tempClass.methods.keySet()) {
                OneMethod tempMethod = tempClass.methods.get(methodName);
                System.out.println("        " + methodName);

                //Print params of method
                if(!tempMethod.params.isEmpty()) System.out.println("            Params:");
                for (String paramsName: tempMethod.params.keySet()) {
                    OneVar tempVar = tempMethod.params.get(paramsName);
                    System.out.println("                " + tempVar.varType + " " + paramsName);
                }

                //Print local vars of method
                if(!tempMethod.locals.isEmpty()) System.out.println("            Local vars:");
                for (String varName: tempMethod.locals.keySet()) {
                    OneVar tempVar = tempMethod.locals.get(varName);
                    System.out.println("                " + tempVar.varType +" " + varName);
                }

                //Print return type
                System.out.println("            Return Type: " + tempMethod.returnType);

            } 
        }
    }

}
