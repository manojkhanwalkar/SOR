package util;

import event.CcvPin;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONUtilTest {

    @Test
    public void plainSolrDocumentJsonDeserializes() {
        CcvPin cp = (CcvPin) JSONUtil.fromJSON("{\"ccv\":\"4111111111111111\",\"ein\":42,\"tin\":7}", CcvPin.class);
        assertNotNull(cp);
        assertEquals(42, cp.getEin());
    }

    @Test
    public void polymorphicTypeIdsAreNotHonoured() {
        // With default typing enabled, a type-id wrapper like this would be accepted and the named
        // class instantiated. Without it, the input is read as a plain JSON array.
        Object typed = JSONUtil.fromJSON("[\"event.CcvPin\",{\"ein\":1}]", Object.class);
        assertFalse("type id must not select a class", typed instanceof CcvPin);
    }
}
