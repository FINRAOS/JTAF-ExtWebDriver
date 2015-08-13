package org.finra.jtaf.ewd.widget;

public interface IContainerElement extends IElement
{
    public boolean contains(IElement targetWidget) throws Exception;
}
