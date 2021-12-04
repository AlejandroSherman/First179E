import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.ast.VFunction.Stack;

public class InstVisitor extends VInstr.Visitor<Exception> {
    Integer functionCounter = 0;
    String name;
    Stack stack;
    Integer localCount;
    Integer outCount;
    Integer frameSize;
    String localAddr;
    String outAddr;
    String inAddr;
    VInstr[] instructs;
    VCodeLabel[] labels;
    TextSeq text = new TextSeq();

    public void translate(VFunction func){
        name = func.ident;
        stack = func.stack;
        localCount = stack.local;
        outCount = stack.out;
        frameSize = (localCount + outCount + 2) * 4;
        instructs = func.body;
        labels = func.labels;

        text.println(name + ":");
        text.incIndent();

        text.println("sw $fp -8($sp)");
        text.println("move $fp $sp");
        text.println("subu $sp $sp " + frameSize);
        text.println("sw $ra -4($fp)");

        var labelIndex = 0;
        for(int i = 0; i < instructs.length; i++){
            text.decIndent();
            while(labelIndex < labels.length && labels[labelIndex].instrIndex == i){
                text.println(labels[labelIndex].ident + ":");
                labelIndex++;
            }
            text.incIndent();
            var instruct = instructs[i];
            visitDispatch(instruct);
        }
        text.decIndent();
    }

    public String localAddr(Integer i){
        return ((outCount + i) * 4) + "($sp)";
    }

    public String outAddr(Integer i){
        return (i * 4) + "($sp)";
    }

    public String inAddr(Integer i){
        return (i * 4) + "($fp)";
    }

    public String genOperand(VOperand operand){
        if(operand instanceof VVarRef){
            var varRef = (VVarRef)operand;
            return genVarRef(varRef);
        }
        else if(operand instanceof VLitStr){
            var litStr = (VLitStr)operand;
            return "\"" + litStr.value + "\"";
        }
        else if(operand instanceof VLitInt){
            var litInt = (VLitInt)operand;
            return String.valueOf(litInt.value);
        }
        else if(operand instanceof VLabelRef){
            var subType = ((VLabelRef<?>)operand).getTarget();
            if(subType instanceof VCodeLabel){
                var labelRef = (VLabelRef<VCodeLabel>)operand;
                return labelRef.ident;
            }
            else if(subType instanceof VDataSegment){
                var labelRef = (VLabelRef<VDataSegment>)operand;
                return labelRef.ident;
            }
            else if(subType instanceof VFunction){
                var labelRef = (VLabelRef<VFunction>)operand;
                return labelRef.ident;
            }
        }
        else{
            return "";
        }
        return "";
    }

    public String genMemRef(VMemRef memRef){
        if(memRef instanceof VMemRef.Global){
            var memRefGlobal = (VMemRef.Global)memRef;
            var baseAddr = memRefGlobal.base;
            var offset = memRefGlobal.byteOffset;
            return offset + "(" + genAddr(baseAddr) + ")";
        }
        else{
            var memRefStack = (VMemRef.Stack)memRef;
            var stackType = memRefStack.region;
            var index = memRefStack.index;

            if(stackType == stackType.In){
                return inAddr(index);
            }
            else if(stackType == stackType.Out){
                return outAddr(index);
            }
            else{
                return localAddr(index);
            }
            
        }
    }

    public String genAddr(VAddr addr){
        if(addr instanceof VAddr.Label){
            var addrLocal = (VAddr.Label)addr;
            return addrLocal.label.ident;
        }
        else{
            var addrVar = (VAddr.Var)addr;
            return genVarRef(addrVar.var);
        }
    }

    public String genVarRef(VVarRef varRef){
        if(varRef instanceof VVarRef.Register){
            var varRefReg = (VVarRef.Register)varRef;
            var reg = varRefReg.ident;
            return "$" + reg;
        }
        else{
            return null;
        }
    }

    @Override
    public void visit(VAssign assign) throws Exception {
        var dest = assign.dest;
        var source = assign.source;
        if(source instanceof VLitInt){
            text.print("li ");
            text.print(genVarRef(dest));
            text.print(" ");
            text.println(genOperand(source));
        }
        else if(source instanceof VLabelRef){
            text.print("la ");
            text.print(genVarRef(dest));
            text.print(" ");
            text.println(genOperand(source));
        }
        else{
            text.print("move ");
            text.print(genVarRef(dest));
            text.print(" ");
            text.println(genOperand(source));
        }
    }

    @Override
    public void visit(VCall call) throws Exception {
        if(call.addr instanceof VAddr.Var){
            text.println("jalr " + genAddr(call.addr));
        }
        else{
            text.println("jal " + genAddr(call.addr));
        }
    }

