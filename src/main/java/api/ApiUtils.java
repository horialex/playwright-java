package api;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.APIRequestContext;
import java.util.Map;

public class ApiUtils {
    private final APIRequestContext api;

    public ApiUtils(APIRequestContext apiRequest) {
        this.api = apiRequest;
    }

    public APIResponse get(String endpoint) {
        return get(endpoint, null);
    }

    public APIResponse get(String endpoint, Map<String, String> queryParams) {
        RequestOptions options = RequestOptions.create();

        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                options.setQueryParam(entry.getKey(), entry.getValue());
            }
        }

        return api.get(endpoint, options);
    }

    public APIResponse postJson(String endpoint, Object payload) {
        return api.post(endpoint, RequestOptions.create()
                .setData(payload)
                .setHeader("Content-Type", "application/json"));
    }

    public APIResponse putJson(String endpoint, Object payload) {
        return api.put(endpoint, RequestOptions.create()
                .setData(payload)
                .setHeader("Content-Type", "application/json"));
    }

    public APIResponse delete(String endpoint) {
        return api.delete(endpoint);
    }

    public APIResponse postForm(String endpoint, Map<String, String> formData) {
        return api.post(endpoint, RequestOptions.create()
                .setData(formData)
                .setHeader("Content-Type", "application/x-www-form-urlencoded"));
    }

}
