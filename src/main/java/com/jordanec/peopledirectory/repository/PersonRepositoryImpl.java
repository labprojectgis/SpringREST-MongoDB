package com.jordanec.peopledirectory.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.client.result.DeleteResult;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import com.jordanec.peopledirectory.model.Person;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepositoryCustom {

	private final MongoOperations mongoOperations;

	@Override
	public List<Person> findBornBetween(LocalDate start, LocalDate end) {
		Query query = new Query(Criteria.where("dateOfBirth").gte(start).lte(end));
		return mongoOperations.find(query, Person.class);
	}

	public long getCountByCountry(String country)
	{
		Query query = new Query(Criteria.where("country").is(country));
		return mongoOperations.count(query, Person.class);
	}

	public List<Person> groupByCountry(String field, String order)
	{
		return groupDocumentByCountry(field, order).getMappedResults();
	}

	public Document groupDocumentByCountryOrdered(String field, String order)
	{
		return groupDocumentByCountry(field, order).getRawResults();
	}

	//Console equivalent:
	//db.persons.aggregate([ {$lookup: {from: "countries", localField: "country._id", foreignField: "_id", as: "country"}}, {$unwind: {path: '$country' }} ]).pretty()
	@Override
	public List<Person> lookupCountry(long dni)
	{
		MatchOperation matchOperation = Aggregation.match(Criteria.where("dni").is(dni));
		LookupOperation lookupOperation = Aggregation.lookup("countries", "country._id", "_id", "country");
		UnwindOperation unwindOperation = Aggregation.unwind("country");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupOperation, unwindOperation);
		return mongoOperations.aggregate(aggregation, Person.class, Person.class).getMappedResults();
	}

	private AggregationResults<Person> groupDocumentByCountry(String field, String order)
	{
		GroupOperation groupOperation = Aggregation.group("country").count().as("total");

		SortOperation sortOperation = Aggregation
				.sort(order != null && order.toLowerCase().contains("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
						field != null && field.equalsIgnoreCase("total") ? "total" : "country");
		ProjectionOperation projectionOperation = Aggregation.project()
				.andExpression("_id").as("country")
				.andExpression("total").as("total")
				.andExclude("_id");

		Aggregation aggregation = Aggregation.newAggregation(groupOperation, projectionOperation, sortOperation);
		return mongoOperations.aggregate(aggregation, Person.class, Person.class);
	}

	public Person isOlderThan(long dni, int age)
	{
		LocalDate minimumBornDate = LocalDate.now().minusYears(age);
		MatchOperation matchOperation =
				Aggregation.match(
					Criteria.where("dni").is(dni).and("dateOfBirth").lte(minimumBornDate));
		ProjectionOperation projectionOperation =
			Aggregation.project()
				.andInclude("dni")
				.andInclude("firstName")
				.andInclude("lastName")
				.andInclude("dateOfBirth")
				.andExpression("[0] + 0", age).as("age")	//Adding 0 was needed to show it
				.andExclude("_id");
		Aggregation aggregation = Aggregation.newAggregation(matchOperation, projectionOperation);
		List<Person> personList = mongoOperations.aggregate(aggregation, Person.class, Person.class).getMappedResults();
		if (personList.isEmpty())
		{
			return null;
		}
		else
		{
			return personList.get(0);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Person> delete(List<Person> persons)
	{
		for (Person person: persons)
		{
			Query query = new Query(Criteria.where("dni").is(person.getDni()));
			DeleteResult deleteResult = mongoOperations.remove(query, Person.class);
			if (deleteResult.getDeletedCount() == 0)
			{
				persons.remove(person);
			}
		}
		return persons;
//		Query query = new Query(Criteria.where("dni").in(persons.stream().map(Person::getDni).collect(Collectors.toList())));
//		return mongoOperations.findAllAndRemove(query, Person.class);
	}

	/**
	 Test
	 @TODO: Implement some date operations
	 */
	public Document test()
	{
		//		ProjectionOperation projectionOperation =
		//			Aggregation.project("age", "customerId", "items")
		//                .andExpression("'$items.price' * '$items.quantity'").as("lineTotal");
		//		Criteria.where("dateBeg").lte(from).lte(to);
		return mongoOperations.aggregate(
				Aggregation.newAggregation(
						Aggregation.project("dni", "firstName", "lastName", "dateOfBirth", "email", "country")
						//					.and("dateOfBirth").minus("dateOfBirth").as("minus"));
						//					.and(DateOperators.dateFromString("06/05/2020")).minus("dateOfBirth").as("minus")
						//					.andExpression("06/05/2020 - $dateOfBirth", "dateOfBirth").as("result")
				), Person.class, Person.class).getRawResults();
	}
	/**
	 Test
	 @TODO: Implement some update operations
	 */
	@Override
	public Person update(Person update)
	{
		Update update1 = new Update();
		//		mongoOperations.update(Person.class).apply();
		return null;
	}
}
