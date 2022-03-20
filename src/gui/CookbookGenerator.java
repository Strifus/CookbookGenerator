package gui;

import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.Ingredient;
import core.Preparation;
import core.Recipe;


public class CookbookGenerator extends JPanel {
	private static final long serialVersionUID = 1L;

	private Recipe recipe;
	
	// Elements for menu bar
    protected JMenuItem mNew, mOpen, mSave, mExit;
	
    // Elements for input tab
    private JTextField tfTitle;
    private JTextField tfCategory;
	private JTextField tfSizeValue;
	private JTextField tfSizeUnit;
	private JTextField tfTimeValue;
	private JTextField tfTimeUnit;
	private ArrayList<JTextField> tfIngrValList;
	private ArrayList<JTextField> tfIngrUnitList;
	private ArrayList<JTextField> tfIngrNameList;
	private ArrayList<JTextArea> taPrepStepsList;
	private JPanel pIngrPanel;
	private JPanel pPrepPanel;
	
	// Elements for preview tab
	private JLabel lTitle;
	private JLabel lCategory;
	private JLabel lTime;
	private JLabel lIngredientsHeader;
	private JLabel lIngredients;
	private JLabel lPrepSteps;
//	private JTextArea taPrepSteps;
	
    public CookbookGenerator() {
        super(new GridLayout(1, 1));
        
        recipe = new Recipe("");
        
        // Initialization of text fields/areas
        tfIngrValList = new ArrayList<JTextField>();
        tfIngrUnitList = new ArrayList<JTextField>();
        tfIngrNameList = new ArrayList<JTextField>();
        taPrepStepsList = new ArrayList<JTextArea>();        
        
        
        JTabbedPane tabbedPane = new JTabbedPane();
    
        // Create and add input tab
        JComponent panelInput = makeInputPanel();
        tabbedPane.addTab("Input", null, panelInput, "Recipe input");
        
        // Create and add preview tab
        JComponent panelPreview = makeTextPanel("The preview of the recipe will be displayed here.");
        tabbedPane.addTab("Preview", null, panelPreview, "Recipe preview");
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
              JTabbedPane pane = (JTabbedPane) evt.getSource();
              if (pane.getSelectedIndex() == 1) {
            	  // Preview pane selected -> refresh preview pane
            	  updateRecipe();
              }
            }
          });
        
        // Add the tabbed pane to this panel.
        this.add(tabbedPane);
         
        // The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
        
    /**
     * Draws the panel with the general information about the recipe.
     * 
     * @return Panel with general information
     */
    private JPanel drawGeneralPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("General"));
    	panel.setLayout(new MigLayout("wrap 3", "[][fill,grow][fill]", "align top"));
    	
    	tfTitle = new JTextField();
    	tfCategory = new JTextField();
    	
    	panel.add(new JLabel("Recipe Title:"));
    	panel.add(tfTitle, "wrap");
    	
    	panel.add(new JLabel("Category:"));
    	panel.add(tfCategory, "wrap");
    	

    	tfSizeValue = new JTextField();
    	tfSizeUnit = new JTextField("");
    	tfTimeValue = new JTextField();
    	tfTimeUnit = new JTextField("");
    	
    	panel.add(new JLabel(" "), "span"); // row spacer
    	panel.add(new JLabel(""));			// column spacer
    	panel.add(new JLabel("Value"));
    	panel.add(new JLabel("Unit"));
    	
    	panel.add(new JLabel("Meal Size:"));
    	panel.add(tfSizeValue);
    	panel.add(tfSizeUnit, "span");
    	
    	panel.add(new JLabel("Preparation Time:"));
    	panel.add(tfTimeValue);
    	panel.add(tfTimeUnit);
    	
        return panel;
    }
    
    /**
     * Draws panel with list of ingredients.
     * 
     * @return Panel with ingredients
     */
    private JPanel drawIngredientsPanel() {
    	JPanel panel = new JPanel();
    	panel.setBorder(new TitledBorder("Ingredients"));
    	panel.setLayout(new MigLayout("wrap", "[fill][fill][fill,grow][][]", "align top"));
    	
    	panel.add(new JLabel("Value"));
    	panel.add(new JLabel("Unit"));
    	panel.add(new JLabel("Name"));
    	
    	// Add add and delete buttons
    	JButton bAdd = new JButton("\u2795");
    	JButton bDel = new JButton("\u2796");
    	
    	panel.add(bAdd);
    	panel.add(bDel);	
    	
    	bAdd.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	    	// Add text area to ingredient list
    	    	tfIngrValList.add(new JTextField());
    	    	tfIngrUnitList.add(new JTextField());
    	    	tfIngrNameList.add(new JTextField());
    	    	// Add text area to panel
    	    	panel.add(tfIngrValList.get(tfIngrValList.size()-1),"grow");
    	    	panel.add(tfIngrUnitList.get(tfIngrUnitList.size()-1),"grow");
    	    	panel.add(tfIngrNameList.get(tfIngrNameList.size()-1),"span,grow");
    	    	// Update panel
    	        panel.revalidate();
    	        
    	    }
    	});
    	
    	bDel.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	    	// Check if ingredient list has enough entries to delete one (at least one entry should remain in the list)
    	    	if (tfIngrNameList.size() > 1) {
    	    		// Remove the last text area from the panel
	    	    	panel.remove(tfIngrValList.get(tfIngrValList.size()-1));
	    	    	panel.remove(tfIngrUnitList.get(tfIngrUnitList.size()-1));
	    	    	panel.remove(tfIngrNameList.get(tfIngrNameList.size()-1));
	    	    	// Remove the last entry from the ingredient list
	    	    	tfIngrValList.remove(tfIngrValList.size()-1);
	    	    	tfIngrUnitList.remove(tfIngrUnitList.size()-1);
	    	    	tfIngrNameList.remove(tfIngrNameList.size()-1);
	    	    	// Update panel
	    	        panel.revalidate();
	    	        panel.updateUI();
    	    	}
    	    }
    	});
    	
    	// Add initial ingredient
    	tfIngrValList.add(new JTextField());
    	tfIngrUnitList.add(new JTextField());
    	tfIngrNameList.add(new JTextField());
    	panel.add(tfIngrValList.get(0),"grow");
    	panel.add(tfIngrUnitList.get(0),"grow");
    	panel.add(tfIngrNameList.get(0),"span,grow");
    	
    	return panel;
    }
    
    
    /**
     * Draws panel with list of preparation steps.
     * 
     * @return Panel with preparation steps
     */
    private JPanel drawPreparationPanel() {
    	JPanel panel = new JPanel();
    	panel.setBorder(new TitledBorder("Preparation Steps"));
    	panel.setLayout(new MigLayout("wrap", "[fill,grow][][]", "align top"));
    	
    	// Add label
    	panel.add(new JLabel("Step"));
    	
    	// Add add and delete buttons
    	JButton bAdd = new JButton("\u2795");
    	JButton bDel = new JButton("\u2796");
    	
    	panel.add(bAdd);
    	panel.add(bDel);	
    	
    	bAdd.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	    	// Add text area to preparation steps list
    	    	taPrepStepsList.add(new JTextArea());
    	    	// Add text area to panel
    	    	panel.add(taPrepStepsList.get(taPrepStepsList.size()-1),"span,grow");
    	    	// Update panel
    	        panel.revalidate();
    	    }
    	});
    	
    	bDel.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	    	// Check if preparation steps list has enough entries to delete one (at least one entry should remain in the list)
    	    	if (taPrepStepsList.size() > 1) {
    	    		// Remove the last text area from the panel
	    	    	panel.remove(taPrepStepsList.get(taPrepStepsList.size()-1));
	    	    	// Remove the last entry from the preparation steps list
	    	    	taPrepStepsList.remove(taPrepStepsList.size()-1);
	    	    	// Update panel
	    	        panel.revalidate();
	    	        panel.updateUI();
    	    	}
    	    }
    	});
    	
    	// Add initial preparation step
    	taPrepStepsList.add(new JTextArea());
    	panel.add(taPrepStepsList.get(0),"span,grow");
    	
    	return panel;
    }
        
    /**
     * Makes panel for first tab, the input panel.
     *  
     * @return Panel with the recipe inputs
     */
    private JComponent makeInputPanel() {
    	
    	JPanel panel = new JPanel(false);
    	panel.setLayout(new GridLayout(3, 1));
    	
    	pIngrPanel = drawIngredientsPanel();
    	pPrepPanel = drawPreparationPanel();
    	
    	panel.add(drawGeneralPanel());
    	panel.add(pIngrPanel);
    	panel.add(pPrepPanel);
    	
    	return panel;
    }

    /**
     * Makes panel for second tab, the output panel.
     *  
     * @return Panel with the recipe preview
     */
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("wrap", "[fill,grow]", "align top"));
        
        // Create & configure elements
        lTitle = new JLabel("Recipe Title");    
        lTitle.setHorizontalAlignment(JLabel.CENTER);
        lTitle.setFont(new Font("", Font.BOLD, 14));
        
        lCategory = new JLabel("Category");
        lCategory.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lTimeHeader = new JLabel("Preparation Time");
        lTimeHeader.setFont(new Font("", Font.BOLD, 12));
        
        lTime = new JLabel();
        
        lIngredientsHeader = new JLabel();
        lIngredientsHeader.setFont(new Font("", Font.BOLD, 12));
        
        lIngredients = new JLabel();
        
        JLabel lPrepStepsHeader = new JLabel("Preparation Steps");
        lPrepStepsHeader.setFont(new Font("", Font.BOLD, 12));
        lPrepSteps = new JLabel();
        
