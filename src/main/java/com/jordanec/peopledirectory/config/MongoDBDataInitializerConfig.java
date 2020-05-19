package com.jordanec.peopledirectory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordanec.peopledirectory.model.Country;
import com.jordanec.peopledirectory.model.Person;
import com.jordanec.peopledirectory.repository.CountryRepository;
import com.jordanec.peopledirectory.service.PersonService;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoDBDataInitializerConfig
{
    private final Logger logger = LoggerFactory.getLogger(MongoDBDataInitializerConfig.class);

    @Value("${people-directory.mongodb.insert-initial-data.person:true}")
    private boolean INSERT_INITIAL_DATA_PERSON;
    @Value("${people-directory.mongodb.update-initial-data.person:false}")
    private boolean UPDATE_INITIAL_DATA_PERSON;
    @Value("${people-directory.mongodb.insert-initial-data.country:true}")
    private boolean INSERT_INITIAL_DATA_COUNTRY;
    @Value("${people-directory.mongodb.update-initial-data.country:false}")
    private boolean UPDATE_INITIAL_DATA_COUNTRY;
    @Value("${people-directory.mongodb.drop-before:false}")
    private boolean DROP_BEFORE;
    @Value("${people-directory.mongodb.custom-schema-validation.person:false}")
    private boolean CUSTOM_SCHEMA_VALIDATION_PERSON;
    @Value("${people-directory.mongodb.custom-schema-validation.country:false}")
    private boolean CUSTOM_SCHEMA_VALIDATION_COUNTRY;

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    MongoMappingContext mongoMappingContext;
    @Autowired
    PersonService personService;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    ObjectMapper objectMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void mongoDBInitializer()
    { // https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#reference
        try
        {
            if (DROP_BEFORE)
            {
                logger.info("mongoDBInitializer(): drop-db-before property is enabled hence dropping the DB...");
                dropDB();
            }
            //Countries collection
            createCountrySchema();
            seedCountryData();

            //Persons collection
            createPersonSchema();
            seedPersonData();
        }
        catch (Exception ex)
        {
            logger.error("mongoDBInitializer():", ex);
        }
    }

    protected void dropDB()
    {
        MongoDatabase mongoDatabase = mongoTemplate.getDb();
        logger.debug("dropDB(): Database name: {}", mongoDatabase.getName());
        mongoDatabase.drop();
    }

    protected void seedPersonData() throws IOException
    {
        Resource sourceData = new ClassPathResource("data/Person.json");
        List<Person> persons = objectMapper.readValue(sourceData.getFile(),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Person.class));
        long totalPersons = personService.count();

        if (totalPersons == 0 && INSERT_INITIAL_DATA_PERSON)
        {
            logger.debug("seedPersonData(): Inserting initial data...");
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            personService.insert(persons);
            stopWatch.stop();
            logger.debug("seedPersonData(): {} person documents inserted in {} ms", persons.size(),
                    stopWatch.getTotalTimeMillis());
        }
        else if ( (totalPersons != 0 && totalPersons < persons.size()) || UPDATE_INITIAL_DATA_PERSON)
        {
            logger.debug("seedPersonData(): Updating initial data...");
            personService.save(persons);
        } else
        {
            logger.debug("seedPersonData(): Data initialization skipped...");
        }
    }

    protected void seedCountryData() throws IOException
    {
        Resource sourceData = new ClassPathResource("data/Country.json");
        List<Country> countries = objectMapper.readValue(sourceData.getFile(),
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Country.class));
        long totalCountries = countryRepository.count();

        if (totalCountries == 0 && INSERT_INITIAL_DATA_COUNTRY)
        {
            logger.debug("seedCountryData(): Inserting initial data...");
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            countryRepository.insert(countries);
            stopWatch.stop();
            logger.debug("seedCountryData(): {} country documents inserted in {} ms", countries.size(),
                    stopWatch.getTotalTimeMillis());
        }
        else if ( (totalCountries != 0 && totalCountries < countries.size()) || UPDATE_INITIAL_DATA_COUNTRY)
        {
            logger.debug("seedCountryData(): Updating initial data...");
            countryRepository.saveAll(countries);
        } else
        {
            logger.debug("seedCountryData(): Data initialization skipped...");
        }
    }

    protected void createCountrySchema()
    {
        if (!mongoTemplate.collectionExists(Country.class))
        {
            if (CUSTOM_SCHEMA_VALIDATION_COUNTRY)
            {
                //MongoCollection<Document> countriesCollection =
                mongoTemplate.createCollection(Country.class, CollectionOptions.empty());
                //TODO: implement validations
            }
            else
            {
                //MongoCollection<Document> countriesCollection =
                mongoTemplate.createCollection(Country.class, CollectionOptions.empty());
            }

            checkIndexForCollection(Country.class);
        }
    }

    protected void createPersonSchema()
    {
        if (!mongoTemplate.collectionExists(Person.class))
        {
            if (CUSTOM_SCHEMA_VALIDATION_PERSON)
            {
                MongoJsonSchema personSchema = MongoJsonSchema.builder().properties(JsonSchemaProperty.int64("dni").gte(0),
                        JsonSchemaProperty.string("firstName").minLength(1).maxLength(40),
                        JsonSchemaProperty.string("lastName").maxLength(80),
                        JsonSchemaProperty.string("email").maxLength(50),
                        JsonSchemaProperty.string("gender").possibleValues("Female", "Male"),
                        JsonSchemaProperty.string("ipAddress").matching("([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])"),
                        JsonSchemaProperty.int64("mobile").gte(0), JsonSchemaProperty.date("dateOfBirth"),
                        JsonSchemaProperty.string("color"), JsonSchemaProperty.string("frequency"), JsonSchemaProperty.string("mac"), JsonSchemaProperty.string("language"),
                        JsonSchemaProperty.string("shirtSize"), JsonSchemaProperty.string("university"),
                        JsonSchemaProperty.object("country")).required("dni", "firstName", "email").build();
                //            MongoCollection<Document> personsCollection =
                mongoTemplate.createCollection(Person.class, CollectionOptions.empty().schema(personSchema));
            }
            else
            {
                mongoTemplate.createCollection(Person.class, CollectionOptions.empty());
            }
            checkIndexForCollection(Person.class);
        }
    }

    protected <T> void checkIndexForCollection(Class<T> tClass)
    {
        logger.info("checkIndexForCollection(): class: {}",tClass.getSimpleName());
        IndexOperations indexOps = mongoTemplate.indexOps(tClass);
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
        resolver.resolveIndexFor(tClass).forEach(i -> {
            indexOps.ensureIndex(i);
            logger.info("checkIndexForCollection(): indices: {}", i.getIndexKeys());
        });
    }
}
