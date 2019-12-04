package graph;

import event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node {

    Event event;

    List<Event> children;

    List<Event> related;

    public void addChild(Event child)
    {
        if (children==null)
        {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    public void addRelated(Event r)
    {
        if (related==null)
        {
            related=new ArrayList<>();
        }
        related.add(r);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Event> getChildren() {
        return children;
    }

    public void setChildren(List<Event> children) {
        this.children = children;
    }

    public List<Event> getRelated() {
        return related;
    }

    public void setRelated(List<Event> related) {
        this.related = related;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return event.equals(node.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event);
    }
}
