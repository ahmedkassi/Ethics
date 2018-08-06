import java.util.*;

public class NaivesBayesClassifier {


    public String companyClass(TreeMap invertedindex,DocumentModel document){
        return null ;
    }
    public Double companyProbabilityOfaClass(TreeMap<String,ArrayList<Integer>> invertedindex, List<String> bagofwords , int moralclass , List<String> moralclasswords ){
        Double result =0.0;
        HashMap<String,Integer> moralclassuniquewords = new HashMap<String, Integer>() ;
        HashSet<String> words = new HashSet<String>(moralclasswords);
        for (String word : words) {
            int occurences = Collections.frequency(moralclasswords,word);
            moralclassuniquewords.put(word,occurences);
        }
        int dominator = moralclassuniquewords.size()+ moralclassuniquewords.values().stream().mapToInt(Integer::intValue).sum();
        int nominator = 1;
        for(String ti : bagofwords){

        if(invertedindex.containsKey(ti)){

            nominator +=  invertedindex.get(ti).get(moralclass);
        }
        }
        return null;

    }
}
