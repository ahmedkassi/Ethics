import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Attr;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class NaivesBayesClassifierTest {

    Map<String ,String> validationset;
    NaivesBayesClassifierTest() throws TransformerConfigurationException {
        this.validationset= new HashMap<>();
        try {
            File inputFile = new File("C:\\Users\\Ahmed\\Desktop\\dataset2\\testdataset.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            org.jdom2.Document document = saxBuilder.build(inputFile);
            org.jdom2.Element classElement = document.getRootElement();
            List<Element> companyList = classElement.getChildren();
            for (int temp = 0; temp < companyList.size(); temp++) {
                org.jdom2.Element company = companyList.get(temp);

                validationset.put(company.getAttribute("name").getValue().replaceAll("@en",""),company.getChild("moraldegree").getText());
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public void createValidationSet() throws ParserConfigurationException, TransformerException {
    DocumentBuilderFactory dcfactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = dcfactory.newDocumentBuilder();
    org.w3c.dom.Document document = documentBuilder.newDocument();
    org.w3c.dom.Element root = document.createElement("companies");
    document.appendChild(root);
        ResultSet rs = Queryexec.getcompaniesTest();
        while (rs.hasNext()) {
            QuerySolution s = rs.nextSolution();
            org.w3c.dom.Element company = document.createElement("company");
            root.appendChild(company);
            Attr name = document.createAttribute("name");
            name.setValue(s.get("?lbl").toString());
            company.setAttributeNode(name);
            org.w3c.dom.Element abs = document.createElement("moraldegree");
            company.appendChild(abs);
            org.w3c.dom.Element products = document.createElement("products");
            company.appendChild(products);
            org.w3c.dom.Element moraldegree = document.createElement("moraldegree");
            moraldegree.setTextContent("faible");
            company.appendChild(moraldegree);
            org.w3c.dom.Element ind = document.createElement("industries");
            company.appendChild(ind);
            StringTokenizer prod = new StringTokenizer(s.get("?prod").toString(), ";");
            StringBuilder produits = new StringBuilder();
            for (int i = 1; prod.hasMoreTokens(); i++) {
                produits.append(DictionaryGenerator.getlocalname(prod.nextToken().toString()) + ",");
            }
            products.appendChild(document.createTextNode(new String(produits)));
            StringBuilder industries = new StringBuilder();
            StringTokenizer indus = new StringTokenizer(s.get("?industries").toString(), ";");
            for (int i = 1; indus.hasMoreTokens(); i++) {
                industries.append(DictionaryGenerator.getlocalname(indus.nextToken().toString()) + ",");/*System.out.println("industry "+i+":"+getlocalname(indus.nextToken().toString()));*/
            }
            ind.appendChild(document.createTextNode(new String(industries)));
        }
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File("C:\\Users\\Ahmed\\Desktop\\dataset2\\testdataset.xml"));
    transformer.transform(domSource, streamResult);
    System.out.println("Done creating XML File");

}

    public Map<String, String> getValidationset() {
        return validationset;
    }
}
