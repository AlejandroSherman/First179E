public class LableManager {
    public Integer labelNum;
    public String currLabel;

    public LableManager(){
        labelNum = 0;
        currLabel = "t."+labelNum;
    }

    public void resetLabel(){
        labelNum = 0;
        currLabel = "t."+labelNum;
    }

    public void nextLabel(){
        labelNum++;
        currLabel = "t."+labelNum;
    }


}
