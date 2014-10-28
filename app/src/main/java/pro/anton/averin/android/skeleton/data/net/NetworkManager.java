package pro.anton.averin.android.skeleton.data.net;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import pro.anton.averin.android.skeleton.BaseContext;
import pro.anton.averin.android.skeleton.Config;
import pro.anton.averin.android.skeleton.data.net.json.JacksonConverter;
import pro.anton.averin.android.skeleton.data.net.json.JsonHelper;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.UrlConnectionClient;

/**
 * Created by AAverin on 07.11.13.
 */
@SuppressWarnings({"StringBufferMayBeStringBuilder", "DefaultFileTemplate"})
public class NetworkManager {
    private static NetworkManager instance = null;
    private RestAdapter restAdapter;
    private RestService restService = null;

    public final static int READ_TIMEOUT = 10000;
    public final static int CONNECT_TIMEOUT = 5000;
    public final static int SIZE_OF_CACHE = 10 * 1024 * 1024;

    private BaseContext baseContext;

    public class RetrofitHttpClient extends UrlConnectionClient {

        private OkUrlFactory generateDefaultOkUrlFactory() {
            OkHttpClient client = new com.squareup.okhttp.OkHttpClient();

            try {
                Cache responseCache = new Cache(baseContext.getCacheDir(), SIZE_OF_CACHE);
                client.setCache(responseCache);
            } catch (Exception e) {
//                Log.d(TAG, "Unable to set http cache", e);
            }

            client.setConnectTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
            client.setReadTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
            return new OkUrlFactory(client);
        }

        private final OkUrlFactory factory;

        public RetrofitHttpClient() {
            factory = generateDefaultOkUrlFactory();
        }

        @Override
        protected HttpURLConnection openConnection(retrofit.client.Request request) throws IOException {
            return factory.open(new URL(request.getUrl()));
        }
    }

    private NetworkManager(BaseContext baseContext) {
        this.baseContext = baseContext;
        restAdapter = new RestAdapter.Builder()
                .setClient(new RetrofitHttpClient())
                .setEndpoint(Config.BASE_URL)
                .setConverter(new JacksonConverter(JsonHelper.getObjectMapper()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("=NETWORK="))
                .build();
    }

    public static NetworkManager getInstance(BaseContext baseContext) {
        if (instance == null) {
            instance = new NetworkManager(baseContext);
        }
        return instance;
    }

    public RestService getRestService() {
        if (restService == null) {
            restService = restAdapter.create(RestService.class);
        }
        return restService;
    }

    public static String getBodyAsString(InputStream stream) {
        StringBuffer responseBody = new StringBuffer();
        try {
            byte[] buffer = new byte[4096];
            int read = -1;
            while ((read = stream.read(buffer)) != -1) {
                responseBody.append(new String(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody.toString().trim();
    }
}
