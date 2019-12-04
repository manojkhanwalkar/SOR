package graph;

import event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TransactionGraph {

    public TransactionGraph()
    {

    }

    Map<String,Node> nodes = new HashMap<>();

    public void add(Event event)
    {
        Node node = new Node();
        node.setEvent(event);
        String key = event.getParentId();
        if (key!=null)
        {
            Node parentNode = nodes.get(key);
            if (node==null)
            {
                System.err.println("Parent Node not found " + event.getParentId() + " " + event.getId());
            }
            else
            {
                parentNode.addChild(event);
            }
        }


    }


}
