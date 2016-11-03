package coms6998.fall2016.managers;

import java.util.Set;

import coms6998.fall2016.models.Comment;
import coms6998.fall2016.models.DBReturnCode;
import coms6998.fall2016.models.Episode;
import coms6998.fall2016.models.Franchise;
import coms6998.fall2016.models.Person;
import coms6998.fall2016.models.Property;
import coms6998.fall2016.models.Series;

public interface GraphDBManager {
	
	public DBReturnCode addPerson(Person person);
	public Person getPerson(long id);
	public DBReturnCode deletePerson(Person person);
	public DBReturnCode updatePerson(Person person);
	
	public DBReturnCode addComment(Person person, Comment comment);
	public Set<Comment> getComments(Person person);
	
	public Comment getComment(long id);
	public DBReturnCode deleteComment(Comment comment);
	public DBReturnCode updateComment(Comment comment);
	
	public DBReturnCode addFriend(Person p1, Person p2);
	public Set<Person> getFriends(Person person);
	public DBReturnCode addFollows(Person p1, Person p2);
	public Set<Person> getFollows(Person person);
	public Set<Person> getFollowedBy(Person person);
	
	
	public DBReturnCode addProperty(Property property);
	public Property getProperty(long id);
	public DBReturnCode deleteProperty(Property property);
	public DBReturnCode updateProperty(Property property);
	public Set<Comment> getComments(Property property);
	
	public DBReturnCode addFranchise(Franchise franchise);
	public Franchise getFranchise(long id);
	public DBReturnCode deleteFranchise(Franchise franchise);
	public DBReturnCode updateFranchise(Franchise franchise);
	public Set<Comment> getComments(Franchise franchise);
	
	public DBReturnCode addSeries(Series series);
	public Series getSeries(long id);
	public DBReturnCode deleteSeries(Series series);
	public DBReturnCode updateSeries(Series series);
	public Set<Comment> getComments(Series series);
	
	
	public DBReturnCode addEpisode(Episode episode);
	public Episode getEpisode(long id);
	public DBReturnCode deleteEpisode(Episode episode);
	public DBReturnCode updateEpisode(Episode episode);
	public Set<Comment> getComments(Episode episode);
}
