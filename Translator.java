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
    public void addFunction(){
        /*
        Each line of the body of a function is one of:
            - code label: Label:
            - assignment: Location = Value
            - branch: if Value goto CodeAddress
            - goto: goto CodeAddress
            - function call: call FunctionAddress (Args...)
            - function return: ret Value
            - call to built-in operation: OpName (Args...)
         */
    }

    // Assignment
    // There are three distinct types of assignment: variable, memory load, memory store
    public void addAssignment(){
        /*
        add variable:
            Value is either an integer literal, a string literal, a variable name, or a label reference :Label.
        */

        /*
        add memory load:
            Memory operations always operate on 4-byte quantities and memory addresses must be 4-byte aligned.
            A memory reference consists of a base address, which is either a label reference or a register.
         */

        /*
        add memory store:
            Memory operations always operate on 4-byte quantities and memory addresses must be 4-byte aligned.
            A memory reference consists of a base address, which is either a label reference or a register.
         */
    }

    // Branch
    // There are two variants of the branch instruction: if and if0
    public void addBranch(){
        /*
        add if:
            The if jumps to CodeLabel if Value is non-zero and falls through to the next instruction otherwise.
         */

        /*
        add if0:
            The if0 does the opposite, jumping to the specified label if Value is zero.
         */
    }

    // Goto
    // The goto instruction is an unconditional jump to the specified target.
    public void addGoto(){
        /*
        add Goto:
            For this phase, goto can only refer to code labels.
         */
    }

    // Function Call
    public void addFunctionCall(){
        /*
        add Function Call:

            Var = call :FunctionLabel (Args...)
                The assignment Var = is optional.
                The arguements list Args... is a whitespace-separated list of value entries: integer literals, variables, or label references.
                The return value of the function is stored in the Var variable.

            Like goto, call can also use a function address loaded from a variable:
                Var = call Var (Args...)
         */
    }

    // Function Return
    public void addFunctionReturn(){
        /*
        The ret instruction returns from a function.
        The return value is optional.
         */
    }


    // *** BUILT-IN OPERATIONS ***

    // Arithmetic
    public void arithmetic(){
        /*
        Basic arithmetic:
            Add, Sub, Mul, Div, Rem, MulS, DivS, RemS, ShiftL, ShiftR, ShiftLA
            The "-S" variants operate on signed integers.
             a = Add(a b)

        Comparison:
            Eq, Ne, Lt, Le, LtS, LeS
            The "-S" variants operate on signed integers.

        Bitwise boolean operators:
            And, Or, Not
         */
    }

    // Memory Allocation
    public void memAlloc(){
        /*
        HeapAlloc:
            take an integer - the number of bytes of memory to allocate.
            Returns the address of newly-allocated memory.

        HeapAllocZ:
            The "-Z" variant initializes the memory to all zero.
         */
    }
    
    // Display Output
    public void output(){
        /* These do not return a value.
        
        PrintInt:
            print out unsigned integers.
            
        PrintIntS:
            print out signed integers.
            
        PrintString:
            print out strings. 

         */
    }
}
