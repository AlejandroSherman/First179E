import java.util.HashMap;

public class VTable extends Scope {
    /* - store classes and offsets. (another map? <key: class, value: offset>)
            class_name1 -> 0
            class_name2 -> 1
       - Copy Vtable of superclass into subclass.
     */

    public HashMap<String, Integer> classMap;

    // constructor
    public VTable(){ classMap = new HashMap<String, Integer>(); }

    // constructor
    public VTable(String clazz, Integer offset){
        classMap = new HashMap<String, Integer>();
        classMap.put(clazz, offset);
    }
}
