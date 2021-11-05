import java.util.HashMap;

public class VTable {
    String className;
    Integer index;
    Integer funcIndex;
    HashMap<String, OneRecord> records;
    HashMap<String, OneFunction> funcs;

    public VTable(){
        className = "none";
        index = -1;
        funcIndex = 0;
        records = new HashMap<String, OneRecord>();
        funcs = new HashMap<String, OneFunction>();
    }

    
    public VTable(String name, Integer indx){
        className = name;
        index = indx;
        funcIndex = 0;
        records = new HashMap<String, OneRecord>();
        funcs = new HashMap<String, OneFunction>();;
    }

    public void addFunc(String name){
        OneFunction tempFunc = new OneFunction(name,funcIndex,this);
        funcs.put(name, tempFunc);
        funcIndex++;
    }


    public void printVTable(){
        if(records.isEmpty() && funcs.isEmpty()){
            
        }
        else {
            //System.out.println("INDEX["+ index +"]");
            System.out.println("const vmt_" + className);
        }

        for (String recordName: records.keySet()) {
            //Integer recordIndex = records.get(recordName);
            //System.out.println("    name: " + recordName + " index: " + recordIndex);  
        }

        for (String funcName: funcs.keySet()) {
            OneFunction tempFunc = funcs.get(funcName);
            System.out.println("    :" + className + "." + tempFunc.name);  
        } 

        System.out.print("\n");
    }
}
