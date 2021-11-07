import java.util.HashMap;

public class VaporGen {
    public HashMap<String, OneFunction> functions;

    public VaporGen(){
        functions = new HashMap<String, OneFunction>();
    }

    public void addFunc(String name, OneFunction func){
        functions.put(name,func);
    }

    public void printFuncs(Integer numClasses){
        //Print functions in order class -> method order
        for (Integer i=0 ; i < numClasses ; i++) {
            for(Integer j=0; j < functions.size() ; j++) {

                for (String funcName: functions.keySet()) {
                    OneFunction tempFunc = functions.get(funcName);
        
                    //Print functions in order class -> method order
                    if(tempFunc.classIndex == i && tempFunc.index == j){
                        //System.out.print("INDEX["+ tempFunc.classIndex + "," + tempFunc.index +"]\n");   

                        if(tempFunc.name.equals("Main")) System.out.print("func Main(");
                        else System.out.print("func "+tempFunc.ofClass+"."+tempFunc.name+"(");

                        tempFunc.printParams();
                        System.out.print(")\n");
                        System.out.println(tempFunc.code);
                    }
                }

            }
        } 

    }
}
