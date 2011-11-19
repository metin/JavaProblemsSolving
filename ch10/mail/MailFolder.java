package mail;

import java.util.*;

public class MailFolder extends MailboxItem {

  public MailFolder(String name) {
    super(name, null);
  }

  public MailFolder(String name, MailFolder owner) {
    super(name, owner);
  }

  public List getItems() {
    return items;
  }

  public List getSubFolders() {
    List folders = new ArrayList();
    Iterator iterator = items.iterator();
    while (iterator.hasNext()) {
      Object item = iterator.next();
      if (item instanceof MailFolder) {
        folders.add(item);
      }
    }
    return folders;
  }

  public List getMails() {
    List mails = new ArrayList();
    Iterator iterator = items.iterator();
    while (iterator.hasNext()) {
      Object item = iterator.next();
      if (item instanceof Mail) {
        mails.add(item);
      }
    }
    return mails;
  }

  public void add(MailboxItem item) {
    items.add(item);
    item.setOwner(this);
  }

  public void add(MailboxItem[] items) {
    if (items != null) {
      for (int i = 0; i < items.length; i++) {
        add(items[i]);
      }
    }
  }

  public int count() {
    int count = 0;
    Iterator iterator = items.iterator();
    while (iterator.hasNext()) {
      Object item = iterator.next();
      if (item instanceof MailboxItem) {
        count += ((MailboxItem) item).count();
      }
    }
    return count;
  }

  public int countNewMail() {
    int count = 0;
    Iterator iterator = items.iterator();
    while (iterator.hasNext()) {
      Object item = iterator.next();
      if (item instanceof MailboxItem) {
        count += ((MailboxItem) item).countNewMail();
      }
    }
    return count;
  }

  protected List items = new ArrayList();

}
