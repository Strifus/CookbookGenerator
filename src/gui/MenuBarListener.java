package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuBarListener implements ActionListener  {
    private CookbookGenerator gui;

    /**
     * Listen to actions at the window's menu bar
     * 
     * @param gui CookbookGenerator window
     */
    protected MenuBarListener(CookbookGenerator gui) {
        this.gui = gui;
    }
    
    /**
     * Do something (create new file, open/save file, exit GUI) depending on action at menu bar.
     *
     * @param e Action event from action event listener
     */
    public void actionPerformed(ActionEvent e) {            
    	// Set up file chooser and its filter
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Cookbook File (*.json)", "json");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        
        if (e.getSource() == this.gui.mNew) {
        	// Create new recipe -> clear input fields and recipe object
        	this.gui.initInputFields();
        	this.gui.updateRecipe();
        } else if (e.getSource() == this.gui.mOpen) {
        	// Load recipe from file
            do {
                if (chooser.showOpenDialog(this.gui) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
            } while (!((chooser.getSelectedFile().exists() == true) && (chooser.accept(chooser.getSelectedFile()) == true)));        	
        	
            this.gui.loadRecipe(chooser.getSelectedFile().toString());
        } else if (e.getSource() == this.gui.mSave) {
        	// Save recipe to file
            int option = chooser.showSaveDialog(this.gui);
            if(option == JFileChooser.APPROVE_OPTION){
               String file = chooser.getSelectedFile().toString();
               if (!file.toLowerCase().endsWith(".json"))
               {
            	   file = file + ".json";
               }
               this.gui.saveRecipe(file);
            }
        } else if (e.getSource() == this.gui.mExit) {
        	// Close the CookbookGenerator
        	this.gui.requestExit();
        } else {
        	// Do nothing (should not occur)
        }
     }

}
