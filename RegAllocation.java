import cs132.vapor.ast.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.lang.*;


public class RegAllocation {
    public Triple allocate(
        VInstr[] insts,
        VVarRef.Local[] params,
        Integer[] regs,
        Integer argRegCount
    ) throws Exception{
        Liveness livenessTest = new Liveness();
        Pair livenessResults = livenessTest.checkLiveness(insts);

        ArrayList<HashSet<Integer>> activeSets = livenessResults.leftArray;
        ArrayList<HashSet<Integer>>  outSets = livenessResults.rightArray;

        Pair splitResults = splitParam(params, argRegCount);

        Integer[] regParams = splitResults.leftIntAry;
        Integer[] stackParams = splitResults.rightIntAry;

        LinearScan linearTest = new LinearScan();
        Pair linearResults = linearTest.linearScan(activeSets, regParams, stackParams, regs);

        Map<Integer, Map<Integer, Interval>> line2Var2Interval = linearResults.lefthere; //need to finish linearScan first
        Integer stackUse = linearResults.righthere; //need to finish linearScan first

        Map<Integer, Map<Integer, Interval>> allocation = line2Var2Interval.map( 
            (Pair t) -> { return new Tuple2(tp._1, tp._2.map( //  <Integer, Map<Integer, Interval>> 
                (Pair tp) -> { return new Tuple2(tp._1, tp._2.location); } // <Integer, Interval>
            ); } )
        );

        /*
        var outRegs = 
        for(outSets(lineNo); varNo; ++){
            if(){

            }
            else{
                throw new Exception();
            }
        }
        */

        Triple results = new Triple(allocation, outRegs, stackUse);
        return results;
        
    }

    public Pair splitParam(
        VVarRef.Local[] params,
        Integer argRegCount
    ){
        Integer regParamCount = Math.min(argRegCount, params.length);
        Integer[] regPrams = new Integer[regParamCount];

        for(Integer i=0; i < regParamCount; i++){
            regPrams[i] = params[i].index;
        }

        Integer stackParamCount = params.length - regParamCount;
        Integer[] stackParams = new Integer[stackParamCount];

        for(Integer i=0; i < stackParamCount; i++){
            stackParams[i] = params[i+regParamCount].index;
        }

        return new Pair(regPrams, stackParams);
    }
}
