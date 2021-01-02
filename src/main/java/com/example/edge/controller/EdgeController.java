package com.example.edge.controller;

import com.example.edge.model.Container;
import com.example.edge.model.Edge;
import com.example.edge.model.Schip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EdgeController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${aptcontainer.baseurl}")
    private String aptContainerBaseurl;

    @Value("${aptschepen.baseurl}")
    private String aptSchepenBaseurl;

    //@Value("${aptrederijen.baseurl}")
    //private String aptRederijenBaseurl;

    @GetMapping("/")
    public Edge getDetailsOfSchip1(){

        Schip schip =
                restTemplate.getForObject("http://" + aptSchepenBaseurl + "/schepen/naam/{naam}",
                        Schip.class,"Schip 1");

        ResponseEntity<List<Container>> responseEntity =
                restTemplate.exchange("http://" + aptContainerBaseurl + "/containers/schip/{id}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Container>>() {
                        },1);

        List<Container> lijstContainers = responseEntity.getBody();
        Edge returnObject = new Edge(schip,lijstContainers);

        return  returnObject;
    }
}
