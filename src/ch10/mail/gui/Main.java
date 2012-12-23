package mail.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import mail.*;
import adapter.*;

public class Main {

  public static final int INITIAL_FRAME_WIDTH = 800;
  public static final int INITIAL_FRAME_HEIGHT = 400;

  public static void main(String[] args) {
    MailFolder inboxFolder = mail.Main.buildInbox();
    JTree tree = new JTree(buildMailFolderTree(inboxFolder));
    JSplitPane splitPane = new JSplitPane();
    splitPane.setLeftComponent(new JScrollPane(tree));
    splitPane.setRightComponent(new JPanel());
    tree.addTreeSelectionListener(new MailFolderTreeSelectionListener(tree,
        splitPane));

    JFrame frame = new JFrame("Mails");
    frame.setContentPane(splitPane);
    frame.setSize(INITIAL_FRAME_WIDTH, INITIAL_FRAME_HEIGHT);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width / 2 - INITIAL_FRAME_WIDTH / 2,
        screenSize.height / 2 - INITIAL_FRAME_HEIGHT / 2);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  static class MailFolderTreeSelectionListener implements TreeSelectionListener {

    MailFolderTreeSelectionListener(JTree tree, JSplitPane splitPane) {
      this.tree = tree;
      this.splitPane = splitPane;
    }

    public void valueChanged(TreeSelectionEvent ev) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
          .getLastSelectedPathComponent();
      if (node != null) {
        Object item = node.getUserObject();
        if (item instanceof MailFolder) {
          splitPane.setRightComponent(new JScrollPane(
              buildTable((MailFolder) item)));
        }
      }
    }

    JTree tree;
    JSplitPane splitPane;
  }

  protected static DefaultMutableTreeNode buildMailFolderTree(MailFolder folder) {
    if (folder != null) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode(folder);
      List subfolders = folder.getSubFolders();
      Iterator iterator = subfolders.iterator();
      while (iterator.hasNext()) {
        Object item = iterator.next();
        if (item instanceof MailFolder) {
          root.add(buildMailFolderTree((MailFolder) item));
        }
      }
      return root;
    }
    return null;
  }

  protected static Table buildTable(MailFolder folder) {
    if (folder != null) {
      List mails = folder.getMails();
      List entries = new ArrayList(mails.size());
      for (int i = 0; i < mails.size(); i++) {
        entries.add(new MailEntry((Mail) mails.get(i)));
      }
      return new Table(entries);
    }
    return null;
  }

}
