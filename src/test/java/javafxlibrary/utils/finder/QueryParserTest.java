package javafxlibrary.utils.finder;

import org.junit.Assert;
import org.junit.Test;

public class QueryParserTest {

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
}
