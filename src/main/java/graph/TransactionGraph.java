package graph;

import event.Event;
import service.EventProcessingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionGraph {

    public TransactionGraph()
    {

    }

    public Map<String,Node> nodes = new HashMap<>();

    public synchronized void add(Event event)
    {

        System.out.println("Added event to graph " + event);
        Node node = new Node();
        node.setEvent(event);
        nodes.put(event.getId(),node);
        String key = event.getParentId();
        if (key!=null)
        {
            Node parentNode = nodes.get(key);
            if (parentNode==null)
            {
                System.err.println("Parent Node not found " + event.getParentId() + " " + event.getId());
            }
            else
            {
                parentNode.addChild(event);
            }
        }

        List<Event>  events = event.getRelatedEvents();
        if (events!=null)
        {
            events.stream().forEach(e->{ add(e);});
        }


    }

    public void printBreadth()
    {
        Node root = getRoot();

        System.out.println(root);
    }

    private Node getRoot()
    {
        Node node = nodes.values().stream().limit(1).collect(Collectors.toList()).get(0);

        String id = node.getEvent().getParentId();
        while (id!=null)
        {

            node = nodes.get(id);
            if (node==null)
            {
                System.out.println("Error in processing the graph to get the root ");
                return null;

            }
            else
            {
                id = node.getEvent().getParentId();
            }
        }

        return node;
    }


}
