package com.jordanec.peopledirectory;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jordanec.peopledirectory.model.Country;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@Ignore
public class Test
{
    Logger logger = LoggerFactory.getLogger(Test.class);
    @org.junit.Test
    public void generateCountriesWithGeometry() throws IOException
    {
        Set<String> EXCLUDED_COUNTRIES = new HashSet<>();
        EXCLUDED_COUNTRIES.add("Kyrgyzstan");
        EXCLUDED_COUNTRIES.add("Fiji");
        EXCLUDED_COUNTRIES.add("Russia");

        ObjectMapper mapper = new ObjectMapper();
        String TEST_DATA_PATH = "testData" + File.separatorChar;

        String RESOURCES_DATA_PATH = "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "data" + File.separatorChar;

        String GEOJSON_FILENAME = "countries.geojson.json";
        String ORIGINAL_COUNTRY_FILENAME = "Country.json.bak";
        String NEW_COUNTRY_FILENAME = "Country_WithGeometry.json";

        Map<String, Object> map = (Map<String, Object>) mapper
                .readValue(new File(TEST_DATA_PATH + GEOJSON_FILENAME), Object.class);

        List<Country> countries = mapper.readValue(new File(RESOURCES_DATA_PATH + ORIGINAL_COUNTRY_FILENAME),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, Country.class));

        // To user only some countries. Comment below lines to use all
/*        Set<String> INCLUDED_COUNTRIES = new HashSet<>();
        INCLUDED_COUNTRIES.add("Aruba");
        INCLUDED_COUNTRIES.add("Costa Rica");
        countries = countries.stream().filter(c -> INCLUDED_COUNTRIES.contains(c.getName())).collect(Collectors.toList());*/
        //
        List<Map<String, Object>> list = (List<Map<String, Object>>)map.get("features");
        Assert.notNull(list);
        for (Map<String, Object> element : list)
        {
            Map<String, String> propertiesMap = (Map<String, String>) element.get("properties");
            Assert.notNull(propertiesMap);
            String countryName = propertiesMap.get("ADMIN");
            if (EXCLUDED_COUNTRIES.contains(countryName))
            {
                logger.debug("Ignoring {}", countryName);
                continue;
            }
            Assert.notNull(countryName);
            Optional<Country> countryOptional = countries.stream()
                    .filter(country -> country.getName().equalsIgnoreCase(countryName)).findFirst();
            if (countryOptional.isPresent())
            {
                Country c = countryOptional.get();
                Map<String, Object> geometryMap = (Map<String, Object>) element.get("geometry");
                Assert.notNull(geometryMap, "geometry null for: " + countryName);
                if ("Polygon".equalsIgnoreCase(geometryMap.get("type").toString()))
                {
                    List<List<?>> coordinates = (List<List<?>>) geometryMap.get("coordinates");
                    Assert.notNull(coordinates, "coordinates null for: " + countryName);
                    Assert.isTrue(coordinates.size() == 1, "coordinates size is not 1 for: " + countryName);
                    List<List<Double>> c2 = (List<List<Double>>) coordinates.get(0);
                    Assert.notEmpty(c2, "c2 empty for: " + countryName);
                    List<Point> points = c2.stream()
                            .map(pointAsList -> new Point(pointAsList.get(0), pointAsList.get(1)))
                            .collect(Collectors.toList());
                    Assert.notEmpty(points, "points empty for: " + countryName);
                    c.setGeometry(new GeoJsonPolygon(points));
                }
                else if ("MultiPolygon".equalsIgnoreCase(geometryMap.get("type").toString()))
                {
                    List<GeoJsonPolygon> polygonList = new ArrayList<>();
                    List<List<List<?>>> coordinates = (List<List<List<?>>>) geometryMap.get("coordinates");
                    Assert.notNull(coordinates, "coordinates null for: " + countryName);
                    Assert.isTrue(coordinates.size() > 0, "coordinates is empty for: " + countryName);

                    for (List<List<?>> el : coordinates)
                    {
                        List<List<Double>> c2 = (List<List<Double>>) el.get(0);
                        Assert.notEmpty(c2, "c2 empty for: " + countryName);
                        List<Point> points = c2.stream()
                                .map(pointAsList -> new Point(pointAsList.get(0), pointAsList.get(1)))
                                .collect(Collectors.toList());
                        Assert.notEmpty(points, "points empty for: " + countryName);
                        polygonList.add(new GeoJsonPolygon(points));
                    }
                    Assert.notEmpty(polygonList, "polygonList empty for: " + countryName);
                    c.setGeometryMulti(new GeoJsonMultiPolygon(polygonList));
                }
            }
            else
            {
                logger.debug("------>> Country: {} is in {} but not in {}", countryName, GEOJSON_FILENAME, ORIGINAL_COUNTRY_FILENAME);
            }
        }
        countries.stream().filter(countryReview -> countryReview.getGeometry() == null
                && countryReview.getGeometryMulti() == null).forEach(c2 -> logger
                .debug("<------- Country: {} is in {} but not in {}", c2.getName(), ORIGINAL_COUNTRY_FILENAME,
                        GEOJSON_FILENAME));
        //save countries as new json
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(RESOURCES_DATA_PATH + NEW_COUNTRY_FILENAME), countries);
    }
}
