import edu.stanford.nlp.simple.Sentence;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class DictionaryGenerator {
    List<String> wordsofclassfaible;
    List<String> wordsofclassmoyen;
    List<String> wordsofclassfort;
    public static String PATH = "C:\\Users\\Ahmed\\Desktop\\dataset";
DictionaryGenerator(){
  //  this.wordsofclassfaible = getNormalizedWordsOfClass(PATH,MoralDegree.FAIBLE.toString());
    //this.wordsofclassfort = getNormalizedWordsOfClass(PATH,MoralDegree.FORT.toString());
    //this.wordsofclassmoyen = getNormalizedWordsOfClass(PATH,MoralDegree.MOYEN.toString());

}
    public List<String> getNormalizedWordsOfClass(String path2dataset,String moralcalss) {
        StringBuilder companytextbrut = new StringBuilder();
        try {
            File inputFile = new File(path2dataset+"\\"+moralcalss+".xml");

            SAXBuilder saxBuilder = new SAXBuilder();
            org.jdom2.Document document = saxBuilder.build(inputFile);
            org.jdom2.Element classElement = document.getRootElement();
            List<Element> companyList = classElement.getChildren();
            for (int temp = 0; temp < companyList.size(); temp++) {
                org.jdom2.Element company = companyList.get(temp);
                companytextbrut.append(company.getName());
                companytextbrut.append( company.getChild("abstract").getText());
                companytextbrut.append( company.getChild("products").getText());
                companytextbrut.append( company.getChild("industries").getText());
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return normalize(new String(companytextbrut));
    }

    public List<String> normalize(String text) {
        String y = text.replaceAll("[^a-zA-Z ' \\. ]", " ");
        Sentence sent = new Sentence(y.toLowerCase());
        List<String> words = new ArrayList<String>();
        for (String x : sent.lemmas()) {
            words.add(x);
        }
        return words;
    }


    public void createDataset(String path) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory dcfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dcfactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("companies");
        document.appendChild(root);
        String[] domainesfort = {"Health_care", "Electric_vehicle", "Artificial_intelligence", "Education", "Solar_energy", "Pharmaceutical_company", "Health_Care", "Renewable_energies"
                , "Renewable_Fuels", "Renewable_Products", "Renewable_Chemicals", "Green_technology", "Green_Energy", "Environmental_protection", "Animal_Welfare", "Animal_Safety", "Emergency_services", "Human_rights", "Climate_control", "Health_care_in_the_United_States", "Energy_conservation", "Islamic_Finance", "Islamic_Development_Bank"
                , "Islamic_banking", "Islamic_Banking_Software", "Sustainable_development", "Sustainable_Agriculture", "Cancer_research", "Antivirus_software", "Waste_Management", "Social_action", "Student"
                , "Green_Building", "Green_enterprise", "Green_technology", "Green_economy", "Electric_bus"};
        String[] domainesfaibles = {"Alcoholic_beverage","Tobacco_industries","Petroleum_industries","Pulp_and_Paper","Heavy_equipment_(construction)","Heavy_equipment","Paper","Mining","Nightclub","Oil_and_Gasoline","Tobacco","Coal_Mining","Sex_industries","Internal_combustion_engine","Defense_(military)","Paper_industries","Petroleum"
                ,"Oil_and_gas_industries","sex"};
        String[] domainesmoyen ={"Accounting","Wireless_networking","Books","Marketing","EBooks","Search_Engine_Marketing","Architectural_glass",
                "Social_Media_Marketing","Computer_hardware","Bank","Investment_Banking","Telecommunications","IT_Engineering","Consultant","Wireless_Development","Mobile_Development","testing",
                "Software_development","Mobile_games","Management_consulting","Architectural","Watchmaking",
                "Journalism","Coffee","internet","Market_Research","Civil_engineering","Accounting_systems","Marketing_Research","Information_security","ECommerce","","","","","","",
                ""
                ,"","","","","","","","","","","","","","","","","","","","","",""};

        for (int j = 0; j < domainesfaibles.length; j++) {
            ResultSet rs = Queryexec.getcompanies(domainesfaibles[j]);
            while (rs.hasNext()) {
                QuerySolution s = rs.nextSolution();
                org.w3c.dom.Element company = document.createElement("company");
                root.appendChild(company);
                Attr name = document.createAttribute("name");
                name.setValue(s.get("?lbl").toString());
                company.setAttributeNode(name);
                // System.out.println(s.get("?lbl"));
                org.w3c.dom.Element abs = document.createElement("abstract");
                company.appendChild(abs);
                abs.appendChild(document.createTextNode(s.get("?abs").toString()));
                //  System.out.println(s.get("?abs"));
                org.w3c.dom.Element products = document.createElement("products");
                company.appendChild(products);
                org.w3c.dom.Element ind = document.createElement("industries");
                company.appendChild(ind);
                StringTokenizer prod = new StringTokenizer(s.get("?prod").toString(), ";");
                StringBuilder produits = new StringBuilder();
                for (int i = 1; prod.hasMoreTokens(); i++) {
                    produits.append(Mainclass.getlocalname(prod.nextToken().toString()) + ",");/*System.out.println("prod " + i + ":" + getlocalname(prod.nextToken().toString()));*/
                }
                products.appendChild(document.createTextNode(new String(produits)));
                StringBuilder industries = new StringBuilder();
                StringTokenizer indus = new StringTokenizer(s.get("?industries").toString(), ";");
                for (int i = 1; indus.hasMoreTokens(); i++) {
                    industries.append(Mainclass.getlocalname(indus.nextToken().toString()) + ",");/*System.out.println("industry "+i+":"+getlocalname(indus.nextToken().toString()));*/
                }
                ind.appendChild(document.createTextNode(new String(industries)));
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("C:\\Users\\Ahmed\\Desktop\\dataset2\\faible.xml"));
        transformer.transform(domSource, streamResult);
        System.out.println("Done creating XML File");
    }





    public void setWordsofclassfaible() {
        this.wordsofclassfaible = getNormalizedWordsOfClass(PATH,MoralDegree.FAIBLE.toString());
    }

    public void setWordsofclassfort() {
        this.wordsofclassfort = getNormalizedWordsOfClass(PATH,MoralDegree.FORT.toString());
    }

    public void setWordsofclassmoyen() {
        this.wordsofclassmoyen = getNormalizedWordsOfClass(PATH,MoralDegree.MOYEN.toString());
    }

    public List<String> getWordsofclassfaible() {
        return wordsofclassfaible;
    }

    public List<String> getWordsofclassfort() {
        return wordsofclassfort;
    }

    public List<String> getWordsofclassmoyen() {
        return wordsofclassmoyen;
    }
}







