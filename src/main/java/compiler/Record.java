package compiler;

public class Record {

    private String name;
    private String type;
    private int depth;
    private int Line;


    public Record(String nameId, String vartp, int line)
    {
        name = new String(nameId);
        type = vartp;
        Line = line;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth1)
    {
        depth = depth1;
    }

    public int getLine()
    {
        return Line;
    }
}
