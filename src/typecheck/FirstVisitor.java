package typecheck;

import syntaxtree.*;
import typecheck.*;
import visitor.GJDepthFirst;

public class FirstVisitor extends GJDepthFirst<AbstractTable, AbstractTable> {

    /**
     * Get a symbol table to store all the classes
     */ //Lab example
    @Override
    public AbstractTable visit(Goal n, AbstractTable argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
    }

    /**
     * Set the type of the Identifier, first need to check whether it's a class type
     */ //Lab example
    @Override
    public AbstractTable visit(Identifier n, AbstractTable argu){

        //Only if the ID's type is TypeTable can edit, if it's MethodName or ClassName, it wouldn't work
        if(argu instanceof TypeTable && ((TypeTable)argu).isClassType())
            ((TypeTable)argu).Type = n.f0.tokenImage;

        n.f0.accept(this, argu);

        return null;
    }

    /**
     * Get a class table of the main class and a method table of main method
     */ //Lab example
    @Override
    public AbstractTable visit(MainClass n, AbstractTable argu){
        ClassTable classtable = new ClassTable(n.f1.f0.tokenImage, "java.lang.Object", argu.Global);
        MethodTable methodtable = new MethodTable("main", n.f1.f0.tokenImage, new TypeTable("main", n.f1.f0.tokenImage);
        TypeTable type = new TypeTable("main", n.f1.f0.tokenImage, argu.Global);
        methodtable.addParameter(n.f11.f0.tokenImage, type);
        classtable.addMethod("main", methodtable);
        argu.Global.addClass(n.f1.f0.tokenImage, classtable);

        n.f0.accept(this, classtable);
        n.f1.accept(this, classtable);
        n.f2.accept(this, classtable);
        n.f3.accept(this, methodtable);
        n.f4.accept(this, methodtable);
        n.f5.accept(this, methodtable);
        n.f6.accept(this, methodtable);
        n.f7.accept(this, methodtable);
        n.f8.accept(this, methodtable);
        n.f9.accept(this, methodtable);
        n.f10.accept(this, methodtable);
        n.f11.accept(this, methodtable);
        n.f12.accept(this, methodtable);
        n.f13.accept(this, methodtable);
        n.f14.accept(this, methodtable);
        n.f15.accept(this, methodtable);
        n.f16.accept(this, classtable);

        return classtable;
    }

    @Override //Lab example
    public AbstractTable visit(ClassDeclaration n, AbstractTable argu){
        //All the classes extends from java.lang.Object
        ClassTable classtable = new ClassTable(n.f1.f0.tokenImage, "java.lang.Object", argu.Global);

        n.f0.accept(this, classtable);
        n.f1.accept(this, classtable);
        n.f2.accept(this, classtable);
        n.f3.accept(this, classtable);
        n.f4.accept(this, classtable);
        n.f5.accept(this, classtable);

        //AllClassTable need to add every new class
        argu.Global.addClass(n.f1.f0.tokenImage, classtable);
        return classtable;
    }

    @Override //Lab example
    public AbstractTable visit(VarDeclaration n, AbstractTable argu){
        TypeTable type;
        type = new TypeTable(n.f0.f0.which, argu.MethodName, argu.ClassName, argu.Global);

        n.f0.accept(this, type);

        //To check whether this variable is a method level variable
        if(argu instanceof MethodTable)
            ((MethodTable)argu).addvariable(n.f1.f0.tokenImage, type);
        else if(argu instanceof ClassTable)
            ((ClassTable)argu).addVariable(n.f1.f0.tokenImage, type);

        n.f1.accept(this, argu);
        n.f2.accept(this, argu);

        return type;
    }

}
