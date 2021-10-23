//this will store the entire class symbol table for the program

public class CompleteTable extends AbstractTable {
    CompleteTable(){
        Global = new SymbolTable();
    }

    //For testing, prints out tables / structure
    public void prntTable() {
        //Print classes of main
        System.out.println("Classes in Main: ");
        System.out.println(Global.classes);

        //Print methods of each class
        for (String className: Global.classes.keySet()) {
            ClassTable tempTable = Global.classes.get(className);
            System.out.println("    Vars in class " + className + ": ");

            //Print vars of a class
            for (String varName: tempTable.vars.keySet()) {
                System.out.println("        " + varName);
            }

            System.out.println("    Methods in class " + className + ": ");
            for (String methodName: tempTable.methods.keySet()) {
                MethodTable tempTable2 = tempTable.methods.get(methodName);
                System.out.println("      " + methodName);

                //Print params of each method
                System.out.println("        Params:");
                for (String paramsName: tempTable2.params.keySet()) {
                    System.out.println("            " + paramsName);
                }

                //Print local vars of each method
                System.out.println("        Local vars:");
                for (String varName: tempTable2.locals.keySet()) {
                    System.out.println("            " + varName + " " + tempTable2.locals.get(varName));
                }
            }
        }
    }

}