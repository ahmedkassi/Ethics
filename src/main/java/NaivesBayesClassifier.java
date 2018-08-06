import java.math.BigDecimal;
import java.util.*;

public class NaivesBayesClassifier {


    public String companyClass(TreeMap invertedindex,DocumentModel document){
        return null ;
    }
    public BigDecimal companyProbabilityOfaClass(TreeMap<String,ArrayList<Integer>> invertedindex, List<String> bagofwords , int moralclass , List<String> moralclasswords ){
        BigDecimal  result = new BigDecimal(1.0);
        HashMap<String,Integer> moralclassuniquewords = new HashMap<String, Integer>() ;
        HashSet<String> words = new HashSet<String>(moralclasswords);
        List<BigDecimal> listOfprobab = new ArrayList<>() ;
        for (String word : words) {
            int occurences = Collections.frequency(moralclasswords,word);
            moralclassuniquewords.put(word,occurences);
        }
        int dominator = moralclassuniquewords.size()+ moralclassuniquewords.values().stream().mapToInt(Integer::intValue).sum();

        for(String ti : bagofwords){
            Double nominator =1.0;
        if(invertedindex.containsKey(ti)){
            nominator += invertedindex.get(ti).get(moralclass);
        }

            listOfprobab.add(new BigDecimal(nominator/dominator));
        }
      return null;

    }
}
