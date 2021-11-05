//First pass, create and fill all the vtables
import syntaxtree.*;
import visitor.GJDepthFirst;

public class VTableVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    Integer classIndex = 0;

    @Override
    public AbstractTable visit(Goal n, AbstractTable argu){
        argu.GlobalVTables.CurrentVTable = new VTable();
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
        VTable tempTable = new VTable("Main", classIndex);
        //tempTable.addFunc("main"); //handle main differently than the others
        argu.GlobalVTables.vtables.put("Main", tempTable);
        classIndex++;
        argu.GlobalVTables.CurrentVTable = tempTable;

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
        return _ret;
    }

    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argu) {
        argu.Global.classOrmethod = "class";
        String className = n.f1.f0.toString();
        VTable tempTable = new VTable(className, classIndex);
        argu.GlobalVTables.vtables.put(className, tempTable);
        classIndex++;
        argu.GlobalVTables.CurrentVTable = tempTable;

        n.f0.accept(this, argu); // f0 -> "class"
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> "{"
        n.f3.accept(this, argu); // f3 -> ( VarDeclaration() )*
        n.f4.accept(this, argu); // f4 -> ( MethodDeclaration() )*
        n.f5.accept(this, argu); // f5 -> "}"
        return null;
    }

    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argu){
        argu.Global.classOrmethod = "method";
        String methodName = n.f2.f0.toString();
        VTable tempTable = argu.GlobalVTables.CurrentVTable;
        tempTable.addFunc(methodName);
  
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
        return null;
    }

    @Override
    public AbstractTable visit(VarDeclaration n, AbstractTable argu) {
        String varName = n.f1.f0.tokenImage;
        if(argu.Global.classOrmethod.equals("class")) argu.GlobalVTables.CurrentVTable.addRecord(varName);

        AbstractTable _ret=null;
        n.f0.accept(this, argu); // f0 -> Type()
        n.f1.accept(this, argu); // f1 -> Identifier()
        n.f2.accept(this, argu); // f2 -> ";"
        return _ret;
    }
  
    @Override
    public AbstractTable visit(ClassExtendsDeclaration n, AbstractTable argu) {
        //argu.Global.CurrentClass.children.add(n.f1.f0.tokenImage);
        argu.Global.classOrmethod = "class";
        String className = n.f1.f0.tokenImage;
        VTable tempTable = new VTable(className, classIndex);

        //Copy VTable from parent
        tempTable.records = argu.GlobalVTables.CurrentVTable.records;
        tempTable.funcs = argu.GlobalVTables.CurrentVTable.funcs;
        
        argu.GlobalVTables.vtables.put(className, tempTable);
        classIndex++;
        argu.GlobalVTables.CurrentVTable = tempTable;

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
}