/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class ACharConstTerm extends PTerm
{
    private TConstChar _constChar_;

    public ACharConstTerm()
    {
        // Constructor
    }

    public ACharConstTerm(
        @SuppressWarnings("hiding") TConstChar _constChar_)
    {
        // Constructor
        setConstChar(_constChar_);

    }

    @Override
    public Object clone()
    {
        return new ACharConstTerm(
            cloneNode(this._constChar_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACharConstTerm(this);
    }

    public TConstChar getConstChar()
    {
        return this._constChar_;
    }

    public void setConstChar(TConstChar node)
    {
        if(this._constChar_ != null)
        {
            this._constChar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._constChar_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._constChar_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._constChar_ == child)
        {
            this._constChar_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._constChar_ == oldChild)
        {
            setConstChar((TConstChar) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
