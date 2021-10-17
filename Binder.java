import syntaxtree.*;

/*
Idea for binder found from Java Compiler Textbook
 */
public class Binder {
    private SymbolTable st;
    private Node nd;
    private Scope sc;

    public Binder(SymbolTable s, Node n, Scope c){
        st = s;
        nd = n;
        sc = c;
    }

    public SymbolTable getSt(){
        return st;
    }

    public Node getNd(){
        return nd;
    }

    public Scope getSc() {
        return sc;
    }
}