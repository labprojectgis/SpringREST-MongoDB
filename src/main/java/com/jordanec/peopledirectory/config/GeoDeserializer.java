package com.jordanec.peopledirectory.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeoDeserializer extends JsonDeserializer<GeoJsonPoint>
{
    @Override
    public GeoJsonPoint deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException
    {
        if (jsonParser.isExpectedStartObjectToken())
        {
            GeoJsonPoint geoJsonPoint;
            try
            {
                geoJsonPoint = deserializationContext.readValue(jsonParser, GeoJsonPoint.class);
            }
            catch (InvalidDefinitionException ex)
            {
                Map<String, Object> map = (Map<String, Object>) deserializationContext
                        .readValue(jsonParser, Object.class);
                List<Double> coordinates = (ArrayList<Double>) map.get("coordinates");
                geoJsonPoint = new GeoJsonPoint(coordinates.get(0), coordinates.get(1));
            }
            return geoJsonPoint;
        }
        else
        {
            jsonParser.nextToken();
        }
        return null;
    }
}
