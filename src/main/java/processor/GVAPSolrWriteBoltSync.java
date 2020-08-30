package processor;

import event.CcvPin;
import event.Event;
import event.GVAPDataRecord;
import service.AbstractBoltSync;
import util.SolrClient;
import util.SolrUtil;

public class GVAPSolrWriteBoltSync extends AbstractBoltSync {

    public GVAPSolrWriteBoltSync() {

        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","ccvpin");

}


    public void process(Event event)
    {
        SolrClient<CcvPin> gvap  = SolrUtil.getInstance().getClient("ccvpin");

        GVAPDataRecord record = (GVAPDataRecord) event;

        CcvPin cp = new CcvPin();
        cp.setCcv(record.getPAN());
        cp.setEin(record.getEin());
        gvap.add(cp);

        gvap.commit();

    }









}
