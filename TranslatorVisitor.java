//Second pass, code generation
import syntaxtree.*;
import visitor.GJDepthFirst;

import java.util.LinkedList;

public class TranslatorVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    /*******************
     *  CLASS GLOBALS  *
     *******************/
    Integer classIndex = 0;
    Integer funcIndex = 0;
    int tempCounter = 0;
    int ifCounter = 0;
    int ifLabelCounter = 0;
    int tabCounter = 0;
    LinkedList<String> temps = new LinkedList<String>();
    LinkedList<String> statements = new LinkedList<String>();
    boolean isPrintStatement = true;
    boolean isPrimaryExpression = false;
    boolean isAssign = false;
    boolean isNew = true;

    /**********************
     *  HELPER FUNCTIONS  *
     **********************/

    private String tab(){
        String tabs = "";
        for(int i = 0; i < tabCounter; i++){
            tabs += "  ";
        }
        return tabs;
    }

    /******************
     *  DECLARATIONS  *
     ******************/

    @Override
    public AbstractTable visit(Goal n, AbstractTable argu){
        argu.Global.CurrentClass = new OneClass();
        argu.Global.CurrentMethod = new OneMethod();
        argu.Global.CurrentVar = new OneVar();
        argu.Global.classOrmethod = "none";
        argu.GlobalVTables.CurrentVTable = new VTable();

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> MainClass()
        n.f1.accept(this, argu); // f1 -> ( TypeDeclaration() )*
        n.f2.accept(this, argu); // f2 -> <EOF>
        return _ret;
    }

    @Override
    public AbstractTable visit(MainClass n, AbstractTable argu) {
        argu.Global.classOrmethod = "class";
        OneClass tempClass = new OneClass("Main");
        OneMethod tempMethod = new OneMethod("main", "void", "Main");
        argu.Global.CurrentClass = tempClass;
        argu.Global.CurrentMethod = tempMethod;

        //Vapor stuff
        VTable tempTable = argu.GlobalVTables.vtables.get("Main");
        OneFunction tempFunc = new OneFunction("Main",0,tempTable);
        argu.GlobalCodeGen.addFunc("Main", tempFunc);
        funcIndex=0;
        argu.GlobalVTables.CurrentFunc = tempFunc;
        argu.GlobalVTables.CurrentVTable = tempTable;

        //System.out.println("\n\nfunc Main()");
        tabCounter++;

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> "class"
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> "{"
        n.f3.accept(this, argu); // f3 -> "public"
        n.f4.accept(this, argu); // f4 -> "static"
        n.f5.accept(this, argu); // f5 -> "void"
        n.f6.accept(this, argu); // f6 -> "main"
        n.f7.accept(this, argu); // f7 -> "("
        n.f8.accept(this, argu); // f8 -> "String"
        n.f9.accept(this, argu); // f9 -> "["
        n.f10.accept(this, argu); // f10 -> "]"
        n.f11.accept(this, argu); // f11 -> Identifier()
        n.f12.accept(this, argu); // f12 -> ")"
        n.f13.accept(this, argu); // f13 -> "{"
        n.f14.accept(this, argu); // f14 -> ( VarDeclaration() )*

        n.f15.accept(this, argu); // f15 -> ( Statement() )*

        n.f16.accept(this, argu); // f16 -> "}"
        n.f17.accept(this, argu); // f17 -> "}"

        tempFunc.code += tab() + "ret\n";
        //System.out.println(tab() + "ret");
        tabCounter = 0;
        return _ret;
    }

    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argu) {
        argu.Global.classOrmethod = "class";
        String className = n.f1.f0.toString();
        OneClass tempClass = new OneClass(className);
        argu.Global.CurrentClass = tempClass;

        //Vapor Stuff
        VTable tempTable = argu.GlobalVTables.vtables.get(className);
        argu.GlobalVTables.CurrentVTable = tempTable;
        funcIndex=0;

        n.f0.accept(this, argu); // f0 -> "class"

        //VTable vtable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        //System.out.println("t." + tempCounter + " = " + memAlloc(vtable));

        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> "{"
        n.f3.accept(this, argu); // f3 -> ( VarDeclaration() )*
        n.f4.accept(this, argu); // f4 -> ( MethodDeclaration() )*
        n.f5.accept(this, argu); // f5 -> "}"
        return null;
    }

    @Override
    public AbstractTable visit(ClassExtendsDeclaration n, AbstractTable argu) {
        argu.Global.CurrentClass.children.add(n.f1.f0.tokenImage);

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> "class"
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> "extends"
        n.f3.accept(this, argu); // f3 -> Identifier()
        n.f4.accept(this, argu); // f4 -> "{"
        n.f5.accept(this, argu); // f5 -> ( VarDeclaration() )*
        n.f6.accept(this, argu); // f6 -> ( MethodDeclaration() )*
        n.f7.accept(this, argu); // f7 -> "}"
        return _ret;
    }

    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argu){
        argu.Global.classOrmethod = "method";
        String methodName = n.f2.f0.toString();
        String returnType = n.f1.f0.choice.getClass().getSimpleName();
        returnType = argu.dictionary.getRealType(returnType);
        OneMethod tempMethod = new OneMethod(methodName, returnType, argu.Global.CurrentClass.className);
        argu.Global.CurrentMethod = tempMethod;

        //Vapor Stuff
        VTable tempTable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        OneFunction tempFunc = new OneFunction(methodName ,funcIndex, tempTable);
        argu.GlobalCodeGen.addFunc(methodName, tempFunc);
        funcIndex++;
        argu.GlobalVTables.CurrentFunc = tempFunc;
        argu.GlobalVTables.CurrentVTable = tempTable;

        String tempName = argu.GlobalCodeGen.functions.get(methodName).ofClass + "." + n.f2.f0.tokenImage;
        //System.out.print("\nfunc " + tempName + "(");
        tabCounter++;

        n.f0.accept(this, argu); // f0 -> "public"
        n.f1.accept(this, argu); // f1 -> Type()
        n.f2.accept(this, argu); // f2 -> Identifier()
        n.f3.accept(this, argu); // f3 -> "("
        n.f4.accept(this, argu); // f4 -> ( FormalParameterList() )?
        //System.out.println(")");
        n.f5.accept(this, argu); // f5 -> ")"
        n.f6.accept(this, argu); // f6 -> "{"
        n.f7.accept(this, argu); // f7 -> ( VarDeclaration() )*
        n.f8.accept(this, argu); // f8 -> ( Statement() )*
        n.f9.accept(this, argu); // f9 -> "return"

        tempFunc.code += tab() + "ret ";
        //System.out.print(tab() + "ret ");
        isPrimaryExpression = true;

        n.f10.accept(this, argu); // f10 -> Expression()
        n.f11.accept(this, argu); // f11 -> ";"
        n.f12.accept(this, argu); // f12 -> "}"


        tabCounter = 0;
        return null;
    }

    @Override
    public AbstractTable visit(VarDeclaration n, AbstractTable argu) {
        String varType = n.f0.f0.choice.getClass().getSimpleName();
        varType = argu.dictionary.getRealType(varType);
        String varName = n.f1.f0.tokenImage;

        //argu.Global.CurrentVar.varName = varName; // test
        //System.out.println("    " + varType + " "  + varName );

        OneVar tempVar = new OneVar(varName, varType, argu.Global.CurrentClass.className, argu.Global.CurrentMethod.methodName);
        argu.Global.CurrentVar = tempVar;

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> Type()
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> ";"
        return _ret;
    }

    /***************
     *  PARAMETERS *
     ***************/

    @Override
    public AbstractTable visit(FormalParameter n, AbstractTable argu) {
        String varType = n.f0.f0.choice.getClass().getSimpleName();
        varType = argu.dictionary.getRealType(varType);
        String varName = n.f1.f0.tokenImage;
        OneVar tempVar = new OneVar(varName, varType, argu.Global.CurrentClass.className, argu.Global.CurrentMethod.methodName);
        argu.Global.CurrentVar = tempVar;

        //Vapor Stuff
        argu.GlobalVTables.CurrentFunc.params.add(" "+ varName);
        //System.out.print("this " + varName);

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> Type()
        n.f1.accept(this, argu); // f1 -> Identifier()
        return _ret;
    }

    /********************
     *  LITERAL VALUES  *
     ********************/

    @Override
    public AbstractTable visit(Type n, AbstractTable argu) {
        AbstractTable _ret=null;
        n.f0.accept(this, argu);  //f0 -> ArrayType() | BooleanType() | IntegerType() | Identifier()
        return _ret;
    }

    @Override
    public AbstractTable visit(Identifier n, AbstractTable argu) {
        if( argu.Global.CurrentVar.varType.equals("(Class)") ){
            argu.Global.CurrentVar.varType = n.f0.tokenImage;
        }
        if( argu.Global.CurrentMethod.returnType.equals("(Class)") ){
            argu.Global.CurrentMethod.returnType = n.f0.tokenImage;
        }

        n.f0.accept(this,argu); // f0 -> <IDENTIFIER>
        if(isPrimaryExpression) {
            argu.GlobalVTables.CurrentFunc.code += n.f0.tokenImage + "\n";
            //System.out.println(n.f0.tokenImage);
            isPrimaryExpression = false;
        }
        return null;
    }

    @Override
    public AbstractTable visit(IntegerLiteral n, AbstractTable argu) {
        String varValue = n.f0.tokenImage;
        if(isAssign) {
            argu.GlobalVTables.CurrentFunc.code += varValue + "\n";
            //System.out.println(varValue);
            isAssign = false;
        }
        /*else{ // prints MulS(num t.3) and adds "t.1 = [t.0]" to Main. ERROR: adds "t.2 = [t.1]" to Fac.ComputeFac()
            tempCounter++;
            tempFunc.code += tab() + "t." + tempCounter + " = [t." + (tempCounter-1) + "]\n";
            System.out.println(tab() + "t." + tempCounter + " = [t." + (tempCounter-1) + "]");
        }*/


        /*//testing
        if(isPrintStatement){
            argu.GlobalVTables.CurrentFunc.code += varValue;
        }*/


        n.f0.accept(this, argu);
        return null;
    }

    /******************
     *  CONTROL FLOW  *
     ******************/

    @Override
    public AbstractTable visit(IfStatement n, AbstractTable argu) {
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        argu.GlobalVTables.CurrentFunc.code += tab() + "t." + tempCounter + " = ";
        //System.out.print(tab() + "t." + tempCounter + " = ");
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);

        // TODO get unique labels
        ++ifLabelCounter;
        argu.GlobalVTables.CurrentFunc.code += tab() + "if" + ifCounter + " t." + tempCounter + " goto :if" + ifLabelCounter + "_else\n";
        //System.out.println(tab() + "if" + ifCounter + " t." + tempCounter + " goto :if" + ifLabelCounter + "_else");
        ifCounter++;
        tempCounter++;
        // TODO do something inside if statement
        tabCounter++;
        n.f4.accept(this,argu);
        if(isAssign){ // TODO temp fix for assignments
            argu.GlobalVTables.CurrentFunc.code += "THERE WAS NOTHING ASSIGNED!\n";
            //System.out.println("THERE WAS NOTHING ASSIGNED!");
            isAssign = false;
        }
        argu.GlobalVTables.CurrentFunc.code += tab() + "goto :if" + ifCounter + "_end\n";
        //System.out.println(tab() + "goto :if" + ifCounter + "_end");
        tabCounter--;
        n.f5.accept(this,argu);
        argu.GlobalVTables.CurrentFunc.code += tab() + "if" + ifLabelCounter + "_else:\n";
        //System.out.println(tab() + "if" + ifLabelCounter + "_else:");
        // TODO do something inside if-else statement
        tabCounter++;
        n.f6.accept(this,argu);
        tabCounter--;
        argu.GlobalVTables.CurrentFunc.code += tab() + "if1_end:\n";
        //System.out.println(tab() + "if" + ifCounter + "_end:");
        ifCounter = 0;
        tempCounter = 0;
        return null;
    }

    @Override
    public AbstractTable visit(WhileStatement n, AbstractTable argu) {
        //TODO unique labels
        //TODO fill in loop bodies
        argu.GlobalVTables.CurrentFunc.code += tab() + "goto :end\n";
        //System.out.println(tab() + "goto :end");
        argu.GlobalVTables.CurrentFunc.code += tab() + "begin:\n";
        //System.out.println(tab() + "begin:");
        tabCounter++;

        argu.GlobalVTables.CurrentFunc.code += tab() + "<inside begin>\n"; //TODO fill loop begin
        //System.out.println(tab() + "<inside begin>");
        tabCounter--;
        argu.GlobalVTables.CurrentFunc.code += tab() + "end:\n";
        //System.out.println(tab() + "end:");
        tabCounter++;
        argu.GlobalVTables.CurrentFunc.code += tab() + "<inside end>\n"; //TODO fill loop end
        //System.out.println(tab() + "<inside end>");
        argu.GlobalVTables.CurrentFunc.code += tab() + "goto :begin\n";
        //System.out.println(tab() + "goto :begin");
        tabCounter--;

        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
        return null;
    }

    /****************
     *  STATEMENTS  *
     ****************/

    @Override
    public AbstractTable visit(Statement n, AbstractTable argu) {
        n.f0.accept(this, argu);
        return null;
    }

    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argu) {
        String id = n.f0.f0.tokenImage;
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        isAssign = true;
        argu.GlobalVTables.CurrentFunc.code += tab() + id + " = ";
        //System.out.print(tab() + id + " = ");
        n.f2.accept(this,argu);
        return null;
    }

    @Override
    public AbstractTable visit(MessageSend n, AbstractTable argu) {
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
        n.f5.accept(this,argu);
        return null;
    }

    /*****************
     *  EXPRESSIONS  *
     *****************/

    @Override
    public AbstractTable visit(Expression n, AbstractTable argu) {
        n.f0.accept(this,argu);
        return null;
    }

    @Override
    public AbstractTable visit(AllocationExpression n, AbstractTable argu) {
        n.f0.accept(this, argu);
        VTable vtable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        if(isNew) {
            argu.GlobalVTables.CurrentFunc.code += tab() + "t." + tempCounter + " = " + memAlloc(vtable) + "\n";
            //System.out.println(tab() + "t." + tempCounter + " = " + memAlloc(vtable));
            argu.GlobalVTables.CurrentFunc.code += tab() + "[t." + tempCounter + "] = :vmt_" + n.f1.f0.tokenImage + "\n";
            //System.out.println(tab() + "[t." + tempCounter + "] = :vmt_" + n.f1.f0.tokenImage);
            argu.GlobalVTables.CurrentFunc.code += error() + "\n";
            //System.out.println(error()); // error checking (null ptr, etc.)
            isNew = false;
        }
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return null;
    }

    @Override
    public AbstractTable visit(PrimaryExpression n, AbstractTable argu) {
        n.f0.accept(this,argu);
        return null;
    }

    /****************
     *  ARITHMETIC  *
     ****************/

    @Override
    public AbstractTable visit(PlusExpression n, AbstractTable argu) {
        argu.GlobalVTables.CurrentFunc.code += "Add(num " + "t." + tempCounter + ")\n";
        //System.out.println("Add(num " + "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value
        return null;
    }

    @Override
    public AbstractTable visit(MinusExpression n, AbstractTable argu) {
        argu.GlobalVTables.CurrentFunc.code += "Sub(num " + "t." + tempCounter + ")\n";
        //System.out.println("Sub(num " + "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value
        return null;
    }

    @Override
    public AbstractTable visit(TimesExpression n, AbstractTable argu) {
        isAssign = false;
        argu.GlobalVTables.CurrentFunc.code += "MulS(num " + "t." + tempCounter + ")\n";
        //System.out.println("MulS(num " + "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value
        return null;
    }

    /****************
     *  COMPARISON  *
     ****************/

    @Override
    public AbstractTable visit(CompareExpression n, AbstractTable argu) {
        isAssign = false;
        boolean signedInt = true; // TODO get type from symbol table and check if signed int or not

        String str = argu.Global.CurrentVar.varName; // TODO figure out if param or var
        if(signedInt){
            argu.GlobalVTables.CurrentFunc.code += "LtS("+ str +" 1)\n"; // TODO get IntegerLiteral
        }
        else{
            argu.GlobalVTables.CurrentFunc.code += "Lt("+ str + " 1)\n";
        }

        //System.out.print("LtS(num 1");
        n.f0.accept(this,argu); // TODO maybe traverse this first and get the var
        n.f1.accept(this,argu); // TODO can get the operator from here
        n.f2.accept(this,argu); // can print 1 from here but also prints every IntegerLiteral
                                   // TODO can get the 2nd value from here and append to first var
        //tempFunc.code += ")\n";
        //System.out.println(")");
        return null;
    }

    /********************
     *  DISPLAY OUTPUT  *
     ********************/

    @Override
    public AbstractTable visit(PrintStatement n, AbstractTable argu) {
        n.f0.accept(this,argu); // f0 -> "System.out.println"
        n.f1.accept(this,argu); // f1 -> "("

        n.f2.accept(this,argu); // f2 -> Expression()
        if(isPrintStatement) { // TODO put the number inside PrintIntS()
            argu.GlobalVTables.CurrentFunc.code += tab() + "PrintIntS(";
            argu.GlobalVTables.CurrentFunc.code += ")\n";
        }
        isPrintStatement = true;
        n.f3.accept(this,argu); // f3 -> ")"
        n.f4.accept(this,argu); // f4 -> ";"
        return null;
    }

    /***********************
     *  MEMORY ALLOCATION  *
     ***********************/

    public String memAlloc(VTable vtable){
        int recordSize = vtable.records.size();
        int allocSize = recordSize * 4 + 4;
        return "HeapAllocZ(" + allocSize + ")";
    }

    /***********
     *  ERROR  *
     ***********/

    public String error(){
        //TODO unique label names
        //TODO message based on error
        String err = tab() + "if t.0 goto :null1\n" +
                     tab() + tab() + "Error(\"null pointer\")\n" +
                     tab() + "null1:";
        return err;
    }

}


