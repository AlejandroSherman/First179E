import java.util.*;

public class OneFunction {
    public String name;
    public String ofClass;
    public ArrayList<String> params;
    public String code; //testing
    public Integer index;
    public Integer classIndex;
    public String label;

    public OneFunction(String n, Integer indx, VTable tempTable){
        name = n;
        ofClass = tempTable.className;
        params = new ArrayList<String>();
        if(!n.equals("Main")) params.add("this"); 
        code = "";
        index = indx;
        classIndex = tempTable.index;
        label = "";
    }

    public void printParams(){
        String out;
        for(Integer i=0;i<params.size();i++){
            out = params.get(i);
            System.out.print(out);
        }
    }

}
