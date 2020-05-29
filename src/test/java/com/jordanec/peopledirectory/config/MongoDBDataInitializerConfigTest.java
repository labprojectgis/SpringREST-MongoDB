package com.jordanec.peopledirectory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jordanec.peopledirectory.model.Country;
import com.jordanec.peopledirectory.repository.CountryRepository;
import com.mongodb.client.MongoDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class MongoDBDataInitializerConfigTest
{
    @InjectMocks
    @Spy
    MongoDBDataInitializerConfig mongoDBDataInitializerConfig;
    @Mock
    MongoTemplate mongoTemplate;
    @Mock
    MongoDatabase mongoDatabase;
    @Mock
    CountryRepository countryRepository;
    @Mock
    ObjectMapper objectMapper;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void dropDB_disabled() throws IOException
    {
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "DROP_BEFORE", false);
        Mockito.doNothing().when(mongoDBDataInitializerConfig).createCountrySchema();
        Mockito.doNothing().when(mongoDBDataInitializerConfig).createPersonSchema();
        Mockito.doNothing().when(mongoDBDataInitializerConfig).seedCountryData();
        Mockito.doNothing().when(mongoDBDataInitializerConfig).seedPersonData();
        mongoDBDataInitializerConfig.mongoDBInitializer();
        Mockito.verify(mongoDBDataInitializerConfig, Mockito.times(0)).dropDB();
    }

    @Test
    public void dropDB_enabled() throws IOException
    {
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "DROP_BEFORE", true);
        //        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "INSERT_INITIAL_DATA_PERSON", false);
        //        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "INSERT_INITIAL_DATA_COUNTRY", false);
        Mockito.doNothing().when(mongoDBDataInitializerConfig).createCountrySchema();
        Mockito.doNothing().when(mongoDBDataInitializerConfig).createPersonSchema();
        Mockito.doNothing().when(mongoDBDataInitializerConfig).seedCountryData();
        Mockito.doNothing().when(mongoDBDataInitializerConfig).seedPersonData();
        Mockito.doNothing().when(mongoDatabase).drop();
        Mockito.doReturn(mongoDatabase).when(mongoTemplate).getDb();

//        PowerMockito.mockStatic(LoggerFactory.class);
//        Logger logger = PowerMockito.mock(Logger.class);
//        PowerMockito.when(LoggerFactory.getLogger(MongoDBDataInitializerConfig.class)).thenReturn(logger);
        mongoDBDataInitializerConfig.mongoDBInitializer();
        Mockito.verify(mongoDBDataInitializerConfig, Mockito.times(1)).dropDB();
//        Mockito.verify(logger, Mockito.never())
//                .error(ArgumentMatchers.anyString(), ArgumentMatchers.any(Exception.class));
//        Mockito.verify(logger, Mockito.times(1))
//                .info(ArgumentMatchers.anyString());
//        Mockito.verify(logger, Mockito.times(1))
//                .debug(ArgumentMatchers.anyString(),ArgumentMatchers.anyString());
    }

    @Test
    public void createCountrySchema_collectionExists()
    {
        Mockito.when(mongoTemplate.collectionExists(ArgumentMatchers.eq(Country.class))).thenReturn(true);

        mongoDBDataInitializerConfig.createCountrySchema();

        Mockito.verify(mongoDBDataInitializerConfig, Mockito.times(0))
                .checkIndexForCollection(ArgumentMatchers.eq(Country.class));
    }

    @Test
    public void createCountrySchema_collectionDoesNotExists()
    {
        Mockito.when(mongoTemplate.collectionExists(ArgumentMatchers.eq(Country.class))).thenReturn(false);

        Mockito.when(mongoTemplate
                .createCollection(ArgumentMatchers.eq(Country.class), ArgumentMatchers.eq(CollectionOptions.empty())))
                .thenReturn(null);

        Mockito.doNothing().when(mongoDBDataInitializerConfig)
                .checkIndexForCollection(ArgumentMatchers.eq(Country.class));

        mongoDBDataInitializerConfig.createCountrySchema();

        Mockito.verify(mongoDBDataInitializerConfig, Mockito.times(1))
                .checkIndexForCollection(ArgumentMatchers.eq(Country.class));
    }

    @Test
    public void seedCountryData_NoCountriesAndInsertEnabled() throws IOException
    {
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "INSERT_INITIAL_DATA_COUNTRY", true);
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "objectMapper", objectMapper);
        Mockito.doReturn(mockCountryList()).when(objectMapper).readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(CollectionType.class));
        Mockito.doReturn(0L).when(countryRepository).count();
        Mockito.doReturn(null).when(countryRepository).insert(ArgumentMatchers.any(List.class));
        mongoDBDataInitializerConfig.seedCountryData();
        //@TODO: asserts/verifies over logs
    }
    @Test
    public void seedCountryData_UpdateEnabled() throws IOException
    {
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "INSERT_INITIAL_DATA_COUNTRY", false);
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "UPDATE_INITIAL_DATA_COUNTRY", true);
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "objectMapper", objectMapper);
        Mockito.doReturn(mockCountryList()).when(objectMapper).readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(CollectionType.class));
        Mockito.doReturn(100L).when(countryRepository).count();
        Mockito.doReturn(null).when(countryRepository).saveAll(ArgumentMatchers.any(List.class));
        mongoDBDataInitializerConfig.seedCountryData();
        //@TODO: asserts/verifies over logs
    }
    @Test
    public void seedCountryData_InsertAndUpdateDisabled() throws IOException
    {
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "INSERT_INITIAL_DATA_COUNTRY", false);
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "UPDATE_INITIAL_DATA_COUNTRY", false);
        ReflectionTestUtils.setField(mongoDBDataInitializerConfig, "objectMapper", objectMapper);
        Mockito.doReturn(mockCountryList()).when(objectMapper).readValue(ArgumentMatchers.any(File.class), ArgumentMatchers.any(CollectionType.class));
        Mockito.doReturn(249L).when(countryRepository).count();
        mongoDBDataInitializerConfig.seedCountryData();
        //@TODO: asserts/verifies over logs
    }
    private List<Country> mockCountryList()
    {
        List<Country> countries = new ArrayList<>();
        for (int i = 0; i < 249; i++)
        {
            countries.add(new Country());
        }
        return countries;
    }
}
