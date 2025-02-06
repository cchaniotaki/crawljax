package com.crawljax.util;

import java.io.IOException;
import java.net.URI;

public class UrlUtils {

    // MAC
    public static String firefox_binary =
            "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/browser-executables/Firefox_135.0.app/Contents/MacOS/firefox";
    public static String chrome_binary =
            "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/browser-executables/chrome_131.0.6778.205.app/Contents/MacOS/Google Chrome";
    public static String edge_binary =
            "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/browser-executables/Edge_132.0.2957.140.app/Contents/MacOS/Microsoft Edge";
    public static String gecko_driver =
            "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/drivers/mac/geckodriver";
    public static String chrome_driver =
            "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/drivers/mac/chromedriver";
    public static String edge_driver =
            "/Users/christinechaniotaki/Documents/Krawler-study/krawler-paper/drivers/mac/msedgedriver";

    // WINDOWS
    //    public static String firefox_binary = "C:\\Users\\nikit\\AppData\\Local\\Mozilla Firefox\\firefox.exe";
    //    public static String chrome_binary = "";
    //    public static String edge_binary = "";
    //    public static String gecko_driver = "C:\\Users\\nikit\\Desktop\\Krawler\\drivers\\geckodriver.exe";
    //    public static String chrome_driver = "C:\\Users\\nikit\\Desktop\\Krawler\\drivers\\chromedriver.exe";
    //    public static String edge_driver = "C:\\Users\\nikit\\Desktop\\Krawler\\drivers\\msedgedriver.exe";

    // LNUX
    //    public static String firefox_binary = "";
    //    public static String chrome_binary = "";
    //    public static String edge_binary = "";
    //    public static String gecko_driver = "";
    //    public static String chrome_driver = "/home/cchaniot/Desktop/Krawler-Study/drivers/chromedriver";
    //    public static String edge_driver = "/home/cchaniot/Desktop/Krawler-Study/drivers/msedgedriver";

    private UrlUtils() {}

    public static void openFile(String file) {
        try {
            // Detect OS and open the file with the default browser using the CLI
            String os = System.getProperty("os.name").toLowerCase();
            Process process = null;

            if (os.contains("win")) {
                process = new ProcessBuilder("cmd", "/c", "start", file).start();
            } else if (os.contains("mac")) {
                process = new ProcessBuilder("open", file).start();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("linux")) {
                process = new ProcessBuilder("xdg-open", file).start();
            } else {
                System.out.println("Unsupported OS: " + os);
            }

            if (process != null) {
                process.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param currentUrl The current url
     * @param href       The target URL, relative or not
     * @return The new URL.
     */
    public static URI extractNewUrl(String currentUrl, String href) {
        if (href == null || isJavascript(href) || href.startsWith("mailto:") || href.equals("about:blank")) {
            throw new IllegalArgumentException(String.format("%s is not a HTTP url", href));
        } else if (href.contains("://")) {
            return URI.create(href);
        } else {
            URI current = URI.create(currentUrl);
            if (current.getPath().isEmpty() && !href.startsWith("/")) {
                return URI.create(currentUrl).resolve("/" + href);
            }
            return URI.create(currentUrl).resolve(href);
        }
    }

    private static boolean isJavascript(String href) {
        return href.startsWith("javascript:");
    }

    /**
     * @param url the URL string. It must contain with ":" e.g, http: or https:
     * @return the base part of the URL.
     */
    public static String getBaseUrl(String url) {
        String head = url.substring(0, url.indexOf(':'));
        String subLoc = url.substring(head.length() + DomUtils.BASE_LENGTH);
        int index = subLoc.indexOf('/');
        String base;
        if (index == -1) {
            base = url;
        } else {
            base = head + "://" + subLoc.substring(0, index);
        }

        return base;
    }

    /**
     * Retrieve the var value for varName from a HTTP query string (format is
     * "var1=val1&amp;var2=val2").
     *
     * @param varName  the name.
     * @param haystack the haystack.
     * @return variable value for varName
     */
    public static String getVarFromQueryString(String varName, String haystack) {
        if (haystack == null || haystack.length() == 0) {
            return null;
        }

        String modifiedHaystack = haystack;

        if (modifiedHaystack.charAt(0) == '?') {
            modifiedHaystack = modifiedHaystack.substring(1);
        }
        String[] vars = modifiedHaystack.split("&");

        for (String var : vars) {
            String[] tuple = var.split("=");
            if (tuple.length == 2 && tuple[0].equals(varName)) {
                return tuple[1];
            }
        }
        return null;
    }

    /**
     * Checks if the given URL is part of the domain, or a sub-domain of the given
     * {@link java.net.URI}.
     *
     * @param currentUrl The url you want to check.
     * @param url        The URL acting as the base.
     * @return If the URL is part of the domain.
     */
    public static boolean isSameDomain(String currentUrl, URI url) {
        String current = URI.create(getBaseUrl(currentUrl)).getHost().toLowerCase();
        String original = url.getHost().toLowerCase();
        return current.endsWith(original) || original.endsWith(current);
    }
}
