import java.util.HashMap;

public class VTable {
    String className;
    Integer index;
    Integer funcIndex;
<<<<<<< Updated upstream
    HashMap<String, Integer> records;
    HashMap<String, Integer> funcs;
=======
    HashMap<String, OneRecord> records;
    HashMap<String, OneFunction> funcs;
>>>>>>> Stashed changes

    public VTable(){
        className = "none";
        index = -1;
        funcIndex = 0;
<<<<<<< Updated upstream
        records = new HashMap<String, Integer>();
        funcs = new HashMap<String, Integer>();;
=======
        records = new HashMap<String, OneRecord>();
        funcs = new HashMap<String, OneFunction>();
>>>>>>> Stashed changes
    }

    
    public VTable(String name, Integer indx){
        className = name;
        index = indx;
        funcIndex = 0;
<<<<<<< Updated upstream
        records = new HashMap<String, Integer>();
        funcs = new HashMap<String, Integer>();;
    }

    public void addMethod(String name){
        funcs.put(name, funcIndex);
=======
        records = new HashMap<String, OneRecord>();
        funcs = new HashMap<String, OneFunction>();;
    }

    public void addFunc(String name){
        OneFunction tempFunc = new OneFunction(name,funcIndex,this);
        funcs.put(name, tempFunc);
>>>>>>> Stashed changes
        funcIndex++;
    }


    public void printVTable(){
<<<<<<< Updated upstream
        System.out.println("const vmt_" + className);

        //System.out.println("records: ");
        for (String recordName: records.keySet()) {
            Integer recordIndex = records.get(recordName);
            System.out.println("    name: " + recordName + " index: " + recordIndex);  
        }

        //System.out.println("functions: ");
        for (String funcName: funcs.keySet()) {
            //Integer funcsIndex = funcs.get(funcName);
            System.out.println("    :" + className + "." + funcName);  
            //System.out.println("    name: " + funcName + " index: " + funcsIndex);  
        } 
=======
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
>>>>>>> Stashed changes
    }
}
