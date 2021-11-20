import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Map;

public class LinearScan {
    public Pair linearScan(List<Set<Integer>> activeSets, List<Integer> regPrams, List<Integer> stackParams, Map<Integer, Map<Integer,Interval>> regs ){
        Pair computedIntervals = computeIntervals(activeSets, regPrams, stackParams, regs);
        var stackUse = scan(computedIntervals.intervals, computedIntervals.fixedIntervals, computedIntervals.remainingRegs, (computedIntervals.regs).size());

        return new Pair(computedIntervals.line2Var2Interval, stackUse);
    }

    private Pair computeIntervals(List<Set<Integer>> activeSets, List<Integer> regParams, List<Integer> stackParams, Map<Integer, Map<Integer, Interval>> regs) {
        // TODO computeIntervals()

        var lineNo2Var2Interval = new HashMap<Integer, Map<Integer,Interval>>();

        for(int i = -1; i < activeSets.size(); i++){
            lineNo2Var2Interval.put(i, new HashMap<Integer, Interval>());
        }
        
        var intervals = new ArrayList<Interval>();
        var fixedIntervals = new HashSet<Interval>();
        var remainingRegs = regs;

        for(int i = 0; i < regParams.size(); i++){
            var varNo = regParams.get(i);
            var reg = remainingRegs.get(0); // .head 
            // remainingRegs = remainingRegs.tail;

            var interval = new Interval(-1, 0, new RegLoc(reg));
            intervals.add(interval);
            fixedIntervals.add(interval);
            lineNo2Var2Interval.get(-1).put(varNo, interval);

        }

        for(int i = 0; i < stackParams.size(); i++){
            var reg = remainingRegs.get(0); // .head
            //remainingRegs = remainingRegs.tail;
            var interval = new Interval(-1, 0, new RegLoc(reg));
            lineNo2Var2Interval.get(-1).put(stackParams.get(i), interval);
        }

        var var2Interval = new HashMap<Integer, Interval>();
        var2Interval.putAll(lineNo2Var2Interval.get(-1));

        for(int lineNo = 0; lineNo < activeSets.size(); lineNo++){
            var activeSet = activeSets(lineNo);

            for(var varNo : (ArrayList)activeSet){
                if(!var2Interval.containsKey(varNo)){
                    var interval = new Interval(lineNo, lineNo, null);
                    intervals.add(interval);
                }
                else{
                    var interval = var2Interval.get(varNo);
                    interval.finishLine = lineNo;
                }
            }
        }

        for(var varNo : var2Interval.keySet()){
            var interval = var2Interval.get(varNo);
            
            for(int lineNo = interval.startLine; lineNo < interval.finishLine; lineNo++){
                // lineNo2Var2Interval(lineNo)(varNo) = interval
            }
        }

        System.out.println("Intervals:");
        for(var interval : intervals){
            System.out.println(interval);
        }
        System.out.println("lineNo2Var2Interval:");
        for(int lineNo = 0; lineNo < activeSets.size(); lineNo++){
            System.out.println("LineNo: " + lineNo + " " + lineNo2Var2Interval.get(lineNo));
        }

        return new Pair(intervals, fixedIntervals, remainingRegs, lineNo2Var2Interval);
    }


    private Object scan(List<Interval> intervals, Set<Interval> fixedIntervals, List<Integer> initRegs, Integer regCount) {
        // TODO scan()
        var stackOffset = -1;
        var freeRegs = new Stack<Integer>();
        
        for (var reg : initRegs){ // need .reverse
            freeRegs.push(reg);
        }
        
        var active = new ArrayList<Interval>(regCount);
        int activeSize = 0;
        
        //nested method declaration???

        return null;
    }

    private Object activeSets(int lineNo) {
        return null;
    }
}
