package coms6998.fall2016.models.contentcatalog;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DynamoDBTable(tableName = "__NO_TABLE_NAME__")
public abstract class ContentModel {

	private String id;
	private String uuid;
	private String name;
	private Long version;
	private Date createdOnTimestamp;
	private Date updatedOnTimestamp;
	private boolean isDeleted;
	private String ref; // should overwrite

	@DynamoDBHashKey
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBAttribute
    @DynamoDBAutoGeneratedKey
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@DynamoDBAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@DynamoDBAttribute
	@DynamoDBTyped(DynamoDBAttributeType.BOOL)
	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@DynamoDBAttribute
	public Date getCreatedOnTimestamp() {
		return createdOnTimestamp;
	}

	public void setCreatedOnTimestamp(Date createdOnTimestamp) {
		this.createdOnTimestamp = createdOnTimestamp;
	}

	@DynamoDBAttribute
	public Date getUpdatedOnTimestamp() {
		return updatedOnTimestamp;
	}

	public void setUpdatedOnTimestamp(Date updatedOnTimestamp) {
		this.updatedOnTimestamp = updatedOnTimestamp;
	}
	
	@DynamoDBVersionAttribute
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * Merges this (old) with a new instance of ContentDynamoDBModel.
	 * Does so by updating null fields to the values in oldContent.
	 * @param newContent new content from which to update.
	 * @return
	 */
	public ContentModel updateFromNew(ContentModel newContent) {
		this.name = newContent.getName() == null ? this.name : newContent.getName();
		this.updatedOnTimestamp = new Date();
		return this;
	}
	
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

}
