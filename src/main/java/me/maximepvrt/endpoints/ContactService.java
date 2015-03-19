package me.maximepvrt.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Named;

@Api(
    name = "myContactApi",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE},
    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID},
    audiences = {Constants.ANDROID_AUDIENCE}
)
public class ContactService {

  @ApiMethod(name = "contact.add", httpMethod = ApiMethod.HttpMethod.POST)
  public Contact add(Contact c) {
    if(c != null)
      return ContactRepository.getInstance().create(c);
    return null;
  }

  @ApiMethod(name = "contact.update", httpMethod = ApiMethod.HttpMethod.PUT)
  public Contact update(Contact c) {
    if(c != null)
      return ContactRepository.getInstance().update(c);
    return null;
  }

  @ApiMethod(name = "contact.remove", httpMethod = ApiMethod.HttpMethod.DELETE)
  public Contact remove(@Named("id") String id) {
    Contact contact = null;
    if(id != null) {
      contact = get(id);
      ContactRepository.getInstance().remove(contact);
    }
    return contact;
  }

  @ApiMethod(name = "contact.all", httpMethod = ApiMethod.HttpMethod.GET)
  public Collection<Contact> getAll() {
      return ContactRepository.getInstance().findContacts();
  }

  @ApiMethod(name = "contact.get", httpMethod = ApiMethod.HttpMethod.GET)
  public Contact get(@Named("id") String id) {
    return ContactRepository.getInstance().get(Long.valueOf(id));
  }
}
