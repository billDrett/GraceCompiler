/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import java.util.*;
import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AFunDefinition extends PFunDefinition
{
    private PHeader _header_;
    private final LinkedList<PLocalDef> _localDef_ = new LinkedList<PLocalDef>();
    private final LinkedList<PStatement> _statement_ = new LinkedList<PStatement>();

    public AFunDefinition()
    {
        // Constructor
    }

    public AFunDefinition(
        @SuppressWarnings("hiding") PHeader _header_,
        @SuppressWarnings("hiding") List<PLocalDef> _localDef_,
        @SuppressWarnings("hiding") List<PStatement> _statement_)
    {
        // Constructor
        setHeader(_header_);

        setLocalDef(_localDef_);

        setStatement(_statement_);

    }

    @Override
    public Object clone()
    {
        return new AFunDefinition(
            cloneNode(this._header_),
            cloneList(this._localDef_),
            cloneList(this._statement_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFunDefinition(this);
    }

    public PHeader getHeader()
    {
        return this._header_;
    }

    public void setHeader(PHeader node)
    {
        if(this._header_ != null)
        {
            this._header_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._header_ = node;
    }

    public LinkedList<PLocalDef> getLocalDef()
    {
        return this._localDef_;
    }

    public void setLocalDef(List<PLocalDef> list)
    {
        this._localDef_.clear();
        this._localDef_.addAll(list);
        for(PLocalDef e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    public LinkedList<PStatement> getStatement()
    {
        return this._statement_;
    }

    public void setStatement(List<PStatement> list)
    {
        this._statement_.clear();
        this._statement_.addAll(list);
        for(PStatement e : list)
        {
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._header_)
            + toString(this._localDef_)
            + toString(this._statement_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._header_ == child)
        {
            this._header_ = null;
            return;
        }

        if(this._localDef_.remove(child))
        {
            return;
        }

        if(this._statement_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._header_ == oldChild)
        {
            setHeader((PHeader) newChild);
            return;
        }

        for(ListIterator<PLocalDef> i = this._localDef_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PLocalDef) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        for(ListIterator<PStatement> i = this._statement_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PStatement) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
