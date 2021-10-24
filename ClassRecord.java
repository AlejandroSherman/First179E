import java.util.HashMap;

public class ClassRecord extends Scope {
    /* - store variables/fields and offsets. (another map? <key: variable, value: offset>)
            variable1 -> 0
            variable2 -> 1
       - ClassRecord has a reference to superclass.
       - Allocation: (# of words)*(wordsize)+(4 for pointer)
                     wordsize = sizeof(type)    // no sizeof() in java?
     */

    public HashMap<String, Integer> varMap; // store variables/fields and offsets.
    public Scope superClass; // ClassRecord has a reference to superclass.

    // constructor
    public ClassRecord(){
        varMap = new HashMap<String, Integer>();
    }

    // constructor
    public ClassRecord(String var, Integer offset){
        varMap = new HashMap();
        varMap.put(var, offset);
    }

}
