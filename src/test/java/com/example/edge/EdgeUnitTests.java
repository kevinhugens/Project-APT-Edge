package com.example.edge;

import com.example.edge.model.Container;
import com.example.edge.model.Rederij;
import com.example.edge.model.Schip;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hamcrest.Matchers;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EdgeUnitTests {


    @Value("${aptcontainer.baseurl}")
    private String aptContainerBaseurl;

    @Value("${aptschepen.baseurl}")
    private String aptSchepenBaseurl;

    @Value("${aptrederijen.baseurl}")
    private String aptRederijenBaseurl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockRestServiceServer;

    private ObjectMapper mapper = new ObjectMapper();

    private Container container1 = new Container( 1, "54945",1000, "Schoenen", "Amsterdam", "Antwerpen");
    private Container container2 = new Container(2, "564" ,800,"Toestellen" , "Dessel", "Schoten");
    private Container container3 =  new Container(1,"a78a78" ,1500 ,"Voedsel", "Amsterdam", "Antwerpen");

    private Schip schip1 =  new Schip("USS Enterprise", 10, "Amsterdam", "Antwerpen", "1");
    private Schip schip2 = new Schip("Yamato", 15, "Dessel", "Schoten", "1");

    private Rederij rederij1 = new Rederij("Thomas More", "thomas@gmail.com", "0474455555", "2440", "Geel");
    private Rederij rederij2 = new Rederij("Turnhout", "turnhout@gmail.com", "0414456585", "2300", "Turnhout");

    private List<Container> containersSchip1 = Arrays.asList(container1, container3);
    private List<Container> containers = Arrays.asList(container3, container2, container1);
    private List<Schip> schips = Arrays.asList(schip1, schip2);
    private List<Rederij> rederijs = Arrays.asList(rederij1, rederij2);


    @BeforeEach
    public void initialiseMockServer() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetAllSchips() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptSchepenBaseurl + "/schepen")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(schips))
                );

        mockMvc.perform(get("/schepen/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].naam", is("USS Enterprise")))
                .andExpect(jsonPath("$[0].capaciteit", is(10)))
                .andExpect(jsonPath("$[0].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$[0].eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$[0].rederijId", is("1")))
                .andExpect(jsonPath("$[1].naam", is("Yamato")))
                .andExpect(jsonPath("$[1].capaciteit", is(15)))
                .andExpect(jsonPath("$[1].startLocatie", is("Dessel")))
                .andExpect(jsonPath("$[1].eindLocatie", is("Schoten")))
                .andExpect(jsonPath("$[1].rederijId", is("1")));
    }

    @Test
    public void testGetAllRederijen() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptRederijenBaseurl + "/rederijen")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(rederijs))
                );

        mockMvc.perform(get("/rederijen"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].naam", is("Thomas More")))
                .andExpect(jsonPath("$[0].mail", is("thomas@gmail.com")))
                .andExpect(jsonPath("$[0].telefoon", is("0474455555")))
                .andExpect(jsonPath("$[0].postcode", is("2440")))
                .andExpect(jsonPath("$[0].gemeente", is("Geel")))
                .andExpect(jsonPath("$[1].naam", is("Ruben")))
                .andExpect(jsonPath("$[1].mail", is("ruben@gmail.com")))
                .andExpect(jsonPath("$[1].telefoon", is("0474455789")))
                .andExpect(jsonPath("$[1].postcode", is("2440")))
                .andExpect(jsonPath("$[1].gemeente", is("Geel")));
    }

    @Test
    public void testGetAllContainers() throws Exception {

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptContainerBaseurl + "/containers")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(containers))
                );
        mockMvc.perform(get("/containers"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].schipId", is(1)))
                .andExpect(jsonPath("$[0].serieCode", is("54945")))
                .andExpect(jsonPath("$[0].gewicht", is(1000.00)))
                .andExpect(jsonPath("$[0].inhoud", is("Schoenen")))
                .andExpect(jsonPath("$[0].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$[0].eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$[1].schipId", is(2)))
                .andExpect(jsonPath("$[1].serieCode", is("564")))
                .andExpect(jsonPath("$[1].gewicht", is(800)))
                .andExpect(jsonPath("$[1].inhoud", is("Toestellen")))
                .andExpect(jsonPath("$[1].startLocatie", is("Dessel")))
                .andExpect(jsonPath("$[1].eindLocatie", is("Schoten")))
                .andExpect(jsonPath("$[2].schipId", is(1)))
                .andExpect(jsonPath("$[2].serieCode", is("a78a78")))
                .andExpect(jsonPath("$[2].gewicht", is(1500)))
                .andExpect(jsonPath("$[2].inhoud", is("Voedsel")))
                .andExpect(jsonPath("$[2].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$[2].eindLocatie", is("Antwerpen")));
    }

    @Test
    public void testGetDetailsOfSchip() throws Exception {
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

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptRederijenBaseurl + "/rederijen/naam/Thomas More")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(rederij1)));

        mockMvc.perform(get("/schepen/{naam}", "USS Enterprise"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schip.naam", is("USS Enterprise")))
                .andExpect(jsonPath("$.schip.capaciteit", is(10)))
                .andExpect(jsonPath("$.rederij.naam", is("Thomas More")))
                .andExpect(jsonPath("$.rederij.mail", is("thomas@gmail.com")))
                .andExpect(jsonPath("$.containers[0].gewicht", is(1000)))
                .andExpect(jsonPath("$.containers[0].inhoud", is("Schoenen")))
                .andExpect(jsonPath("$.containers[0].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.containers[0].eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$.containers[1].gewicht", is(1500)))
                .andExpect(jsonPath("$.containers[1].inhoud", is("Voedsel")))
                .andExpect(jsonPath("$.containers[1].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.containers[1].eindLocatie", is("Antwerpen")));
    }

    @Test
    public void testDetailsOfRederij() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptRederijenBaseurl + "/rederijen/naam/Thomas More")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(rederij1)));
        String rederijID = rederij1.getId();
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptSchepenBaseurl + "/schepen/rederij/" + rederijID)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(schips))
                );

        mockMvc.perform(get("/rederijen/{naam}", "Thomas More"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rederij.naam", is("Thomas More")))
                .andExpect(jsonPath("$.rederij.mail", is("thomas@gmail.com")))
                .andExpect(jsonPath("$.rederij.telefoon", is("0474455555")))
                .andExpect(jsonPath("$.rederij.postcode", is("2440")))
                .andExpect(jsonPath("$.rederij.gemeente", is("Geel")))
                .andExpect(jsonPath("$.rederij.naam", is("Thomas More")))
                .andExpect(jsonPath("$.rederij.mail", is("thomas@gmail.com")))
                .andExpect(jsonPath("$.schip[0].naam", is("USS Enterprise")))
                .andExpect(jsonPath("$.schip[0].capaciteit", is(10)))
                .andExpect(jsonPath("$.schip[0].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.schip[0].eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$.schip[0].rederijId", is("1")))
                .andExpect(jsonPath("$.schip[1].naam", is("Yamato")))
                .andExpect(jsonPath("$.schip[1].capaciteit", is(15)))
                .andExpect(jsonPath("$.schip[1].startLocatie", is("Dessel")))
                .andExpect(jsonPath("$.schip[1].eindLocatie", is("Schoten")))
                .andExpect(jsonPath("$.schip[1].rederijId", is("1")));
    }

    @Test
    public void testDetailsOfContainer() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptContainerBaseurl + "/containers/serieCode/54945")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(container1)));

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptSchepenBaseurl + "/schepen/naam/USS Enterprise")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(schip1))
                );

        mockMvc.perform(get("/containers/{serieCode}", "54945"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.container.schipId", is(1)))
                .andExpect(jsonPath("$.container.serieCode", is("54945")))
                .andExpect(jsonPath("$.container.gewicht", is(1000.00)))
                .andExpect(jsonPath("$.container.inhoud", is("Schoenen")))
                .andExpect(jsonPath("$.container.startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.container.eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$.schip[0].naam", is("USS Enterprise")))
                .andExpect(jsonPath("$.schip[0].capaciteit", is(10)))
                .andExpect(jsonPath("$.schip[0].startLocatie", is("Amsterdam")))
                .andExpect(jsonPath("$.schip[0].eindLocatie", is("Antwerpen")))
                .andExpect(jsonPath("$.schip[0].rederijId", is("1")));
    }

    @Test
    public void testPostSchip() throws Exception {
        Schip newSchip = new Schip("Hood", 1500, "Londen", "Hamburg", "2");

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptSchepenBaseurl + "/schepen/insert")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                       .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newSchip))
                );

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(newSchip);

        mockMvc.perform(post("/schepen/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam", is("Hood")))
                .andExpect(jsonPath("$.capaciteit", is(1500)))
                .andExpect(jsonPath("$.startLocatie", is("Londen")))
                .andExpect(jsonPath("$.eindLocatie", is("Hamburg")))
                .andExpect(jsonPath("$.rederijId", is("2")));

    }

    @Test
    public void testPostContainer() throws Exception {
        Container newContainer = new Container(2, "ABC123",1000 ,"Hoeden","Londen", "Hamburg");

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptContainerBaseurl + "/containers/insert")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newContainer))
                );

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(newContainer);

        mockMvc.perform(post("/containers/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schipId", is(2)))
                .andExpect(jsonPath("$.serieCode", is("ABC123")))
                .andExpect(jsonPath("$.gewicht", is(1000)))
                .andExpect(jsonPath("$.inhoud", is("Hoeden")))
                .andExpect(jsonPath("$.startLocatie", is("Londen")))
                .andExpect(jsonPath("$.eindLocatie", is("Hamburg")))
                ;

    }

    @Test
    public void testPostRederij() throws Exception {
        Rederij newRederij = new Rederij("Kevin", "kevin@gmail.com","0458758" ,"2000", "Antwerpen");

        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptRederijenBaseurl + "/rederijen/insert")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(newRederij))
                );

        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String request = ow.writeValueAsString(newRederij);

        mockMvc.perform(post("/rederijen/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.naam", is("Kevin")))
                .andExpect(jsonPath("$.mail", is("kevin@gmail.com")))
                .andExpect(jsonPath("$.telefoon", is("0458758")))
                .andExpect(jsonPath("$.postcode", is("2000")))
                .andExpect(jsonPath("$.gemeente", is("Antwerpen")))
        ;

    }

    @Test
    public void testDeleteSchip() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptSchepenBaseurl + "/schepen/delete/0")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/schepen/delete/{id}", 0))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteContainer() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptContainerBaseurl + "/containers/delete/0")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/containers/delete/{id}", 0))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRederij() throws Exception {
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + aptRederijenBaseurl + "/rederijen/delete/0")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        mockMvc.perform(delete("/rederijen/delete/{id}", 0))
                .andExpect(status().isOk());
    }
}
