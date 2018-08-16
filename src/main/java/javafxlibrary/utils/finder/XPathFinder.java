package javafxlibrary.utils.finder;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafxlibrary.exceptions.JavaFXLibraryNonFatalException;
import javafxlibrary.utils.RobotLog;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        RobotLog.debug("Executing XPathFinder.find using query: " + xpathQuery + " and root: " + root);
        String fxmlString = this.getFxml(root);
        Document xml = getXmlDocument(fxmlString);
        XPathExpression expression = getXPathExpression(xpathQuery);

        try {
            org.w3c.dom.Node node = (org.w3c.dom.Node) expression.evaluate(xml, XPathConstants.NODE);
            NamedNodeMap attributes = node.getAttributes();
            int nodeIndex = Integer.parseInt(attributes.getNamedItem("jfxlibid").getNodeValue());
            return nodes.get(nodeIndex);
        } catch (XPathExpressionException e) {
            throw new JavaFXLibraryNonFatalException("Could not parse XPathExpression! " + e.getCause().getMessage());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Set<Node> findAll(String xpathQuery, Parent root) {
        RobotLog.debug("Executing XPathFinder.findAll using query: " + xpathQuery + " and root: " + root);
        String fxmlString = this.getFxml(root);
        Document xml = getXmlDocument(fxmlString);
        XPathExpression expression = getXPathExpression(xpathQuery);

        try {
            NodeList xmlNodes = (NodeList) expression.evaluate(xml, XPathConstants.NODESET);
            Set<Node> foundNodes = new LinkedHashSet<>();

            // NodeList items must be accessed using index
            for (int i = 0; i < xmlNodes.getLength(); i++) {
                NamedNodeMap attributes = xmlNodes.item(i).getAttributes();
                int nodeIndex = Integer.parseInt(attributes.getNamedItem("jfxlibid").getNodeValue());
                foundNodes.add(nodes.get(nodeIndex));
            }

            return foundNodes;
        } catch (XPathExpressionException e) {
            throw new JavaFXLibraryNonFatalException("Could not parse XPathExpression! " + e.getCause().getMessage());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getFxml(Parent root) {
        addTag(root, 0);
        return sb.toString();
    }

    private Document getXmlDocument(String fxmlString) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(new ByteArrayInputStream(fxmlString.getBytes()));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new JavaFXLibraryNonFatalException("Unable to generate FXML for XPath lookup: " + e.getCause().getMessage());
        }
    }

    private XPathExpression getXPathExpression(String xpathQuery) {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            return xpath.compile(xpathQuery);
        } catch (XPathExpressionException e) {
            throw new JavaFXLibraryNonFatalException("Could not parse XPathExpression! " + e.getCause().getMessage());
        }
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
        sb.append(getSelector(node));

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
                sb.append(getSelector(node));
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

        // TODO: Nodes with LabeledText containing ']'-characters will have an effect on the last attribute, fix
        String attributes = " " + nodeString.substring(nodeString.indexOf('[') + 1, nodeString.lastIndexOf(']'));
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

    // Inner classes have a dollar sign in their selector, which is not allowed in XML and has to be replaced
    private String getSelector(Node node) {
        // TODO: Are there more possible characters for type selectors that require replacing?
        return node.getTypeSelector().replaceAll("\\$", "");
    }
}
