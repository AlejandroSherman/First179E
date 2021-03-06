import syntaxtree.*;
import visitor.GJDepthFirst;

public class SecondVisitor extends GJDepthFirst<AbstractTable,AbstractTable> {
    TypeDictionary dictionary = new TypeDictionary();
    String inClass;
    String inMethod;
    Boolean isReturning = false;
    String exRetType;
    Boolean isAssignLHS = false;
    Boolean isAssignRHS = false;
    String lhsType;
    String rhsName;
    Boolean isArray = false;
    Boolean isIndex = false;
    String arrayType;
    String indexType;

    @Override
    public AbstractTable visit(MainClass n, AbstractTable argTable){
        ClassTable classTable = new ClassTable();
        try{
            classTable = argTable.Global.classes.get(n.f1.f0.tokenImage);
        }
        catch(Exception e) {
            System.out.println("Type error");
            System.exit(1);
        }

        n.f0.accept(this, classTable);
        n.f1.accept(this, classTable);
        n.f2.accept(this, classTable);

        MethodTable methodTable = classTable.methods.get("main");
        n.f4.accept(this, methodTable);
        n.f5.accept(this, methodTable);
        n.f6.accept(this, methodTable);
        n.f7.accept(this, methodTable);
        n.f8.accept(this, methodTable);
        n.f9.accept(this, methodTable);
        n.f10.accept(this, methodTable);
        n.f11.accept(this, methodTable);
        n.f12.accept(this, methodTable);
        n.f13.accept(this, methodTable);
        n.f14.accept(this, methodTable);
        n.f15.accept(this, methodTable);
        n.f16.accept(this, classTable);

        inClass = "Main";
        inMethod = "main";

        return null;
    }
 
    @Override
    public AbstractTable visit(AssignmentStatement n, AbstractTable argTable){
        String leftSideName = n.f0.f0.tokenImage;
        isAssignLHS = true;
        ClassTable cTable = argTable.Global.classes.get(inClass);
        MethodTable mTable = cTable.methods.get(inMethod);

        //Checking if the LHS variable exisits
        try {
            //Check locals
            if(mTable.locals.get(leftSideName) == null) {
                boolean isFound = false;

                //Check parameters
                for (String paramsName: mTable.params.keySet()) {
                    //System.out.println(leftSideName + " vs " + paramsName);
                    if(leftSideName == paramsName){
                        isFound = true;
                        lhsType = mTable.params.get(leftSideName).dataType;
                        //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
                    }
                }

                //Check class
                if(!isFound){
                    for (String varsName: cTable.vars.keySet()) {
                        //System.out.println(leftSideName + " vs " + varsName);
                        if(leftSideName == varsName){
                            isFound = true;
                            lhsType = cTable.vars.get(leftSideName).dataType;
                            //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
                        }
                    }

                    //Check super class(es)
                    if(!isFound){
                        String superClass = cTable.super_class;
                        ClassTable tempTable;
                        while (superClass != null){
                            tempTable = argTable.Global.classes.get(superClass);
                            if(tempTable.vars.get(leftSideName) != null) {
                                isFound = true;
                                lhsType = tempTable.vars.get(leftSideName).dataType;
                                //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
                            }
                            superClass = tempTable.super_class;
                        }
                    }
                }
                if(!isFound) throw new Exception("ERROR - " + leftSideName + "does not exist in scope.");
            }
            else {
                lhsType = mTable.locals.get(leftSideName);
                //System.out.println("Name: " + leftSideName + "  Type: " + lhsType);
            }
        }
        catch(Exception e){
            //System.out.println("Type error on: " + leftSideName + " = " + rhsName + ",  LHS not found  " + inClass + "->" + inMethod);
            System.out.println("Type error");
            System.exit(1);
        }

        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);

