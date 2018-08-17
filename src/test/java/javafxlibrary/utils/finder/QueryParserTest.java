package javafxlibrary.utils.finder;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class QueryParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void startsWithPrefix_AcceptedValues() {
        Assert.assertTrue(QueryParser.startsWithPrefix("css=.Vbox .button"));
        Assert.assertTrue(QueryParser.startsWithPrefix("id=testNode"));
        Assert.assertTrue(QueryParser.startsWithPrefix("class=java.lang.String"));
        Assert.assertTrue(QueryParser.startsWithPrefix("xpath=//Rectangle"));
        Assert.assertTrue(QueryParser.startsWithPrefix("pseudo=hover"));
        Assert.assertTrue(QueryParser.startsWithPrefix("text=\"Text\""));
    }

    @Test
    public void startsWithPrefix_InvalidValue() {
        Assert.assertFalse(QueryParser.startsWithPrefix("invalid=should be false"));
    }

    @Test
    public void getIndividualQueries_ContainsSpaces() {
        String[] result = QueryParser.getIndividualQueries("xpath=SomeNode[@text=\"test text\"] text=\"text with spaces\" id=sub");
        String[] target = { "xpath=SomeNode[@text=\"test text\"]", "text=\"text with spaces\"", "id=sub" };
        Assert.assertArrayEquals(target, result);
    }

    @Test
    public void getIndividualQueries_ContainsQuotes() {
        String[] result = QueryParser.getIndividualQueries("text=\"Teemu \\\"The Finnish Flash\\\" Selanne\"");
        String[] target = { "text=\"Teemu \"The Finnish Flash\" Selanne\"" };
        Assert.assertArrayEquals(target, result);
    }

    @Test
    public void getPrefix_AcceptedValues() {
        Assert.assertEquals(FindPrefix.ID, QueryParser.getPrefix("id=nodeId"));
        Assert.assertEquals(FindPrefix.CLASS, QueryParser.getPrefix("class=java.lang.String"));
        Assert.assertEquals(FindPrefix.CSS, QueryParser.getPrefix("css=.vBox .hBox"));
        Assert.assertEquals(FindPrefix.XPATH, QueryParser.getPrefix("xpath=//Rectangle[@id=\"lime\"]"));
        Assert.assertEquals(FindPrefix.PSEUDO, QueryParser.getPrefix("pseudo=hover"));
        Assert.assertEquals(FindPrefix.TEXT, QueryParser.getPrefix("text=\"Text\""));
    }

    @Test
    public void getPrefix_NoEquals() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query \"noEquals\" does not contain any supported prefix");
        QueryParser.getPrefix("noEquals");
    }

    @Test
    public void getPrefix_InvalidValue() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Query \"notaprefix=someValue\" does not contain any supported prefix");
        QueryParser.getPrefix("notaprefix=someValue");
    }
}
