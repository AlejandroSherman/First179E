public class TextSeq {
    StringBuilder text;
    Integer indentCounter = 0;

    public TextSeq(){
        text = new StringBuilder();
        indentCounter = 0;
    }

    public TextSeq(Integer indentCount) {
        text = new StringBuilder();
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
            text.append("  ");
        }
        text.append(string + "\n");
    }

    public void print(String string) {
        for(int i = 0; i < indentCounter; i++){
            text.append("  ");
        }
        text.append(string);
    }

    public String get() {
        return text.toString();
    }

}
