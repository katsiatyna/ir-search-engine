package fr.univ_tours.etu.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DBLP {
	
	public static final String SERVICE="http://dblp.l3s.de/d2r/sparql";

	private String query;
	private String keywords;
	private String author;
	private String sign;
	private String conditions;
	private int year;
	private  Map<Integer, RowLP> result;
	
	
	
	
	public DBLP(String keywords, String author, int year, String sign) {
	
		this.keywords = keywords;
		this.author = author;
		this.year = year;
		this.sign = sign;
		this.conditions="";
		this.result= new HashMap<Integer, RowLP>();
		this.buildQuery();
		this.ExecuteQuery();
		
	}
	
	public HashMap<Integer,RowLP> getResults()
	{
		return (HashMap<Integer, RowLP>) this.result;
	}
	
	public String getQuery()
	{
		return this.query;
	}
	
	private void buildQuery() {
		// TODO Auto-generated method stub
		this.Tokenize(this.keywords);
		
		String header= "PREFIX d2r: <http://sites.wiwiss.fu-berlin.de/suhl/bizer/d2r-server/config.rdf#> "+
						"PREFIX swrc: <http://swrc.ontoware.org/ontology#> "+
						"PREFIX dcterms: <http://purl.org/dc/terms/> "+
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
						"PREFIX dc: <http://purl.org/dc/elements/1.1/> "+
						"PREFIX map: <file:///home/diederich/d2r-server-0.3.2/dblp-mapping.n3#> "+
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
						"PREFIX owl: <http://www.w3.org/2002/07/owl#> "+
						"SELECT DISTINCT ?year ?paper ?title ?author ?name "+
						"WHERE { ?paper dc:creator ?author;  dc:title ?title; "+ 
						"dcterms:issued ?year. ?author foaf:name ?name. ";
		
		String footer= " }LIMIT 30";
		
		
		this.query=header + this.getAuthorQuery()+this.conditions+ this.getYearQuery()+footer ;
		
	}
	
	private String getYearQuery()
	{
		String y="";
		if(this.year==0)
		{
			return y;
		}
		else
		{
			y="FILTER (?year "+ this.sign+" "+ this.year+")";
		}
		
		return y;
	}
	
	private String getAuthorQuery()
	{
		String a="";
		
		if(this.author.isEmpty())
		{
			return a;
		}
		else
		{
			a="FILTER regex(?name,"+ this.author+",\"i\").";
		}
		return a;
	}

	private void Tokenize(String keywords)
	{
		String str = keywords;
		StringTokenizer defaultTokenizer = new StringTokenizer(str);
		//System.out.println("Total number of tokens found : " + defaultTokenizer.countTokens());
		while (defaultTokenizer.hasMoreTokens())
		{
			this.conditions+="FILTER regex(?title, \""+defaultTokenizer.nextToken()+"\", \"i\"). ";
		    
		}

	}
	
	
	private void ExecuteQuery()
	{
		QueryEngineHTTP qe=(QueryEngineHTTP) QueryExecutionFactory.sparqlService(DBLP.SERVICE,this.query);
		
		//many timeout issues, to avoid them added big number
		qe.addParam("timeout","500000"); 
		
		ResultSet rs=qe.execSelect();
		int i=1;
		while (rs.hasNext())
		{
		QuerySolution sol=rs.nextSolution(); 
		
		RowLP row= new RowLP(sol.getLiteral("?year").getInt(),sol.getResource("?paper").toString(),
				sol.getLiteral("?title").getString(),sol.getResource("?author").toString(),sol.getLiteral("?name").getString());
		
		this.result.put(i, row);
		i++;
		

		}
	
	}
	
	public static void main(String args[])
	{
		DBLP d= new DBLP("classification network","",1990,">=");
		System.out.println(d.getQuery());
		HashMap<Integer, RowLP> res=d.getResults();
		
		for(int i:res.keySet())
		{
			System.out.println("PID is: " + i);
			System.out.println(res.get(i).getPublicationTitle());
		}
		
	}

}
