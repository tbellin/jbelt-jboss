/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbelt.people.model;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.logging.Logger;
import org.jboss.seam.examples.booking.i18n.DefaultBundleKey;
import org.jboss.seam.examples.booking.inventory.SearchCriteria;
import org.jboss.seam.examples.booking.model.Hotel;
import org.jboss.seam.examples.booking.model.Hotel_;
import org.jboss.seam.examples.booking.model.User;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;
import org.jboss.seam.international.status.builder.TemplateMessage;

/**
 * The view controller for registering a new person
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@Named
@Stateful
@SessionScoped
public class PersonAction {
	@PersistenceContext
	private EntityManager em;

    @Inject
    private Logger log;

	@Inject
	private Messages messages;

    @Inject
    private Instance<TemplateMessage> messageBuilder;

    
	@Inject
	private FacesContext facesContext;

    @Inject
    private SearchCriteria criteria;

    private List<Person> persons = new ArrayList<Person>();
    
	private UIInput usernameInput;

	private final Person newPerson = new Person();

    @Produces
    @Named
    public List<Person> getPersons() {
        return persons;
    }
	
	@NotNull
	@Size(min = 5, max = 15)
	private String confirmPassword;

	private boolean registered;

	private boolean registrationInvalid;

    private boolean nextPageAvailable = false;
    
    private Person personSelection;

    public void find() {
        criteria.firstPage();
        queryPersons(criteria);
    }

	
	public void register() {
		registered = true;
		em.persist(newPerson);

		messages.info(new DefaultBundleKey("registration_registered"))
		.defaults("You have been successfully registered as the person {0}! You can now login.")
		.params(newPerson.getFirstName());
	}

	public boolean isRegistrationInvalid() {
		return registrationInvalid;
	}

    public void nextPage() {
		log.info("misura della lista :" + criteria.getPageSize());
		log.info("con il criterio  :>" + criteria.getSearchPattern() + "<");
        criteria.nextPage();
        queryPersons(criteria);
    }

    public void previousPage() {
        criteria.previousPage();
        queryPersons(criteria);
    }

    public boolean isNextPageAvailable() {
        return nextPageAvailable;
    }

    public boolean isPreviousPageAvailable() {
        return criteria.getPage() > 0;
    }

	/**
	 * This method just shows another approach to adding a status message.
	 * <p>
	 * Invoked by:
	 * </p>
	 * 
	 * <pre>
	 * &lt;f:event type="preRenderView" listener="#{registrar.notifyIfRegistrationIsInvalid}"/>
	 * </pre>
	 */
	public void notifyIfRegistrationIsInvalid() {
		if (facesContext.isValidationFailed() || registrationInvalid) {
			messages.warn(new DefaultBundleKey("registration_invalid")).defaults(
			"Invalid registration. Please correct the errors and try again.");
		}
	}
	
	public void queryPersons(final SearchCriteria criteria) {
		
		log.info("misua della lista :" + criteria.getPageSize());
		
	        CriteriaBuilder builder = em.getCriteriaBuilder();
	        CriteriaQuery<Person> cquery = builder.createQuery(Person.class);
	        Root<Person> person = cquery.from(Person.class);
	        // QUESTION can like create the pattern for us?
	        cquery.select(person).where(
	                builder.or(builder.like(builder.lower(person.get(Person_.firstName)), criteria.getSearchPattern()),
	                        builder.like(builder.lower(person.get(Person_.lastName)), criteria.getSearchPattern())));

	        List<Person> results = em.createQuery(cquery).setMaxResults(criteria.getFetchSize())
	                .setFirstResult(criteria.getFetchOffset()).getResultList();

	        nextPageAvailable = results.size() > criteria.getPageSize();
	        if (nextPageAvailable) {
	            // NOTE create new ArrayList since subList creates unserializable list
	            persons = new ArrayList<Person>(results.subList(0, criteria.getPageSize()));
	        } else {
	            persons = results;
	        }
	        log.info(messageBuilder.get().text("Found {0} person(s) matching search term [ {1} ] (limit {2})")
	                .textParams(persons.size(), criteria.getQuery(), criteria.getPageSize()).build().getText());
	    }

    public void selectPerson(Person personSelection) {
        // NOTE get a fresh reference that's managed by the extended persistence context
        if (personSelection != null) {
            log.info(" -->" + personSelection.getFirstName() + "<>" + personSelection.getLastName()+"<--");
        }
    }

    public void updatePerson(Person personSelection) {
        // NOTE get a fresh reference that's managed by the extended persistence context
        if (personSelection != null) {
        	em.merge(personSelection);
        	em.flush();
            log.info(" -->" + personSelection.getFirstName() + "<>" + personSelection.getLastName()+"<--");
        }
    }

	@Produces
	@Named
	public Person getNewPerson() {
		return newPerson;
	}

	public UIInput getUsernameInput() {
		return usernameInput;
	}

	public void setUsernameInput(final UIInput usernameInput) {
		this.usernameInput = usernameInput;
	}


}
