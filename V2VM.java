import cs132.vapor.ast.VInstr;

public class V2VM {
    public static void main(String[] a){
        Liveness liveness = new Liveness();
        VInstr[] instructs = new VInstr[1];
        liveness.checkLiveness(instructs);
    }

}
