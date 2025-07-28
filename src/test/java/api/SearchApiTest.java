package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchApiTest {
    
    public void testSearchFunctionality() throws Exception {
        // working only if it is execute using mvn test command
        String baseUrl = "http://qa3magento.dev.evozon.com/customer/account/index/?";
        String searchTerm = "dress";
        String searchUrl = baseUrl + "q=" + searchTerm;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(searchUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Accept both 200 (OK) and 302 (Found/Redirect) as valid responses
        assertTrue(response.statusCode() == 200 || response.statusCode() == 302, "Expected HTTP 200 or 302 response");
        System.out.println(response.body().contains(searchTerm));
        assertTrue(response.body().contains(searchTerm), "Search results should contain the search term");
    }
}
