package compiler;

import java.util.LinkedList;

/**
 * Created by bill on 08/05/17.
 */
public class RecordFunction extends Record
{
    private LinkedList<Record> fparameters;
    boolean declared;

    public RecordFunction(String nameId, String vartp, String deftp, LinkedList<Record> fparam)
    {
        super(nameId, vartp, deftp);
        fparameters = new LinkedList<>(fparam);
        declared = false;
    }

    public int getNoParameters()
    {
        return fparameters.size();
    }

    public LinkedList<Record> getFparameters()
    {
        return fparameters;
    }

    public boolean getDeclared()
    {
        return declared;
    }

    public void setDeclared(boolean Declared)
    {
        declared = Declared;
    }
}

