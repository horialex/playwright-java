package utils;

import com.microsoft.playwright.options.Cookie;

import java.util.ArrayList;
import java.util.List;

public class CookieParserUtil {

    /**
     * Parses a list of Set-Cookie header values into Playwright Cookie objects.
     */
    public static List<Cookie> parseSetCookieHeaders(List<String> setCookieHeaders, String domain) {
        List<Cookie> cookies = new ArrayList<>();
        if (setCookieHeaders == null || setCookieHeaders.isEmpty())
            return cookies;

        for (String header : setCookieHeaders) {
            String[] sections = header.split(";");
            String[] nameValue = sections[0].trim().split("=", 2);

            if (nameValue.length != 2)
                continue;

            String name = nameValue[0].trim();
            String value = nameValue[1].trim();

            // Skip deleted cookies like "persistent_shopping_cart=deleted"
            if ("deleted".equalsIgnoreCase(value))
                continue;

            Cookie cookie = new Cookie(name, value)
                    .setDomain(domain)
                    .setPath("/")
                    .setHttpOnly(true)
                    .setSecure(true);

            cookies.add(cookie);
        }

        return cookies;
    }
}
