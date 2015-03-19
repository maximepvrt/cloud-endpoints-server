package me.maximepvrt.endpoints;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.sun.tools.javac.comp.Todo;

import java.util.Collection;
import java.util.List;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Maxime on 19/03/15.
 */
public class ContactRepository {

    private static ContactRepository todoRepository = null;

    static {
        ObjectifyService.register(Contact.class);
    }

    private ContactRepository() {
    }

    public static synchronized ContactRepository getInstance() {
        if (null == todoRepository) {
            todoRepository = new ContactRepository();
        }
        return todoRepository;
    }

    public Collection<Contact> findContacts() {
        List<Contact> contacts = ofy().load().type(Contact.class).list();
        return contacts;
    }

    public Contact get(long id) {
        return ofy().load().type(Contact.class).id(Long.valueOf(id)).now();
    }

    public Contact create(Contact contact) {
        ofy().save().entity(contact).now();
        return contact;
    }

    public Contact update(Contact editedContact) {
        if (editedContact.getId() == null) {
            return null;
        }

        Contact contact = ofy().load().key(Key.create(Contact.class, editedContact.getId())).now();
        contact.setFirstName(editedContact.getFirstName());
        contact.setLastName(editedContact.getLastName());
        contact.setPhoneNumber(editedContact.getPhoneNumber());
        ofy().save().entity(contact).now();

        return contact;
    }

    public void remove(Contact c) {
        ofy().delete().entity(c).now();
    }
}
