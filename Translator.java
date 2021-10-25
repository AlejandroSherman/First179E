public class Translator { // converts java -> vapor

    // Identifiers
    // Identifiers are used for two things: variables and labels.
    public void addID(String name, String value) {
        /* Identifiers can be declared with different types in different scopes.
            Thus, a symbol table that maps identifiers to types is kept during the translation.
            The type of an identifier is either class, method, field or local variable.
            The symbol table keeps a stack of scopes.
            For example:
                at the beginning of a class, a new scope is pushed and the implicit field "this",
                the fields and the methods of the class are added to the scope.
                The main class is handled separately to generate the "Main()" function.
         */
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
        // In the translation of each class, its v-table is translated to a const data segment.
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
        In the translation of each (non-static) method, the implicit this parameter is explicitly added to the parameters of the translated function.
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
        In the translation of the if statement, we label the beginning of the else statement and the end and use the instruction if0 to goto to the else label if the condition fails.
        We goto to the end label at the end of the then statement.
        The while statement can be similarly translated by labeling the start and the end.
        */

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
        In the translation of a method call, if the value of the receiver is zero, a null pointer error is reported.
        The class type and then the v-table of the receiver object can be retrieved from the symbol table.
        The offset of the method can be retrieved from the v-table.
                vt = [rc]      where rc is the receiver
                f = [vt + d]   where d = o * 4,
                                     o is the offset of the method retrieved from the v-table
                r = call f(rc arg1 arg2)
        */

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
            Add, Sub, MulS
            The "-S" variants operate on signed integers.
            a = Add(a b)

        Comparison:
            Eq, Lt, LtS
            The "-S" variants operate on signed integers.
         */
    }

    // Memory Allocation
    public void memAlloc(){
        /*
        In the translation of an object allocation, the class type and then the class record can be retrieved from the symbol table.
        The allocation size is 4 times the record size plus 4.
        This is because a word is four bytes and the first word is used to store a reference to the v-table of the object.
             r  = HeapAllocZ(s)      where s = rs * 4 + 4
                                               rs = record size
            [r] = :ClassVTableName
         */
        
        /*
        HeapAllocZ:
            takes an integer - the number of bytes of memory to allocate.
            Returns the address of newly-allocated memory.
            The "-Z" variant initializes the memory to all zero.
            When a field access is translated to a memory operation, the class record is used to determine the offset of the object memory that the memory operation should access.
         */
    }

    // Display Output
    public void output(){
        /*
        PrintIntS:
            print out signed integers.
            Does not return a value.

        Error:
            is for abnormal program termination (for errors like null pointer deferences, etc).
            It takes a string message to display to the user.

        DebugPrint:
            is only for debugging.
            It accepts any number of values and prints out the interpreter's internal representation of the value.
            This can be useful for getting information about pointers.
         */
    }
}