        //Checking RHS variable
        if (!lhsType.equals("Identifier")){ //Ignoring classes            
            try {
                //System.out.println("Left hand side var: " + leftSideName);
                //System.out.println("Right hand side var: " + rhsName);
                boolean isFound = false;

                //Check for counter or raw data
                if(rhsName.equals("Int") || rhsName.equals("Bool")) isFound = true;

                //Ignoring classes for now
                else if(lhsType.equals("Identifier")){
                    //System.out.println(leftSideName + " is of type Class" );
                    isFound = true;
                }

                //Check Locals
                else if(mTable.locals.get(rhsName) == null) {

                    //Check parameters
                    for (String paramsName: mTable.params.keySet()) {
                        if(rhsName == paramsName){
                            isFound = true;
                            lhsType = mTable.params.get(rhsName).dataType;
                        }
                    }

                    //Check class vars
                    if(!isFound){
                        for (String varsName: cTable.vars.keySet()) {
                            if(rhsName == varsName){
                                isFound = true;
                                lhsType = cTable.vars.get(rhsName).dataType;
                            }
                        }

                        //Check class methods for "this"
                        if(cTable.methods.get(rhsName) != null) {
                            isFound = true;
                        }

                        //Check super class(es)
                        if(!isFound){
                            String superClass = cTable.super_class;
                            ClassTable tempTable;
                            while (superClass != null){
                                tempTable = argTable.Global.classes.get(superClass);
                                if(tempTable.vars.get(rhsName) != null) {
                                    isFound = true;
                                    lhsType = tempTable.vars.get(rhsName).dataType;
                                }

                                //Check super class(es) methods for "this"
                                if(cTable.methods.get(rhsName) != null) {
                                    isFound = true;
                                }

                                superClass = tempTable.super_class;
                            }
                        }
                    }

                    //Else not found
                    if(!isFound) throw new Exception("ERROR - " + rhsName + "does not exist in scope.");
                }

                //Else is found
                else {
                    isFound = true;
                    //System.out.println("Type OKAY: " + leftSideName + " = " + rhsName + ",  found in  " + inClass + "->" + inMethod);
                }

            }
            catch(Exception e){
                //System.out.println("Type error on: " + leftSideName + " = " + rhsName + ",  RHS not found  " + inClass + "->" + inMethod);
                System.out.println("Type error");
                System.exit(1);
            }
        }

        n.f3.accept(this, argTable);

