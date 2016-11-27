package coms6998.fall2016.managers;

import java.util.Set;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.config.DriverConfiguration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import coms6998.fall2016.models.Address;
import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.Episode;
import coms6998.fall2016.models.Franchise;
import coms6998.fall2016.models.Person;
import coms6998.fall2016.models.Property;
import coms6998.fall2016.models.Series;

public class Neo4jDBManager implements GraphDBManager {
	
	private Session session;

	public Neo4jDBManager() {
		Configuration configuration = new Configuration();
	             configuration.driverConfiguration()
	             .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
	             .setURI("http://serverless:3SqmIQ7C75bhLh6RbeJU@hobby-cbkknbeejildgbkehipmbhol.dbs.graphenedb.com:24789");
		SessionFactory sessionFactory = new SessionFactory(configuration, "coms6998.fall2016.models");
		session = sessionFactory.openSession();
		}
	
	public static void main(String args[]){
		Neo4jDBManager graphDBManager = new Neo4jDBManager();
		/*Person p1 = new Person();
		p1.setEmail("gaurav@gmail.com");
		p1.setFirstName("gaurav");
		p1.setLastName("singhal");
		graphDBManager.addPerson(p1);
		p1 = graphDBManager.getPerson(p1.getId());
		Person p2 = new Person();
		p2.setEmail("gaurav2@gmail.com");
		p2.setFirstName("gaurav2");
		p2.setLastName("singhal");
		p2.addFollows(p1);
		graphDBManager.addPerson(p1);*/
		Person p = graphDBManager.getPerson(13).cloneRemoveRecursiveRelationship();
		System.out.println(p.getFriends().size());
		for(Person friend: p.getFriends()){
			System.out.println(friend.getFriends().size());
		}
	}
	
	public DBReturnCode addComment(Comment comment) {
		session.save(comment);
		return DBReturnCode.Success;
	}
	
	public Comment getComment(long id){
		return session.load(Comment.class, id);
	}

	@Override
	public DBReturnCode addPerson(Person person) {
		session.save(person);
		return DBReturnCode.Success;
	}

	@Override
	public Person getPerson(long id) {
		Person person = session.load(Person.class, id);
		if(person.isDeleted()) {
			return null;
		}
		return person;
	}

