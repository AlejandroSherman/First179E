

import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;

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

public class TestVisitor extends VInstr.VisitorR<Pair, Exception>{

    @Override
    public Pair visit(VAssign assign) {
        var source = assign.source;
        var gen = new HashSet<Integer>();

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }
        
        var dest = (VVarRef.Local)assign.dest;
        var kill = new HashSet<>(dest.index);

        //System.out.println(dest + "=" + source); //num_aux = 1 in factorial function

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
            // gen += varAddr.'var'.asInstanceOf[VVarRef.Local].index
        }

        var dest = call.dest;
        HashSet<?> kill;

        if (dest != null){
            kill = new HashSet<>(dest.index);
        }
        else{
            kill = new HashSet<>();
        }

        //System.out.println(dest + " = call " + addr + "("+args.length+" args)"); //t.3 = call t.1(this t.2) in factorial function

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VBuiltIn builtIn) {
        var args = builtIn.args;
        var gen = new HashSet<>();

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
        var gen = new HashSet<>();

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }

        VMemRef dest = memWrite.dest;

        //System.out.println(" " + source); //[t.0] = :vmt_Fac

        if(dest instanceof VMemRef.Global){
            // TODO Finish visit(VMemWrite memWrite)

            // val base = dest.asInstanceOf[VMemRef.Global].base
            // if(base.isInstanceOf[VAddr.Var[VDataSegment]]){
                // val varAddr = base.asInstanceOf[VAddr.Var[VDataSegment]]
                // gen += varAddr.'var'.asInstanceOf[VVarRef.Local].index
            // }
        }

        var kill = new HashSet<>();

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VMemRead r) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pair visit(VBranch branch) {
        var source = branch.value;
        var gen = new HashSet<>();

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }

        var kill = new HashSet<>();

        return new Pair(gen, kill);
    }

    @Override
    public Pair visit(VGoto goto1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Pair visit(VReturn ret) {
        var source = ret.value;
        var gen = new HashSet<>();

        if(source instanceof VVarRef.Local){
            var varSource = (VVarRef.Local)source;
            gen.add(varSource.index);
        }

        var kill = new HashSet<>();

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
