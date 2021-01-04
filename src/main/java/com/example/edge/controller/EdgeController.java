package com.example.edge.controller;

import com.example.edge.model.Container;
import com.example.edge.model.Edge;
import com.example.edge.model.Rederij;
import com.example.edge.model.Schip;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class EdgeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${aptcontainer.baseurl}")
    private String aptContainerBaseurl;

    @Value("${aptschepen.baseurl}")
    private String aptSchepenBaseurl;

    @Value("${aptrederijen.baseurl}")
    private String aptRederijenBaseurl;

    @GetMapping("/containers/all")
    public List<Container> getAllContainers() {
        ResponseEntity<List<Container>> responseEntity =
                restTemplate.exchange("http://" + aptContainerBaseurl + "/containers",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Container>>() {
                        });
        List<Container> lijstContainers = responseEntity.getBody();
        return lijstContainers;
    }

    @GetMapping("/schepen/all")
    public List<Schip> getAllSchips() {
        ResponseEntity<List<Schip>> responseEntity =
                restTemplate.exchange("http://" + aptSchepenBaseurl + "/schepen",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Schip>>() {
                        });
        List<Schip> listSchips = responseEntity.getBody();
        return listSchips;
    }

    @GetMapping("/rederijen/all")
    public List<Rederij> getAllRederijen() {
        ResponseEntity<List<Rederij>> responseEntity =
                restTemplate.exchange("http://" + aptRederijenBaseurl + "/rederijen",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Rederij>>() {
                        });

        List<Rederij> rederijs = responseEntity.getBody();
        return rederijs;
    }

    @GetMapping("/schepen/{naam}")
    public Edge getDetailsOfSchip(@PathVariable String naam){
        Schip schip =
                restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/naam/{naam}",
                        Schip.class,naam);

        ResponseEntity<List<Container>> responseEntity =
                restTemplate.exchange("http://" + aptContainerBaseurl + "/containers/schip/{id}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Container>>() {
                        },schip.getId());

        List<Container> lijstContainers = responseEntity.getBody();
        Edge returnObject = new Edge(schip,lijstContainers);

        return  returnObject;
    }

    @GetMapping("/rederijen/{naam}")
    public Edge getDetailsOfRederij(@PathVariable String naam) {
        Rederij rederij = restTemplate.getForObject("http://" + aptRederijenBaseurl + "/rederij/naam/{naam}",
                Rederij.class, naam);

        ResponseEntity<List<Schip>> responseEntity =
                restTemplate.exchange("http://" + aptSchepenBaseurl + "/schepen/rederij/{id}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Schip>>() {
                        }, rederij.getId());

        List<Schip> listSchips = responseEntity.getBody();
        Edge returnObject = new Edge(rederij, listSchips);

        return returnObject;
    }

    @GetMapping("/containers/{serieCode}")
    public Edge getDetailsOfContainer(@PathVariable String serieCode) {
        Container container = restTemplate.getForObject("http://" + aptContainerBaseurl + "/containers/serieCode/{serieCode}",
                Container.class, serieCode);

        Schip schip = restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/" + container.getSchipId(), Schip.class);

        return new Edge(container, schip);
    }

    @PostMapping("/schepen/insert")
    public Schip addSchip(@RequestBody Schip newSchip) {
        return restTemplate.postForObject("http://" + aptSchepenBaseurl + "/schepen", new Schip(newSchip.getNaam(), newSchip.getCapaciteit(), newSchip.getStartLocatie(), newSchip.getEindLocatie(), newSchip.getRederijId()), Schip.class);
    }

    @PostMapping("/containers/insert")
    public Container addContainer(@RequestBody Container newContainer) {
        return restTemplate.postForObject("http://" + aptContainerBaseurl + "/containers/insert", new Container(newContainer.getSchipId(), newContainer.getSerieCode() ,newContainer.getGewicht(), newContainer.getInhoud(), newContainer.getStartLocatie(), newContainer.getEindLocatie()), Container.class);
    }

    @PostMapping("/rederijen/insert")
    public Rederij addRederij(@RequestBody Rederij newRederij) {
        return restTemplate.postForObject("http://" + aptRederijenBaseurl + "/rederij", new Rederij(newRederij.getNaam(), newRederij.getMail(), newRederij.getTelefoon(), newRederij.getPostcode(), newRederij.getGemeente()), Rederij.class);
    }

    @PutMapping("/schepen/update")
    public Edge updateSchip(@RequestBody Schip updateSchip) {
        Schip schip = restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/naam/{naam}",
                Schip.class, updateSchip.getNaam());

        assert schip != null;
        schip.setNaam(updateSchip.getNaam());
        schip.setCapaciteit(updateSchip.getCapaciteit());
        schip.setEindLocatie(updateSchip.getEindLocatie());
        schip.setRederijId(updateSchip.getRederijId());
        schip.setStartLocatie(updateSchip.getStartLocatie());

        ResponseEntity<Schip> responseEntitySchip = restTemplate.exchange("http://" + aptSchepenBaseurl + "/schepen",
                HttpMethod.PUT, new HttpEntity<>(schip), Schip.class);

        ResponseEntity<List<Container>> responseEntity =
                restTemplate.exchange("http://" + aptContainerBaseurl + "/containers/schip/{id}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Container>>() {
                        },schip.getId());

        List<Container> lijstContainers = responseEntity.getBody();
        Edge returnObject = new Edge(schip,lijstContainers);

        return  returnObject;
    }

    @PutMapping("/containers/update")
    public Edge updateContainer(@RequestBody Container updateContainer) {
        Container container = restTemplate.getForObject("http://" + aptContainerBaseurl + "/containers/serieCode/{serieCode}",
                Container.class, updateContainer.getSerieCode());

        assert container != null;
        container.setEindLocatie(updateContainer.getEindLocatie());
        container.setGewicht(updateContainer.getGewicht());
        container.setInhoud(updateContainer.getInhoud());
        container.setSchipId(updateContainer.getSchipId());
        container.setStartLocatie(updateContainer.getStartLocatie());

        ResponseEntity<Container> responseEntityContainer = restTemplate.exchange("http://" + aptContainerBaseurl + "/containers/update",
                HttpMethod.PUT, new HttpEntity<>(container), Container.class);

        Schip schip = restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/" + container.getSchipId(), Schip.class);
        return new Edge(container, schip);
    }

    @PutMapping("/rederijen/update")
    public Edge updateRederij(@RequestBody Rederij updateRederij) {
        Rederij rederij = restTemplate.getForObject("http://" + aptRederijenBaseurl + "/rederij/naam/{naam}",
                Rederij.class, updateRederij.getNaam());

        assert rederij != null;
        rederij.setGemeente(updateRederij.getGemeente());
        rederij.setMail(updateRederij.getMail());
        rederij.setNaam(updateRederij.getNaam());
        rederij.setPostcode(updateRederij.getPostcode());
        rederij.setTelefoon(updateRederij.getTelefoon());

        ResponseEntity<Rederij> responseEntityContainer = restTemplate.exchange("http://" + aptRederijenBaseurl + "/rederij/update",
                HttpMethod.PUT, new HttpEntity<>(rederij), Rederij.class);

        ResponseEntity<List<Schip>> responseEntity =
                restTemplate.exchange("http://" + aptSchepenBaseurl + "/schepen/rederij/{id}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Schip>>() {
                        }, rederij.getId());

        List<Schip> listSchips = responseEntity.getBody();
        Edge returnObject = new Edge(rederij, listSchips);

        return returnObject;
    }

    @DeleteMapping("/schepen/delete/{id}")
    public ResponseEntity deleteSchip(@PathVariable int id) {
        restTemplate.delete("http://" + aptSchepenBaseurl + "/schepen/" + id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/containers/delete/{id}")
    public ResponseEntity deleteContainer(@PathVariable int id) {
        restTemplate.delete("http://" + aptContainerBaseurl + "/containers/delete/" + id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rederijen/delete/{id}")
    public ResponseEntity deleteRederij(@PathVariable String id) {
        restTemplate.delete("http://" + aptRederijenBaseurl + "/rederij/delete/" + id);
        return ResponseEntity.ok().build();
    }



}
