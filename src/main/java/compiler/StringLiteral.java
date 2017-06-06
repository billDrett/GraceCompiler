package compiler;

public class StringLiteral extends RecordLValue
{
    private String literalId;

    StringLiteral(String id)
    {
        super();
        literalId =id;
    }

    @Override
    public boolean emptyDimension()
    {
        return addressIndex != null;
    }

    String getLiteralId()
    {
        return literalId;
    }
}
