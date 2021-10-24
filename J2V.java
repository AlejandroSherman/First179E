import syntaxtree.Node;

import java.io.InputStream;

public class J2V {

    public static void main(String[] args){
        // driver program like Typecheck.java


        //read in file via <
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);

    
        FirstVisitor first_visitor = new FirstVisitor();
        SecondVisitor second_visitor = new SecondVisitor();
        CompleteTable completeTable = new CompleteTable(); // Global Symbol Table. Map for classes and map for methods.
        try {
            Node root = MiniJavaParser.Goal();          // does the parsing.
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
