public class Translator { // converts java -> vapor

    // Identifiers
    // Identifiers are used for two things: variables and labels.
    public void addID(String name, String value) {
        try {
            if(name == "variable") {
            /*
               add variable:
                   variables are always local to a function;
                   variable names must be unique within a function.
                   the first character cannot be a digit or a dot.
             */
            }
            else if(name == "label") {
            /*
               add label:
                   There are three types of labels:
                        data labels
                        function labels
                        code labels
                   All label names must be unique across the entire program.
             */
            }
            else {
                throw new Exception("Error: Identifier " + name + " is not a variable or label.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Data Segments
    // Vapor has two types of global data segments:const and var.
    public void addData(String name, String value){
        try {
            if(name == "const") {
            /*
               add const:
                    A const segment is for read-only data (like virtual function tables).
             */
            }
            else if(name == "var") {
            /*
               add var:
                    A var segment is for global mutable data.
             */
            }
            else{
                throw new Exception("Error: global data segment " + name + " is not a const or var.");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Functions

    // Assignment

    // Branch

    // Goto

    // Function Call

    // Function Return



    // *** BUILT-IN OPERATIONS ***

    // Arithmetic

    // Memory Allocation
    
    // Display Output

}
