package event;

import java.util.List;

public interface Event {

    public String getId();
    public default String getParentId()
    {
        return null;
    }

    public default List<Event> getRelatedEvents()
    {
        return null;
    }


}
