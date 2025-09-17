package com.tpmetodosagiles.tpmetodosagiles.API;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.tpmetodosagiles.tpmetodosagiles.model.*;

@Service
public class ServiceAPI {

    @Autowired
    private RestTemplate restTemplate;

    private final String API_URL = "http://localhost:8082/";

    public List<Titular> getAllTitulares() {
        ResponseEntity<List<Titular>> response = restTemplate.exchange(
                API_URL + "titulares",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Titular>>() {
                });
        return response.getBody();
    }

}
