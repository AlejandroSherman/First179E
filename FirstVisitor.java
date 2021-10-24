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
        String className = n.f1.f0.tokenImage;

        ClassTable classData = new ClassTable(className, "java.lang.Object", argTable.Global);
        MethodTable methodData = new MethodTable("main", className, "IntegerType", new TypeTable("main", className));
        TypeTable type = new TypeTable("main", className, argTable.Global);
        methodData.addParam(n.f11.f0.tokenImage, type);
        classData.addMethod("main", methodData);
        argTable.Global.addClass(className, classData);

        //System.out.println("className: "+className);
        //System.out.println("addParam: "+n.f11.f0.tokenImage);

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

        //System.out.println("classID: "+ classID);

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
        //Identifier means its a class
        String dataType = n.f0.f0.choice.getClass().getSimpleName();
        String varName = n.f1.f0.tokenImage;
        
        TypeTable type = new TypeTable(n.f0.f0.which, argTable.MethodName, argTable.ClassName, dataType, argTable.Global);

        //System.out.println(argTable.ClassName + "->" + argTable.MethodName + "->" + varName);

        n.f0.accept(this, type);

        //To check whether this variable is a method level variable
        //System.out.println(argTable.getClass().getSimpleName()); 

        //This is a hacky way to handle this (not good)
        if(argTable.MethodName == null) ((ClassTable)argTable).addVar(varName, type); // its a class variable
        else {
            //String className = argTable.ClassName;
            //String methodName = argTable.MethodName;
            //System.out.println(className + " " + methodName);
            ClassTable classTable = (ClassTable)argTable;
            MethodTable methodTable = classTable.methods.get(argTable.MethodName);
            methodTable.addLocal(varName, type);
        }

        //if(argTable instanceof MethodTable) ((MethodTable)argTable).addLocal(varName, type);
        //else if(argTable instanceof ClassTable) ((ClassTable)argTable).addVar(varName, type);

        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);

        return type;
    }

    // Called when visiting a method of the program
    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argTable) {
        // get return type
        String ret_type = n.f1.f0.choice.getClass().getSimpleName();
        // get method name
        String methodName = n.f2.f0.tokenImage;
        // get the method's class name
        String ofClass = argTable.ClassName;
        //System.out.println("of class: " + ofClass);

        MethodTable methodData = new MethodTable(methodName, ofClass, ret_type, new TypeTable(methodName, ofClass));       
        ( (ClassTable)argTable ).addMethod(methodName, methodData);
        ( (ClassTable)argTable ).updateMethodName(methodName); //Updates the current method name (not permanent)

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

        return methodData;
    }

}
