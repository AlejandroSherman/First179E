import java.util.HashMap;

public class SymbolTable extends AbstractTable {
    public HashMap<String, OneClass> classes;
    public OneClass CurrentClass;
    public OneMethod CurrentMethod;
    public OneVar CurrentVar;
<<<<<<< Updated upstream
=======
    public OneFunction CurrentFunc;
>>>>>>> Stashed changes
    public String classOrmethod;

    public SymbolTable(){
        classes = new HashMap<String, OneClass>();
    }

    public void addClass(String name, OneClass classData){
        classes.put(name, classData);
    }
}

