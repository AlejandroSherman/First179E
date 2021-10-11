import typevistors.*;
import syntaxtree.Node;

public class Typecheck {
    public static void main(String[] args){
        //get input??
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        
        //create classes
        FirstVisitor first = new FirstVisitor();
        AllClassTable Global = new AllClassTable();
        SecondVisitor second = new SecondVisitor();
        
        //tree stuff
        Node root = MiniJavaParser.Goal();
        root.accept(first, Global);
        root.accept(second, Global);
        System.out.println("Program type checked successfully");
        //else ERROR
    }
}