/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.sablecc.grammars.java_1_7.node;

import org.sablecc.grammars.java_1_7.analysis.*;

@SuppressWarnings("nls")
public final class TConstKeyword extends Token
{
    public TConstKeyword()
    {
        super.setText("const");
    }

    public TConstKeyword(int line, int pos)
    {
        super.setText("const");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TConstKeyword(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTConstKeyword(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TConstKeyword text.");
    }
}