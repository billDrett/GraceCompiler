package compiler;

/**
 * Created by bill on 04/05/17.
 */

public class Record {

    private String name;
    private String type;
    private String deftype;
    private int depth;


    public Record(String nameId, String vartp, String deftp)
    {
        name = new String(nameId);
        type = vartp;
        deftype = deftp;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDeftype() {
        return deftype;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth1)
    {
        depth = depth1;
    }
}
