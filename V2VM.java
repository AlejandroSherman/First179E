//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

//compile code with: 
//      javac V2VM.java -cp vapor-parser.jar:
//test run with:
//      java -cp vapor-parser.jar: V2VM < tests/Basic.vapor > tests/Basic.vaporm
//test run to standard print:
//      java -cp vapor-parser.jar: V2VM < tests/Basic.vapor

import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.*;
import cs132.vapor.parser.VaporParser;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.*;

public class V2VM {

    public static void main(String [] args) throws Exception {
        //Initialize variables for VaporProgram var
        InputStream in = System.in;
        Op[] ops = {
            Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
            Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };
        boolean allowLocals = true;
        String[] registers = {
            "v0", "v1",
            "a0", "a1", "a2", "a3",
            "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "t8",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
        };
        //Integer registerCount = registers.length;
        boolean allowStack = true;
        VaporProgram program;

        try {
            //Create program variable
            program = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers, allowStack);

			//Print VTable
			for (VDataSegment vdataseg : program.dataSegments) {
				
                System.out.println("const "+vdataseg.ident);
				
                for (VOperand.Static vos : vdataseg.values) {
					VLabelRef vlabelref = (VLabelRef) vos;
					System.out.println(":"+vlabelref.ident);
				}

			}

            //Iterate through functions, by instruction
            for (VFunction vfunc : program.functions) {
                int ins = 0;
                //vfunc.params.length is the number of params in a function call
                int outs = 0;
                int locals = 0;
                //vfunc.vars.length is the number of vars stored locally in a function
                // ins, outs, and locals are only used if we need to spill
                int labelNum = 0;
                int instrNum = 0;
                String funcName = "";

                System.out.println("func " + vfunc.ident + " [in " + ins + ", out " + outs + ", local " + locals + "]");

                
                Liveness liveCheck = new Liveness();
                liveCheck.checkLiveness(vfunc.body);

                for (VInstr vinstr : vfunc.body) {
                    if(vfunc.labels != null){
                        while (labelNum < vfunc.labels.length && instrNum == vfunc.labels[labelNum].instrIndex) {
                            funcName = vfunc.labels[labelNum].ident;
                            System.out.println(funcName + ":");
                            labelNum++;
                        }
                    }
                    //vinstr.accept(new TestVisitor());
                    instrNum++;
                }
                System.out.println("");
            }




        }
        catch (Exception ex) {
        }
        
    }
}