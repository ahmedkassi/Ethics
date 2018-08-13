import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
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
import java.io.*;
import java.util.*;
public class DictionaryGenerator {
    List<String> wordsofclassfaible;
    List<String> wordsofclassmoyen;
    List<String> wordsofclassfort;
    public static String PATH = "C:\\Users\\Ahmed\\Desktop\\dataset";
DictionaryGenerator(){
this.wordsofclassfaible = getNormalizedWordsOfClass(PATH,MoralDegree.FAIBLE.toString());
    this.wordsofclassfort = getNormalizedWordsOfClass(PATH,MoralDegree.FORT.toString());
   this.wordsofclassmoyen = getNormalizedWordsOfClass(PATH,MoralDegree.MOYEN.toString());
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
        return normalize2(new String(companytextbrut));
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
    public static List<String> normalize2(String text) {
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        List<String> lemmas = new ArrayList();
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase();
                if(word.matches("[a-zA-Z_]+")&& word.length()>2)
                { lemmas.add(word); }
            }
        }
        return lemmas;
    }
    public static void createDataset() throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory dcfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dcfactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("companies");
        document.appendChild(root);
       /* String[] domaines = {"Health_care", "Electric_vehicle", "Artificial_intelligence", "Education", "Solar_energy", "Pharmaceutical_company", "Health_Care", "Renewable_energies"
                , "Renewable_Fuels", "Renewable_Products", "Renewable_Chemicals", "Green_technology", "Green_Energy", "Environmental_protection", "Animal_Welfare", "Animal_Safety", "Emergency_services", "Human_rights", "Climate_control", "Health_care_in_the_United_States", "Energy_conservation", "Islamic_Finance", "Islamic_Development_Bank"
                , "Islamic_banking", "Islamic_Banking_Software", "Sustainable_development", "Sustainable_Agriculture", "Cancer_research", "Antivirus_software", "Waste_Management", "Social_action", "Student"
                , "Green_Building", "Green_enterprise", "Green_technology", "Green_economy", "Electric_bus"};*/

     /*String[] domaines = {"Alcoholic_beverage","Tobacco_industries","Petroleum_industries","Pulp_and_Paper","Heavy_equipment_(construction)","Heavy_equipment","Paper","Mining","Nightclub","Oil_and_Gasoline","Tobacco","Coal_Mining","Sex_industries","Internal_combustion_engine","Defense_(military)","Paper_industries","Petroleum"
                ,"Oil_and_gas_industries","sex","car","Pornography","Porn"};*/
       String[] domaines = {"Accounting","Marketing" ,"Electronic","Rental" ,"Computer" ,"Bank" ,"Investment_Banking"
               ,"Software" ,"Heavy_equipment_(construction)" ,"Financial_Services" ,"Mobile","game" ,"Wireless" ,"Architectural" ,"Journalism"
               ,"Semiconductor" ,"rail" ,"Civil_engineering" ,"Home_improvement"
               ,"Information_Technology" ,"Financial_services"  ,"ECommerce" ,"Computer_networking" ,"Software_industry" ,"Tourism" ,"Software_services"
               ,"Coffeehouse","International_Banking" ,"Architectural_Lighting","Computer"  ,"Telecommunication" ,"Business-to-business","Public_relations"
               ,"Banking" ,"Investment_management"  ,"Coffee_house" ,"Financial_Services" ,"Software" ,"Software",
               "Digital_distribution","computer",
               "software","Music","Retail","Video_game","Entertainment","Hardware_store","Food","Integrated_circuit_design"
               ,"Commercial_broadcasting"
       };

        for (int j = 0; j < domaines.length; j++) {
            ResultSet rs = Queryexec.getcompanies(domaines[j]);
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
                    produits.append(getlocalname(prod.nextToken().toString()) + ",");/*System.out.println("prod " + i + ":" + getlocalname(prod.nextToken().toString()));*/
                }
                products.appendChild(document.createTextNode(new String(produits)));
                StringBuilder industries = new StringBuilder();
                StringTokenizer indus = new StringTokenizer(s.get("?industries").toString(), ";");
                for (int i = 1; indus.hasMoreTokens(); i++) {
                    industries.append(getlocalname(indus.nextToken().toString()) + ",");/*System.out.println("industry "+i+":"+getlocalname(indus.nextToken().toString()));*/
                }
                ind.appendChild(document.createTextNode(new String(industries)));
            }
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("C:\\Users\\Ahmed\\Desktop\\dataset2\\moyen.xml"));
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

    public  TreeMap<String,ArrayList<Integer>> invertedIndex(){
        TreeMap<String,ArrayList<Integer>> invertedIndex = new TreeMap<String, ArrayList<Integer>>();
     //   DictionaryGenerator dic = new DictionaryGenerator();
        String filePath = "C:\\Users\\Ahmed\\Desktop\\filstatfaible1.txt";
        HashMap<String,Integer> wf1 = wordfrequency(filePath,this.getWordsofclassfaible());
        for(String key : wf1.keySet() ) {
            ArrayList<Integer> list = new ArrayList<Integer>() ;
            list.add(0,wf1.get(key));
            list.add(1,0);
            list.add(2,0);
            invertedIndex.put(key,list);
        }
        String filePath1 = "C:\\Users\\Ahmed\\Desktop\\filstatfort1.txt";
        HashMap<String,Integer> wf2 = wordfrequency(filePath1,this.getWordsofclassfort());
        for(String key :  wf2.keySet() ) {
            if(invertedIndex.containsKey(key)){
                int previousvalue =invertedIndex.get(key).get(2);
                int updatedvalue = wf2.get(key);
                invertedIndex.get(key).set(2,previousvalue+updatedvalue);

            }
            else{
                ArrayList<Integer> list = new ArrayList<Integer>() ;
                list.add(0,0);
                list.add(1,0);
                list.add(2,wf2.get(key));
                invertedIndex.put(key,list);
            }
        }
        String filePath2 = "C:\\Users\\Ahmed\\Desktop\\filstatmoyen1.txt";
        HashMap<String,Integer> wf3 = wordfrequency(filePath2,this.getWordsofclassmoyen());
        for(String key :  wf3.keySet() ) {
            if(invertedIndex.containsKey(key)){
               int updatedvalue = invertedIndex.get(key).get(1)+wf3.get(key);
                invertedIndex.get(key).set(1,updatedvalue);
            }
            else{
                ArrayList<Integer> list = new ArrayList<Integer>() ;
                list.add(0,0);
                list.add(1,wf3.get(key));
                list.add(2,0);
                invertedIndex.put(key,list);
            }
        }

        Writer bufferedWriter = null;
        String invertfile ="C:\\Users\\Ahmed\\Desktop\\invert1.txt" ;
        try {
            Writer fileWriter = new FileWriter(invertfile);
            bufferedWriter = new BufferedWriter(fileWriter);
            //  HashSet<String> words = new HashSet<String>(dic.getWordsofclassfaible());
            System.out.println(invertedIndex.size());
            for (String word : invertedIndex.keySet()) {
                ArrayList<Integer> x = invertedIndex.get(word);
                if(word.length()>2)   bufferedWriter.write(word +" "+ x.get(0)+" "+ x.get(1)+" "+ x.get(2)+"\n");
            }
        } catch (IOException e) {
            System.out.println("Problem occurs when creating file " + filePath);
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println("Problem occurs when closing file !");
                    e.printStackTrace();
                }
            }

        }
return invertedIndex;
    }
    public static HashMap<String, Integer> wordfrequency(String pathOfoutput, List<String> wordsOfClass){
        Writer bufferedWriter = null;
        HashMap<String,Integer> result = new HashMap<String, Integer>() ;
        try {
            Writer fileWriter = new FileWriter(pathOfoutput);
            bufferedWriter = new BufferedWriter(fileWriter);
            HashSet<String> words = new HashSet<String>(wordsOfClass);

            for (String word : words) {
                int occurences = Collections.frequency(wordsOfClass,word);
                if(word.length()>2) bufferedWriter.write(word +" "+occurences +"\n") ;
                result.put(word,occurences);
            }

        } catch (IOException e) {
            System.out.println("Problem occurs when creating file " + pathOfoutput);
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.out.println("Problem occurs when closing file !");
                    e.printStackTrace();
                }
            }

        }
        return result;
    }
    public static String getlocalname(String uri) {
        int x = 0;
        for (int i = uri.length(); i > 0; i--) {
            if ('/' == uri.charAt(i - 1)) {
                x = i;
                break;
            }
        }
        return uri.substring(x);
    }
}







