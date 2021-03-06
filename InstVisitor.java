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
        localAddr = ((outCount + functionCounter) * 4) + "($sp)";
        outAddr = (functionCounter * 4) + "($sp)";
        inAddr = (functionCounter * 4) + "($fp)";
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

    @Override
    public void visit(VAssign assign) throws Exception {
        var dest = assign.dest;
        var source = assign.source;
        if(source instanceof VLitInt){
            text.print("li ");
            text.print("genVarRef(dest)"); // text.print(genVarRef(dest));
            text.print(" ");
            text.println("genOperand(source)"); //text.println(genOperand(source));
        }
        else if(source instanceof VLabelRef){
            text.print("la ");
            text.print("genVarRef(dest)"); //text.print(genVarRef(dest));
            text.print(" ");
            text.println("genOperand(source)"); //text.println(genOperand(source));
        }
        else{
            text.print("move ");
            text.print("genVarRef(dest)"); //text.print(genVarRef(dest));
            text.print(" ");
            text.println("genOperand(source)"); //text.println(genOperand(source));
        }
    }

    @Override
    public void visit(VCall call) throws Exception {
        if(call.addr instanceof VAddr.Var){
            text.println("jalr " + "genAddr(call.addr)"); // + genAddr(call.addr));
        }
        else{
            text.println("jal " + "genAddr(call.addr)"); // + genAddr(call.addr));
        }
    }

    @Override
    public void visit(VBuiltIn builtIn) throws Exception {
        var op = builtIn.op;
        var dest = builtIn.dest;
        var args = builtIn.args;

        if(op == Op.PrintIntS){
            var arg = args[0];
            if(arg instanceof VVarRef){
                var varRef = (VVarRef)arg;
                text.println("move $a0 " + "genVarRef(varRef)"); // + genVarRef(varRef));
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
                text.println("move $a0 " + "genOperand(arg)"); //TextSeq.println("move $a0 " + genOperand(arg));
            }
            text.println("jal _heapAlloc");
            text.println("move " + "genVarRef(dest)" + " $v0"); //TextSeq.println("move " + genVarRef(dest) + " $v0");
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
                var litInt1 = ((VLitInt)arg0).value;
                int value;
                if(op == Op.Sub){
                    value = litInt0 - litInt1;
                }
                else{
                    value = litInt0 * litInt1;
                }
                text.println("li " + "genVarRef(dest)" + " " + value); //TextSeq.println("li " + genVarRef(dest) + " " + value);
                return;
            }
        }
        //TODO finish builtins
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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(VMemRead r) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(VBranch b) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void visit(VGoto g) throws Exception {
        text.print("j ");
        var addr = g.target;
        text.println("genAddr(addr)"); //TextSeq.println(genAddr(addr));        
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
