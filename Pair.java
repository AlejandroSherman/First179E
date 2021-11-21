import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.Map;

public class Pair {
    ArrayList<HashSet<Integer>> leftArray;
    ArrayList<HashSet<Integer>> rightArray;
    HashSet<Integer> left;
    HashSet<Integer> right;
    Set<Integer> leftSet;
    Set<Integer> rightSet;
    Integer[] leftIntAry;
    Integer[] rightIntAry;
    Integer leftInt;
    Map<Integer, Interval> rightMap;
    Interval rightInterval;


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

    public Pair(Integer[] l, Integer[] r){
        leftIntAry = l;
        rightIntAry = r;
    }


    public Pair(Integer l, Map<Integer, Interval> r){
        leftInt = l;
        rightMap = r;
    }

    
    public Pair(Integer l, Interval r){
        leftInt = l;
        rightInterval = r;
    }


    public Pair() {
    }
}