import cs132.vapor.ast.*;

import java.util.Collection;
import java.util.HashSet;

public class Liveness {

    public Pair checkLiveness(VInstr[] instructs){
        var ins = new HashSet<?>[instructs.length];
        var outs = new HashSet<?>[instructs.length];
        var gens = new HashSet<?>[instructs.length];
        var kills = new HashSet<?>[instructs.length];

        GenKillVisitor genKillVisitor = new GenKillVisitor();
        for(var i = 0; i < instructs.length; i++){
            var instruct = instructs[i];
            var temp = genKillVisitor.visitDispatch(instruct);
            gens[i] = (HashSet<?>)temp.left;
            kills[i] = (HashSet<?>)temp.right;
            ins[i] = (HashSet<?>)temp.left;
            ins[i].removeAll(temp.right);
            outs[i] = new HashSet<Integer>(0);
        }

        var succ = new HashSet[instructs.length];
        for(var i = 0; i < instructs.length; i++){
            var instruct = instructs[i];
            succ[i] = new HashSet<Integer>(0);
            if(instruct instanceof VGoto){
                VGoto goto1 = (VGoto)instruct;
                var target = goto1.target;
                if(target instanceof VAddr<?>){
                    var labelRef = (VAddr.Label<VCodeLabel>)target;
                    succ[i].add(labelRef.label.getTarget().instrIndex);
                }
            }
            else{
                if(i != instructs.length - 1){
                    succ[i].add(instructs[i+1]);
                }
                if(instruct instanceof VBranch){
                    var branch = (VBranch)instruct;
                    succ[i].add(branch.target.getTarget().instrIndex);
                }
            }
        }

        var changed = true;
        while(changed){
            changed = false;
            for(var i = instructs.length-1; i != -1; i--){
                var prevIns = ins[i].clone();
                for(var s : succ[i]){
                    outs[i].addAll((Collection)ins[(Integer)s]);
                }
                // ins(i) ++= outs(i)
                ins[i].addAll((Collection)outs[(Integer)i]);
                // ins(i) --= kills(i)
                ins[i].removeAll((Collection)kills[i]);
                changed = changed || (!prevIns.equals(ins[i]));
            }
        }

        var actives = new HashSet[instructs.length]; 
        for(var i = 0; i < instructs.length; i++){
            actives[i] = ins[i];
            actives[i].add(kills[i]);
        }

        return new Pair(actives, outs);
    }

}
