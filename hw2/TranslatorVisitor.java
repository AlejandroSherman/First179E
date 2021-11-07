//Second pass, code generation
import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.LinkedList;
import java.util.HashMap;

public class TranslatorVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    /*******************
     *  CLASS GLOBALS  *
     *******************/
    Integer classIndex = 0;
    Integer funcIndex = 0;
    //int tempCounter = 0;
    int ifCounter = 0;
    int ifLabelCounter = 0;
    int tabCounter = 0;
    LinkedList<String> temps = new LinkedList<String>();
    LinkedList<String> statements = new LinkedList<String>();
    boolean isPrintStatement = true;
    boolean isPrimaryExpression = false;
    boolean isAssign = false;
    boolean isNew = true;
    boolean isCompare = false;
    boolean isParam = false;
    boolean isMessageSend = false;
    boolean isRet = false;
    boolean isVarDeclaration = false;
    boolean isPlusExpression = false;
    boolean isClassHeader = false;
    boolean isVaporParam = false;
    boolean isRightSide = false;
    String add1 = "";
    String add2 = "";
    int sum;
    String sysPrint = "";
    LableManager lableMngr = new LableManager();
    String returnData = "";
    String funcCalled = "";
    String classUsed = "";
    String varToAssign = "";
    String returnLabel = "";
    String leftSide = "";
    boolean isPrintVar = false;
    boolean isArrayAssign = false;
    boolean isArrayDecl = false;
    HashMap<String, String> arrayData = new HashMap<String, String>(); // <name,size>
    String arrayName = "";
    String arraySize = "";
    boolean isArrayLookup = false;

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
        lableMngr.resetLabel();
        VTable tempTable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        OneFunction tempFunc = new OneFunction(methodName ,funcIndex, tempTable);
        argu.GlobalCodeGen.addFunc(methodName, tempFunc);
        funcIndex++;
        argu.GlobalVTables.CurrentFunc = tempFunc;
        argu.GlobalVTables.CurrentVTable = tempTable;

        String tempName = argu.GlobalCodeGen.functions.get(methodName).ofClass + "." + n.f2.f0.tokenImage;
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
        
        isRet = true;

        n.f10.accept(this, argu); // f10 -> Expression()
        n.f11.accept(this, argu); // f11 -> ";"
        n.f12.accept(this, argu); // f12 -> "}"

        argu.GlobalVTables.CurrentFunc.code += tab() + "ret " + returnData + "\n";
        tabCounter = 0;
        return null;
    }

    @Override
    public AbstractTable visit(VarDeclaration n, AbstractTable argu) {
        isVarDeclaration = true;
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
        isVarDeclaration = false;
        return _ret;
    }

    /***************
     *  PARAMETERS *
     ***************/

    @Override
    public AbstractTable visit(FormalParameter n, AbstractTable argu) {
        isParam = true;
        String varName = n.f1.f0.tokenImage;
        argu.GlobalVTables.CurrentFunc.params.add(" "+ varName);

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> Type()
        n.f1.accept(this, argu); // f1 -> Identifier()
        isParam = false;
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
        if(isArrayDecl) arrayName = n.f0.tokenImage;
        if(isArrayLookup) arrayName = n.f0.tokenImage;

        if(isClassHeader) classUsed = n.f0.tokenImage;
        else if(isMessageSend) funcCalled = n.f0.tokenImage;
        else if(isVaporParam) argu.GlobalVTables.CurrentFunc.code += n.f0.tokenImage;
        else if(isRightSide) argu.GlobalVTables.CurrentFunc.code += "  " + leftSide + " = " + n.f0.tokenImage + "\n";

        if(isPrintVar && !isMessageSend && !isArrayLookup) sysPrint = n.f0.tokenImage;
        if(isRet) returnData = n.f0.tokenImage;

        n.f0.accept(this,argu); // f0 -> <IDENTIFIER>
/*
        if(isPrimaryExpression && isRet) {
            argu.GlobalVTables.CurrentFunc.code += n.f0.tokenImage + "\n";
            //System.out.println(n.f0.tokenImage);
            isPrimaryExpression = false;
        }
  */
        return null;
    }

    @Override
    public AbstractTable visit(IntegerLiteral n, AbstractTable argu) {
        String varValue = n.f0.tokenImage;

        if(isArrayLookup) arraySize = varValue;

        if(isArrayDecl){
            arraySize = varValue;
            arrayData.put(arrayName, arraySize);
            argu.GlobalVTables.CurrentFunc.code += "  s1 = MulS(" +arraySize+" 4)\n";
            argu.GlobalVTables.CurrentFunc.code += "  s2 = Add(s1 4)\n";
            argu.GlobalVTables.CurrentFunc.code += "  "+ arrayName + " = HeapAllocZ(s2)\n";
            argu.GlobalVTables.CurrentFunc.code += "  ["+ arrayName + "] = "+ arraySize+"\n";
            isArrayDecl = false;
        }


        if(isPlusExpression) argu.GlobalVTables.CurrentFunc.code += varValue;

        if(isPlusExpression && isPrintStatement) sysPrint = lableMngr.currLabel;
        else if(isMessageSend) argu.GlobalVTables.CurrentFunc.code += " " + varValue;
        else if(isPrintStatement) sysPrint += varValue;

        if(isRet) returnData = varValue;


        n.f0.accept(this, argu); // f0 -> <INTEGER_LITERAL>
        return null;
    }

    /******************
     *  CONTROL FLOW  *
     ******************/

    @Override
    public AbstractTable visit(IfStatement n, AbstractTable argu) {
        n.f0.accept(this,argu); // f0 -> "if"
        n.f1.accept(this,argu); // f1 -> "("

        argu.GlobalVTables.CurrentFunc.code += tab() + lableMngr.currLabel + " = ";

        n.f2.accept(this,argu); // f2 -> Expression()
        n.f3.accept(this,argu); // f3 -> ")"

        ++ifLabelCounter;
        argu.GlobalVTables.CurrentFunc.code += tab() + "if" + ifCounter + lableMngr.currLabel + " goto :if" + ifLabelCounter + "_else\n";

        ifCounter++;
        lableMngr.nextLabel();
        tabCounter++;

        n.f4.accept(this,argu); // f4 -> Statement()

        if(isAssign){
            argu.GlobalVTables.CurrentFunc.code += "THERE WAS NOTHING ASSIGNED!\n";
            isAssign = false;
        }
        argu.GlobalVTables.CurrentFunc.code += tab() + "goto :if" + ifCounter + "_end\n";
        tabCounter--;
        
        n.f5.accept(this,argu); // f5 -> "else"

        argu.GlobalVTables.CurrentFunc.code += tab() + "if" + ifLabelCounter + "_else:\n";
        tabCounter++;

        n.f6.accept(this,argu); // f6 -> Statement()
        
        tabCounter--;
        argu.GlobalVTables.CurrentFunc.code += tab() + "if1_end:\n";
        ifCounter = 0;
        //tempCounter = 0;
        lableMngr.resetLabel();

        return null;
    }

    @Override
    public AbstractTable visit(WhileStatement n, AbstractTable argu) {
        argu.GlobalVTables.CurrentFunc.code += tab() + "goto :end\n";
        argu.GlobalVTables.CurrentFunc.code += tab() + "begin:\n";
        tabCounter++;
        argu.GlobalVTables.CurrentFunc.code += tab() + "<inside begin>\n";
        tabCounter--;
        argu.GlobalVTables.CurrentFunc.code += tab() + "end:\n";
        tabCounter++;
        argu.GlobalVTables.CurrentFunc.code += tab() + "<inside end>\n";
        argu.GlobalVTables.CurrentFunc.code += tab() + "goto :begin\n";
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
        leftSide = n.f0.f0.tokenImage;
        varToAssign = leftSide;
        isAssign = true;

        n.f0.accept(this,argu); // f0 -> Identifier()
        n.f1.accept(this,argu); // f1 -> "=" 
        isRightSide = true;
        n.f2.accept(this,argu); // f2 -> Expression()
        isRightSide = false;
        n.f3.accept(this,argu); // f3 -> ";"
        isAssign = false;
        return null;
    }

    @Override
    public AbstractTable visit(MessageSend n, AbstractTable argu) {        
        isMessageSend = true;
        isClassHeader = true;
        n.f0.accept(this,argu); // f0 -> PrimaryExpression()
        isClassHeader = false;
        n.f1.accept(this,argu); // f1 -> "."
        n.f2.accept(this,argu); // f2 -> Identifier()
        n.f3.accept(this,argu); // f3 -> "("

        VTable tempVTable = argu.GlobalVTables.vtables.get(classUsed);
        OneFunction tempFunc = tempVTable.funcs.get(funcCalled);
        Integer funcOffset = tempFunc.index;
        String vtableLabel = "";

        if(argu.GlobalVTables.CurrentFunc.name.equals("Main")){
            vtableLabel = lableMngr.currLabel;
            lableMngr.nextLabel();
            argu.GlobalVTables.CurrentFunc.label = lableMngr.currLabel;
            String funcLabel = argu.GlobalVTables.CurrentFunc.label;
            lableMngr.nextLabel();
            String tempLabel = lableMngr.currLabel;
    
            argu.GlobalVTables.CurrentFunc.code += "  " + funcLabel + " = ["+ vtableLabel +"]\n";
            argu.GlobalVTables.CurrentFunc.code += "  " + funcLabel + " = [" + funcLabel + "+" + funcOffset*4 + "]\n";
            argu.GlobalVTables.CurrentFunc.code += "  " + tempLabel + " = call " + funcLabel + "("+ vtableLabel;
    
            n.f4.accept(this,argu); // f4 -> ( ExpressionList() )?
            n.f5.accept(this,argu); // f5 -> ")"
    
            argu.GlobalVTables.CurrentFunc.code += ")\n";
            sysPrint += tempLabel;

        }
        else if(isAssign){
            vtableLabel = "this";
            String funcLabel = argu.GlobalVTables.CurrentFunc.label;

            if(funcLabel.equals("")){
                argu.GlobalVTables.CurrentFunc.label = lableMngr.currLabel;
                funcLabel = argu.GlobalVTables.CurrentFunc.label;

                argu.GlobalVTables.CurrentFunc.code += "  " + funcLabel + " = ["+ vtableLabel +"]\n";
                argu.GlobalVTables.CurrentFunc.code += "  " + funcLabel + " = [" + funcLabel + "+" + funcOffset*4 + "]\n";
            }

            argu.GlobalVTables.CurrentFunc.code += "  " + varToAssign + " = call " + funcLabel + "("+ vtableLabel;
    
            n.f4.accept(this,argu); // f4 -> ( ExpressionList() )?
            n.f5.accept(this,argu); // f5 -> ")"
    
            argu.GlobalVTables.CurrentFunc.code += ")\n";
            
        }
        else {
            vtableLabel = "this";
            argu.GlobalVTables.CurrentFunc.label = lableMngr.currLabel;
            String funcLabel = argu.GlobalVTables.CurrentFunc.label;
            lableMngr.nextLabel();
            String tempLabel = lableMngr.currLabel;
    
            argu.GlobalVTables.CurrentFunc.code += "  " + funcLabel + " = ["+ vtableLabel +"]\n";
            argu.GlobalVTables.CurrentFunc.code += "  " + funcLabel + " = [" + funcLabel + "+" + funcOffset*4 + "]\n";
            argu.GlobalVTables.CurrentFunc.code += "  " + tempLabel + " = call " + funcLabel + "("+ vtableLabel;
    
            n.f4.accept(this,argu); // f4 -> ( ExpressionList() )?
            n.f5.accept(this,argu); // f5 -> ")"
    
            argu.GlobalVTables.CurrentFunc.code += ")\n";
            sysPrint += tempLabel;
        }


        isMessageSend = false;

        return null;
    }

    @Override
    public AbstractTable visit(ArrayType n, AbstractTable argu) {
        isArrayDecl =true;
        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> "int"
        n.f1.accept(this, argu); // f1 -> "["
        n.f2.accept(this, argu); // f2 -> "]"
        return _ret;
    }


    @Override
    public AbstractTable visit(ArrayLookup n, AbstractTable argu) {
        isArrayLookup = true;
        
        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> PrimaryExpression()
        n.f1.accept(this, argu); // f1 -> "["
        n.f2.accept(this, argu); // f2 -> PrimaryExpression()
        n.f3.accept(this, argu); // f3 -> "]"

        
        String tempSize = arrayData.get(arrayName);
        int size = Integer.parseInt(arraySize);
        int access = Integer.parseInt(tempSize);
        
        if(access >= size){
            argu.GlobalVTables.CurrentFunc.code += "  PrintIntS(0)\n";
            argu.GlobalVTables.CurrentFunc.code += "  Error(\"array index out of bounds\")\n";
            //System.out.println("error array out of bounds");
            //System.exit(1);
        }

        //do array access stuff here   
        //argu.GlobalVTables.CurrentFunc.code += "  s = ["+ arrayName +"]\n";
        //argu.GlobalVTables.CurrentFunc.code += "  ok = LtS("+ tempSize +","+ arraySize +")\n";
        //argu.GlobalVTables.CurrentFunc.code += "  if ok goto :l'\n";
        //argu.GlobalVTables.CurrentFunc.code += "    Error(\"Array index out of bounds\")\n";
        //argu.GlobalVTables.CurrentFunc.code += "  l': ok = LtS(-1,"+ tempSize +")\n";
        //argu.GlobalVTables.CurrentFunc.code += "  if ok goto :l\n";
        //argu.GlobalVTables.CurrentFunc.code += "    Error(\"Array index out of bounds\")\n";
        //argu.GlobalVTables.CurrentFunc.code += "  l: o = MultS("+tempSize+" 4)\n";
        //argu.GlobalVTables.CurrentFunc.code += "     d = Add("+arrayName+" o)\n";
        //argu.GlobalVTables.CurrentFunc.code += "     r = [d+4]\n";


        isArrayLookup = false;
        return _ret;
    }
  

    @Override
    public AbstractTable visit(ArrayAssignmentStatement n, AbstractTable argu) {
        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> Identifier()
        n.f1.accept(this, argu); // f1 -> "["
        n.f2.accept(this, argu); // f2 -> Expression()
        n.f3.accept(this, argu); // f3 -> "]"
        n.f4.accept(this, argu); // f4 -> "="
        n.f5.accept(this, argu); // f5 -> Expression()
        n.f6.accept(this, argu); // f6 -> ";"
        return _ret;
    }
  

    /*****************
     *  EXPRESSIONS  *
     *****************/
    @Override
    public AbstractTable visit(Expression n, AbstractTable argu) {
        n.f0.accept(this,argu);
        /*
        * f0 -> AndExpression()
        *       | CompareExpression()
        *       | PlusExpression()
        *       | MinusExpression()
        *       | TimesExpression()
        *       | ArrayLookup()
        *       | ArrayLength()
        *       | MessageSend()
        *       | PrimaryExpression()
        */

        return null;
    }


    @Override
    public AbstractTable visit(ExpressionList n, AbstractTable argu) {
        //if(isMessageSend) argu.GlobalVTables.CurrentFunc.code += varValue;
        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> Expression()
        n.f1.accept(this, argu); // f1 -> ( ExpressionRest() )*
        return _ret;
    }

    @Override
    public AbstractTable visit(ExpressionRest n, AbstractTable argu) {
        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> ","
        n.f1.accept(this, argu); // f1 -> Expression()
        return _ret;
    }

    @Override
    public AbstractTable visit(AllocationExpression n, AbstractTable argu) {
        VTable tempVTable = argu.GlobalVTables.vtables.get(n.f1.f0.tokenImage);
        argu.GlobalVTables.CurrentVTable = tempVTable;
        
        argu.GlobalVTables.CurrentFunc.code += tab() + lableMngr.currLabel + " = " + memAlloc(tempVTable) + "\n";
        argu.GlobalVTables.CurrentFunc.code += tab() + "[" + lableMngr.currLabel + "] = :vmt_" + n.f1.f0.tokenImage + "\n";
        argu.GlobalVTables.CurrentFunc.code += error() + "\n"; 
        
        n.f0.accept(this, argu); // f0 -> "new"
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> "("
        n.f3.accept(this, argu); // f3 -> ")"
        return null;
    }

    @Override
    public AbstractTable visit(PrimaryExpression n, AbstractTable argu) {
        isPrimaryExpression = true;

        n.f0.accept(this,argu);
        /*
        * f0 -> IntegerLiteral()
        *       | TrueLiteral()
        *       | FalseLiteral()
        *       | Identifier()
        *       | ThisExpression()
        *       | ArrayAllocationExpression()
        *       | AllocationExpression()
        *       | NotExpression()
        *       | BracketExpression()
        */

        isPrimaryExpression = false;
        return null;
    }

    /****************
     *  ARITHMETIC  *
     ****************/

    @Override
    public AbstractTable visit(PlusExpression n, AbstractTable argu) {
        isPlusExpression = true;

        if(isAssign){
            argu.GlobalVTables.CurrentFunc.code += "  " + leftSide + " = ";
            returnLabel = leftSide;
        }
        else{
            lableMngr.nextLabel();
            argu.GlobalVTables.CurrentFunc.code += "  " + lableMngr.currLabel + " = ";
            returnLabel = lableMngr.currLabel;
        }

        argu.GlobalVTables.CurrentFunc.code += "Add(";
        isVaporParam = true;
        n.f0.accept(this,argu); // f0 -> PrimaryExpression()
        argu.GlobalVTables.CurrentFunc.code += " ";
        n.f1.accept(this,argu); // f1 -> "+"
        n.f2.accept(this,argu); // f2 -> PrimaryExpression()
        argu.GlobalVTables.CurrentFunc.code += ")\n";
        isVaporParam = false;

        isPlusExpression = false;

        if(isRet) returnData = returnLabel;

        return null;
    }

    @Override
    public AbstractTable visit(MinusExpression n, AbstractTable argu) {
        argu.GlobalVTables.CurrentFunc.code += "Sub(num " + "t." + lableMngr.currLabel + ")\n";
        //System.out.println("Sub(num " + "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value
        return null;
    }

    @Override
    public AbstractTable visit(TimesExpression n, AbstractTable argu) {
        isAssign = false;
        argu.GlobalVTables.CurrentFunc.code += "MulS(num " + "t." + lableMngr.currLabel + ")\n";
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
        isCompare = true;
        boolean signedInt = true; // TODO get type from symbol table and check if signed int or not

        String str = argu.Global.CurrentVar.varName; // TODO figure out if param or var
        if(signedInt){
            argu.GlobalVTables.CurrentFunc.code += "LtS(num 1)\n"; // TODO get IntegerLiteral
        }
        else{
            argu.GlobalVTables.CurrentFunc.code += "Lt(num 1)\n";
        }

        //System.out.print("LtS(num 1");
        n.f0.accept(this,argu); // maybe traverse this first and get the var
        n.f1.accept(this,argu); // can get the operator from here
        n.f2.accept(this,argu); // can print 1 from here but also prints every IntegerLiteral
                                   // can get the 2nd value from here and append to first var
        //tempFunc.code += ")\n";
        //System.out.println(")");
        return null;
    }

    /********************
     *  DISPLAY OUTPUT  *
     ********************/

    @Override
    public AbstractTable visit(PrintStatement n, AbstractTable argu) {
        isPrintStatement = true;

        n.f0.accept(this,argu); // f0 -> "System.out.println"
        n.f1.accept(this,argu); // f1 -> "(

        isPrintVar = true;

        n.f2.accept(this,argu); // f2 -> Expression()

        isPrintVar = false;

        argu.GlobalVTables.CurrentFunc.code += tab() + "PrintIntS(" + sysPrint + ")\n";
        sysPrint = "";

        n.f3.accept(this,argu); // f3 -> ")"
        n.f4.accept(this,argu); // f4 -> ";"

        isPrintStatement = false;
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