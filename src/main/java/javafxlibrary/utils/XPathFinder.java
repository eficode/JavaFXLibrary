package javafxlibrary.utils;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XPathFinder {

    private StringBuilder sb;
    private List<Node> nodes;
    private boolean nodeLogging;

    public XPathFinder() {
        this.sb = new StringBuilder();
        this.nodes = new ArrayList<>();
        this.nodeLogging = true;
    }

    public void setNodeLogging(boolean nodeLogging) {
        this.nodeLogging = nodeLogging;
    }

    public Node find(String xpathQuery, Parent root) {

        HelperFunctions.robotLog("DEBUG", "Executing XPathFinder.find using query: " + xpathQuery + " and root: " + root);
        String fxmlString = this.getFxml(root);

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xml = documentBuilder.parse(new ByteArrayInputStream(fxmlString.getBytes()));

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            XPathExpression expression = xpath.compile(xpathQuery);

            // TODO: Add support for returning multiple nodes
            // TODO: Fix getting nth element with XPath
            // NodeList nodes = (NodeList) expression.evaluate(xml, XPathConstants.NODESET);
            org.w3c.dom.Node node = (org.w3c.dom.Node) expression.evaluate(xml, XPathConstants.NODE);

            //NamedNodeMap attributes = nodes.item(0).getAttributes();
            NamedNodeMap attributes = node.getAttributes();

            int nodeIndex = Integer.parseInt(attributes.getNamedItem("jfxlibid").getNodeValue());
            return nodes.get(nodeIndex);
        }
        catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            throw new JavaFXLibraryNonFatalException("Could not parse XPathExpression! " + e.getCause().getMessage());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getFxml(Parent root) {
        addTag(root, 0);
        return sb.toString();
    }

    private void parseChildren(Parent parent, int indentation) {
        for (Node node : parent.getChildrenUnmodifiable())
            addTag(node, indentation);
    }

    private void indentRow(int indentation) {
        sb.append(new String(new char[indentation * 4]).replace("\0", " "));
    }

    private void addTag(Node node, int indentation) {
        indentRow(indentation);
        sb.append("<");
        sb.append(node.getTypeSelector());

        // Should this feature just have its own method?
        // getFXML can be used for printing the UI structure to test logs also. If so, there is no need to track the nodes.
        if (this.nodeLogging) {
            nodes.add(node);
            sb.append(" jfxlibid=\"");
            sb.append(Integer.toString(nodes.size() - 1));
            sb.append("\"");
        }

        parseAttributes(node);

        if (node instanceof Parent) {
            Parent subParent = (Parent) node;
            // If subParent has children: close the opening tag, parse children, add closing tag
            if (!subParent.getChildrenUnmodifiable().isEmpty()) {
                sb.append(">\n");
                parseChildren(subParent, indentation + 1);
                indentRow(indentation);
                sb.append("</");
                sb.append(node.getTypeSelector());
                sb.append(">\n");
            } else {
                // If subParent has no children: use self-closing tag
                sb.append(" />\n");
            }
        } else {
            // If node is not an instance of Parent: use self-closing tag
            sb.append(" />\n");
        }
    }

    private void parseAttributes(Node node) {
        String nodeString = node.toString();

        if (!nodeString.contains("["))
            return;

        String attributes = " " + nodeString.substring(nodeString.indexOf('[') + 1, nodeString.length() - 1);
        String[] attributeArray = attributes.split(",");
        StringBuilder attributeBuilder = new StringBuilder();
        boolean unsupported = false;

        for (String att : attributeArray) {
            att = att.replace(",", "");

            if (!unsupported) {
                if (!att.contains("[")) {
                    if (!att.contains("=\"")) {
                        attributeBuilder.append(att.replace("=", "=\""));
                        attributeBuilder.append("\"");
                    } else {
                        attributeBuilder.append(att);
                    }
                } else {
                    /*  Attribute has inner []-block which are not supported yet, e.g.
                        font=Font[name=System Regular, family=System, style=Regular, size=10.0]  */
                    unsupported = true;
                }
            } else {
                // Attributes inner []-block ends, continue parsing
                if (att.contains("]"))
                    unsupported = false;
            }
        }

        sb.append(attributeBuilder.toString());
    }
}
