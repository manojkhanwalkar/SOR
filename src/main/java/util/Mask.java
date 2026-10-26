package util;

/**
 * Redaction helpers so card numbers, tax ids and names never reach logs or console output.
 */
public final class Mask {

    private Mask() {}

    /** Shows only the last four characters, e.g. "************1234". */
    public static String last4(String value) {
        if (value == null) return null;
        String v = value.trim();
        if (v.length() <= 4) return "****";
        return "*".repeat(v.length() - 4) + v.substring(v.length() - 4);
    }

    /** Fully redacts a value, keeping only whether it was present. */
    public static String full(Object value) {
        return value == null ? null : "****";
    }

    /** Keeps the first character of a name, e.g. "J***". */
    public static String name(String value) {
        if (value == null) return null;
        String v = value.trim();
        if (v.isEmpty()) return v;
        return v.charAt(0) + "***";
    }
}
