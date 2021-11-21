import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pair {
    ArrayList<HashSet<Integer>> leftArray;
    ArrayList<HashSet<Integer>> rightArray;
    HashSet<Integer> left;
    HashSet<Integer> right;
    Set<Integer> leftSet;
    Set<Integer> rightSet;
    public List<Interval> intervals;
    public Set<Interval> fixedIntervals;
    public List<Integer> remainingRegs;
    public Map<Integer, Interval> regs;
    public HashSet<?>[] line2Var2Interval;

    public Pair(ArrayList<HashSet<Integer>> leftSide, ArrayList<HashSet<Integer>> rightSide){
        leftArray = leftSide;
        rightArray = rightSide;
    }
    
    public Pair(HashSet<Integer> leftSide, HashSet<Integer> rightSide){
        left = leftSide;
        right = rightSide;
    }

    public Pair(Set<Integer> l, Set<Integer> r){
        leftSet = l;
        rightSet = r;
    }


    public Pair() {
    }

    public Pair(String string, Integer stackUse, Integer outStackUse) {
        // TODO Pair(str,int,int)
    }

    public Pair(List<Interval> intervals2, Set<Interval> fixedIntervals2,
            Map<Integer, Map<Integer, Interval>> remainingRegs2,
            Map<Integer, Map<Integer, Interval>> lineNo2Var2Interval) {
        // TODO Pair(list, set, map, map)
    }

    public Pair(HashSet<?>[] line2Var2Interval2, Object stackUse) {
        // TODO Pair(hashSet, object)
    }
}