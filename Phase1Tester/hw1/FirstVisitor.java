import syntaxtree.*;
import visitor.GJDepthFirst;

public class FirstVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {

    //OKAY
    //gets a global table to store classes
    @Override
    public AbstractTable visit(Goal n, AbstractTable argTable){
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        return null;
    }

    //OKAY
    // Called when visiting the program's main class
    @Override
    public AbstractTable visit(MainClass n, AbstractTable argTable) {
        //get class name
        String classID = n.f1.f0.tokenImage;

        ClassTable classData = new ClassTable(classID, "java.lang.Object", argTable.Global);
        MethodTable methodData = new MethodTable("main", classID, new TypeTable("main", classID));
        TypeTable type = new TypeTable("main", classID, argTable.Global);
        methodData.addParam(n.f11.f0.tokenImage, type);
        classData.addMethod("main", methodData);
        argTable.Global.addClass(classID, classData);

        n.f0.accept(this, classData);
        n.f1.accept(this, classData);
        n.f2.accept(this, classData);
        n.f3.accept(this, methodData);
        n.f4.accept(this, methodData);
        n.f5.accept(this, methodData);
        n.f6.accept(this, methodData);
        n.f7.accept(this, methodData);
        n.f8.accept(this, methodData);
        n.f9.accept(this, methodData);
        n.f10.accept(this, methodData);
        n.f11.accept(this, methodData);
        n.f12.accept(this, methodData);
        n.f13.accept(this, methodData);
        n.f14.accept(this, methodData);
        n.f15.accept(this, methodData);
        n.f16.accept(this, classData);

        return classData;
    }

    //OKAY
    // Called when visiting a class of the program
    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argTable) {
        
        //All the classes extends from java.lang.Object
        String classID = n.f1.f0.tokenImage;
        ClassTable classData = new ClassTable(classID, "java.lang.Object", argTable.Global);

        n.f0.accept(this, classData);
        n.f1.accept(this, classData);
        n.f2.accept(this, classData);
        n.f3.accept(this, classData);
        n.f4.accept(this, classData);
        n.f5.accept(this, classData);

        //AllClassTable need to add every new class
        argTable.Global.addClass(classID, classData);

        return classData;
    }

    //OKAY
    // Called when visiting a variable of the program
    @Override
    public AbstractTable visit(VarDeclaration n, AbstractTable argTable) {
        TypeTable type;
        type = new TypeTable(n.f0.f0.which, argTable.MethodName, argTable.ClassName, argTable.Global);

        n.f0.accept(this, type);

        //To check whether this variable is a method level variable
        if(argTable instanceof MethodTable) ((MethodTable)argTable).addLocal(n.f1.f0.tokenImage, type);
        else if(argTable instanceof ClassTable) ((ClassTable)argTable).addVar(n.f1.f0.tokenImage, type);

        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);

        return type;
    }

/*
    // Called when visiting a method of the program
    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argTable) {
        try {
            // get return type
            String ret_type = n.f1.f0.choice.getClass().getSimpleName();
            // get id
            String id = n.f2.f0.tokenImage;
            argTable.currMethodName = id;
            
            

            // get class

            argTable.class_map.put(id, new ClassTable());
            argTable.putClassMethod(id, ret_type, id);
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
        } catch(Exception e){
            System.out.println("Type error");
            System.exit(1);
            //e.printStackTrace();
        }
        return null;
    }
*/

}
