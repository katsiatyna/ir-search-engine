package fr.univ_tours.etu.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
//import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DBPedia {
	
	private String query;
	private String keywords;
	private String conditions;
	public static final String SERVICE="http://dbpedia.org/sparql";
	private  Map<Integer, RowPedia> result;
	
	
	public DBPedia(String keywords)
	{
		this.result= new HashMap<Integer, RowPedia>();
		this.conditions="";
		this.keywords=keywords;
		this.buildQuery();
		this.ExecuteQuery();
		
	}
	
	public String getQuery()
	{
		return this.query;
	}
	
	
	public HashMap<Integer,RowPedia> getResults()
	{
		return (HashMap<Integer, RowPedia>) this.result;
	}
	
	private void buildQuery() {
		// TODO Auto-generated method stub
		this.Tokenize(this.keywords);
		
		String header= "PREFIX dbo:<http://dbpedia.org/ontology/> "+
			"PREFIX : <http://dbpedia.org/resource/> "+
			"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX foaf:<http://xmlns.com/foaf/0.1/> "+
			"select distinct ?pid ?link ?label ?thing ?abstract "+
			"where { "+
			"?thing dbo:abstract ?abstract; "+
			"dbo:wikiPageID ?pid; "+
			"rdfs:label ?title. ";
		
		String footer= "FILTER (LANG(?title) = 'en'). "+
				"FILTER (LANG(?abstract) = 'en'). "+
				"BIND (STR(?title)  AS ?label). "+
				"BIND( concat(str(\"http://en.wikipedia.org/?curid=\"),str(?pid)) as ?link). "+
				"OPTIONAL {?thing foaf:name ?author} "+
				"}LIMIT 30";
		
		this.query=header+this.conditions+footer;
		
	}


	private void Tokenize(String query)
	{
		String str = query;
		StringTokenizer defaultTokenizer = new StringTokenizer(str);
		//System.out.println("Total number of tokens found : " + defaultTokenizer.countTokens());
		while (defaultTokenizer.hasMoreTokens())
		{
			this.conditions+="FILTER (REGEX(STR(?abstract), \""+defaultTokenizer.nextToken()+"\", \"i\")). ";
		    
		}

	}
	
	private void ExecuteQuery()
	{
		QueryEngineHTTP qe=(QueryEngineHTTP) QueryExecutionFactory.sparqlService(DBPedia.SERVICE,this.query);
		
		//many timeout issues, to avoid them added big number
		qe.addParam("timeout","500000"); 
		
		ResultSet rs=qe.execSelect();
		
		while (rs.hasNext())
		{
		QuerySolution sol=rs.nextSolution(); 
		
		RowPedia row= new RowPedia(sol.getLiteral("?link").getString(),sol.getLiteral("?label").getString(),
				sol.getResource("?thing").toString(),sol.getLiteral("?abstract").getString());
		
		this.result.put(sol.getLiteral("?pid").getInt(), row);
		
//		System.out.println(sol.getLiteral("?pid").getInt());
//		System.out.println(row.getLabel());
//		System.out.println(row.getLink());
//		System.out.println(row.getSummary());
		}
	}
	
	
	
	public static void main(String args[])
	{
		DBPedia d=new DBPedia("classification neural network");
		HashMap<Integer, RowPedia> res=d.getResults();
		
		for(int i:res.keySet())
		{
			System.out.println("PID is: " + i);
			System.out.println(res.get(i).getLabel());
		}
		
	}
		
	
	

}
