package processor;

import event.CHFDataRecord;
import event.CcvPin;
import event.Event;
import event.PinCCVPii;
import service.AbstractBoltSync;
import util.SolrClient;
import util.SolrUtil;

import java.util.List;

public class CHFGVAPSolrWriterBoltSync extends AbstractBoltSync {

    public CHFGVAPSolrWriterBoltSync() {

        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","pinccvpii");





    }


    public void process(Event event)
    {
        SolrClient<PinCCVPii> merge  = SolrUtil.getInstance().getClient("pinccvpii");

        PinCCVPii bean = (PinCCVPii) event;
        merge.add(bean);

        merge.commit();

    }









}
