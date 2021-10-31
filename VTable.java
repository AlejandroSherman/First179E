import java.util.HashMap;

public class VTable {
    String className;
    Integer index;
    Integer funcIndex;
    HashMap<String, Integer> records;
    HashMap<String, Integer> funcs;

    public VTable(){
        className = "none";
        index = -1;
        funcIndex = 0;
        records = new HashMap<String, Integer>();
        funcs = new HashMap<String, Integer>();;
    }

    
    public VTable(String name, Integer indx){
        className = name;
        index = indx;
        funcIndex = 0;
        records = new HashMap<String, Integer>();
        funcs = new HashMap<String, Integer>();;
    }

    public void addMethod(String name){
        funcs.put(name, funcIndex);
        funcIndex++;
    }


    public void printVTable(){
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
    }
}
