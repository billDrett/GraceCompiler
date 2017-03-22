/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.sablecc.grammars.java_1_7.node;

import org.sablecc.grammars.java_1_7.analysis.*;

@SuppressWarnings("nls")
public final class TThrowKeyword extends Token
{
    public TThrowKeyword()
    {
        super.setText("throw");
    }

    public TThrowKeyword(int line, int pos)
    {
        super.setText("throw");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TThrowKeyword(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTThrowKeyword(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TThrowKeyword text.");
    }
}
