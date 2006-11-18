package net.sourceforge.taggerplugin.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TagSelectionDialog extends Dialog {

	public TagSelectionDialog(Shell parentShell){
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.TagSelectionDialog_Title);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = (Composite)super.createDialogArea(parent);
		final GridLayout layout = (GridLayout)composite.getLayout();
//		layout.numColumns = 2;
//		layout.makeColumnsEqualWidth = false;
		
		final Font labelFont = parent.getFont();
		
		final Label label = new Label(composite,SWT.LEFT);
		label.setFont(labelFont);
		label.setText("Dummy Content for now.");
		
		return(composite);
	}
}
