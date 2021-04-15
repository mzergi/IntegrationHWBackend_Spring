package com.integrationhw.backend.Controllers;

import com.google.gson.Gson;
import com.integrationhw.backend.Models.ArtistQueryResponseModel;
import com.integrationhw.backend.Models.addDataDemoResponseModel;
import com.integrationhw.backend.Repositories.RDF4JRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.certpath.OCSPResponse;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class IntegrationHWController {

    private RDF4JRepository repository;

    public IntegrationHWController() {
        this.repository = new RDF4JRepository("http://localhost:8080/rdf4j-server/repositories/Szep");
    }

    @RequestMapping(value = "/artpiecequery", method = GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<ArtistQueryResponseModel> artpieceEndpoint(@RequestParam("name") String name){
        List<ArtistQueryResponseModel> result = repository.getCreationsOfArtist(name);
        System.out.println("received" + result.size());

        return result;
    }
    @RequestMapping(value = "/addDataDemo", method = POST)
    @ResponseStatus(HttpStatus.OK)
    public void addDataEndpoint(){
        repository.addDataToOntology();
    }
    @RequestMapping(value = "/getAddDataDemoData", method = GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<addDataDemoResponseModel> getAddDataArtists() {
        List<addDataDemoResponseModel> result = repository.getBirthplaceAndBuried();

        return result;
    }
}
