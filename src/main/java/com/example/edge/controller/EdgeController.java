package com.example.edge.controller;

import com.example.edge.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
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
        GenericWrapper wrapper = restTemplate.getForObject("http://" + aptContainerBaseurl + "/containers", GenericWrapper.class);
        return objectMapper.convertValue(wrapper.get_embedded().get("containers"), new TypeReference<List<Container>>(){});
    }

    @GetMapping("/schepen/all")
    public List<Schip> getAllSchips() {
        GenericWrapper wrapper = restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen", GenericWrapper.class);
        return objectMapper.convertValue(wrapper.get_embedded().get("schips"), new TypeReference<List<Schip>>(){});
    }

    @GetMapping("/rederijen/all")
    public List<Rederij> getAllRederijen() {
        GenericWrapper wrapper = restTemplate.getForObject("http://" + aptRederijenBaseurl + "/rederijen", GenericWrapper.class);
        return objectMapper.convertValue(wrapper.get_embedded().get("rederijs"), new TypeReference<List<Rederij>>(){});
    }

    @GetMapping("/schepen/{naam}")
    public Edge getDetailsOfSchip1(@PathVariable String naam){

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
    @GetMapping("/test")
    public String test(){
        return "Connection OK";
    }

    @GetMapping("/rederijen/{id}")
    public Edge getDetailsOfRederij(@PathVariable int id) {
        Rederij rederij = restTemplate.getForObject("http://" + aptRederijenBaseurl + "/rederij/{id}",
                Rederij.class, id);

        ResponseEntity<List<Schip>> responseEntity =
                restTemplate.exchange("http://" + aptSchepenBaseurl + "/schepen/{rederijID}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Schip>>() {
                        }, rederij.getRederijID());

        List<Schip> listSchips = responseEntity.getBody();
        Edge returnObject = new Edge(rederij, listSchips);

        return returnObject;
    }

    @GetMapping("/containers/{id}")
    public Edge getDetailsOfContainer(@PathVariable int id) {
        Container container = restTemplate.getForObject("http://" + aptContainerBaseurl + "/containers/{id}",
                Container.class, id);

        Schip schip = restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/" + container.getSchipId(), Schip.class);

        return new Edge(container, schip);
    }

    @PostMapping("/schepen/insert")
    public Schip addSchip(@RequestBody Schip newSchip) {
        return restTemplate.postForObject("http://" + aptSchepenBaseurl + "/schepen", new Schip(newSchip.getName(), newSchip.getCapaciteit(), newSchip.getStartLocatie(), newSchip.getEindLocatie(), newSchip.getRederijId()), Schip.class);
    }

    @PostMapping("/containers/insert")
    public Container addContainer(@RequestBody Container newContainer) {
        return restTemplate.postForObject("http://" + aptContainerBaseurl + "/containers/insert", new Container(newContainer.getSchipId(), newContainer.getGewicht(), newContainer.getInhoud(), newContainer.getStartLocatie(), newContainer.getEindLocatie()), Container.class);
    }

    @PostMapping("/rederijen/insert")
    public Rederij addRederij(@RequestBody Rederij newRederij) {
        return restTemplate.postForObject("http://" + aptRederijenBaseurl + "/rederij", new Rederij(newRederij.getRederijID(), newRederij.getNaam(), newRederij.getMail(), newRederij.getTelefoon(), newRederij.getPostcode(), newRederij.getGemeente()), Rederij.class);
    }

    @PutMapping("/schepen/update")
    public Edge updateSchip(@RequestBody Schip updateSchip) {
        Schip schip = restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/{id}",
                Schip.class, updateSchip.getId());

        assert schip != null;
        schip.setName(updateSchip.getName());
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
        Container container = restTemplate.getForObject("http://" + aptContainerBaseurl + "/containers/{id}",
                Container.class, updateContainer.getId());

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
        Rederij rederij = restTemplate.getForObject("http://" + aptRederijenBaseurl + "/rederij/{id}",
                Rederij.class, updateRederij.getRederijID());

        assert rederij != null;
        rederij.setGemeente(updateRederij.getGemeente());
        rederij.setMail(updateRederij.getMail());
        rederij.setNaam(updateRederij.getNaam());
        rederij.setPostcode(updateRederij.getPostcode());
        rederij.setTelefoon(updateRederij.getTelefoon());

        ResponseEntity<Rederij> responseEntityContainer = restTemplate.exchange("http://" + aptRederijenBaseurl + "/rederij/update",
                HttpMethod.PUT, new HttpEntity<>(rederij), Rederij.class);

        ResponseEntity<List<Schip>> responseEntity =
                restTemplate.exchange("http://" + aptSchepenBaseurl + "/schepen/{rederijID}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Schip>>() {
                        }, rederij.getRederijID());

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
    public ResponseEntity deleteRederij(@PathVariable int id) {
        restTemplate.delete("http://" + aptRederijenBaseurl + "/rederij/delete/" + id);
        return ResponseEntity.ok().build();
    }



}
