package compiler;

import java.util.LinkedList;

public class RecordFunction extends Record
{
    private LinkedList<Record> fparameters;
    private boolean defined;
    private String fixedName;

    public RecordFunction(String nameId, String vartp, LinkedList<Record> fparam, int line, int uniqueId)
    {
        super(nameId, vartp, line);
        fparameters = new LinkedList<>(fparam);
        defined = false;

        if(uniqueId == 0) //for main and library functions
        {
            fixedName = nameId+"_grace";
        }
        else
        {
            fixedName = nameId+"_"+uniqueId;
        }

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

    public String getFixedName()
    {
        return fixedName;
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
            if(i instanceof RecordParam)
            {
                if(((RecordParam) i).getReference()) //print if its a reference
                {
                    System.out.print("ref ");
                }

                System.out.print(i.getName()+" : "+i.getType()+";");
            }
            else //RecordParamArray
            {
                RecordParamArray recParamArray = (RecordParamArray) i;

                if(((RecordParamArray) i).getReference()) //print if its a reference
                {
                    System.out.print("ref ");
                }

                System.out.print(i.getName()+" : "+i.getType()+" ");
                for(Integer dim : recParamArray.getDimensions()) //print dimensions of array
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
        }
        System.out.println(")");
    }
}

