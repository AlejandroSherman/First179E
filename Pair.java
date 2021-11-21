import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pair {
    HashSet<?>[] leftArray;
    HashSet<?>[] rightArray;
    Set<?> left;
    Set<?> right;
    public List<Interval> intervals;
    public Set<Interval> fixedIntervals;
    public List<Integer> remainingRegs;
    public Map<Integer, Interval> regs;
    public HashSet<?>[] line2Var2Interval;

    public Pair(HashSet<?>[] leftSide, HashSet<?>[] rightSide){
        leftArray = leftSide;
        rightArray = rightSide;
    }
    
    public Pair(Set<?> leftSide, Set<?> rightSide){
        left = leftSide;
        right = rightSide;
    }

    public Pair() {
    }

    public Pair(HashSet<?>[] line2Var2Interval2, Object stackUse) {
    }

    public Pair(List<Interval> intervals2, Set<Interval> fixedIntervals2,
            Map<Integer, Map<Integer, Interval>> remainingRegs2,
            Map<Integer, Map<Integer, Interval>> lineNo2Var2Interval) {
    }

    public Pair(String string, Integer stackUse, Integer outStackUse) {
    }

}
