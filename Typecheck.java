//Input: takes in a file P.java

import vistor.*;

class Typecheck {
    //not sure if i need to bring in vistor or just edit the class
}

class TypecheckVisitor extends GJDepthFirst<HashMap<...>,String> {
    //Hash map is symbol tree (got the ... from piazza, not sure what it's suppose to be yet)
    //I'm assuming the string is a name of the type?
}

//Output: at some point print Program type checked successfully or Type error