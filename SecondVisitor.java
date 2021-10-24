import syntaxtree.*;
import visitor.GJDepthFirst;

public class SecondVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    String inClass;
    String inMethod;

    @Override
    public AbstractTable visit(MainClass n, AbstractTable argTable){
        ClassTable classTable = argTable.Global.classes.get(n.f1.f0.tokenImage);
        n.f0.accept(this, classTable);
        n.f1.accept(this, classTable);
        n.f2.accept(this, classTable);

        MethodTable methodTable = classTable.methods.get("main");
        n.f4.accept(this, methodTable);
        n.f5.accept(this, methodTable);
        n.f6.accept(this, methodTable);
        n.f7.accept(this, methodTable);
        n.f8.accept(this, methodTable);
        n.f9.accept(this, methodTable);
        n.f10.accept(this, methodTable);
        n.f11.accept(this, methodTable);
        n.f12.accept(this, methodTable);
        n.f13.accept(this, methodTable);
        n.f14.accept(this, methodTable);
        n.f15.accept(this, methodTable);
        n.f16.accept(this, classTable);

        inClass = "Main";
        inMethod = "main";

        return null;
    }
 
    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argTable){
        String leftSideName = n.f0.f0.tokenImage;

        //System.out.println("Class: " + inClass + "    Method: " + inMethod);
        //System.out.println("Left hand side var: " + leftSideName);

        ClassTable cTable = argTable.Global.classes.get(inClass);
        MethodTable mTable = cTable.methods.get(inMethod);

        try { 
            if( mTable.locals.get(leftSideName) == null) {
                throw new Exception("ERROR - " + leftSideName + "does not exist in scope.");
            }
        }
        catch(Exception e){
            System.out.println("Type error");
            System.exit(1);
        }

        return null;
    }


    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argTable) {
        inMethod = n.f2.f0.tokenImage;
        AbstractTable _ret=null;
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        n.f3.accept(this, argTable);
        n.f4.accept(this, argTable);
        n.f5.accept(this, argTable);
        n.f6.accept(this, argTable);
        n.f7.accept(this, argTable);
        n.f8.accept(this, argTable);
        n.f9.accept(this, argTable);
        n.f10.accept(this, argTable);
        n.f11.accept(this, argTable);
        n.f12.accept(this, argTable);
        return _ret;
    }



//Everything below this line is redoing first pass stuff to get class and method names
/**************************************************************************************/
    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argTable) {
        inClass = n.f1.f0.tokenImage;
        AbstractTable _ret=null;
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        n.f3.accept(this, argTable);
        n.f4.accept(this, argTable);
        n.f5.accept(this, argTable);
        return _ret;
    }

}  