        return null;
    }

    @Override
    public AbstractTable visit(MethodDeclaration n, AbstractTable argTable) {
        inMethod = n.f2.f0.tokenImage;
        ClassTable cTable = argTable.Global.classes.get(inClass);
        MethodTable mTable = cTable.methods.get(inMethod);

        AbstractTable _ret=null;
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        n.f3.accept(this, argTable);
        n.f4.accept(this, argTable);
        n.f5.accept(this, argTable);
        n.f6.accept(this, argTable);
        n.f7.accept(this, argTable);
        n.f8.accept(this, argTable);
        n.f9.accept(this, argTable);

        isReturning = true;
        exRetType = mTable.returnType;

        n.f10.accept(this, argTable);

        isReturning = false;

        n.f11.accept(this, argTable);
        n.f12.accept(this, argTable);

        return _ret;
    }


    @Override
    public AbstractTable visit(PrimaryExpression n, AbstractTable argu) {
        AbstractTable _ret=null;
        
        if(isReturning) {
            String returningType = n.f0.choice.getClass().getSimpleName();
            //Find type for indentifiers
            if(returningType.equals("Identifier") || returningType.equals("ThisExpression")){
                //Returning a varaiable or a class
            }
            else {
                returningType = dictionary.getRealType(returningType);
                exRetType = dictionary.getRealType(exRetType);

                if(returningType!=null && exRetType!=null){
                    try{
                        if(!(returningType.equals(exRetType))) throw new Exception("ERROR - Invalid return type.");
                    }
                    catch(Exception e){
                        System.out.println("Type error");
                        System.exit(1);
                    }   
                } 
            }
        }
        if(isAssignRHS){
            String rhsType = n.f0.choice.getClass().getSimpleName();
            if(rhsType.equals("IntegerLiteral")) rhsName = "Int";
            else if (rhsType.equals("TrueLiteral")) rhsName = "Bool";
            else if (rhsType.equals("FalseLiteral")) rhsName = "Bool";
        }

        n.f0.accept(this, argu);

        return _ret;
    }

    @Override
    public AbstractTable visit(Identifier n, AbstractTable argu) {
        AbstractTable _ret=null;
 
        if(isReturning) {
            ClassTable cTable = argu.Global.classes.get(inClass);
            MethodTable mTable = cTable.methods.get(inMethod);
            String returnName = n.f0.tokenImage;

            //Ignore classes for now
            if(exRetType.equals("Identifier")){

            }
            //Check locals for return type locals
            else if(mTable.locals.get(returnName) != null) {
                String returnType = mTable.locals.get(returnName);
                returnType = dictionary.getRealType(returnType);
                exRetType = dictionary.getRealType(exRetType);

                try{
                    if(!returnType.equals(exRetType)) throw new Exception("ERROR - Invalid return type.");
                }
                catch(Exception e){
                    System.out.println("Type error");
                    System.exit(1);
                }   
            }
            
        }
        else if(isArray) {
            ClassTable cTable = argu.Global.classes.get(inClass);
            MethodTable mTable = cTable.methods.get(inMethod);
            String arrayName = n.f0.tokenImage;

            //Gets arrayType
            try {
                boolean isFound = false;
                //Check Locals
                if(mTable.locals.get(arrayName) == null) {
                    //Check parameters
                    for (String paramsName: mTable.params.keySet()) {
                        if(arrayName == paramsName){
                            isFound = true;
                            arrayType = mTable.params.get(arrayName).dataType;
                        }
                    }
                    //Check class vars
                    if(!isFound){
                        for (String varsName: cTable.vars.keySet()) {
                            if(arrayName == varsName){
                                isFound = true;
                                arrayType = cTable.vars.get(arrayName).dataType;
                            }
                        }
                        //Check class methods for "this"
                        if(cTable.methods.get(arrayName) != null) {
                            isFound = true;
                        }
                        //Check super class(es)
                        if(!isFound){
                            String superClass = cTable.super_class;
                            ClassTable tempTable;
                            while (superClass != null){
                                tempTable = argu.Global.classes.get(superClass);
                                if(tempTable.vars.get(arrayName) != null) {
                                    isFound = true;
                                    arrayType = tempTable.vars.get(arrayName).dataType;
                                }
                                //Check super class(es) methods for "this"
                                if(cTable.methods.get(arrayName) != null) {
                                    isFound = true;
                                }
                                superClass = tempTable.super_class;
                            }
                        }
                    }

                    //Else not found
                    if(!isFound) throw new Exception("ERROR - " + arrayName + "does not exist in scope.");
                }
                //Else is found
                else isFound = true;

            }
            catch(Exception e){
                //System.out.println("Type error on: " + leftSideName + " = " + rhsName + ",  RHS not found  " + inClass + "->" + inMethod);
                System.out.println("Type error");
                System.exit(1);
            }
            
        }
        else if(isIndex) {
            ClassTable cTable = argu.Global.classes.get(inClass);
            MethodTable mTable = cTable.methods.get(inMethod);
            String indexName = n.f0.tokenImage;

            //Gets indexType
            try {
                boolean isFound = false;
                //Check Locals
                if(mTable.locals.get(indexName) == null) {
                    //Check parameters
                    for (String paramsName: mTable.params.keySet()) {
                        if(indexName == paramsName){
                            isFound = true;
                            indexType = mTable.params.get(indexName).dataType;
                        }
                    }
                    //Check class vars
                    if(!isFound){
                        for (String varsName: cTable.vars.keySet()) {
                            if(indexName == varsName){
                                isFound = true;
                                indexType = cTable.vars.get(indexName).dataType;
                            }
                        }
                        //Check class methods for "this"
                        if(cTable.methods.get(indexName) != null) {
                            isFound = true;
                        }
                        //Check super class(es)
                        if(!isFound){
                            String superClass = cTable.super_class;
                            ClassTable tempTable;
                            while (superClass != null){
                                tempTable = argu.Global.classes.get(superClass);
                                if(tempTable.vars.get(indexName) != null) {
                                    isFound = true;
                                    indexType = tempTable.vars.get(indexName).dataType;
                                }
                                //Check super class(es) methods for "this"
                                if(cTable.methods.get(indexName) != null) {
                                    isFound = true;
                                }
                                superClass = tempTable.super_class;
                            }
                        }
                    }

                    //Else not found
                    if(!isFound) throw new Exception("ERROR - " + indexName + "does not exist in scope.");
                }
                //Else is found
                else {
                    isFound = true;
                    indexType = mTable.locals.get(indexName);

                }

            }
            catch(Exception e){
                //System.out.println("Type error on: " + leftSideName + " = " + rhsName + ",  RHS not found  " + inClass + "->" + inMethod);
                System.out.println("Type error");
                System.exit(1);
            }
            
        }
        
        if(isAssignLHS) {
            isAssignLHS = false;  
            isAssignRHS = true;          
        }
        else if(isAssignRHS){
            rhsName = n.f0.tokenImage;   
            if(rhsName.equals("i")) rhsName = "Int";
            isAssignRHS = false;
        }

        n.f0.accept(this, argu);
        return _ret;
    }

    @Override
    public AbstractTable visit(ArrayLookup n, AbstractTable argu) {
        AbstractTable _ret=null;

        isArray = true;
        n.f0.accept(this, argu); //First expression (the array)
        isArray = false;

        arrayType = dictionary.getRealType(arrayType);
        try{
            if(!arrayType.equals("Array")) throw new Exception("ERROR - accessing a non array.");
        }
        catch(Exception e){
            System.out.println("Type error");
            System.exit(1);
        }

        n.f1.accept(this, argu);

        isIndex = true;
        n.f2.accept(this, argu); //Second expression (the index)
        isIndex = false;

        n.f3.accept(this, argu);
        return _ret;
    }
  


//Everything below this line is redoing first pass stuff to get class and method names
/**************************************************************************************/
    @Override
    public AbstractTable visit(ClassDeclaration n, AbstractTable argTable) {
        inClass = n.f1.f0.tokenImage;
        AbstractTable _ret=null;
        n.f0.accept(this, argTable);
        n.f1.accept(this, argTable);
        n.f2.accept(this, argTable);
        n.f3.accept(this, argTable);
        n.f4.accept(this, argTable);
        n.f5.accept(this, argTable);
        return _ret;
    }

}  