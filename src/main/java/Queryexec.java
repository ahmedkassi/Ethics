import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class Queryexec {

    public static ResultSet getcompanies(String sect){
        String service = "http://dbpedia.org/sparql";
        String test ="select  ?abs   group_concat(DISTINCT ?indus ; separator = \" ; \") as ?industries (sql:GROUP_CONCAT_DISTINCT(?products, ';') as ?prod)";
        String query = "prefix dbpedia-owl: <http://dbpedia.org/ontology/>\n" +
                "select ?lbl , ?abs , (sql:GROUP_CONCAT_DISTINCT(?indus , ';') as ?industries), (sql:GROUP_CONCAT_DISTINCT(?products, ';') as ?prod)\n" +
                "where {\n" +
                " [] a dbpedia-owl:Company;\n" +
                "   dbpedia-owl:abstract ?abs;\n" +
                "  rdfs:label ?lbl;\n" +
                "  dbpedia-owl:industry ?indus;\n" +
                "  dbpedia-owl:product ?products.\n" +
                "   FILTER (lang(?abs) = 'en').\n" +
                " FILTER (lang(?lbl) = 'en').\n" +
                "    FILTER regex((?indus) ,\""+sect+"+?\").\n" +
                "}\n" +
                "LIMIT 40 ";

        QueryEngineHTTP qe = new QueryEngineHTTP(service, query);
         ResultSet rs = qe.execSelect();
        return rs;
    }


    public static ResultSet getcompanybyname(String company){
        String service = "http://dbpedia.org/sparql";
        String test ="select  ?abs   group_concat(DISTINCT ?indus ; separator = \" ; \") as ?industries (sql:GROUP_CONCAT_DISTINCT(?products, ';') as ?prod)";
        String query = "prefix dbpedia-owl: <http://dbpedia.org/ontology/>\n" +
                "select  ?abs , (sql:GROUP_CONCAT_DISTINCT(?indus , ';') as ?industries), (sql:GROUP_CONCAT_DISTINCT(?products, ';') as ?prod)\n" +
                "where {\n" +
                " [] a dbpedia-owl:Company;\n" +
                "   dbpedia-owl:abstract ?abs;\n" +
                "  rdfs:label \""+company+"\"@en;\n" +
                "  dbpedia-owl:industry ?indus;\n" +
                " dbpedia-owl:product ?products.\n" +
                "   FILTER (lang(?abs) = 'en').\n" +
                "}\n";
        QueryEngineHTTP qe = new QueryEngineHTTP(service, query);
        ResultSet rs = qe.execSelect();
        return rs;

    }
}

