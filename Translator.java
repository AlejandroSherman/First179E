public class Translator { //converts java -> vapor

    // Identifiers are used for two things: variables and labels.
    public void addID(String name, String value) {
        try {
            if(name == "variable") {
            /*
               add variable:
                   variables are always local to a function;
                   variable names must be unique within a function.
                   the first character cannot be a digit or a dot.
             */
            }
            else if(name == "label") {
            /*
               add label:
                   There are three types of labels:
                        data labels
                        function labels
                        code labels
                   All label names must be unique across the entire program.
             */
            }
            else {
                throw new Exception("Error: " + name + " is not a variable or label.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
