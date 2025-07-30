package utils;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static String get(String key) {
        String value = System.getProperty(key); // allow -DKEY=... overrides
        if (value == null) {
            value = dotenv.get(key);
        }

        if (value == null) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }

        return value;
    }
}
