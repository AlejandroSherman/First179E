import typecheck.*;

import syntaxtree.Node;
import syntaxtree.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;

public class Typecheck {

    public static void main(String[] args) throws FileNotFoundException{
        InputStream fileStream = System.in;
        new MiniJavaParser(fileStream);
        FirstVisitor ty = new FirstVisitor();
        AllClassTable Global = new AllClassTable();
        SecondVisitor typeCheck = new SecondVisitor();
        try {
            Node root = MiniJavaParser.Goal();
            root.accept(ty, Global);
            root.accept(typeCheck, Global);
            System.out.println("Program type checked successfully");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}