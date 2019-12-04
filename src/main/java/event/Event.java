package event;

public interface Event {

    public String getId();
    public default String getParentId()
    {
        return null;
    }


}
