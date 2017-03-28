/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AFuncCallStmt extends PStmt
{
    private PFuncCall _funcCall_;
    private TSemi _semi_;

    public AFuncCallStmt()
    {
        // Constructor
    }

    public AFuncCallStmt(
        @SuppressWarnings("hiding") PFuncCall _funcCall_,
        @SuppressWarnings("hiding") TSemi _semi_)
    {
        // Constructor
        setFuncCall(_funcCall_);

        setSemi(_semi_);

    }

    @Override
    public Object clone()
    {
        return new AFuncCallStmt(
            cloneNode(this._funcCall_),
            cloneNode(this._semi_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFuncCallStmt(this);
    }

    public PFuncCall getFuncCall()
    {
        return this._funcCall_;
    }

    public void setFuncCall(PFuncCall node)
    {
        if(this._funcCall_ != null)
        {
            this._funcCall_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._funcCall_ = node;
    }

    public TSemi getSemi()
    {
        return this._semi_;
    }

    public void setSemi(TSemi node)
    {
        if(this._semi_ != null)
        {
            this._semi_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._semi_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._funcCall_)
            + toString(this._semi_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._funcCall_ == child)
        {
            this._funcCall_ = null;
            return;
        }

        if(this._semi_ == child)
        {
            this._semi_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._funcCall_ == oldChild)
        {
            setFuncCall((PFuncCall) newChild);
            return;
        }

        if(this._semi_ == oldChild)
        {
            setSemi((TSemi) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
