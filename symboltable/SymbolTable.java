// book talks about red-black tree https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html
// map class https://docs.oracle.com/javase/8/docs/api/java/util/Map.html
// treemap vs hashmap. treemap has order so may be better for scope https://www.baeldung.com/java-treemap-vs-hashmap

package symboltable;

import syntaxtree.Type;

import java.util.HashMap;
import java.util.Map;


public class SymbolTable {
    public Map<String, ClassTable> class_map;

    // Constructor
    public SymbolTable(){
        class_map = new HashMap<String, ClassTable>();
    }

    // Constructor
    public SymbolTable(String clazz, ClassTable class_Table) {
        class_map = new HashMap<String, ClassTable>();
        class_map.put(clazz, class_Table);
    }

    // puts ===================================================================

    // <key:clazz, value:class_table> -> class_map
    public ClassTable putClass(String clazz) throws Exception {
        // duplicate class check
        if(class_map.get(clazz) != null) {
            throw new Exception("Error: duplicate class " + clazz + " found in class_map");
        }
        // make class table
        ClassTable class_table = new ClassTable();
        //put in map
        class_map.put(clazz, class_table);

        return class_table;
    }

    // <key:field, value:type> -> clazz
    public void putClassField(String var, Type type, String clazz) throws Exception {
        // get class table
        ClassTable class_table = class_map.get(clazz);
        // duplicate var check
        if(class_table.vars.get(var) != null) {
            throw new Exception("Error: duplicate variable " + var + " found in class " + clazz);
        }
        //put in map
        class_table.vars.put(var, type);
    }

    // <key:method, value:type> -> clazz
    public void putClassMethod(String method, Type type, String clazz) throws Exception {
        // get class table
        ClassTable class_table = class_map.get(clazz);
        // duplicate method check
        if(class_table.methods.get(method) != null) {
            throw new Exception("Error: duplicate method " + method + " found in class " + clazz);
        }
        // make method table
        MethodTable m = new MethodTable(method, type);
        // put in map
        class_table.methods.put(method, m);
    }

    // <key:param, value:type> -> method -> clazz
    public void putMethodParam(String param, Type type, String method, String clazz) throws Exception {
        // get method table
        MethodTable method_table = class_map.get(clazz).methods.get(method); // class_table = class_map.get(clazz)
        // duplicate param check
        if(method_table.params.get(param) != null){
            throw new Exception("Error: duplicate param " + param + " found in method " + method + " from class " + clazz);
        }
        // put in map
        method_table.params.put(param, type);
    }

    // <key:local, value:type> -> method -> clazz
    public void putMethodVar(String local, Type type, String method, String clazz) throws Exception {
        // get method table
        MethodTable method_table = class_map.get(clazz).methods.get(method); // class_table = class_map.get(clazz)
        // duplicate local check
        if(method_table.locals.get(local) != null){
            throw new Exception("Error: duplicate local " + local + " found in method " + method + " from class " + clazz);
        }
        // put in map
        method_table.locals.put(local, type);
    }

    // gets ===================================================================

    public ClassTable getClass(String clazz) throws Exception {
        // get class table
        ClassTable class_table = class_map.get(clazz);
        // check for class
        if(class_table != null){
            return class_table;
        }
        throw new Exception("Error: there is no class " + clazz);
    }

    public Type getClassField(String var, Type type, String clazz) throws Exception {
        // get class var
        Type class_field = class_map.get(clazz).vars.get(var);
        // check for class var
        if(class_field != null){
            return class_field;
        }
        throw new Exception("Error: there is no field " + var);
    }

    public MethodTable getClassMethod(String clazz, String method) throws Exception {
        // get method table
        MethodTable method_table = getClass(clazz).methods.get(method); // class_table = getClass(clazz)
        // check for method table
        if(method_table !=null){
            return method_table;
        }
        throw new Exception("Error: there is no method " + method);
    }

    public Type getMethodParam(String param, String method, String clazz) throws Exception {
        // get method table
        MethodTable method_table = class_map.get(clazz).methods.get(method); // class_table = class_map.get(clazz)
        // check for method param
        Type method_param = method_table.locals.get(param);
        if(method_param != null){
            return method_param;
        }
        throw new Exception("Error: there is no param " + param + " in method " + method + " from class " + clazz);
    }

    public Type getMethodLocal(String local, String method, String clazz) throws Exception {
        // get method table
        MethodTable method_table = class_map.get(clazz).methods.get(method); // class_table = class_map.get(clazz)
        // check for method local
        Type method_local = method_table.locals.get(local);
        if(method_local != null){
            return method_local;
        }
        throw new Exception("Error: there is no local " + local + " in method " + method + " from class " + clazz);
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "class_map=" + class_map +
                '}';
    }

}

