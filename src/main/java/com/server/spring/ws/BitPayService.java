package com.server.spring.ws;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.spring.ws.dto.BitPay;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.write;

@Service
public class BitPayService {

    private final RestTemplate restTemplate;

    @Value("${spring.url}")
    private String baseURL;

    public BitPayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void persistBitPay() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<BitPay>> responseType = new ParameterizedTypeReference<List<BitPay>>() {};
        ResponseEntity<List<BitPay>> response = restTemplate.exchange(baseURL, HttpMethod.GET, requestEntity, responseType);
        List<BitPay> bitPays = response.getBody();
        System.out.println("Recuperation de WS : " + bitPays);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            persistFileBitPayToJson(bitPays);
        } else {
            write(Path.of(format("", baseURL)), null, UTF_8);
        }
    }

    private void persistFileBitPayToJson(List<BitPay> bitPays) throws IOException {
        File file = new File( "src/main/resources/json/bitpays.json");
        // Vérifier si le fichier existe et le supprimer
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Fichier existant supprimé : " + file.getAbsolutePath());
            } else {
                throw new IOException("Impossible de supprimer le fichier existant : " + file.getAbsolutePath());
            }
        }

        // Créer un nouveau fichier et écrire les données
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, bitPays);
        System.out.println("Données sauvegardées dans le fichier : " + file.getAbsolutePath());
    }
}
