package ui;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import models.*;

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
    buildLeftPanel();
    add(leftPanel, BorderLayout.WEST);
    buildRightPanel();
    add(rightPanel, BorderLayout.EAST);
  }
  
  private void buildLeftPanel(){
    leftPanel = new Panel();
    GridLayout bl = new GridLayout(6, 1);
    leftPanel.setLayout(bl);

    selector = new Choice();
    
    try {
      allProducts = Product.findAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    for (Product p : allProducts) {
      selector.add(p.getName());
    }
    leftPanel.add(new Label("Chosee a drink"));  
    leftPanel.add(selector);
    
    ingredients = new Choice();
    try {
      allIngredients = Ingredient.findAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (Ingredient i : allIngredients) {
      ingredients.add(i.getName());
    }

    leftPanel.add(new Label("Ingredient"));  
    leftPanel.add(ingredients);
    
    price = new JLabel();
    leftPanel.add(price);
    description = new JLabel();
    leftPanel.add(description);
  }
  
  private void buildRightPanel()
  {
    rightPanel = new Panel();
    rightPanel.setLayout(new BorderLayout());
    Image myPicture = null;
    myPicture = applet.getImage(applet.getCodeBase(), "test.jpg"); 
    productImage = new JLabel(new ImageIcon(myPicture));
    rightPanel.add(productImage);
    
    selector.addItemListener(new ProductSelectorListener());
    ingredients.addItemListener(new IngredientSelectorListener());
  }
  
  
  //Product selectior listener
  class ProductSelectorListener implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
      order = new Order();
      int selection = selector.getSelectedIndex();
      Product p = allProducts.get(selection);
      System.out.println(selection);
      System.out.println( p.getName());
      System.out.println( p.getImage());  
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
      Ingredient p = allIngredients.get(selection);
      System.out.println(selection);
      System.out.println( p.getName());
      order.decorate(p);
      price.setText("Total:" + order.total().toString());
      description.setText(order.description());
    }
  }
}


