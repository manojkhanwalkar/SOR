package util;

import event.CHFDataRecord;
import event.CcvPin;
import event.GVAPDataRecord;
import event.PinCCVPii;
import org.junit.Test;

import static org.junit.Assert.*;

public class MaskTest {

    private static final String PAN = "4111111111111111";

    @Test
    public void last4KeepsOnlyLastFourCharacters() {
        assertEquals("************1111", Mask.last4(PAN));
        assertEquals("****", Mask.last4("123"));
        assertNull(Mask.last4(null));
    }

    @Test
    public void gvapRecordToStringDoesNotLeakPanOrTin() {
        GVAPDataRecord record = GVAPDataRecord.create(12345, 987654321L, PAN);
        String s = record.toString();
        assertFalse(s, s.contains(PAN));
        assertFalse(s, s.contains("987654321"));
        assertTrue(s, s.contains("1111"));
        // the file format itself is unchanged
        assertTrue(record.getRecord().contains(PAN));
    }

    @Test
    public void chfRecordToStringDoesNotLeakNames() {
        String s = CHFDataRecord.create(12345, "Alice", "Smith").toString();
        assertFalse(s, s.contains("Alice"));
        assertFalse(s, s.contains("Smith"));
    }

    @Test
    public void beansToStringDoNotLeakPan() {
        CcvPin cp = new CcvPin();
        cp.setCcv(PAN);
        cp.setTin(5551234);
        assertFalse(cp.toString().contains(PAN));
        assertFalse(cp.toString().contains("5551234"));

        PinCCVPii pii = new PinCCVPii();
        pii.setCcv(PAN);
        pii.setFName("Alice");
        pii.setLName("Smith");
        String s = pii.toString();
        assertFalse(s, s.contains(PAN));
        assertFalse(s, s.contains("Alice"));
        assertFalse(s, s.contains("Smith"));
    }

    @Test
    public void shortOrLongInputLinesDoNotThrow() {
        GVAPDataRecord shortRec = GVAPDataRecord.create("123");
        assertEquals(123, shortRec.getEin());
        assertEquals("", shortRec.getPAN());
        assertNotNull(shortRec.toString());

        CHFDataRecord longRec = CHFDataRecord.create("1".repeat(500));
        assertNotNull(longRec.getLName());
        assertNotNull(longRec.toString());
    }
}
