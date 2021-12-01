//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

//compile code with: 
//      javac VM2M.java -cp vapor-parser.jar:
//test run with:
//      java -cp vapor-parser.jar: VM2M < tests/Basic.vaporm > tests/Basic.s
//test run to standard print:
//      java -cp vapor-parser.jar: VM2M < tests/Basic.vaporm

import java.io.InputStream;
import java.io.InputStreamReader;
import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;
import cs132.vapor.parser.VaporParser;

public class VM2M {

    public static void main(String [] args) throws Exception {
        InputStream in = System.in;
        Op[] ops = {
            Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
            Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };
        
        boolean allowLocals = false;
        String[] registers = {
            "v0", "v1",
            "a0", "a1", "a2", "a3",
            "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
            "t8",
        };
        boolean allowStack = true;
        
        VaporProgram tree;
        try {
            tree = VaporParser.run(new InputStreamReader(in), 1, 1,
                                java.util.Arrays.asList(ops),
                                allowLocals, registers, allowStack);

            InstVisitor instVisitor = new InstVisitor();
            //TextSeq text = new TextSeq();

            //Print VTable
            System.out.println(".data\n");
			for (VDataSegment vdataseg : tree.dataSegments) {
                System.out.println(vdataseg.ident + ":");
				
                for (VOperand.Static vos : vdataseg.values) {
					VLabelRef vlabelref = (VLabelRef) vos;
					System.out.println("  " + vlabelref.ident);
				} 

            }
            System.out.println();

            System.out.println(".text\n");
            System.out.println("  jal Main");
            System.out.println("  li $v0 10");
            System.out.println("  syscall\n");
            
            //Iterate through functions, by instruction
            for (VFunction vfunc : tree.functions) {
                instVisitor.translate(vfunc);
                instVisitor.text.println("");
            }
            instVisitor.genLib();
            System.out.println(instVisitor.text.get());
        }
        catch (ProblemException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
      }
    }

}

