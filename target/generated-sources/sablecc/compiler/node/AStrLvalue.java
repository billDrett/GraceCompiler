/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AStrLvalue extends PLvalue
{
    private TConstString _constString_;

    public AStrLvalue()
    {
        // Constructor
    }

    public AStrLvalue(
        @SuppressWarnings("hiding") TConstString _constString_)
    {
        // Constructor
        setConstString(_constString_);

    }

    @Override
    public Object clone()
    {
        return new AStrLvalue(
            cloneNode(this._constString_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAStrLvalue(this);
    }

    public TConstString getConstString()
    {
        return this._constString_;
    }

    public void setConstString(TConstString node)
    {
        if(this._constString_ != null)
        {
            this._constString_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._constString_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._constString_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._constString_ == child)
        {
            this._constString_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._constString_ == oldChild)
        {
            setConstString((TConstString) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}