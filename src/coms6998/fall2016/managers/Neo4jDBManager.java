package coms6998.fall2016.managers;

import java.util.Set;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.config.DriverConfiguration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

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
	             .setURI("http://serverless:5Zq7mXpTExz2J8suICO1@hobby-ifegjbeejildgbkeoedfhhol.dbs.graphenedb.com:24789");
		SessionFactory sessionFactory = new SessionFactory(configuration, "coms6998.fall2016.models");
		session = sessionFactory.openSession();
		}
	
	public static void main(String args[]){
		Neo4jDBManager graphDBManager = new Neo4jDBManager();
		Person p1 = new Person();
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
		graphDBManager.addPerson(p1);
		System.out.println(p2.getEmail());
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
		return session.load(Person.class, id);
	}

	@Override
	public DBReturnCode deletePerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode updatePerson(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addProperty(Property property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Property getProperty(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode deleteProperty(Property property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode updateProperty(Property property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addFranchise(Franchise franchise) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Franchise getFranchise(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode deleteFranchise(Franchise franchise) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode updateFranchise(Franchise franchise) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addSeries(Series series) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Series getSeries(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode deleteSeries(Series series) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode updateSeries(Series series) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addEpisode(Episode episode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Episode getEpisode(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode deleteEpisode(Episode episode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode updateEpisode(Episode episode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addComment(Person person, Comment comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Comment> getComments(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode deleteComment(Comment comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode updateComment(Comment comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addFriend(Person p1, Person p2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Person> getFriends(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DBReturnCode addFollows(Person p1, Person p2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Person> getFollows(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Person> getFollowedBy(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Comment> getComments(Property property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Comment> getComments(Franchise franchise) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Comment> getComments(Series series) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Comment> getComments(Episode episode) {
		// TODO Auto-generated method stub
		return null;
	}

}
