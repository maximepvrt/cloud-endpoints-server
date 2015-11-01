package me.maximepvrt.endpoints;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import java.util.Collection;
import java.util.Locale;

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
  public Contact add(User user, Contact c) throws UnauthorizedException, BadRequestException {
    if(user == null)
      throw new UnauthorizedException("User not found");
    if(c != null) {
      c.setUser(user.getUserId());
      return ContactRepository.getInstance().create(c);
    }
    else
      throw new BadRequestException("Contact not found");
  }

  @ApiMethod(name = "contact.update", httpMethod = ApiMethod.HttpMethod.PUT)
  public Contact update(User user, Contact c) throws UnauthorizedException, BadRequestException {
    if(user == null)
      throw new UnauthorizedException("User not found");
    if(c != null) {
      if(c.getUser().equals(user.getUserId()))
        return ContactRepository.getInstance().update(c);
      else
        throw new UnauthorizedException("You don't can see, edit or remove this contact");
    }
    else
      throw new BadRequestException("Contact not found");
  }

  @ApiMethod(name = "contact.remove", httpMethod = ApiMethod.HttpMethod.DELETE)
  public Contact remove(User user, @Named("id") String id) throws UnauthorizedException, BadRequestException, NotFoundException {
    if(user == null)
      throw new UnauthorizedException("User not found");
    Contact contact = null;
    if(id != null) {
      contact = get(user, id);
      ContactRepository.getInstance().remove(user.getUserId(), contact);
    } else {
      throw new BadRequestException("ContactId not found");
    }
    return contact;
  }

  @ApiMethod(name = "contact.all", httpMethod = ApiMethod.HttpMethod.GET)
  public Collection<Contact> getAll(User user) throws UnauthorizedException {
    if(user == null)
      throw new UnauthorizedException("User not found");
    return ContactRepository.getInstance().findContactsByUser(user.getUserId());
  }

  @ApiMethod(name = "contact.get", httpMethod = ApiMethod.HttpMethod.GET)
  public Contact get(User user, @Named("id") String id) throws NotFoundException, UnauthorizedException, BadRequestException {
    if(user == null)
      throw new UnauthorizedException("User not found");
    if(id != null) {
      Contact c = ContactRepository.getInstance().get(Long.valueOf(id));
      if(c == null)
        throw new NotFoundException("Contact not found");
      else {
        if (c.getUser().equals(user.getUserId()))
          return c;
        else
          throw new UnauthorizedException("You don't can see, edit or remove this contact");
      }
    } else {
      throw new BadRequestException("ContactId not found");
    }
  }
}
