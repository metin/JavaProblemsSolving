package ui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import models.*;
import utils.*;

public class ContentPanel extends Panel  {
  private Panel leftPanel;
  private Panel rightPanel;
  private Choice selector;
  private Choice ingredients;
  private ArrayList<Product> allProducts = null;
  private ArrayList<Ingredient> allIngredients = null;
  private JLabel productImage;
  private Applet applet;
  private Order order;
  private JLabel price;
  private JLabel description;
  DefaultListModel ordersListModel = new DefaultListModel();  
  JList orderList = new JList(ordersListModel);
  
  public ContentPanel(Applet parent)
  {
    super();
    order = new Order();
    this.applet = parent;
    setUI();
    
  }
  
  private void setUI()
  {
    this.setLayout(new BorderLayout());
    add(new Label("Drink Station", Label.CENTER) , BorderLayout.NORTH);
    buildLeftPanel();
    buildRightPanel();
    buildSouthPanel();
  }
  
  private void buildLeftPanel(){
    leftPanel = new Panel();
    GridLayout bl = new GridLayout(7, 1, 0, 0);
    leftPanel.setLayout(bl);
    selector = new Choice();
    try {
      allProducts = Product.findAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    selector.add("");
    for (Product p : allProducts) {
      selector.add(p.getName());
    }
    leftPanel.add(new Label("Chosee a drink:"));  
    leftPanel.add(selector);
    
    ingredients = new Choice();
    try {
      allIngredients = Ingredient.findAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ingredients.add("");
    for (Ingredient i : allIngredients) {
      ingredients.add(i.getName());
    }

    leftPanel.add(new Label("Ingredients"));  
    leftPanel.add(ingredients);
    

    price = new JLabel();
    leftPanel.add(price);
    description = new JLabel();
    description.setSize(500, 300);
    leftPanel.add(description);
    add(leftPanel, BorderLayout.CENTER);
    selector.addItemListener(new ProductSelectorListener());
    ingredients.addItemListener(new IngredientSelectorListener());
  }
  
  private void buildRightPanel()
  {
    rightPanel = new Panel();
    rightPanel.setLayout(new BorderLayout());
    Image myPicture = null;
    myPicture = applet.getImage(applet.getCodeBase(), "home.jpg"); 
    productImage = new JLabel(new ImageIcon(myPicture));
    rightPanel.add(productImage);
    add(rightPanel, BorderLayout.EAST);
  }
  
  private void buildSouthPanel()
  {
    Panel southPanel = new Panel();
    southPanel.setLayout(new BorderLayout());
    Button brewBtn = new Button("Prepare");
    southPanel.add(brewBtn, BorderLayout.NORTH);
    
    orderList.setLayoutOrientation(JList.VERTICAL);
    orderList.setVisibleRowCount(500);
    
    JScrollPane listScroller = new JScrollPane(orderList);
    listScroller.setPreferredSize(new Dimension(250, 100));
    southPanel.add(listScroller, BorderLayout.SOUTH);
    add(southPanel, BorderLayout.SOUTH);
    brewBtn.addActionListener(new BrewActionListener());
    
    ArrayList<Order> orders;
    try {
      orders = Order.findAll();
      for (Order i : orders) {
        ordersListModel.addElement(i.id + " - " + i.description + " - " + i.price); 
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  //Product selectior listener
  class ProductSelectorListener implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      int selection = selector.getSelectedIndex();
      if(selection==0) return;
      Product p = allProducts.get(selection-1);  
      order = new Order();
      Image myPicture = applet.getImage(applet.getCodeBase(), p.getImage()); 
      order.decorate(p);
      productImage.setIcon(new ImageIcon(myPicture));
      price.setText("Total:" + order.total().toString());
      description.setText(order.description());

    }
  }

  class IngredientSelectorListener implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      int selection = ingredients.getSelectedIndex();
      if(selection==0) return;
      Ingredient p = allIngredients.get(selection-1);
      order.decorate(p);
      price.setText("Total:" + order.total().toString());
      description.setText(order.description());
    }
  }

  class BrewActionListener implements ActionListener {

    public void actionPerformed(ActionEvent arg0) {
      if(order==null) return; 
      Image myPicture = applet.getImage(applet.getCodeBase(), "brewing1.gif"); 
      productImage.setIcon(new ImageIcon(myPicture));
      applet.repaint();
      try {
        order.save();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      
      ordersListModel.clear();
      ArrayList<Order> orders = null;
      try {
        orders = Order.findAll();
        for (Order i : orders) {
          ordersListModel.addElement(i.id + " - " + i.description + " - " + i.price); 
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      myPicture = applet.getImage(applet.getCodeBase(), "home.jpg"); 
      productImage.setIcon(new ImageIcon(myPicture));

      AudioClip audio = AudioUtility.getAudioClip("audio/drip.au");
      audio.play();
      price.setText("Order placed: " + orders.get(0).id);
      description.setText("");
    }
  }

}


