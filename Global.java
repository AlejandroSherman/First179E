import java.util.HashMap;

public class Global {
    /* holds the scopes

    +--------------------+
    +     Scope 0        +
    +--------------------+
    +     Scope 1        +
    +--------------------+
             .
             .
             .
     */

    public HashMap<String, Scope> scopeMap;
    public Scope currScope;

    // constructor
    public Global(){
        scopeMap = new HashMap<String, Scope>();
    }

    // constructor
    public Global(String scopeName, Scope scope){
        scopeMap = new HashMap<String, Scope>();
        scopeMap.put(scopeName, scope);
    }

}
