// package base;

// public class PlaywrightManager {
// private static final ThreadLocal<BrowserConfig> threadLocal = new
// ThreadLocal<>();

// public static void init() {
// BrowserConfig config = new BrowserConfig();
// config.launch();
// threadLocal.set(config);
// }

// public static BrowserConfig getBrowserConfig() {
// return threadLocal.get();
// }

// public static void close() {
// if (threadLocal.get() != null) {
// threadLocal.get().close();
// threadLocal.remove();
// }
// }
// }