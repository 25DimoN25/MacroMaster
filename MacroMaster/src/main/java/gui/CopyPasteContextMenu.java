package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Context menu to copy/cut/paste operations.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public class CopyPasteContextMenu extends ContextMenu {
	
	private MenuItem copy = new MenuItem("Copy");
	private MenuItem cut = new MenuItem("Cut");
	private MenuItem paste = new MenuItem("Paste");
	
	public CopyPasteContextMenu() {
		copy.setDisable(true);
		cut.setDisable(true);
		paste.setDisable(true);
		
		getItems().addAll(copy, cut, paste);
	}
		
	public void setOnActionCopy(EventHandler<ActionEvent> value) {
		copy.setOnAction(value);
	}
	
	public void setOnActionCut(EventHandler<ActionEvent> value) {
		cut.setOnAction(value);
	}
	
	public void setOnActionPaste(EventHandler<ActionEvent> value) {
		paste.setOnAction(value);
	}

	public void setCopyCutDisabled(boolean disabled) {
		copy.setDisable(disabled);
		cut.setDisable(disabled);
	}
	
	public void setPasteDisable(boolean disabled) {
		paste.setDisable(disabled);
	}
	
}
