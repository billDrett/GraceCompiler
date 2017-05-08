package compiler;

/**
 * Created by bill on 08/05/17.
 */
public class RecordParam extends Record
{
    private boolean reference;

    public RecordParam(String nameId, String vartp, String deftp, boolean ref)
    {
        super(nameId, vartp, deftp);

        reference =ref;
    }

    public boolean getReference()
    {
        return reference;
    }
}
