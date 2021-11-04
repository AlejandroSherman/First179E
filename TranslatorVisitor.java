import syntaxtree.*;
import visitor.GJDepthFirst;

import java.util.LinkedList;



public class TranslatorVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    public static int tempCounter = 0;
    public static int ifCounter = 0;
    public static int ifLabelCounter = 0;
    public static int tabCounter = 0;
    public static LinkedList<String> temps = new LinkedList();
    public static LinkedList<String> statements = new LinkedList();

    public String memAlloc(VTable vtable){
        int recordSize = vtable.records.size();
        int allocSize = recordSize * 4 + 4;
        String r = "HeapAllocZ("+allocSize+")";

        return r;
    }

    public String tab(){
        String tabs = "";
        for(int i = 0; i < tabCounter; i++){
            tabs += "  ";
        }

        return tabs;
    }


    @Override
    public AbstractTable visit(MainClass n, AbstractTable argu) {

        System.out.println("\n\nfunc Main()");

        LinkedList<String> main = new LinkedList<>();
        main.add("func Main()");
        AbstractTable i = new AbstractTable();
        for(Node statement: n.f15.nodes){
            //System.out.println(statement.accept(this, argu));//
            i = statement.accept(this,argu);
        }

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
        
        System.out.println(tab() + "ret");
        tabCounter = 0;

        return _ret;
    }

    @Override
    public AbstractTable visit(VarDeclaration n, AbstractTable argu) {
        String varType = n.f0.f0.choice.getClass().getSimpleName();
        varType = argu.dictionary.getRealType(varType);
        String varName = n.f1.f0.tokenImage;

        argu.Global.CurrentVar.varName = varName; // test
        //System.out.println("    " + varType + " "  + varName );

        AbstractTable _ret=null;
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        n.f2.accept(this,argu);
        return _ret;
    }

    @Override
    public AbstractTable visit(IntegerLiteral n, AbstractTable argu) {
        /////////////////// test
        //String varName = argu.Global.CurrentVar.varName;
        String varValue = n.f0.tokenImage;

        //if(isAssign) {
        //    System.out.println(varValue);
        //    isAssign = false;
        //}

        n.f0.accept(this, argu);

        return null;
    }

    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argu){
        System.out.println("\nfunc " + n.f2.f0.tokenImage + "()");
        tabCounter++;

        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
        n.f5.accept(this,argu);
        n.f6.accept(this,argu);
        n.f7.accept(this,argu);
        n.f8.accept(this,argu);
        n.f9.accept(this,argu);
        n.f10.accept(this,argu);
        n.f11.accept(this,argu);
        n.f12.accept(this,argu);

        System.out.println(tab() + "ret");
        tabCounter = 0;
        return null;
    }

    @Override
    public AbstractTable visit(IfStatement n, AbstractTable argu) {
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        System.out.print(tab() + "t." + tempCounter + " = ");
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);



        System.out.println(tab() + "if" + ifCounter++ + " t." + tempCounter++ + " goto :if" + ++ifLabelCounter + "_else");
            // do something inside if statement
            tabCounter++;
            n.f4.accept(this,argu);
        if(isAssign){
            System.out.println("THERE WAS NOTHING ASSIGNED!");
            isAssign = false;
        }
        System.out.println(tab() + "goto :if" + ifCounter + "_end");
        tabCounter--;
        n.f5.accept(this,argu);
        System.out.println(tab() + "if" + ifLabelCounter + "_else:");
            // do something inside if-else statement
            tabCounter++;
            n.f6.accept(this,argu);
            tabCounter--;
        System.out.println(tab() + "if" + ifCounter + "_end:");
        ifCounter = 0;
        tempCounter = 0;
        return null;
    }

    @Override
    public AbstractTable visit(CompareExpression n, AbstractTable argu) {
        isAssign = false;
        System.out.print("LtS(num 1");

        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        n.f2.accept(this,argu); // can print 1 from here but also prints every IntegerLiteral
        System.out.println(")");
        return null;
    }

    @Override
    public AbstractTable visit(TimesExpression n, AbstractTable argu) {
        isAssign = false;
        System.out.println("MulS(num " +  "t." + tempCounter + ")");
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //n.f2.accept(this,argu); skip print IntegerLiteral value

        return null;
    }

    public static boolean isAssign = false;
    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argu) {
        String id = n.f0.f0.tokenImage;
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        isAssign = true;
        System.out.print(tab() + id + " = ");
        n.f2.accept(this,argu);

        return null;
    }

    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argu) {
        n.f0.accept(this,argu);
        //VTable vtable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        //System.out.println("t." + tempCounter + " = " + memAlloc(vtable));
        n.f1.accept(this,argu);
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
        n.f5.accept(this,argu);

        return null;
    }

    @Override
    public AbstractTable visit(Statement n, AbstractTable argu) {
        statements.add(n.f0.choice.getClass().getSimpleName());
        //System.out.println(statements.getLast());
        n.f0.accept(this,argu);

        return null;
    }

    @Override
    public AbstractTable visit(PrintStatement n, AbstractTable argu) {
        n.f0.accept(this,argu);
        n.f1.accept(this,argu);
        //System.out.println("    "+n.f2.getClass().getSimpleName());
        n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
        return null;
    }

    @Override
    public AbstractTable visit(Expression n, AbstractTable argu) {
        //System.out.println(n.f0.choice.getClass().getSimpleName());
        n.f0.accept(this,argu);
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

    public static Boolean isNew = true;
    @Override
    public AbstractTable visit(AllocationExpression n, AbstractTable argu) {
        n.f0.accept(this, argu);
        VTable vtable = argu.GlobalVTables.vtables.get(argu.Global.CurrentClass.className);
        if(isNew) {
            tabCounter++;
            System.out.println(tab() + "t." + tempCounter + " = " + memAlloc(vtable));
            System.out.println(tab() + "[t." + tempCounter + "] = :vmt_" + n.f1.f0.tokenImage);
            isNew = false;
            tabCounter--;
        }
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);


        return null;
    }

    @Override
    public AbstractTable visit(PrimaryExpression n, AbstractTable argu) {
        //System.out.println(n.f0.choice.getClass().getSimpleName());
        n.f0.accept(this,argu);

        return null;
    }

    @Override
    public AbstractTable visit(Identifier n, AbstractTable argu) {
        n.f0.accept(this,argu);
        return null;
    }
}