	@Override
	public DBReturnCode deletePerson(Person person) {
		Person personToDelete = session.load(Person.class, person.getId());
		if(personToDelete != null) {
			personToDelete.setDeleted(true);
			session.save(personToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode updatePerson(Person person) {
		Person personToUpdate = this.getPerson(person.getId());
		if(personToUpdate != null){
			if(person.getFirstName() != null && !person.getFirstName().isEmpty()){
				personToUpdate.setFirstName(person.getFirstName());
			}
			if(person.getLastName() != null && !person.getLastName().isEmpty()){
				personToUpdate.setLastName(person.getLastName());
			}
			session.save(personToUpdate);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode addProperty(Property property) {
		session.save(property);
		return DBReturnCode.Success;
	}

	@Override
	public Property getProperty(long id) {
		Property property = session.load(Property.class, id);
		if(property.isDeleted()) {
			return null;
		}
		return property;
	}

	@Override
	public DBReturnCode deleteProperty(Property property) {
		Property propertyToDelete = getProperty(property.getId());
		if(propertyToDelete != null) {
			propertyToDelete.setDeleted(true);
			session.save(propertyToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode updateProperty(Property property) {
		Property oldProperty = session.load(Property.class, property.getId());
		if(oldProperty == null || oldProperty.isDeleted()) {
			return DBReturnCode.NotFound;
		} else{
			property.setName(property.getName());
		}
		session.save(property);
		return DBReturnCode.Success;
	}

	@Override
	public DBReturnCode addFranchise(Franchise franchise) {
		session.save(franchise);
		return DBReturnCode.Success;
	}

	@Override
	public Franchise getFranchise(long id) {
		Franchise franchise = session.load(Franchise.class, id);
		if(franchise.isDeleted()) {
			return null;
		}
		return franchise;
	}

	@Override
	public DBReturnCode deleteFranchise(Franchise franchise) {
		Franchise franchiseToDelete = getFranchise(franchise.getId());
		if(franchiseToDelete != null) {
			franchiseToDelete.setDeleted(true);
			session.save(franchiseToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode updateFranchise(Franchise franchise) {
		Franchise oldFranchise = session.load(Franchise.class, franchise.getId());
		if(oldFranchise == null || oldFranchise.isDeleted()) {
			return DBReturnCode.NotFound;
		} else{
			franchise.setName(franchise.getName());
		}
		session.save(franchise);
		return DBReturnCode.Success;
	}

	@Override
	public DBReturnCode addSeries(Series series) {
		session.save(series);
		return DBReturnCode.Success;
	}

	@Override
	public Series getSeries(long id) {
		Series series = session.load(Series.class, id);
		if(series.isDeleted()) {
			return null;
		}
		return series;
	}

	@Override
	public DBReturnCode deleteSeries(Series series) {
		Series seriesToDelete = getSeries(series.getId());
		if(seriesToDelete != null) {
			seriesToDelete.setDeleted(true);
			session.save(seriesToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode updateSeries(Series series) {
		Series oldSeries = session.load(Series.class, series.getId());
		if(oldSeries == null || oldSeries.isDeleted()) {
			return DBReturnCode.NotFound;
		} else{
			series.setName(series.getName());
		}
		session.save(series);
		return DBReturnCode.Success;
	}

	@Override
	public DBReturnCode addEpisode(Episode episode) {
		session.save(episode);
		return DBReturnCode.Success;
	}

	@Override
	public Episode getEpisode(long id) {
		Episode episode = session.load(Episode.class, id);
		if(episode.isDeleted()) {
			return null;
		}
		return episode;
	}

	@Override
	public DBReturnCode deleteEpisode(Episode episode) {
		Episode episodeToDelete = getEpisode(episode.getId());
		if(episodeToDelete != null) {
			episodeToDelete.setDeleted(true);
			session.save(episodeToDelete);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode updateEpisode(Episode episode) {
		Episode oldEpisode = session.load(Episode.class, episode.getId());
		if(oldEpisode == null || oldEpisode.isDeleted()) {
			return DBReturnCode.NotFound;
		} else{
			episode.setName(episode.getName());
		}
		session.save(episode);
		return DBReturnCode.Success;
	}

	@Override
	public DBReturnCode addComment(Person person, Comment comment) {
		person = this.getPerson(person.getId());
		if(person != null){
			person.addComment(comment);
			session.save(person);
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public Set<Comment> getComments(Person person) {
		person = this.getPerson(person.getId());
		if(person != null){
			return person.getComments();
		}
		return null;
	}

	@Override
	public DBReturnCode deleteComment(Comment comment) {
		comment = this.getComment(comment.getId());
		if(comment != null){
			session.delete(comment);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode updateComment(Comment comment) {
		Comment commentToUpdate = this.getComment(comment.getId());
		if(commentToUpdate != null){
			if(comment.getText()!=null && !comment.getText().isEmpty()){
				commentToUpdate.setText(comment.getText());
			}
			session.save(commentToUpdate);
			return DBReturnCode.Success;
		}
		return DBReturnCode.NotFound;
	}

	@Override
	public DBReturnCode addFriend(Person p1, Person p2) {
		p1 = this.getPerson(p1.getId());
		p2 = this.getPerson(p2.getId());
		if(p1 != null && p2 != null) {
			p1.addFriend(p2);
			session.save(p1);
			p2.addFriend(p1);
			session.save(p2);
			return DBReturnCode.NotFound;
		}
		return DBReturnCode.Success;
	}

	@Override
	public Set<Person> getFriends(Person person) {
		person = this.getPerson(person.getId());
		if(person != null){
			return person.getFriends();
		}
		return null;
	}

	@Override
	public DBReturnCode addFollows(Person p1, Person p2) {
		p1 = this.getPerson(p1.getId());
		p2 = this.getPerson(p2.getId());
		if(p1 != null && p2 != null) {
			p1.addFollows(p2);
			p2.addFollowedBy(p1);
			session.save(p1);
			session.save(p2);
			return DBReturnCode.NotFound;
		}
		return DBReturnCode.Success;
	}

	@Override
	public Set<Person> getFollows(Person person) {
		person = this.getPerson(person.getId());
		if(person != null){
			return person.getFollows();
		}
		return null;
	}

	@Override
	public Set<Person> getFollowedBy(Person person) {
		person = this.getPerson(person.getId());
		if(person != null){
			return person.getFollowedBy();
		}
		return null;
	}

	@Override
	public Set<Comment> getComments(Property property) {
		property = session.load(Property.class, property.getId());
		if (property.isDeleted()) {
			return null;
		}
		return property.getComments();
	}

	@Override
	public Set<Comment> getComments(Franchise franchise) {
		franchise = session.load(Franchise.class, franchise.getId());
		if (franchise.isDeleted()) {
			return null;
		}
		return franchise.getComments();
	}

	@Override
	public Set<Comment> getComments(Series series) {
		series = session.load(Series.class, series.getId());
		if (series.isDeleted()) {
			return null;
		}
		return series.getComments();
	}

	@Override
	public Set<Comment> getComments(Episode episode) {
		episode = session.load(Episode.class, episode.getId());
		if (episode.isDeleted()) {
			return null;
		}
		return episode.getComments();
	}
	
	@Override
	public DBReturnCode addComments(Comment comment, Property property) {
		property = session.load(Property.class, property.getId());
		if (property.isDeleted()) {
			return null;
		}
		property.addComment(comment);
		session.save(property);
		return DBReturnCode.Success;
	}

	@Override
	public DBReturnCode addComments(Comment comment, Franchise franchise) {
		franchise = session.load(Franchise.class, franchise.getId());
		if (franchise.isDeleted()) {
			return null;
		}
		franchise.addComment(comment);
		session.save(franchise);
		return DBReturnCode.Success;
	}
	
	@Override
	public DBReturnCode addComments(Comment comment, Series series) {
		series = session.load(Series.class, series.getId());
		if (series.isDeleted()) {
			return null;
		}
		series.addComment(comment);
		session.save(series);
		return DBReturnCode.Success;
	}
	
	@Override
	public DBReturnCode addComments(Comment comment, Episode episode) {
		episode = session.load(Episode.class, episode.getId());
		if (episode.isDeleted()) {
			return null;
		}
		episode.addComment(comment);
		session.save(episode);
		return DBReturnCode.Success;
	}

}
