

import java.util.HashSet;
import java.util.Set;

public class Pair {
    HashSet<?>[] leftArray;
    HashSet<?>[] rightArray;
    Set<?> left;
    Set<?> right;

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
}