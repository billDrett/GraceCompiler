package compiler;

import java.util.LinkedList;

/**
 * Created by bill on 08/05/17.
 */
public class RecordParamArray extends RecordArray
{
    private boolean reference;

    public RecordParamArray(String nameId, String vartp, String deftp, LinkedList<Integer> dim, boolean ref, int line)
    {
        super(nameId, vartp, deftp, dim, line);

        reference = ref;
    }

    public boolean getReference()
    {
        return reference;
    }
}
