import org.apache.jena.base.Sys;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class Mainclass {
    public static void oldmain(String[] args) throws Exception {
      //  try {
            /*File inputFile = new File("C:\\Users\\Ahmed\\Desktop\\dataset\\moyen.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
            org.jdom2.Document document = saxBuilder.build(inputFile);
            System.out.println("Root element :" + document.getRootElement().getName());
            org.jdom2.Element classElement = document.getRootElement();

            List<org.jdom2.Element> companyList = classElement.getChildren();
            System.out.println("----------------------------");
            HashSet<String> x = new HashSet<String>();*/
          /*  for (int temp = 0; temp < companyList.size(); temp++) {
                org.jdom2.Element company = companyList.get(temp);
              /*  System.out.println("\nCurrent Element :"
                        + company.getName());
                System.out.println("abstract  : "
                        + company.getChild("abstract").getText());
                System.out.println("products  : "
                        + company.getChild("products").getText());*/
                 // x.add(company.getChild("industries").getText()) ;
          //}
         // StringBuilder y = new StringBuilder("String[] domainesneutre = {");*/
       /*for(String s : x){
                y.append("\""+ s.substring(0,s.lastIndexOf(",")) +"\" ,");
              //  System.out.println(s.substring(0,s.lastIndexOf(",")));
        }
        y.append("};");
        System.out.print(y);*/
       /*} catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
     /*   HashMap<String,ArrayList<Integer>> invertedIndex = new HashMap<String, ArrayList<Integer>>();
        DictionaryGenerator dic = new DictionaryGenerator();
        String filePath = "C:\\Users\\Ahmed\\Desktop\\filstatfaible1.txt";
        HashMap<String,Integer> wf1 = wordfrequency(filePath,dic.getWordsofclassfaible());
       for(String key : wf1.keySet() ) {
        ArrayList<Integer> list = new ArrayList<Integer>() ;
        list.add(0,wf1.get(key));
        list.add(1,0);
        list.add(2,0);
        invertedIndex.put(key,list);
        }
        String filePath1 = "C:\\Users\\Ahmed\\Desktop\\filstatfort1.txt";
        HashMap<String,Integer> wf2 = wordfrequency(filePath1,dic.getWordsofclassfort());
        for(String key :  wf2.keySet() ) {
      if(invertedIndex.containsKey(key)){
          invertedIndex.get(key).add(2,wf2.get(key));
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
        HashMap<String,Integer> wf3 = wordfrequency(filePath2,dic.getWordsofclassmoyen());
        for(String key :  wf3.keySet() ) {
            if(invertedIndex.containsKey(key)){
                invertedIndex.get(key).add(1,wf3.get(key));
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

        }*/

    }
    public static void main(String[] args) throws TransformerException, ParserConfigurationException {


       //DictionaryGenerator.createDataset();
       DictionaryGenerator dic = new DictionaryGenerator();
        TreeMap<String,ArrayList<Integer>> iid = dic.invertedIndex();
        NaivesBayesClassifier nb = new NaivesBayesClassifier();
        DocumentModel documenttest = new DocumentModel();
        documenttest.setContent(Queryexec.getcompanybyname("Toyota"));
        BigDecimal pb1 = nb.companyProbabilityOfaClass(iid, documenttest.normlizedContent(), 1, dic.getWordsofclassmoyen());
        BigDecimal pb0= nb.companyProbabilityOfaClass(iid, documenttest.normlizedContent(), 0, dic.getWordsofclassfaible());
        BigDecimal pb2 = nb.companyProbabilityOfaClass(iid, documenttest.normlizedContent(), 2, dic.getWordsofclassfort());
        System.out.println(pb2.compareTo(pb1));
        System.out.println(pb2.compareTo(pb0));

    }

/*public static HashMap<String, Integer> wordfrequency(String pathOfoutput, List<String> wordsOfClass){
  /*  Writer bufferedWriter = null;
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
}*/

   /* public static String getlocalname(String uri) {
        int x = 0;
        for (int i = uri.length(); i > 0; i--) {
            if ('/' == uri.charAt(i - 1)) {
                x = i;
                break;
            }
        }
        return uri.substring(x);
    }*/


}