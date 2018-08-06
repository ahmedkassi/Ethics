import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class DocumentModel {

    String Content;


    public String getContent() {
        return Content;
    }

    public void setContent(ResultSet resultSet) {
        QuerySolution s = resultSet.next();
        StringBuilder result= new StringBuilder();
       result.append(s.get("?abs").toString());
        StringTokenizer prod = new StringTokenizer(s.get("?prod").toString(), ";");
        StringBuilder produits = new StringBuilder();
        if(prod!=null){
        for (int i = 1; prod.hasMoreTokens(); i++) {
            produits.append(DictionaryGenerator.getlocalname(prod.nextToken().toString()) + ",");/*System.out.println("prod " + i + ":" + getlocalname(prod.nextToken().toString()));*/
        }
        }
        result.append(produits);
        StringBuilder industries = new StringBuilder();
        StringTokenizer indus = new StringTokenizer(s.get("?industries").toString(), ";");
        if(indus!=null){
        for (int i = 1; indus.hasMoreTokens(); i++) {
            industries.append(DictionaryGenerator.getlocalname(indus.nextToken().toString()) + ",");
        }}
        result.append(industries);
        this.Content = new String(result);
    }
    public List<String> normlizedContent (String content){
    return DictionaryGenerator.normalize2(content);
    }
}

