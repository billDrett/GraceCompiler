package compiler;

import java.util.LinkedList;
import java.util.Set;

public class RecordArray extends Record
{
    private LinkedList<Integer> dimensions;

    public RecordArray(String nameId, String vartp, LinkedList<Integer> dim, int line)
    {
        super(nameId, vartp, line);
        dimensions =new LinkedList<>(dim);
    }

    public LinkedList<Integer> getDimensions()
    {
        return dimensions;
    }

    public int getSerializedSize()
    {
        int sum =1;
        for(Integer i: dimensions)
        {
            sum *=i;
        }

        return sum;
    }

}
