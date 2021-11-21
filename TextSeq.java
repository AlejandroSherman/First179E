import java.util.ArrayList;

public class TextSeq {
    String text;
    Integer indentCounter;

    public TextSeq(Integer indentCount) {
        text = "";
        indentCounter = indentCount;
    }

    public void incIndent() {
        indentCounter++;
    }

    public void decIndent() {
        indentCounter--;
    }

    public void println(String string) {
        text += string + "\n";
    }

    public void print(String string) {
        text += string;
    }

    public String get() {
        return text;
    }

}
