package compiler;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by bill on 07/05/17.
 */
public class RecordArray extends Record
{
    private LinkedList<Integer> dimensions;

    public RecordArray(String nameId, String vartp, String deftp, LinkedList<Integer> dim)
    {
        super(nameId, vartp, deftp);
        dimensions =new LinkedList<>(dim);

        for(Integer i : dimensions)
        {
            System.out.println("!! "+i);
        }

    }

    public LinkedList<Integer> getDimensions()
    {
        return dimensions;
    }

}
