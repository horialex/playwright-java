package api;

import com.microsoft.playwright.APIResponse;
import constants.Routes;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginApiHelper {
    private final ApiUtils apiUtils;

    public LoginApiHelper(ApiUtils apiUtils) {
        this.apiUtils = apiUtils;
    }

    private String extractFormKeyFromHtml(String html) {
        Pattern pattern = Pattern.compile("<input[^>]*name=[\"']form_key[\"'][^>]*value=[\"'](.*?)[\"']");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("form_key not found in login HTML.");
    }

    public APIResponse login(String username, String password) {
        // Step 1: GET login page
        APIResponse loginPageResponse = apiUtils.get(Routes.LOGIN_PAGE);
        if (loginPageResponse.status() != 200) {
            throw new RuntimeException("Failed to load login page. Status: " + loginPageResponse.status());
        }

        String html = loginPageResponse.text();
        String formKey = extractFormKeyFromHtml(html);

        // Step 2: Prepare form data
        Map<String, String> formData = new HashMap<>();
        formData.put("form_key", formKey);
        formData.put("login[username]", username);
        formData.put("login[password]", password);
        formData.put("send", "");

        // Step 3: POST login
        return apiUtils.postForm(Routes.LOGIN_SUBMIT, formData);
    }
}
