package pro.anton.averin.android.skeleton.data.net.json;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A converter for Retrofit to replace default GSON with Jackson library
 * ObjectMapper is a Jackson object mapper instance
 * Created by AAverin on 08.11.13.
 */
public class JacksonConverter implements Converter {

    private final ObjectMapper objectMapper;

    public JacksonConverter(ObjectMapper mapper) {
        this.objectMapper = mapper;
    }

    @Override
    public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(type);

        try {
            return objectMapper.readValue(typedInput.in(), javaType);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object o) {
        try {
            return new JsonTypedOutput(objectMapper.writeValueAsString(o).getBytes("UTF-8"));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
