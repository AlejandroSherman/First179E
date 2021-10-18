import symboltable.SymbolTable;
import syntaxtree.Node;
import typecheckvisitor.*;

import java.io.InputStream;


public class Typecheck {

    public static void main(String [] args) throws Exception {
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        FirstVisitor first_visitor = new FirstVisitor();
        SecondVisitor second_visitor = new SecondVisitor();
        SymbolTable Global = new SymbolTable(); // Global Symbol Table. Map for classes and map for methods.
        try {
            Node root = MiniJavaParser.Goal();   // does the parsing.
            root.accept(first_visitor, Global);  // fills global symbol table.
            root.accept(second_visitor, Global); // does the type checking.
            System.out.println("Program type checked successfully");
        }
        catch(Exception e) {
            System.out.println("Type error");
            System.out.println(e.getCause());
            e.printStackTrace();
        }
    }

}
