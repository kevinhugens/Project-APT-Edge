package com.example.edge.controller;

import com.example.edge.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

}
