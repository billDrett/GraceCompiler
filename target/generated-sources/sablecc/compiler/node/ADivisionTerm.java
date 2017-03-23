/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class ADivisionTerm extends PTerm
{
    private PTerm _term_;
    private TDivision _division_;
    private PFactor _factor_;

    public ADivisionTerm()
    {
        // Constructor
    }

    public ADivisionTerm(
        @SuppressWarnings("hiding") PTerm _term_,
        @SuppressWarnings("hiding") TDivision _division_,
        @SuppressWarnings("hiding") PFactor _factor_)
    {
        // Constructor
        setTerm(_term_);

        setDivision(_division_);

        setFactor(_factor_);

    }

    @Override
    public Object clone()
    {
        return new ADivisionTerm(
            cloneNode(this._term_),
            cloneNode(this._division_),
            cloneNode(this._factor_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADivisionTerm(this);
    }

    public PTerm getTerm()
    {
        return this._term_;
    }

    public void setTerm(PTerm node)
    {
        if(this._term_ != null)
        {
            this._term_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._term_ = node;
    }

    public TDivision getDivision()
    {
        return this._division_;
    }

    public void setDivision(TDivision node)
    {
        if(this._division_ != null)
        {
            this._division_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._division_ = node;
    }

    public PFactor getFactor()
    {
        return this._factor_;
    }

    public void setFactor(PFactor node)
    {
        if(this._factor_ != null)
        {
            this._factor_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._factor_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._term_)
            + toString(this._division_)
            + toString(this._factor_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._term_ == child)
        {
            this._term_ = null;
            return;
        }

        if(this._division_ == child)
        {
            this._division_ = null;
            return;
        }

        if(this._factor_ == child)
        {
            this._factor_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._term_ == oldChild)
        {
            setTerm((PTerm) newChild);
            return;
        }

        if(this._division_ == oldChild)
        {
            setDivision((TDivision) newChild);
            return;
        }

        if(this._factor_ == oldChild)
        {
            setFactor((PFactor) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}