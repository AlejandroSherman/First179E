import java.util.*;
import syntaxtree.*;
import visitor.*;

/*
 * The second visitor typechecks
 */
public class SecondPhaseVisitor extends GJDepthFirst<String, Scope> {
    /** From visitor file
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
    @Override
    public String visit(VarDeclaration n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

        //collect type
        Typevar t = New Typevar(symt);
        n.f1.accept(this, t);

        if (t == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    @Override
    public String visit(MainClass n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f1.tokenImage);

        Binder bin = s.find(symt);
        n.f15.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public String visit(ClassDeclaration n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f1.tokenImage);

        Binder bin = s.find(symt);
        n.f4.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    @Override
    public String visit(ClassExtendsDeclaration n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f1.tokenImage);

        Binder bin = s.find(symt);
        n.f6.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    @Override
    public String visit(MethodDeclaration n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f1.tokenImage);

        Binder bin = s.find(symt);
        Scope sc = bin.getSC();
        n.f8.accept(this, sc);

        if (sc == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
    @Override
    public String visit(AssignmentStatement n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

        Binder bin = s.find(symt);
        n.f2.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    @Override
    public String visit(ArrayAssignmentStatement n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

        Binder bin = s.find(symt);
        n.f2.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
    @Override
    public String visit(IfStatement n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f2.tokenImage);

        Binder bin = s.find(symt);
        Scope sc = bin.getSC();
        n.f4.accept(this, sc);
        n.f6.accept(this, sc);

        if (sc == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    @Override
    public String visit(WhileStatement n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f2.tokenImage);

        Binder bin = s.find(symt);
        n.f4.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
    @Override
    public String visit(PrintStatement n, Scope s){
        n.f4.accept(this, s);
    }

    /** From visitor file
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    @Override
    public String visit(Expression n, Scope s){
        n.f0.accept(this, s);
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    @Override
    public String visit(AndExpression n, Scope s){
        Typevar left = n.f0.accept(this, s);
        Typevar right = n.f2.accept(this, s);
        if (left != right){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
    @Override
    public String visit(CompareExpression n, Scope s){
        Typevar left = n.f0.accept(this, s);
        Typevar right = n.f2.accept(this, s);
        if (left != right){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
    @Override
    public String visit(PlusExpression n, Scope s){
        Typevar left = n.f0.accept(this, s);
        Typevar right = n.f2.accept(this, s);
        if (left != right){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
    @Override
    public String visit(MinusExpression n, Scope s){
        Typevar left = n.f0.accept(this, s);
        Typevar right = n.f2.accept(this, s);
        if (left != right){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
    @Override
    public String visit(TimesExpression n, Scope s){
        Typevar left = n.f0.accept(this, s);
        Typevar right = n.f2.accept(this, s);
        if (left != right){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    @Override
    public String visit(ArrayLookup n, Scope s){
        //No idea
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    @Override
    public String visit(ArrayLength n, Scope s){
        //No idea
    }

    /** From visitor file
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
    @Override
    public String visit(MessageSend n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

        Binder bin = s.find(symt);
        n.f2.accept(this, bin);
        n.f4.accept(this, bin);

        if (bin == null){
            throw Exception("Type Error");
        }
    }

    /** From visitor file
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
    @Override
    public String visit(PrimaryExpression n, Scope s){
         //No idea
    }

    /** From visitor file
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, Scope s){
        SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

        //collect type
        Typevar t = New Typevar(symt);
        n.f1.accept(this, t);

        if (t == null){
            throw Exception("Type Error");
        }
    }
}