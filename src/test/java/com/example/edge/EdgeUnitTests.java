package com.example.edge;

import com.example.edge.model.Container;
import com.example.edge.model.Rederij;
import com.example.edge.model.Schip;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class EdgeUnitTests {


    @Value("${aptcontainer.baseurl}")
    private String aptContainerBaseurl;

    @Value("${aptschepen.baseurl}")
    private String aptSchepenBaseurl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockRestServiceServer;

    private ObjectMapper mapper = new ObjectMapper();

    private Container container1 = new Container( 1, 1000, "Schoenen", "Amsterdam", "Antwerpen");
    private Container container2 = new Container(2, 800,"Toestellen" , "Dessel", "Schoten");
    private Container container3 =  new Container(1, 1500, "Voedsel", "Amsterdam", "Antwerpen");

    private Schip schip1 =  new Schip("USS Enterprise", 10, "Amsterdam", "Antwerpen", 1);
    private Schip schip2 = new Schip("Yamato", 15, "Dessel", "Schoten", 1);

    private Rederij rederij1 = new Rederij(1, "Thomas More", "thomas@gmail.com", "0474455555", "2440", "Geel");
    private Rederij rederij2 = new Rederij(2, "Stad Turnhout", "turnhout@gmail.com", "0414456585", "2300", "Turnhout");

    private List<Container> containersSchip1 = Arrays.asList(container1, container3);


    @BeforeEach
    public void initialiseMockServer() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetDetailsOfSchip() throws Exception {
        //GET schip1
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptSchepenBaseurl + "/schepen/naam/USS Enterprise")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(schip1))
                );

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptContainerBaseurl + "/containers/schip/1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(containersSchip1))
                );

        mockMvc.perform(get("/"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.schipNaam", is("Thomas More")))
                .andExpect(jsonPath("$.schipCapaciteit", is(10)))
                .andExpect(jsonPath("$.containers[0].gewicht", is(1000)))
                .andExpect(jsonPath("$.containers[0].inhoud", is("Schoenen")))
                .andExpect(jsonPath("$.containers[0].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.containers[0].eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$.containers[1].gewicht", is(1500)))
                .andExpect(jsonPath("$.containers[1].inhoud", is("Voedsel")))
                .andExpect(jsonPath("$.containers[1].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.containers[1].eindLocatie", is("Antwerpen")));
    }
}
