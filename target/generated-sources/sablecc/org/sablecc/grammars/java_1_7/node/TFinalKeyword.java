/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.sablecc.grammars.java_1_7.node;

import org.sablecc.grammars.java_1_7.analysis.*;

@SuppressWarnings("nls")
public final class TFinalKeyword extends Token
{
    public TFinalKeyword()
    {
        super.setText("final");
    }

    public TFinalKeyword(int line, int pos)
    {
        super.setText("final");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TFinalKeyword(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFinalKeyword(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TFinalKeyword text.");
    }
}
