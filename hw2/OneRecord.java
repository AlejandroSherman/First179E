public class OneRecord {
    public String name;
    public String ofClass;
    public Integer index;
    public Integer classIndex;
    
    public OneRecord(){
    }

    public OneRecord(String n, Integer indx, VTable tempTable){
        name = n;
        ofClass = tempTable.className;
        index = indx;
        classIndex = tempTable.index;
    }
}
