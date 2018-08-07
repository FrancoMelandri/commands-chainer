import org.junit.Test;

import static org.junit.Assert.*;

public class TypedPropertyTest {

    @Test
    public void shouldAddAndGetObject() {
        Object expected = new Integer(10);
        TypedProperty sut = new TypedProperty();
        sut.put("test", expected);
        Object result = sut.get("test");
        assertEquals(expected, result);
    }
}