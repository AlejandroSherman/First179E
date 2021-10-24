import java.util.HashMap;

public class Scope extends Global {
    /* each Scope has a ClassRecord and VTable.

    (Superclass)
       +============================+
       + Class Record:              +
       +      vars and offsets      +
       + VTable:                    +
       +      classes and offsets   +
       +=============================

    - Class record has a reference to superclass.
    - Copy Vtable of superclass into subclass.

     */

    public HashMap<String, ClassRecord> classRecordMap;
    public HashMap<String, VTable> vTableMap;
    public ClassRecord classRecord;
    public VTable vTable;

    // constructor
    public Scope(){
        classRecordMap = new HashMap<>();
        vTableMap = new HashMap<>();
        classRecord = null;
        vTable = null;
    }

    // constructor
    public Scope(String name, ClassRecord classRecord, VTable vtable){
        classRecordMap = new HashMap<String, ClassRecord>();
        vTableMap = new HashMap<String, VTable>();
        classRecordMap.put(name, classRecord);
        vTableMap.put(name, vtable);
    }
}
