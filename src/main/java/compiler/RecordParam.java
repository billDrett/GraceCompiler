package compiler;

public class RecordParam extends Record
{
    private boolean reference;

    public RecordParam(String nameId, String vartp, boolean ref, int line)
    {
        super(nameId, vartp, line);

        reference =ref;
    }

    public boolean getReference()
    {
        return reference;
    }
}
