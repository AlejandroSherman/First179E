//First pass, create and fill all the vtables
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

    public AbstractTable visit(Goal n, AbstractTable argu){
        argu.Global.CurrentClass = new OneClass();
        argu.Global.CurrentMethod = new OneMethod();
        argu.Global.CurrentVar = new OneVar();
        argu.Global.classOrmethod = "none";

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
        argu.Global.addClass("Main", tempClass);

        //Vapor stuff
        VTable tempTable = new VTable("Main", classIndex);
        argu.GlobalVTables.vtables.put("Main", tempTable);
        OneFunction tempFunc = new OneFunction("Main",0,tempTable);
        argu.GlobalCodeGen.addFunc("Main", tempFunc);
        classIndex++;
        funcIndex=0;
        argu.Global.CurrentFunc = tempFunc;

        System.out.println("\n\nfunc Main()");
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

        //System.out.println(n.f15.accept(this,argu));
        n.f15.accept(this, argu); // f15 -> ( Statement() )*
        
        n.f16.accept(this, argu); // f16 -> "}"
        n.f17.accept(this, argu); // f17 -> "}"

        tempFunc.code += "   ret\n";

        System.out.println(tab() + "ret");
        tabCounter = 0;
        return _ret;
    }

    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argu) {
        argu.Global.classOrmethod = "class";
        String className = n.f1.f0.toString();
        OneClass tempClass = new OneClass(className);
        argu.Global.CurrentClass = tempClass;
        argu.Global.addClass(className, tempClass);

        //Vapor Stuff (Missing extended classes)
        VTable tempTable = new VTable(className, classIndex);
        argu.GlobalVTables.vtables.put(className, tempTable);
        classIndex++;
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
        argu.Global.CurrentClass.addMethod(methodName, tempMethod);

        //Vapor Stuff
        VTable tempTable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        tempTable.addFunc(methodName);
        OneFunction tempFunc = new OneFunction(methodName ,funcIndex, tempTable);
        argu.GlobalCodeGen.addFunc(methodName, tempFunc);
        funcIndex++;
        argu.Global.CurrentFunc = tempFunc;

        String tempName = argu.GlobalCodeGen.functions.get(methodName).ofClass + "." + n.f2.f0.tokenImage;
        System.out.println("\nfunc " + tempName + "()");
        tabCounter++;

        n.f0.accept(this, argu); // f0 -> "public"
        n.f1.accept(this, argu); // f1 -> Type()
        n.f2.accept(this, argu); // f2 -> Identifier()
        n.f3.accept(this, argu); // f3 -> "("
        n.f4.accept(this, argu); // f4 -> ( FormalParameterList() )?
        n.f5.accept(this, argu); // f5 -> ")"
        n.f6.accept(this, argu); // f6 -> "{"
        n.f7.accept(this, argu); // f7 -> ( VarDeclaration() )*
        n.f8.accept(this, argu); // f8 -> ( Statement() )*
        n.f9.accept(this, argu); // f9 -> "return"

        System.out.print(tab() + "ret ");
        isPrimaryExpression = true;

        n.f10.accept(this, argu); // f10 -> Expression()
        n.f11.accept(this, argu); // f11 -> ";"
        n.f12.accept(this, argu); // f12 -> "}"

        tempFunc.code += "   ret\n";
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

        if(argu.Global.classOrmethod.equals("class"))
            argu.Global.CurrentClass.addVar(varName, tempVar);
        else
            argu.Global.CurrentMethod.addLocal(varName, tempVar);

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
        argu.Global.CurrentMethod.addParam(varName, tempVar);
        argu.Global.CurrentVar = tempVar;

        //Vapor Stuff
        argu.Global.CurrentFunc.params.add(" "+ varName);

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
            //System.out.println(n.f0.tokenImage);
            isPrimaryExpression = false;
        }
        return null;
    }

    @Override
    public AbstractTable visit(IntegerLiteral n, AbstractTable argu) {
        /////////////////// test
        //String varName = argu.Global.CurrentVar.varName;

        String varValue = n.f0.tokenImage;
        if(isAssign) {
            System.out.println(varValue);
            isAssign = false;
        }
        n.f0.accept(this, argu);
        return null;
    }

    /******************
     *  CONTROL FLOW  *
     ******************/

    @Override
    public AbstractTable visit(IfStatement n, AbstractTable argu) {
        OneFunction tempFunc = argu.Global.CurrentFunc;

        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        tempFunc.code += "   t.0 = ";
        System.out.print(tab() + "t." + tempCounter + " = ");
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);

        // TODO get unique labels
        tempFunc.code += "   if0 t.0 goto :if1_else\n";
        System.out.println(tab() + "if" + ifCounter++ + " t." + tempCounter++ + " goto :if" + ++ifLabelCounter + "_else");
        // TODO do something inside if statement
        tabCounter++;
        n.f4.accept(this,argu);
        if(isAssign){ // TODO temp fix for assignments
        System.out.println("THERE WAS NOTHING ASSIGNED!");
        isAssign = false;
        }
        tempFunc.code += "      goto :if1_end\n";
        System.out.println(tab() + "goto :if" + ifCounter + "_end");
        tabCounter--;
        n.f5.accept(this,argu);
        tempFunc.code += "   if1_else:\n";
        System.out.println(tab() + "if" + ifLabelCounter + "_else:");
        // TODO do something inside if-else statement
        tabCounter++;
        n.f6.accept(this,argu);
        tabCounter--;
        tempFunc.code += "   if1_end:\n";
        System.out.println(tab() + "if" + ifCounter + "_end:");
        ifCounter = 0;
        tempCounter = 0;
        return null;
    }

    @Override
    public AbstractTable visit(WhileStatement n, AbstractTable argu) {
        //TODO unique labels
        //TODO fill in loop bodies
        System.out.println(tab() + "goto :end");
        System.out.println(tab() + "begin:");
        tabCounter++;
        System.out.println(tab() +
                "<inside begin>"); //TODO fill loop begin
        tabCounter--;
        System.out.println(tab() + "end:");
        tabCounter++;
        System.out.println(tab() +
                "<inside end>"); //TODO fill loop end
        System.out.println(tab() + "goto :begin");
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
        //statements.add(n.f0.choice.getClass().getSimpleName());
        //System.out.println(statements.getLast());
        n.f0.accept(this, argu);
        return null;
    }

    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argu) {
        OneFunction tempFunc = argu.Global.CurrentFunc;

        String id = n.f0.f0.tokenImage;
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        isAssign = true;
        tempFunc.code += "      " + id + " = \n"; // I think we can drop "\n"
        System.out.print(tab() + id + " = ");
        n.f2.accept(this,argu);
        return null;
    }

    @Override
    public AbstractTable visit(MessageSend n, AbstractTable argu) {
        //System.out.println("            "+n.f0.f0.choice.getClass().getSimpleName());
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
        //System.out.println(n.f0.choice.getClass().getSimpleName());
        n.f0.accept(this,argu);
        return null;
    }

    @Override
    public AbstractTable visit(AllocationExpression n, AbstractTable argu) {
        n.f0.accept(this, argu);
        VTable vtable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        if(isNew) {
            //tabCounter++;
            System.out.println(tab() + "t." + tempCounter + " = " + memAlloc(vtable));
            System.out.println(tab() + "[t." + tempCounter + "] = :vmt_" + n.f1.f0.tokenImage);
            error(); // error checking (null ptr, etc.)
            //tabCounter--;
            isNew = false;
        }
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        return null;
    }

    @Override
    public AbstractTable visit(PrimaryExpression n, AbstractTable argu) {
        //System.out.println(n.f0.choice.getClass().getSimpleName());
        if(isPrimaryExpression){
            //System.out.println(n.f0.choice.getClass().getSimpleName());
        }
        n.f0.accept(this,argu);
        return null;
    }

    /****************
     *  ARITHMETIC  *
     ****************/

    @Override
    public AbstractTable visit(PlusExpression n, AbstractTable argu) {
        System.out.println("Sub(num " + "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value
        return null;
    }

    @Override
    public AbstractTable visit(MinusExpression n, AbstractTable argu) {
        System.out.println("Sub(num " + "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value
        return null;
    }

    @Override
    public AbstractTable visit(TimesExpression n, AbstractTable argu) {
        isAssign = false;
        System.out.println("MulS(num " + "t." + tempCounter + ")");
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
        OneFunction tempFunc = argu.Global.CurrentFunc;

        isAssign = false;
        boolean signedInt = true; // TODO get type from symbol table and check if signed int or not
        if(signedInt){
            tempFunc.code += "LtS(num 1)\n";
            System.out.println("LtS(num 1)"); // TODO get variable and IntegerLiteral
        }
        else{
            tempFunc.code += "Lt(num 1)";
            System.out.println("Lt(num 1)"); // TODO get variable and IntegerLiteral
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
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //System.out.println("    "+n.f2.getClass().getSimpleName());
        if(isPrintStatement) {
            System.out.println(tab() + "PrintIntS()"); // TODO get argument
        }
        isPrintStatement = true;
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
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

    public void error(){
        //TODO unique label names
        //TODO message based on error
        System.out.println(tab() + "if t.0 goto :null1\n" +
                tab() + "  Error(\"null pointer\")\n" +
                "  null1:");
    }

}


