// book talks about red-black tree https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html
// map class https://docs.oracle.com/javase/8/docs/api/java/util/Map.html
// treemap vs hashmap. treemap has order so may be better for scope https://www.baeldung.com/java-treemap-vs-hashmap

import java.util.HashMap;

public class SymbolTable extends AbstractTable {
    public HashMap<String, ClassTable> classes;

    // Constructor
    public SymbolTable(){
        classes = new HashMap<String, ClassTable>();
    }

    // Constructor
    public SymbolTable(String className, String superClass, ClassTable class_Table) {
        classes = new HashMap<String, ClassTable>();
        classes.put(className, class_Table);
    }

    // need addClass
    public void addClass(String name, ClassTable classData){
        classes.put(name, classData);
    }

}

