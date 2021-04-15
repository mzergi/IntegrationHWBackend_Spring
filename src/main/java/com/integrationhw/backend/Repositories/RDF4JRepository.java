package com.integrationhw.backend.Repositories;

import com.integrationhw.backend.Models.ArtistQueryResponseModel;
import com.integrationhw.backend.Models.addDataDemoResponseModel;
import org.apache.lucene.queries.function.valuesource.MultiFunction;
import org.eclipse.rdf4j.federated.FedXFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import java.util.ArrayList;
import java.util.List;

public class RDF4JRepository {
    private Repository repository;
    private String URL;

    public RDF4JRepository(String URL) {
        repository = FedXFactory.newFederation()
                .withSparqlEndpoint(URL)
                .create();

        this.URL = URL;
    }

    public void addDataToOntology(){

        Repository httpconn = new HTTPRepository(URL);

        try(RepositoryConnection conn = httpconn.getConnection()) {
            ValueFactory factory = conn.getValueFactory();

            IRI JulesAdler = factory.createIRI("http://data.szepmuveszeti.hu/id/collections/museum/E39_Actor/006f52e9-102a-3d3b-a2fe-5614f42ba989");
            IRI IanStephenson = factory.createIRI("http://data.szepmuveszeti.hu/id/collections/museum/E39_Actor/007d4a12-1428-3aea-89b9-759ae1324e96");
            IRI JorgBreuSenior = factory.createIRI("http://data.szepmuveszeti.hu/id/collections/museum/E39_Actor/015e3193-3548-3610-a0e2-ba448e85995e");

            Literal JulesAdlerBirthplace = factory.createLiteral("Birthplace: Luxeuil-les-Bains");
            Literal JulesAdlerDiedAt = factory.createLiteral("Buried: Nogent-sur-Marne");
            conn.add(JulesAdler, RDFS.LABEL, JulesAdlerBirthplace);
            conn.add(JulesAdler, RDFS.LABEL, JulesAdlerDiedAt);

            Literal IanStephensonBirthplace = factory.createLiteral("Birthplace: County Durham");
            Literal IanStephensonDiedAt = factory.createLiteral("Buried: London");
            conn.add(IanStephenson, RDFS.LABEL, IanStephensonBirthplace);
            conn.add(IanStephenson, RDFS.LABEL, IanStephensonDiedAt);

            Literal JorgBreuSeniorBirthplace = factory.createLiteral("Birthplace: Augsburg");
            Literal JorgBreuSeniorDiedAt = factory.createLiteral("Buried: Augsburg");
            conn.add(JorgBreuSenior, RDFS.LABEL, JorgBreuSeniorBirthplace);
            conn.add(JorgBreuSenior, RDFS.LABEL, JorgBreuSeniorDiedAt);
        }

    }

    public ArrayList<addDataDemoResponseModel> getBirthplaceAndBuried() {
        try (RepositoryConnection conn = repository.getConnection()) {
            String query = String.format("PREFIX ecrm: <http://erlangen-crm.org/current/>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?name ?birthplace ?buried\n" +
                    "WHERE {\n" +
                    "\t{\t\n" +
                    "    \t?x ecrm:P131_is_identified_by ?id .\n" +
                    "\t\t?x rdfs:label ?name .\n" +
                    "      \t?x rdfs:label ?birthplace .\n" +
                    "      \t?x rdfs:label ?buried\n" +
                    "      \tFILTER regex (?name, \"Jules Adler\", \"i\")\n" +
                    "      \tFILTER regex (?birthplace, \"^Birthplace\", \"i\")\n" +
                    "      \tFILTER regex (?buried, \"^Buried\", \"i\")\n" +
                    "  \t}\n" +
                    "  \tUNION \n" +
                    "  \t{\n" +
                    "    \t?x ecrm:P131_is_identified_by ?id .\n" +
                    "\t\t?x rdfs:label ?name .\n" +
                    "        ?x rdfs:label ?birthplace .\n" +
                    "      \t?x rdfs:label ?buried\n" +
                    "      \tFILTER regex (?name, \"Ian Stephenson\", \"i\")\n" +
                    "    \tFILTER regex (?birthplace, \"^Birthplace\", \"i\")\n" +
                    "      \tFILTER regex (?buried, \"^Buried\", \"i\")\n" +
                    "  \t}\n" +
                    "  \tUNION\n" +
                    "  \t{\n" +
                    "    \t?x ecrm:P131_is_identified_by ?id .\n" +
                    "\t\t?x rdfs:label ?name .\n" +
                    "        ?x rdfs:label ?birthplace .\n" +
                    "      \t?x rdfs:label ?buried\n" +
                    "      \tFILTER regex (?name, \"id. Jörg Breu\", \"i\")\n" +
                    "    \tFILTER regex (?birthplace, \"^Birthplace\", \"i\")\n" +
                    "      \tFILTER regex (?buried, \"^Buried\", \"i\")\n" +
                    "  \t}\n" +
                    "}");

            TupleQuery tq = conn.prepareTupleQuery(query);

            TupleQueryResult tqr = tq.evaluate();

            ArrayList<addDataDemoResponseModel> list = new ArrayList<addDataDemoResponseModel>();

            while (tqr.hasNext()) {
                BindingSet b = tqr.next();

                list.add(new addDataDemoResponseModel(b.getValue("name").stringValue(),
                        b.getValue("birthplace").stringValue(),
                        b.getValue("buried").stringValue()));
            }

            return list;
        }
    }

    public ArrayList<ArtistQueryResponseModel> getCreationsOfArtist(String ArtistName) {
        try (RepositoryConnection conn = repository.getConnection()) {
            String query = String.format("PREFIX ecrm: <http://erlangen-crm.org/current/>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "SELECT ?artname ?artistname\n" +
                    "\tWHERE {\n" +
                    "\t?creation a ecrm:E65_Creation.\n" +
                    "  \t?creation ecrm:P11_had_participant ?artist.\n" +
                    "  \t?artpiece ecrm:P12i_was_present_at ?creation.\n" +
                    "  \t?artpiece rdfs:label ?artname.\n" +
                    "    ?artist rdfs:label ?artistname\n" +
                    "    FILTER regex(?artistname, \"%s\", \"i\")\n" +
                    "  ?artist ecrm:P3_has_note ?type\n" +
                    "    FILTER regex(?type, \"alkotó\", \"i\")\n" +
                    "}", ArtistName);
            TupleQuery tq = conn.prepareTupleQuery(query);

            TupleQueryResult tqr = tq.evaluate();

            ArrayList<ArtistQueryResponseModel> list = new ArrayList<ArtistQueryResponseModel>();

            while (tqr.hasNext()) {
                BindingSet b = tqr.next();
                //note to self: toString-gel fura \-ek kerulnek be a stringbe, stringValue()-val jo
                list.add(new ArtistQueryResponseModel(b.getValue("artistname").stringValue(), b.getValue("artname").stringValue()));
            }
            return list;
        }
    }
}
