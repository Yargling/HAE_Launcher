package mearns.douglas.haelauncher.geo;

import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public final class CountryInfoRetriever {

    private CountryInfoRetriever() {
        informationCache = new ConcurrentHashMap<>();
    }

    public static CountryInfoRetriever instance() {
        return instance;
    }

    public interface InformationProcessor {
        void process(JSONObject data);
    }

    public void get(final String countryCode, final InformationProcessor callback) {
        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject countryData = informationCache.get(countryCode);
                    if (countryData == null) {
                        countryData = getDataFromRemote(countryCode);
                        informationCache.putIfAbsent(countryCode, countryData);
                    }
                    callback.process(countryData);
                } catch (Exception e) {
                    Log.i("HAE", "Failed to get country information");
                    e.printStackTrace();
                }
            }
        });
    }

    private JSONObject getDataFromRemote(final String countryCode) throws Exception {
        URL url = new URL(URL_BASE + countryCode);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inStream = urlConnection.getInputStream();
            byte[] bufferIn = new byte[BUFFER_SIZE];
            inStream.read(bufferIn, 0, bufferIn.length);
            return new JSONObject(new JSONTokener(new String(bufferIn)));
        } finally {
            urlConnection.disconnect();
        }
    }

    private final Executor executorPool = Executors.newCachedThreadPool();
    private final ConcurrentMap<String, JSONObject> informationCache;

    private static final CountryInfoRetriever instance = new CountryInfoRetriever();
    private static final String URL_BASE = "https://restcountries.eu/rest/v2/alpha/";
    private static final int BUFFER_SIZE = 5 * 1024;
}
