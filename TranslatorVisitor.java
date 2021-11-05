//First pass, create and fill all the vtables
import syntaxtree.*;
import visitor.GJDepthFirst;

public class TranslatorVisitor extends GJDepthFirst<AbstractTable,AbstractTable>{
    Integer classIndex = 0;
    Integer funcIndex = 0;

    @Override
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

        tempFunc.code += "   ret\n";

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

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> "class"
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> "{"
        n.f3.accept(this, argu); // f3 -> ( VarDeclaration() )*
        n.f4.accept(this, argu); // f4 -> ( MethodDeclaration() )*
        n.f5.accept(this, argu); // f5 -> "}"
        return _ret;
    }

    @Override
    public AbstractTable visit(VarDeclaration n, AbstractTable argu) {
        String varType = n.f0.f0.choice.getClass().getSimpleName();
        varType = argu.dictionary.getRealType(varType);
        String varName = n.f1.f0.tokenImage;
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

    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argu) {

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

        AbstractTable _ret=null;
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
        n.f10.accept(this, argu); // f10 -> Expression()
        n.f11.accept(this, argu); // f11 -> ";"
        n.f12.accept(this, argu); // f12 -> "}"

        tempFunc.code += "   ret\n";

        return _ret;
    }

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

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> <IDENTIFIER>
        return _ret;
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

    /*
    **********************************Below here is new code**************************************************
    */ 
    
    @Override
    public AbstractTable visit(IfStatement n, AbstractTable argu) {
        OneFunction tempFunc = argu.Global.CurrentFunc;

        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        tempFunc.code += "   t.0 = ";
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);

        tempFunc.code += "   if0 t.0 goto :if1_else\n";
        // do something inside if statement
        n.f4.accept(this,argu);
        tempFunc.code += "      goto :if1_end\n";
        n.f5.accept(this,argu);
        tempFunc.code += "   if1_else:\n";
        // do something inside if-else statement
        n.f6.accept(this,argu);
        tempFunc.code += "   if1_end:\n";

        //System.out.println("new code: " + tempFunc.code);

        return null;
    }

    @Override
    public AbstractTable visit(CompareExpression n, AbstractTable argu) {
        OneFunction tempFunc = argu.Global.CurrentFunc;
        tempFunc.code += "LtS(num 1";

        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        n.f2.accept(this,argu); // can print 1 from here but also prints every IntegerLiteral
        tempFunc.code += ")\n";
        return null;
    }

    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argu) {
        OneFunction tempFunc = argu.Global.CurrentFunc;
        String id = n.f0.f0.tokenImage;
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        tempFunc.code += "      " + id + " = \n";
        n.f2.accept(this,argu);
        return null;
    }
}
