package compiler;

/**
 * Created by bill on 08/05/17.
 */
public class RecordParam extends Record
{
    private boolean reference;

    public RecordParam(String nameId, String vartp, String deftp, boolean ref, int line)
    {
        super(nameId, vartp, deftp, line);

        reference =ref;
    }

    public boolean getReference()
    {
        return reference;
    }
}
