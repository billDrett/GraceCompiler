/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class TOr extends Token
{
    public TOr()
    {
        super.setText("or");
    }

    public TOr(int line, int pos)
    {
        super.setText("or");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TOr(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTOr(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TOr text.");
    }
}
