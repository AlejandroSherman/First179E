import syntaxtree.*;
import visitor.GJDepthFirst;

public class FirstVisitor extends GJDepthFirst {
    public SymbolTable symbol_table;

    // Constructor
    public FirstVisitor(){
        symbol_table = new SymbolTable();
    }

    @Override
    public Object visit(Goal n, Object argu){
        n.f0.accept(this, symbol_table);
        n.f1.accept(this, symbol_table);
        n.f2.accept(this, symbol_table);
        return null;
    }

    @Override
    public Object visit(MainClass n, Object argu) {
        try {
            // get class name
            String clazz = n.f1.f0.tokenImage;
            // put class in symbol table
            symbol_table.putClass(clazz);
            n.f0.accept(this, symbol_table);
            n.f1.accept(this, symbol_table);
            n.f2.accept(this, symbol_table);
            n.f3.accept(this, symbol_table);
            n.f4.accept(this, symbol_table);
            n.f5.accept(this, symbol_table);
            n.f6.accept(this, symbol_table);
            n.f7.accept(this, symbol_table);
            n.f8.accept(this, symbol_table);
            n.f9.accept(this, symbol_table);
            n.f10.accept(this, symbol_table);
            n.f11.accept(this, symbol_table);
            n.f12.accept(this, symbol_table);
            n.f13.accept(this, symbol_table);
            n.f14.accept(this, symbol_table);
            n.f15.accept(this, symbol_table);
            n.f16.accept(this, symbol_table);
            n.f17.accept(this, symbol_table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object visit(ClassDeclaration n, Object argu) {
        try {
            String clazz = n.f1.f0.toString();
            symbol_table.putClass(clazz);
            // vars and methods?
            n.f0.accept(this, symbol_table);
            n.f1.accept(this, symbol_table);
            n.f2.accept(this, symbol_table);
            n.f3.accept(this, symbol_table);
            n.f4.accept(this, symbol_table);
            n.f5.accept(this, symbol_table);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object visit(VarDeclaration n, Object argu) {
        // get type
        String t = n.f0.f0.choice.getClass().getSimpleName();
        // get id
        String id = n.f1.f0.tokenImage;
        //symbol_table.putMethodVar(id, t);
        n.f0.accept(this, symbol_table);
        n.f1.accept(this, symbol_table);
        n.f2.accept(this, symbol_table);
        return null;
    }

    @Override
    public Object visit(MethodDeclaration n, Object argu) {

        n.f0.accept(this, symbol_table);
        n.f1.accept(this, symbol_table);
        n.f2.accept(this, symbol_table);
        n.f3.accept(this, symbol_table);
        n.f4.accept(this, symbol_table);
        n.f5.accept(this, symbol_table);
        n.f6.accept(this, symbol_table);
        n.f7.accept(this, symbol_table);
        n.f8.accept(this, symbol_table);
        n.f9.accept(this, symbol_table);
        n.f10.accept(this, symbol_table);
        n.f11.accept(this, symbol_table);
        n.f12.accept(this, symbol_table);
        return null;
    }
}
