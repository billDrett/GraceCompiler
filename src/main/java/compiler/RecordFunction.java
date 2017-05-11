package compiler;

import java.util.LinkedList;

/**
 * Created by bill on 08/05/17.
 */
public class RecordFunction extends Record
{
    private LinkedList<Record> fparameters;
    public RecordFunction(String nameId, String vartp, String deftp, LinkedList<Record> fparam)
    {
        super(nameId, vartp, deftp);
        fparameters = new LinkedList<>(fparam);
    }

    public int getNoParameters()
    {
        return fparameters.size();
    }

    public LinkedList<Record> getFparameters()
    {
        return fparameters;
    }

    public void Tst()
    {
        for(Record rec : fparameters)
        {
            if(rec instanceof RecordParam)
            {
                System.out.println("IS parameter simple");
            }
            else if (rec instanceof RecordParamArray)
            {
                System.out.println("IS parameter Array");
            }
        }
    }
}
