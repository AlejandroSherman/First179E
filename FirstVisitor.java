import java.util.*;
import syntaxtree.*;
import visitor.*;

/*
 * The first visitor extends an existing one and populates a scope table
 */
public class FirstVisitor extends GJDepthFirst<Scope> {
    /** From visitor file
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
     @Override
     public void visit(Goal n, Scope s) {
      n.f0.accept(this, s);
      n.f1.accept(this, s);
      n.f2.accept(this, s);
      //now void, so no return
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
     public void visit(MainClass n, Scope s){
      SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

      Scope spe = new Scope(n, s);
      s.add(symt, n, spe);

      //Here is where we check the type of vardeclaration
      n.f14.accept(this, spe);
      n.f15.accept(this, spe);
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
     public void visit(ClassDeclaration n, Scope s){
      SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

      Scope spe = new Scope(n, s);
      s.add(symt, n, spe);

      //Here is where we check the type of vardeclaration
      n.f3.accept(this, spe);
      //Here is where we check the type of methodDeclaration
      n.f4.accept(this, spe);
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
     public void visit(ClassExtendsDeclaration n, Scope s){
      SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

      Binder bn = s.find(symt);
      Scope spe = new Scope(n, bn.getSc());
      s.add(symt, n, spe);

      //Here is where we check the type of vardeclaration
      n.f5.accept(this, spe);
      //Here is where we check the type of methodDeclaration
      n.f6.accept(this, spe);
     }

     /** From visitor file
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
     @Override
     public void visit(VarDeclaration n, Scope s){
      SymbolTable symt = SymbolTable.getTable(m.f1.tokenImage);

      //Here is where we check the type of var
      s.add(symt, n, s);
     }

     /**
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
     public void visit(MethodDeclaration n, Scope s){
      SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

      Scope spe = new Scope(n, s);
      s.add(symt, n, spe);

      //Here is where we check the state of formal parameter
      n.f4.accept(this, spe);
      //Here is where we check the type of vardeclaration
      n.f7.accept(this, spe);
     }

     /** From visitor file
     * f0 -> Type()
     * f1 -> Identifier()
     */
     @Override
     public void visit(FormalParameter n, Scope s){
      SymbolTable symt = SymbolTable.getTable(m.f0.tokenImage);

      //Here is where we check the type of identifier
      s.add(symt, n, s);
     }

}