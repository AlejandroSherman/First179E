//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

//compile code with: 
//      javac V2VM.java -classpath vapor-parser.jar
//test run with:
//      java V2VM < tests/Basic.vapor > tests/Basic.vaporm

import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.*;
import cs132.vapor.ast.*;

public class V2VM {

    public static void main(String [] args) throws Exception {
        InputStream fileStream = System.in;
        PrintStream error = new PrintStream("Error in parseVapor");
        VaporProgram program = parseVapor(fileStream, error);

        System.out.println("didn't crash");
    }

    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws Exception {
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
            program = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers, allowStack);
        }
        catch (Exception ex) {
            err.println(ex.getMessage());
            return null;
        }
        return program;
    }
}