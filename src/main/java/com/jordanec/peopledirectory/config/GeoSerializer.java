package com.jordanec.peopledirectory.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.io.IOException;
import java.util.List;

public class GeoSerializer extends JsonSerializer<Object>
{
    @Override
    public void serialize(Object geometryObject, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException
    {
        String type;
        if (geometryObject instanceof GeoJsonPolygon)
        {
            GeoJsonPolygon polygon = (GeoJsonPolygon) geometryObject;
            type = "Polygon";
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeFieldName("coordinates");
            //Polygon
            writePolygonCoordinates(jsonGenerator, polygon.getCoordinates());
            //End Polygon
            jsonGenerator.writeEndObject();
        } else if (geometryObject instanceof GeoJsonMultiPolygon)
        {
            GeoJsonMultiPolygon multiPolygon = (GeoJsonMultiPolygon) geometryObject;
            type = "MultiPolygon";
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeFieldName("coordinates");
            //MultiPolygon
            jsonGenerator.writeStartArray();
            for (GeoJsonPolygon polygon : multiPolygon.getCoordinates())
            {
                writePolygonCoordinates(jsonGenerator, polygon.getCoordinates());
            }
            jsonGenerator.writeEndArray();
            //End MultiPolygon
            jsonGenerator.writeEndObject();
        } else if (geometryObject instanceof GeoJsonPoint)
        {
            GeoJsonPoint point = (GeoJsonPoint) geometryObject;
            type = "Point";
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeFieldName("coordinates");
            //Point
            writePoint(jsonGenerator, point);
            //End Point
            jsonGenerator.writeEndObject();
        }
    }

    private void writePolygonCoordinates(JsonGenerator jsonGenerator, List<GeoJsonLineString> polygonCoordinates)
            throws IOException
    {
        jsonGenerator.writeStartArray();
        for (GeoJsonLineString lineString : polygonCoordinates)
        {
            jsonGenerator.writeStartArray();
            for (Point point : lineString.getCoordinates())
            {
//                jsonGenerator.writeStartArray();
//                jsonGenerator.writeNumber(point.getX());
//                jsonGenerator.writeNumber(point.getY());
//                jsonGenerator.writeEndArray();
                writePoint(jsonGenerator, point);
            }
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndArray();
    }
    private void writePoint(JsonGenerator jsonGenerator, Point point) throws IOException
    {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(point.getX());
        jsonGenerator.writeNumber(point.getY());
        jsonGenerator.writeEndArray();
    }
}
