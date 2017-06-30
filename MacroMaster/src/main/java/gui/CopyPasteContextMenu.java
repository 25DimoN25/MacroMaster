package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * Context menu to copy/cut/paste operations.
 * 
 * @author Saltykov Dmitry (25DimoN25)
 *
 */
public class CopyPasteContextMenu extends ContextMenu {

	private MenuItem edit = new MenuItem("Edit");
	private MenuItem copy = new MenuItem("Copy");
	private MenuItem cut = new MenuItem("Cut");
	private MenuItem del = new MenuItem("Delete");
	private MenuItem paste = new MenuItem("Paste");
	
	public CopyPasteContextMenu() {
		edit.setDisable(true);
		copy.setDisable(true);
		cut.setDisable(true);
		paste.setDisable(true);
		del.setDisable(true);
		
		getItems().addAll(edit, new SeparatorMenuItem(), copy, cut, del, paste);
	}
		

	public void setOnActionEdit(EventHandler<ActionEvent> value) {
		edit.setOnAction(value);
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

	public void setOnActionDelete(EventHandler<ActionEvent> value) {
		del.setOnAction(value);
	}

	
	public void setCopyCutEditDelDisabled(boolean disabled) {
		edit.setDisable(disabled);
		copy.setDisable(disabled);
		cut.setDisable(disabled);
		del.setDisable(disabled);
	}
	
	public void setPasteDisable(boolean disabled) {
		paste.setDisable(disabled);
	}

	
}