    String theI = "";
    @Override
    public void visit(VBuiltIn builtIn) throws Exception {
        var op = builtIn.op;
        var dest = builtIn.dest;
        var args = builtIn.args;

        if(op == Op.PrintIntS){
            var arg = args[0];
            if(arg instanceof VVarRef){
                var varRef = (VVarRef)arg;
                text.println("move $a0 " + genVarRef(varRef));
            }
            else if(arg instanceof VLitInt){
                var litInt = (VLitInt)arg;
                text.println("li $a0 " + litInt.value);
            }
            text.println("jal _print");
            return;
        }
        else if(op == Op.HeapAllocZ){
            var arg = args[0];
            if(arg instanceof VLitInt){
                var litInt = (VLitInt)arg;
                text.println("li $a0 " + litInt.value);
            }
            else{
                text.println("move $a0 " + genOperand(arg));
            }
            text.println("jal _heapAlloc");
            text.println("move " + genVarRef(dest) + " $v0");
            return;
        }
        else if(op == Op.Error){
            text.println("la $a0 _str0");
            text.println("j _error");
            return;
        }
        else if(op == Op.Sub || op == Op.MulS){
            var arg0 = args[0];
            var arg1 = args[1];
            if(arg0 instanceof VLitInt && arg1 instanceof VLitInt){
                var litInt0 = ((VLitInt)arg0).value;
                var litInt1 = ((VLitInt)arg1).value;
                int value;
                if(op == Op.Sub){
                    value = litInt0 - litInt1;
                }
                else{
                    value = litInt0 * litInt1;
                }
                text.println("li " + genVarRef(dest) + " " + value);
                return;
            }
            String arg0Str = Arg0Str(arg0);
            String arg1Str = Arg1Str(arg1);
            String mipsOp = MipsOp(op);
            text.println(mipsOp + " " + genVarRef(dest) + " " + arg0Str + " " + arg1Str);
            return;
        }
        else if((op == Op.Add) || (op == Op.Lt) || (op == Op.LtS)){
            var arg0 = args[0];
            var arg1 = args[1];
            if((arg0 instanceof VLitInt) && (arg1 instanceof VLitInt)){
                var litInt0 = ((VLitInt)arg0).value;
                var litInt1 = ((VLitInt)arg1).value;
                Integer value = 0;
                if(op == Op.Add){
                    value = litInt0 + litInt1;
                }
                else{
                    if(litInt0 < litInt1){
                        value = 1;
                    }
                    else{
                        value = 0;
                    }
                }
                text.println("li " + genVarRef(dest) + " " + value);
                return;
            }
            
            String arg0Str = Arg0Str(arg0);
            String arg1Str = Arg1Str2(arg1);
            String mipsOp = MipsOp(op);
            text.println(mipsOp + " " + genVarRef(dest) + " " + arg0Str + " " + arg1Str);
            return;
        }
    }

    public String Arg0Str(VOperand arg0){
        if(arg0 instanceof VLitInt){
            var litInt = (VLitInt)arg0;
            text.println("li $t9 " + litInt.value);
            return "$t9";
        }
        else{
            return genOperand(arg0);
        }
    }

    public String Arg1Str(VOperand arg1){
        if(arg1 instanceof VLitInt){
            var litInt = (VLitInt)arg1;
            //text.println("li $t9 " + litInt.value);
            return genOperand(arg1);
        }
        else{
            return genOperand(arg1);
        }
    }

    public String Arg1Str2(VOperand arg1){
        if(arg1 instanceof VLitInt){
            var litInt = (VLitInt)arg1;
            theI = "i";
            return String.valueOf(litInt.value);
        }
        else{
            return genOperand(arg1);
        }
    }
    
    public String MipsOp(Op op){
        if(op == Op.Sub){
            return "subu";
        }
        else if(op == Op.MulS){
            return "mul";
        }
        else if(op == Op.Add){
            return "add" + theI + "u";
        }
        else if((op == Op.Lt) || (op == Op.LtS)){
            return "slt" + theI;
        }
        else{
            return "";
        }
    }

    public void genLib(){
        text.println("_print:");
        text.incIndent();
        text.println("li $v0 1   # syscall: print integer");
        text.println("syscall");
        text.println("la $a0 _newline");
        text.println("li $v0 4   # syscall: print string");
        text.println("syscall");
        text.println("jr $ra");
        text.decIndent();
        text.println("\n_error:");
        text.incIndent();
        text.println("li $v0 4   # syscall: print string");
        text.println("syscall");
        text.println("li $v0 10  # syscall: exit");
        text.println("syscall");
        text.decIndent();
        text.println("\n_heapAlloc:");
        text.incIndent();
        text.println("li $v0 9   # syscall: sbrk");
        text.println("syscall");
        text.println("jr $ra");
        text.decIndent();
        text.println("\n.data");
        text.println(".align 0");
        text.println("_newline: .asciiz \"\\n\"");
        text.println("_str0: .asciiz \"null pointer\\n\"");
    }

    @Override
    public void visit(VMemWrite w) throws Exception {
        var dest = w.dest;
        var source = w.source;
        if (source instanceof VLabelRef){
            text.print("la $t9 ");
            text.println(genOperand(source));
            text.print("sw $t9 ");
            text.println(genMemRef(dest));
        }
        else if(source instanceof VLitInt){
            text.print("li $t9 ");
            text.println(genOperand(source));
            text.print("sw $t9 ");
            text.println(genMemRef(dest));
        }
        else{
            text.print("sw ");
            text.print(genOperand(source));
            text.print(" ");
            text.println(genMemRef(dest));
        }
    }

    @Override
    public void visit(VMemRead memRead) throws Exception {
        var dest = memRead.dest;
        var source = memRead.source;
        text.print("lw ");
        text.print(genVarRef(dest));
        text.print(" ");
        text.println(genMemRef(source));
    }

    @Override
    public void visit(VBranch branch) throws Exception {
        var positive = branch.positive;
        var value = branch.value;
        var label = branch.target.ident;
        if(positive){
            text.print("bnez");
        }
        else{
            text.print("beqz");
        }
        text.print(" ");
        text.print(genOperand(value));
        text.print(" ");
        text.println(label);
    }

    @Override
    public void visit(VGoto g) throws Exception {
        text.print("j ");
        var addr = g.target;
        text.println(genAddr(addr));    
    }

    @Override
    public void visit(VReturn r) throws Exception {
        text.println("lw $ra -4($fp)");
        text.println("lw $fp -8($fp)");
        text.println("addu $sp $sp " + frameSize);
        text.println("jr $ra");
    }
    
    public void visitDispatch(VInstr instruct){
        try {
            instruct.accept(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
