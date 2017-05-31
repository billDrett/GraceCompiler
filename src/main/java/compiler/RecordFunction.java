package compiler;

import java.util.LinkedList;

/**
 * Created by bill on 08/05/17.
 */
public class RecordFunction extends Record
{
    private LinkedList<Record> fparameters;
    boolean defined;

    public RecordFunction(String nameId, String vartp, String deftp, LinkedList<Record> fparam, int line)
    {
        super(nameId, vartp, deftp, line);
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

    public void printHeader()
    {
        System.out.print(this.getName()+"(");
        for(Record i : fparameters)
        {
            if(i instanceof RecordParam) //print if its a reference
            {
                if(((RecordParam) i).getReference())
                {
                    System.out.print("ref ");
                }

                System.out.print(i.getName()+" : "+i.getType()+";");
            }
            else
            {
                RecordParamArray recParamArray = (RecordParamArray) i;

                if(((RecordParamArray) i).getReference())
                {
                    System.out.print("ref ");
                }

                System.out.print(i.getName()+" : "+i.getType()+" ");
                for(Integer dim : recParamArray.getDimensions())
                {
                    System.out.print("[");
                    if(dim != 0)
                    {
                        System.out.print(dim);
                    }
                    System.out.print("]");
                }
                System.out.print(";");
            }

           // System.out.print(i.getName()+" : "+i.getType()+" dim " +dimensions+";");
        }
        System.out.println(")");
    }
}

