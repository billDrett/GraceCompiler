package compiler;

/**
 * Created by d on 10/05/17.
 */

public class RecType {

    private String varName;
    private String type;
    private int dimensions;
    private int line;


    public RecType(String name, String tp, int dim, int Line)
    {
        varName = name; //name of the variable
        type = tp;
        dimensions = dim;
        line = Line; //line in which it was found
    }

    public String getVarName()
    {
        return varName;
    }

    public String getType() {
        return type;
    }

    public int getDimensions()
    {
        return dimensions;
    }

    public int getLine()
    {
        return line;
    }

    public void setDimensions(int dim)
    {
        dimensions = dim;
    }


}
