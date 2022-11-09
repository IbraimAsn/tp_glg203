package com.yaps.utils.model;

/**
 * Every domain object should extend this abstract class.
 */
public abstract class DomainObject {


	/**
	 * The unique identifier of the object.
	 */    
    protected String id;

	
	protected DomainObject() {
	}
	
	protected DomainObject(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns a short (one-line) rendering of this object.
	 * @return
	 */
	abstract public String shortDisplay();

}
