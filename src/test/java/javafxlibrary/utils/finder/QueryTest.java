package javafxlibrary.utils.finder;

import javafxlibrary.exceptions.JavaFXLibraryQueryException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class QueryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void validQuery_withIndex() {
        Query query = new Query("css=VBox[1]");
        Assert.assertEquals("VBox", query.getQuery());
        Assert.assertEquals(FindPrefix.CSS, query.getPrefix());
        Assert.assertTrue(query.containsIndex());
        Assert.assertEquals(0, query.getIndex());
    }

    @Test
    public void validQuery_noIndex() {
        Query query = new Query("class=javafx.scene.layout.VBox");
        Assert.assertEquals("javafx.scene.layout.VBox", query.getQuery());
        Assert.assertEquals(FindPrefix.CLASS, query.getPrefix());
        Assert.assertFalse(query.containsIndex());
        Assert.assertEquals(-1, query.getIndex());
    }

    @Test
    public void invalidQueryIndex() {
        thrown.expect(JavaFXLibraryQueryException.class);
        thrown.expectMessage("Invalid query \"css=VBox[0]\": Minimum index value is 1!");
        new Query("css=VBox[0]");
    }
}
