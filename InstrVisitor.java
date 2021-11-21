import java.util.Map;
import java.util.Set;

import javax.swing.text.LabelView;

import cs132.vapor.ast.*;

public class InstrVisitor extends VInstr.VisitorP<Pair, Exception> {

    TextSeq text;

    @Override
    public void visit(Pair p, VAssign a) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        var dest = a.dest;
        var source = a.source;
        text.print(gen(dest, p.left));
        text.print(" = ");
        text.println(gen(source, p.left));
    }

    @Override
    public void visit(Pair p, VCall c) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        
        // dont need to save s regs
        //FIXME var toBeSavedRegs = activeRegs &~ usedSRegs;

        // store used regs
        //FIXME var storeOffset = varStackUse + stackUse2SaveRegs;
        Integer j = -1;
        /*FIXME for(p.right <- toBeSavedRegs){
            j++;
            text.print("local[" + (startOffset+i) + "]");
            text.print(" = ");
            text.println(num2Reg.get(activeReg));
        } */
        
        //FIXME stackUse2SaveRegs = max(stackUse2SaveRegs, i+1);

        var args = c.args;
        //FIXME var regArgCount = min(args.length, num2ArgReg.size);

        //copy args to $a register
        /*FIXME for(int i = 0; i < regArgCount; i++){
            text.print(num2ArgReg.get(i));
            text.print(" = ");
            text.println(gen(args[i], p.left));
        } */

        //copy spilled args to out stack
        /*FIXME for(int i = regArgCount; i < args.length; i++){
            text.print("out[" + (i-regArgCount) + "]");
            text.print(" = ");
            text.println(gen(args[i], p.left));
        } */

        //FIXME var thisOutStackuse = args.length - regArgCount;        
        //FIXME outStackUse = max(thisOutStackuse, outStackUse);

        var addr = c.addr;
        text.println("call " + gen(addr, p.left));

        // restore used regs after a call
        j = -1;
        /*FIXME for(var activeReg : toBeSavedRegs){
            j++;
            text.print(num2Reg.get(activeReg));
            text.print(" = ");
            text.println("local[" + (startOffset + i) + "]");
        } */

        var dest = c.dest;
        if(dest != null){
            text.print(gen(dest, p.left));
            text.print(" = ");
            text.println("$v0");
        }
    }

    @Override
    public void visit(Pair p, VBuiltIn c) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        var dest = c.dest;
        var op = c.op;
        var args = c.args;

        if(dest != null){
            text.print(gen(dest, p.left));
            text.print(" = ");
        }

        text.print(op.name);
        text.print("(");

        for(int i = 0; i < args.length; i++){
            text.print(gen(args[i], p.left));

            if(i != args.length - 1){
                text.print(" ");
            }
        }

        text.println(")");
    }

    @Override
    public void visit(Pair p, VMemWrite w) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        var dest = w.dest;
        var source = w.source;
        //FIXME text.print(gen(dest, p.left));
        text.print(" = ");
        text.println(gen(source, p.left));
    }

 @Override
    public void visit(Pair p, VMemRead r) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        var dest = r.dest;
        var source = r.source;
        text.print(gen(dest, p.left));
        text.print(" = ");
        //FIXME text.println(gen(source, p.left));
    }

    @Override
    public void visit(Pair p, VBranch b) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        var positive = b.positive;

        if(positive){
            text.print("if ");
        }
        else{
            text.print("if0 ");
        }

        var value = b.value;

        text.print(gen(value, p.left));
        text.print(" goto ");
        var label = b.target.ident;
        text.println(":" + label);        
    }

    @Override
    public void visit(Pair p, VGoto g) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        text.print("goto ");
        var addr = g.target;
        //FIXME text.println(gen(addr, p.left)); 
    }

    @Override
    public void visit(Pair p, VReturn r) throws Exception {
        Pair pair = new Pair(p.left, p.right);
        var value = r.value;

        if(value != null){
            text.print("$v0 = ");
            text.println(gen(value, p.left));
        }

        // pop saved regs $s0..$s7
        var i = 0;
        /*FIXME for(sReg <- usedSRegs){
            val offset: Int = varStackUse + i
            i += 1
            text.println(num2Reg(sReg) + " = " + "local[" + offset + "]")
        } */

        text.println("ret");
    }

    public static void visitDispatch(VInstr instruct, Map<Integer, Location> map, Object activeRegs){
        try {
            //FIXME instruct.accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String gen(VOperand operand, Set<?> alloc) {
        if(operand instanceof VVarRef varRef){
            return gen(varRef, alloc);
        }
        else if(operand instanceof VLitStr litStr){
            return "\"" + litStr.value + "\"";
        }
        else if(operand instanceof VLitInt litInt){
            return String.valueOf(litInt.value);
        }
        else if(operand instanceof VLabelRef<?>){       //FIXME need to check for VLabelRef<VCodeLabel>
            var labelRef = (VLabelRef<VCodeLabel>)operand;
            return ":" + labelRef.ident;
        }
        else if(operand instanceof VLabelRef<?>){       //FIXME need to check for VLabelRef<VDataSegment>
            var labelRef = (VLabelRef<VDataSegment>)operand;
            return ":" + labelRef.ident;
        }
        else{
            return null;
        }
    }

    private String gen(VVarRef varRef, Set<?> alloc) {
        if(varRef instanceof VVarRef.Local){
            var varRefLocal = (VVarRef.Local)varRef;
            var index = varRefLocal.index;
            var location = alloc.toArray()[index];
            //FIXME return gen(location);
            return "####### DELETE THIS RETURN AFTER FIXME ##########"; // placeholder for return
        }
        else{
            return null;
        }
    }

    private String gen(VAddr<VFunction> addr, Set<?> alloc) {
        if (addr instanceof VAddr.Label){ // label reference
            var addrLocal = (VAddr.Label)addr;
            return ":" + addrLocal.label.ident;
        }
        else{ //variable
            var addrVar = (VAddr.Var)addr;
            return gen(addrVar.var, alloc);
        }
    }    
    
}
