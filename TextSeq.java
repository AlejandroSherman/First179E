public class TextSeq {
    StringBuilder text;
    Integer indentCounter;

    public TextSeq(Integer indentCount) {
        text.append("");
        indentCounter = indentCount;
    }

    public void incIndent() {
        indentCounter++;
    }

    public void decIndent() {
        indentCounter--;
    }

    public void println(String string) {
        for(int i = 0; i < indentCounter; i++){
            text.append("\t");
        }
        text.append(string + "\n");
    }

    public void print(String string) {
        for(int i = 0; i < indentCounter; i++){
            text.append("\t");
        }
        text.append(string);
    }

    public String get() {
        return text.toString();
    }

}
