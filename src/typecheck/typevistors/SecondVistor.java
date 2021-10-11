package typecheck;

import syntaxtree.*;
import typecheck.*;
import java.util.*;
import visitor.GJDepthFirst;

public class SecondVisitor extends GJDepthFirst<String, AbstractTable> {
    @Override //Lab example
    public String visit(MainClass n, AbstractTable argu){
        //Get the main class table from 'all class table' variable Global
        ClassTable classtable = argu.Global.getClassTable(n.f1.f0.tokenImage);

        n.f0.accept(this, classtable);
        n.f1.accept(this, classtable);
        n.f2.accept(this, classtable);

        //Get the main method table from the main class table
        MethodTable methodtable = classtable.getMethodTable("main");
        n.f3.accept(this, methodtable);
        n.f4.accept(this, methodtable);
        n.f5.accept(this, methodtable);
        n.f6.accept(this, methodtable);
    }

    /** Lab example
     * Check and expression's type
     */
    @Override
    public String visit(AndExpression n, AbstractTable argu) {
        //Set left exp's type by accept it
        TypeTable lefttype = New TypeTable(argu);

        String leftid = n.f0.accept(this, lefttype);
        n.f1.accept(this, argu);

        TypeTable righttype = new TypeTable(argu);

        String rightid = n.f2.accept(this, righttype);

        //Check another path of left and right are boolean type
        if(!lefttype.Type.equals("boolean") || !righttype.Type.equals("boolean"))
            argu.exit(1);
        else //If no problem this expression's type will be boolean
            ((TypeTable)argu).Type = "boolean";
        return leftid + "&&" + rightid;
    }
}
