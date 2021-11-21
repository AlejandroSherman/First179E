

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class Pair {
    ArrayList<HashSet<Integer>> leftArray;
    ArrayList<HashSet<Integer>> rightArray;
    HashSet<Integer> left;
    HashSet<Integer> right;
    Set<Integer> leftSet;
    Set<Integer> rightSet;

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
}