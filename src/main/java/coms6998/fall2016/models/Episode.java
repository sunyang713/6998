package coms6998.fall2016.models;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Episode {

	@GraphId
	private Long id;
	
	private String name;
	
	private boolean isDeleted;
	
	@Relationship(type = "COMMENTS", direction=Relationship.UNDIRECTED)
	private Set<Comment> comments = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}
	
	public void addComment(Comment comment){
		this.comments.add(comment);
	}

	public void setDeleted(boolean b) {
		isDeleted = b;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
}
