package coms6998.fall2016.models;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Property {
	
	@GraphId
	private long id;
	
	private String name;
	
	@Relationship(type = "COMMENTS", direction=Relationship.UNDIRECTED)
	private Set<Comment> comments = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}
	
	public void addComment(Comment comment){
		this.comments.add(comment);
	}
}
