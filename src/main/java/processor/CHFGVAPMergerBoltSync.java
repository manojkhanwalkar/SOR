package processor;

import event.*;
import service.AbstractBoltSync;
import util.SolrClient;
import util.SolrUtil;

import java.util.List;

public class CHFGVAPMergerBoltSync extends AbstractBoltSync {

    public CHFGVAPMergerBoltSync() {

        SolrUtil.getInstance().addClient("http://localhost:8983/solr/","ccvpin");

}


    public void process(Event event)
    {
        SolrClient<CcvPin> gvap  = SolrUtil.getInstance().getClient("ccvpin");

        CHFDataRecord chfRecord = (CHFDataRecord) event;


        List<CcvPin> list1 = gvap.query("ein:"+chfRecord.getEin(),CcvPin.class);

        for (CcvPin ccvPin : list1) {

            PinCCVPii bean = new PinCCVPii();
            bean.setCcv(ccvPin.getCcv());
            bean.setEin(chfRecord.getEin());
            bean.setFName(chfRecord.getFName());
            bean.setLName(chfRecord.getLName());

            emit("CHFGVAPSolrWriter",bean);

        }
    }









}
