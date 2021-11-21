import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs132.vapor.ast.VCodeLabel;
import cs132.vapor.ast.VInstr;

public class BodyCodeGen {
    List<VInstr> instructs; 
    Integer paramCount; 
    List<VCodeLabel> labels; 
    Map<Integer, Map<Integer, Location>> allocation; 
    Map<Integer, Set<Integer>> outRegs; // outRegs: Int=>Set[Int]   // give int, get back set?
    Map<Integer, String> num2Reg;
    Map<Integer, String> num2ArgReg;
    List<Integer> regList;
    Integer varStackUse; 
    Integer indentCount;
    Integer outStackUse = 0;
    Integer stackUse2SaveRegs = 0;
    Integer stackUse2SaveSRegs = 0;
    Integer argRegCount = num2ArgReg.size();
    //FIXME var reg2Num = num2Reg.map(_.swap)
    Set<Integer> usedSRegs = (Set)num2Reg;     // var usedSRegs: Set[Int] = _
    TextSeq text = new TextSeq(indentCount);     // this is a custom import "lesani.compiler.texttree.seq.TextSeq"
    
    public BodyCodeGen(List<VInstr> instructs, 
                        Integer paramCount, 
                        List<VCodeLabel> labels, 
                        Map<Integer, Map<Integer, Location>> allocation, 
                        Map<Integer, Set<Integer>> outRegs, // outRegs: Int=>Set[Int]   // give int, get back set?
                        Map<Integer, String> num2Reg,
                        Map<Integer, String> num2ArgReg,
                        List<Integer> regList,
                        Integer varStacuUse, 
                        Integer indentCount){
        outStackUse = 0;
        stackUse2SaveRegs = 0;
        stackUse2SaveSRegs = 0;
        argRegCount = num2ArgReg.size();
        //FIXME var reg2Num = num2Reg.map(num2Reg.swap)
        Set<Integer> usedSRegs = (Set)num2Reg;      // var usedSRegs: Set[Int] = _
        TextSeq text = new TextSeq(indentCount);     // this is a custom import "lesani.compiler.texttree.seq.TextSeq"
    }

    public Pair genBodyCode(){
        // get used function registers from allocation map
        var usedRegs = new HashSet<Integer>(0);
        for(int i = 0; i < instructs.size(); i++){
            /*FIXME allocation.get(i).values().map(_ match {
                case RegLoc(r) => usedRegs += r
                case _ =>
            }) */
        }

        // $s0 to $s8
        var sRegs = new HashSet<Integer>(0);
        for(int i = 0; i < 8; i++){
            //FIXME sRegs.add(reg2Num.get("$s" + i));     // reg2Num commented out above
        }

        usedSRegs.addAll(usedRegs);
        usedSRegs.addAll(sRegs);

        text.incIndent(); 

        // load arg regs to body regs
        for(int i = 0; i < argRegCount; i++){ 
            text.println(num2Reg.get(regList.get(i)) + " = " + num2ArgReg.get(i));
        }

        // load in stack to body regs
        for(int i = 0; i < argRegCount; i++){ 
            text.println(num2Reg.get(regList.get(i + argRegCount)) + " = in[" + i + "]");
        }

        // push used save regs $s0..$s7 when entering a function
        for(var sReg : usedSRegs){
            Integer offset = varStackUse + stackUse2SaveRegs++;
            text.println("local[" + offset + "] = " + num2Reg.get(sReg));
        }

        // for each instruction
        Integer labelIndex = 0;
        for(int i = 0; i < instructs.size(); i++){
            text.decIndent();
            // print label
            while(labelIndex < labels.size() && labels.get(labelIndex).instrIndex == i){
                text.println(labels.get(labelIndex++).ident + ":");
            }
            text.incIndent();
            var instruct = instructs.get(i);
            var activeRegs = outRegs.get(i);

            InstrVisitor.visitDispatch(instruct, allocation.get(i), activeRegs);
        }

        // pop save regs $s0..$s7 just before the ret
        text.decIndent();
        text.println("");

        var stackUse = varStackUse;

        stackUse += stackUse2SaveRegs;
        stackUse += usedSRegs.size();
        
        return new Pair(text.get(), stackUse, outStackUse);
    }
    
}