//        taPrepSteps = new JTextArea();
//        taPrepSteps.setEditable(false);
//        taPrepSteps.setBackground(getBackground());
        
        // Update element content
        this.updateRecipe();
        
        // Add elements to panel
        panel.add(lTitle);
        panel.add(lCategory);
        panel.add(new JLabel(" "));	// spacer
        panel.add(lTimeHeader);
        panel.add(lTime);
        panel.add(lIngredientsHeader);
        panel.add(lIngredients);
        panel.add(lPrepStepsHeader);
        panel.add(lPrepSteps);
//        panel.add(taPrepSteps);
        
        return panel;
    }
    
    /**
     * Initializes/Clears user input fields from input tab.
     */
    protected void initInputFields() {
    	// Clear general information text fields
    	tfTitle.setText("");
    	tfCategory.setText("");
    	tfSizeValue.setText("");
    	tfSizeUnit.setText("");
    	tfTimeValue.setText("");
    	tfTimeUnit.setText("");
    	
    	// Remove all but one ingredient
    	while (tfIngrNameList.size() > 1) {
    		// Remove the last text area from the panel
    		pIngrPanel.remove(tfIngrValList.get(tfIngrValList.size()-1));
    		pIngrPanel.remove(tfIngrUnitList.get(tfIngrUnitList.size()-1));
    		pIngrPanel.remove(tfIngrNameList.get(tfIngrNameList.size()-1));
	    	// Remove the last entry from the ingredient list
	    	tfIngrValList.remove(tfIngrValList.size()-1);
	    	tfIngrUnitList.remove(tfIngrUnitList.size()-1);
	    	tfIngrNameList.remove(tfIngrNameList.size()-1);
    	}
    	// Clear ingredient text fields
    	tfIngrValList.get(0).setText("");
    	tfIngrUnitList.get(0).setText("");
    	tfIngrNameList.get(0).setText("");
    	
    	// Remove all but one preparation step
    	while (taPrepStepsList.size() > 1) {
    		// Remove the last text area from the panel
	    	pPrepPanel.remove(taPrepStepsList.get(taPrepStepsList.size()-1));
	    	// Remove the last entry from the preparation steps list
	    	taPrepStepsList.remove(taPrepStepsList.size()-1);	
    	}
    	// Clear preparation step text area
    	taPrepStepsList.get(0).setText("");
    	
    	// Refresh GUI
    	this.revalidate();
    	this.updateUI();
    }
    
    private void updateInputFields() {
    	// Update general information text fields
    	tfTitle.setText(recipe.getName());
    	try {
    		// Check if category exists TODO: find a better way!
    		recipe.getCategory();
    	} catch (Exception e){
    		recipe.setCategory(""); // Default category
    	}
    	tfCategory.setText(recipe.getCategory());
    	tfSizeValue.setText(recipe.size.getValue());
    	tfSizeUnit.setText(recipe.size.getUnit());
    	tfTimeValue.setText(recipe.time.getValue());
    	tfTimeUnit.setText(recipe.time.getUnit());
    	
    	// First, remove all ingredients
    	while (tfIngrNameList.size() > 0) {
    		// Remove the last text area from the panel
    		pIngrPanel.remove(tfIngrValList.get(tfIngrValList.size()-1));
    		pIngrPanel.remove(tfIngrUnitList.get(tfIngrUnitList.size()-1));
    		pIngrPanel.remove(tfIngrNameList.get(tfIngrNameList.size()-1));
	    	// Remove the last entry from the ingredient list
	    	tfIngrValList.remove(tfIngrValList.size()-1);
	    	tfIngrUnitList.remove(tfIngrUnitList.size()-1);
	    	tfIngrNameList.remove(tfIngrNameList.size()-1);
    	}
    	// Second, add ingredients
    	for (Ingredient curIngr : recipe.ingredients) {
	    	// Add text area to ingredient list
	    	tfIngrValList.add(new JTextField(curIngr.value));
	    	tfIngrUnitList.add(new JTextField(curIngr.unit));
	    	tfIngrNameList.add(new JTextField(curIngr.name));
	    	// Add text area to panel
	    	pIngrPanel.add(tfIngrValList.get(tfIngrValList.size()-1),"grow");
	    	pIngrPanel.add(tfIngrUnitList.get(tfIngrUnitList.size()-1),"grow");
	    	pIngrPanel.add(tfIngrNameList.get(tfIngrNameList.size()-1),"span,grow");
    	}
    		
    	// First, remove all preparation steps
    	while (taPrepStepsList.size() > 0) {
    		// Remove the last text area from the panel
	    	pPrepPanel.remove(taPrepStepsList.get(taPrepStepsList.size()-1));
	    	// Remove the last entry from the preparation steps list
	    	taPrepStepsList.remove(taPrepStepsList.size()-1);	
    	}
    	// Second, add preparation steps
    	for (Preparation curPrepStep : recipe.preparation)
    	{
	    	// Add text area to preparation steps list
	    	taPrepStepsList.add(new JTextArea(curPrepStep.step));
	    	// Add text area to panel
	    	pPrepPanel.add(taPrepStepsList.get(taPrepStepsList.size()-1),"span,grow");
    	}
    	
    	// Refresh GUI
    	this.revalidate();
    	this.updateUI();
    }
    
    private void clearRecipe() {
    	recipe.setName("");
    	recipe.setCategory("");
    	recipe.size.setValue("");
    	recipe.size.setUnit("");
    	recipe.time.setValue("");
    	recipe.time.setUnit("");
    	recipe.clearIngredient();
    	recipe.clearPreparation();
    }
    
    /**
     * Creates new recipe object from specified JSON file and updates text fields.
     * 
     * @param fileName Name of JSON recipe file
     */
    protected void loadRecipe(String fileName) {
    	recipe = recipe.readJson(fileName);
    	this.updateInputFields();
    }
    
    /**
     * Updates recipe object with user data and writes to specified JSON file.
     * 
     * @param fileName Name of JSON recipe file
     */
    protected void saveRecipe(String fileName) {
    	this.updateRecipe();
    	recipe.writeJson(fileName);
    }
    
    /**
     * Reads data from user input fields, writes it to recipe object and updates the preview tab.
     */
    protected void updateRecipe() {   	
    	// Fetch data from input tab and update recipe object
    	recipe.setName(tfTitle.getText());
    	recipe.setCategory(tfCategory.getText());
    	recipe.size.setValue(tfSizeValue.getText());
    	recipe.size.setUnit(tfSizeUnit.getText());
    	recipe.time.setValue(tfTimeValue.getText());
    	recipe.time.setUnit(tfTimeUnit.getText());
    	
    	recipe.clearIngredient();
    	for (int i=0;i< tfIngrNameList.size();i++)
    	{
    		String name = tfIngrNameList.get(i).getText();
    		String value = tfIngrValList.get(i).getText();
    		String unit = tfIngrUnitList.get(i).getText();
    		recipe.addIngredient(new Ingredient(name, value, unit));
    	}
    	
    	recipe.clearPreparation();
    	for (JTextArea curStep : taPrepStepsList) {
    		recipe.addPreparation(new Preparation(curStep.getText()));
    	}
    	
    	// Update output tab with date from recipe object
    	lTitle.setText(recipe.getName());
    	lCategory.setText(recipe.getCategory());
//    	lTime.setText("  " + recipe.time.getValue() + " " + recipe.time.getUnit());
    	lTime.setText("<html><table><tr><td align=Right>" + recipe.time.getValue() + "</td><td align=Left>" + recipe.time.getUnit() + "</td></tr></table></html>");
    	lIngredientsHeader.setText("Ingredients for " + recipe.size.getValue() + " " + recipe.size.getUnit());
    	lIngredients.setText("Zutatenliste...");
    	
    	String ingredients = "";
    	for (Ingredient curIngr : recipe.ingredients) {
    		ingredients = ingredients + "<tr><td align=Right>" + curIngr.value + "</td><td align=Left>" + curIngr.unit + "</td><td>" + curIngr.name + "</td></tr>";
    	}
    	ingredients = "<html><table>" + ingredients + "</table></html>";
    	lIngredients.setText(ingredients);
    	
    	String prepSteps = "";
    	for (Preparation curStep : recipe.preparation) {
    		prepSteps = prepSteps + curStep.step + "<br/>";
    	}
    	prepSteps = "<html>" + prepSteps + "</html>";
    	lPrepSteps.setText(prepSteps);
    	
//    	prepSteps = "";
//    	for (String curStep : recipe.preparationList) {
//    		prepSteps = prepSteps + curStep + "\n";
//    	}
//    	taPrepSteps.setText(prepSteps);
    	
    	// Refresh GUI
    	this.revalidate();
    	this.updateUI();
    }
    
    /**
     * Creates menu bar for main window.
     * 
     * @return Menu bar object
     */
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
 
        // Create the menu bar
        menuBar = new JMenuBar();
 
        // Build the first menu
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
 
        // Create menu items and define action commands
        mNew = new JMenuItem("New", KeyEvent.VK_N);
        mNew.setActionCommand("New");

        mOpen = new JMenuItem("Open", KeyEvent.VK_O);
        mOpen.setActionCommand("Open");

        mSave = new JMenuItem("Save", KeyEvent.VK_S);
        mSave.setActionCommand("Save");

        mExit = new JMenuItem("Exit", KeyEvent.VK_E);
        mExit.setActionCommand("Exit");
        
        // Add action listener to menu items
        mNew.addActionListener(new MenuBarListener(this));
        mOpen.addActionListener(new MenuBarListener(this));
        mSave.addActionListener(new MenuBarListener(this));
        mExit.addActionListener(new MenuBarListener(this));
        
        // Add menu items to menu
        menu.add(mNew);
        menu.add(mOpen);
        menu.add(mSave);
        menu.add(new JSeparator(SwingConstants.HORIZONTAL));
        menu.add(mExit);
 
        return menuBar;
    }
    
    
    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("CookbookGenerator");
       
        CookbookGenerator cookBook = new CookbookGenerator();
        
        // Add menu to the window
        frame.setJMenuBar(cookBook.createMenuBar());
        
        // Add content to the window.
        frame.add(cookBook, BorderLayout.CENTER);
        
        // Display the window.
        frame.pack();
        frame.setVisible(true);
        
        // Configure window closing event
        frame.addWindowListener(new WindowAdapter() {
      	  public void windowClosing(WindowEvent we) {
      		cookBook.requestExit();
      	  }
      	});
    }
    
    /**
     * Close window if confirmation prompt closes with positive response.
     */
    protected void requestExit() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?\nAll unsaved data will be lost.", "Exit",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
	public static void main(String[] args) {
        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            	UIManager.put("swing.boldMetal", Boolean.FALSE);
            	createAndShowGUI();
            }
        });
	}

}
