package practice;

import org.apache.catalina.mapper.Mapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import practice.poos.Root;
import reactor.core.publisher.Flux;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class RestTemplateClient {

    public void getwithpojo (){

        RestTemplate restobj = new RestTemplate();

        Root rootobj =  restobj.getForObject("https://jsonplaceholder.typicode.com/users/1", Root.class);

        System.out.println( rootobj.getEmail());

       /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/

        ResponseEntity<Root> ddf= restobj.getForEntity("https://jsonplaceholder.typicode.com/users/1", Root.class);

        Root obj = ddf.getBody();

        System.out.println(obj.getPhone());

    }

    public void postwithpojo () {

        RestTemplate restobj = new RestTemplate();

        HttpHeaders hedders  = new HttpHeaders();

        String paylood ="{\n" +
                "    \"title\": \"Spring Boot Practice\",\n" +
                "    \"body\": \"I am learning POST API\",\n" +
                "    \"userId\": 1\n" +
                "}";


        hedders.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<String> Httpobj = new HttpEntity<>(paylood,hedders);

        ResponseEntity<String> obj = restobj.postForEntity("https://jsonplaceholder.typicode.com/posts",Httpobj , String.class);

        String strobj = obj.getBody();

        System.out.println(strobj);

        int  code = obj.getStatusCode().value();

        System.out.println("status "+code);

        ObjectMapper mapobj = new ObjectMapper();

        JsonNode nodeobj = mapobj.readTree(strobj);

        String objstring = nodeobj.get("title").asString();

        System.out.println(objstring);

    }


    public void dfadadf(){


        WebClient client = WebClient.builder()
              .baseUrl("https://stream.wikimedia.org")
                .build();

        Flux<String> stream = client.get()
                .uri("/v1/forecast?latitude=18.52&longitude=73.85&hourly=temperature_2m")
                .header("Accept", "text/event-stream")
                .retrieve()
                .bodyToFlux(String.class);

        stream.subscribe(event -> {
            System.out.println("Wikipedia update: " + event);
        });



    }





}
