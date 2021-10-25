//Cody Steimle (SID: 862137374)
//Alejandro Sherman (SID: 862062898)
//Steven Carrasco (SID: 862060283)

import syntaxtree.Node;
import java.io.InputStream;

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
            System.exit(1);
            //System.out.println(e.getCause());
            //e.printStackTrace();
        }   
    }

}
