package coms6998.fall2016.models;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Person {
	@GraphId
	private Long id;
	
	private String email;
	private String firstName;
	private String lastName;
	private boolean isDeleted;
	
	@Relationship(type = "FOLLOWS", direction=Relationship.OUTGOING)
	private Set<Person> follows = new HashSet<>();
	
	@Relationship(type = "FOLLOWED_BY", direction=Relationship.OUTGOING)
	private Set<Person> followedBy = new HashSet<>();
	
	@Relationship(type = "FRIENDS", direction=Relationship.UNDIRECTED)
	private Set<Person> friends = new HashSet<>();
	
	@Relationship(type = "COMMENTS", direction=Relationship.UNDIRECTED)
	private Set<Comment> comments = new HashSet<>();
	
	public Person() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Set<Person> getFollows() {
		return follows;
	}
	
	public Set<Person> getFollowedBy(){
		return followedBy;
	}

	public Set<Person> getFriends() {
		return friends;
	}

	public Set<Comment> getComments() {
		return comments;
	}
	
	public void addFollows(Person personToFollow) {
		this.follows.add(personToFollow);
		personToFollow.followedBy.add(this);
	}
	
	public void addFriend(Person newFriend){
		this.friends.add(newFriend);
		newFriend.friends.add(this);
	}
	
	public void addComment(Comment comment){
		this.comments.add(comment);
	}
}
