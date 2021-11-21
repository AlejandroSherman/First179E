public class Interval {

    public int finishLine;
    public int startLine;
    public RegLoc location;

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