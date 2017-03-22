/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.sablecc.grammars.java_1_7.node;

import org.sablecc.grammars.java_1_7.analysis.*;

@SuppressWarnings("nls")
public final class TCaretAssign extends Token
{
    public TCaretAssign()
    {
        super.setText("^=");
    }

    public TCaretAssign(int line, int pos)
    {
        super.setText("^=");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TCaretAssign(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTCaretAssign(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TCaretAssign text.");
    }
}
