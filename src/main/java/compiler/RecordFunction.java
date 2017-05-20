package compiler;

import java.util.LinkedList;

/**
 * Created by bill on 08/05/17.
 */
public class RecordFunction extends Record
{
    private LinkedList<Record> fparameters;
    boolean defined;

    public RecordFunction(String nameId, String vartp, String deftp, LinkedList<Record> fparam)
    {
        super(nameId, vartp, deftp);
        fparameters = new LinkedList<>(fparam);
        defined = false;
    }

    public int getNoParameters()
    {
        return fparameters.size();
    }

    public LinkedList<Record> getFparameters()
    {
        return fparameters;
    }

    public boolean getDefined()
    {
        return defined;
    }

    public void setDefined(boolean Defined)
    {
        defined = Defined;
    }
}

