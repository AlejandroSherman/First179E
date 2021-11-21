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
            lineNo2Var2Interval.put(i, new HashMap<>());
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
            var activeSet = activeSets.get(lineNo);

            for(var varNo : activeSet){
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



    // Possibly make global: interval, active, activeSize
    Interval interval;
    ArrayList<Interval> active;
    int activeSize = 0;
    private Object scan(List<Interval> intervals, Set<Interval> fixedIntervals, List<Integer> initRegs, Integer regCount) {
        // TODO scan()
        var stackOffset = -1;
        var freeRegs = new Stack<Integer>();
        
        for (var reg : initRegs){ // need .reverse
            freeRegs.push(reg);
        }
        
        active = new ArrayList<>(regCount);
        
        
        //nested method declaration???
        // def insert()
        // def remove()
        
        for(Interval currInterval : intervals){
            interval = currInterval; // for global var assignment
            System.out.println(activeSize);
            System.out.println(freeRegs);

            while(activeSize != 0){
                var thisInterval = active.get(0);
                if(thisInterval.finishLine >= interval.startLine){
                    break;
                }
                else{
                    /* active(0).location match {
                        case RegLoc(i) =>
                            freeRegs.push(i)
                        case _ =>
                    } */
                    remove(0, active, activeSize);
                }
            }
        }

        if(fixedIntervals.contains(interval)){ // insert interval to active
            insert(interval,active, activeSize);
        }
        else if(activeSize != regCount){ // get a free register and assign it to this interval
            var regNo = freeRegs.pop();
            //interval.location = new RegLoc(regNo);
            insert(interval, active, activeSize);
        }
        else{ // spill a (non-fixed) interval
            System.out.println(activeSize);
            System.out.println(freeRegs);
            var i = activeSize - 1;
            var done = false;
            while(!done){
                var last = active.get(i);
                if(fixedIntervals.contains(last)){
                    i--;
                }
                else if(last.finishLine < interval.finishLine){
                    stackOffset++;
                    //interval.location = new StackLoc(stackOffset);
                    done = true;
                }
                else{
                    remove(i, active, activeSize);
                    interval.location = last.location;
                    insert(interval, active, activeSize);
                    stackOffset++;
                    //last.location = new StackLoc(stackOffset);
                    done = true;
                }
            }
        }
        stackOffset++;

        return null;
    }

    private void insert(Interval interval, ArrayList<Interval> active, Integer activeSize){
        int i = 0;

        while(i != activeSize){
            var thisInterval = active.get(i);
            if(interval.finishLine < thisInterval.finishLine){ 
                break; 
            }
            else{ 
                i++; 
            }
        }
        
        for(int j = activeSize; j != i; j--){
            active.add(j, active.get(j-1));
            active.add(i, interval);
            activeSize++;
        }
    }

    private void remove(Integer i, ArrayList<Interval> active, Integer activeSize){
        for(var j = i; j < activeSize-1; j++){
            active.add(j, active.get(j+1));
        }
        if(i < activeSize){
            activeSize--;
        }
    }

}
