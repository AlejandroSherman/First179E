public class Interval {

    int finishLine;
    int startLine;
    RegLoc location;

    public Interval(int start, int finish, RegLoc regLoc) {
        startLine = start;
        finishLine = finish;
        location = regLoc;
    }

    @Override
    public String toString() {
        return "[" + startLine + "-" + finishLine + "]";
    }

}