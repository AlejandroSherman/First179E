import java.util.*;
import syntaxtree.*;

public class Scope {//Scope creates the scopetable takes the symbol table made in first visitor
    private Map<SymbolTable> scopetable = new LinkedHashMap<>();
    private Scope parent;
    private Node bind;

    public Scope(Node b) {
        parent = null;
        bind = b;
    }

    public Scope(Node b, Scope pt){
        parent = pt;
        bind = b;
    }

    public void add(SymbolTable st, Node n, Scope se) {
        scopetable.put(st, new Binder(st, n, se));
    }

    public Scope getParent(){
        return parent;
    }

    public Node getBind(){
        return bind;
    }

    public Binder find(SymbolTable sm){
        Binder bin = scopetable.get(sm);
        return bin;
    }
}