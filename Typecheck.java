import syntaxtree.Node;
import java.io.InputStream;


//TO DO

//First Pass
//Still need properly store types for class declarations

//Second Pass
//Second pass / type checking still needs to happen


public class Typecheck {

    public static void main(String [] args) throws Exception {
        //read in file via <
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        FirstVisitor first_visitor = new FirstVisitor();
        SecondVisitor second_visitor = new SecondVisitor();
        CompleteTable completeTable = new CompleteTable(); // Global Symbol Table. Map for classes and map for methods.
        try {
            Node root = MiniJavaParser.Goal();   // does the parsing.
            root.accept(first_visitor, completeTable);  // fills global symbol table.
            //completeTable.prntTable();
            root.accept(second_visitor, completeTable); // does the type checking.
            System.out.println("Program type checked successfully");
        }
        catch(Exception e) {
            System.out.println("Type error");
            System.out.println(e.getCause());
            e.printStackTrace();
        }   
    }

}
