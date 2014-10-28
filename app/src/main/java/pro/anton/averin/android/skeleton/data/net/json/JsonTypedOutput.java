package pro.anton.averin.android.skeleton.data.net.json;

import retrofit.mime.TypedOutput;

import java.io.IOException;
import java.io.OutputStream;

/**
 * JsonTypedOutput for Jackson conversion in Retrofit
 * Created by AAverin on 08.11.13.
 */
public class JsonTypedOutput implements TypedOutput {
    private final byte[] jsonBytes;

    public JsonTypedOutput(byte[] bytes) {
        this.jsonBytes = bytes;
    }

    @Override
    public String fileName() {
        return null;
    }

    @Override
    public String mimeType() {
        return "application/json; charset=UTF-8";
    }

    @Override
    public long length() {
        return jsonBytes.length;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(jsonBytes);
    }
}
