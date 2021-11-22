import cs132.vapor.ast.*;

import java.util.*;

public class Liveness {

    public Pair checkLiveness(VInstr[] instructs){        
        ArrayList<HashSet<Integer>> ins = new ArrayList<>();
        ArrayList<HashSet<Integer>> outs = new ArrayList<>();
        ArrayList<HashSet<Integer>> gens = new ArrayList<>();
        ArrayList<HashSet<Integer>> kills = new ArrayList<>();
        ArrayList<HashSet<Integer>> succ = new ArrayList<>();
        ArrayList<HashSet<Integer>> actives = new ArrayList<>();

        //Fill ArrayLists
        for (int i = 0; i < instructs.length; i++) {
            ins.add(new HashSet<>(0));
            outs.add(new HashSet<>(0));
            gens.add(new HashSet<>(0));
            kills.add(new HashSet<>(0));
            succ.add(new HashSet<>(0));
            actives.add(new HashSet<>(0));
        }

        GenKillVisitor genKillVisitor = new GenKillVisitor();
        for(var i = 0; i < instructs.length; i++){
            var instruct = instructs[i];
            Pair temp = genKillVisitor.visitDispatch(instruct);
            gens.set(i, temp.left);
            kills.set(i,temp.right);
            ins.set(i, temp.left);
            ins.get(i).removeAll(temp.right);
            outs.set(i, new HashSet<>(0));
        }


        for(var i = 0; i < instructs.length; i++){
            var instruct = instructs[i];
            succ.set(i, new HashSet<>(0));

            if(instruct instanceof VGoto goto1){
                goto1 = (VGoto)instruct;
                var target = goto1.target;
                if(target instanceof VAddr<?>){
                    var labelRef = (VAddr.Label<VCodeLabel>)target;
                    succ.get(i).add(labelRef.label.getTarget().instrIndex);
                }
            }

            else{
                if(i != instructs.length - 1){
                    //If anything is wrong in liveness its most likely this line
                    //succ[i].add(instructs[i+1]);
                    succ.get(i).add(i+1);

                }
                if(instruct instanceof VBranch branch){
                    branch = (VBranch)instruct;
                    succ.get(i).add(branch.target.getTarget().instrIndex);
                }
            }
        }


        Boolean changed = true;
        while(Boolean.TRUE.equals(changed)){
            changed = false;
            for(Integer i = instructs.length-1; i != -1; i--){
                HashSet<Integer> prevIns = new HashSet<>(ins.get(i));
                for(Integer s : succ.get(i)){
                    outs.get(i).addAll(ins.get(s));
                }
                ins.get(i).addAll(outs.get(i));
                ins.get(i).removeAll(kills.get(i));
                if(!prevIns.equals(ins.get(i))) changed = true;
            }
        }

        for(var i = 0; i < instructs.length; i++){
            actives.set(i, ins.get(i));
            actives.get(i).addAll(kills.get(i));
        }


        for(var i = 0; i < instructs.length; i++){
            //FIXME System.out.println("line in code: " + instructs[i].sourcePos.line);
            //System.out.println("    var(s) active: " + actives.get(i));
            //System.out.println("    var(s) out: " + outs.get(i));
        }


        return new Pair(actives, outs);
    }

}