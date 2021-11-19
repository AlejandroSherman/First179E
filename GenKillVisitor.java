import java.util.HashSet;

import cs132.vapor.ast.VAddr;
import cs132.vapor.ast.VAssign;
import cs132.vapor.ast.VBranch;
import cs132.vapor.ast.VBuiltIn;
import cs132.vapor.ast.VCall;
import cs132.vapor.ast.VCodeLabel;
import cs132.vapor.ast.VDataSegment;
import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VGoto;
import cs132.vapor.ast.VInstr;
import cs132.vapor.ast.VMemRead;
import cs132.vapor.ast.VMemRef;
import cs132.vapor.ast.VMemWrite;
import cs132.vapor.ast.VOperand;
import cs132.vapor.ast.VReturn;
import cs132.vapor.ast.VVarRef;

public class GenKillVisitor extends VInstr.VisitorR<Pair, Exception> {
    
    @Override
    public Pair visit(VAssign assign) {
        var source = assign.source;
        var gen = new HashSet<Integer>(0);

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }
        
        var dest = (VVarRef.Local)assign.dest;
        var kill = new HashSet<>(dest.index);

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VCall call) {
        var args = call.args;
        var gen = new HashSet<Integer>();

        for(VOperand arg : args){
            if(arg instanceof VVarRef.Local){
                var varSource = (VVarRef.Local)arg;
                gen.add(varSource.index);
            }
        }

        var addr = call.addr;

        if(addr instanceof VAddr.Var<?>){
            var varAddr = (VAddr.Var<VFunction>)addr;
            gen.add(((VVarRef.Local)(varAddr.var)).index);
        }

        var dest = call.dest;
        HashSet<?> kill;

        if (dest != null){
            kill = new HashSet<>(dest.index);
        }
        else{
            kill = new HashSet<>();
        }

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VBuiltIn builtIn) {
        var args = builtIn.args;
        var gen = new HashSet<>(0);

        for(VOperand arg : args){
            if(arg instanceof VVarRef.Local){
                var varSource = (VVarRef.Local)arg;
                gen.add(varSource.index);
            }
        }

        var kill = new HashSet<>();
        var dest = (VVarRef.Local)builtIn.dest;

        if(dest != null){
            kill = new HashSet<>(dest.index);
        }
        
        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VMemWrite memWrite) {
        var source = memWrite.source;
        var gen = new HashSet<>(0);

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }

        VMemRef dest = memWrite.dest;

        if(dest instanceof VMemRef.Global){
            var base = ((VMemRef.Global)dest).base;
            if(base instanceof VAddr.Var<?>){
                var varAddr = (VAddr.Var<VDataSegment>)base;
                gen.add(((VVarRef.Local)(varAddr.var)).index);
            }
        }

        var kill = new HashSet<>(0);

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VMemRead memRead) {
        var gen = new HashSet<Integer>();
        if(memRead.source instanceof VMemRef.Global){
            var base = ((VMemRef.Global)(memRead.source)).base;
            if(base instanceof VAddr.Var<?>){
                var varAddr = (VAddr.Var<VDataSegment>)base;
                gen.add(((VVarRef.Local)(varAddr.var)).index);
            }
        }

        var kill = new HashSet<Integer>();

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VBranch branch) {
        var source = branch.value;
        var gen = new HashSet<>(0);

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }

        var kill = new HashSet<>(0);

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VGoto goto1) {
        var gen = new HashSet<Integer>();
        var target = goto1.target;

        if(target instanceof VAddr.Var<?>){
            var theVar = (VAddr.Var<VCodeLabel>)target;
            VVarRef thisVar = theVar.var;

            if(thisVar instanceof VVarRef.Local){
                gen.add(((VVarRef.Local)thisVar).index);
            }
        }

        var kill = new HashSet<>();

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VReturn ret) {
        var source = ret.value;
        var gen = new HashSet<>(0);

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }

        var kill = new HashSet<>(0);

        return new Pair(gen, kill);
    }

    public Pair visitDispatch(VInstr instruct) {
        try {
            return instruct.accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair();
    }
    
}
