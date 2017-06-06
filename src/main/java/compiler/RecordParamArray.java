package compiler;

import java.util.LinkedList;

public class RecordParamArray extends RecordArray
{
    private boolean reference;

    public RecordParamArray(String nameId, String vartp, LinkedList<Integer> dim, boolean ref, int line)
    {
        super(nameId, vartp, dim, line);

        reference = ref;
    }

    public boolean getReference()
    {
        return reference;
    }
}
