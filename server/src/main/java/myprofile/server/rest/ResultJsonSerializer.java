package myprofile.server.rest;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import myprofile.common.result.Result;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
@JsonComponent
public class ResultJsonSerializer extends JsonSerializer<Result> {
    @Override
    public void serialize(Result data, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("isError");
        jsonGenerator.writeBoolean(data.isError());
        if (data.isError()) {
            jsonGenerator.writeFieldName("errorCodes");
            jsonGenerator.writeObject(data.getErrorCodes());
        } else {
            jsonGenerator.writeFieldName("ok");
            jsonGenerator.writeObject(data.ok());
        }
        jsonGenerator.writeEndObject();
    }
}