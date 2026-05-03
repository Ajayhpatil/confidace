package practice;

import org.springframework.web.reactive.function.client.WebClient;

import java.sql.SQLOutput;

public class classwebclient {

    public void webclientapicalling() {

        String result = WebClient.create().get().uri("https://jsonplaceholder.typicode.com/users/1").retrieve().bodyToMono(String.class).block();
        System.out.println("calling the API in webclient");
        System.out.println(result);
    }

}
