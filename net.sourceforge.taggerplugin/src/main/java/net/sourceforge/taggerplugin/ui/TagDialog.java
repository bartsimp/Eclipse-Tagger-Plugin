package net.sourceforge.taggerplugin.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TagDialog extends Dialog {

    private String title,errorMessage,nameValue,descValue;
    private Text nameTxt,descTxt,errorMessageText;

    public TagDialog(Shell parentShell) {
        super(parentShell);
    }

    public int showCreate(){
    	this.title = Messages.TagDialog_Title_Create;
    	return(open());
    }

    public int showModify(){
    	this.title = Messages.TagDialog_Title_Modify;
    	return(open());
    }
    
    public String getNameValue(){
    	return(nameValue);
    }
    
    public String getDescriptionValue(){
    	return(descValue);
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null){
        	shell.setText(title);
        }
    }
    
    /**
     * 
     */
    protected void buttonPressed(int buttonId) {
        if (buttonId == IDialogConstants.OK_ID) {
        	nameValue = nameTxt.getText();
        	descValue = descTxt.getText();
        } else {
            nameValue = null;
            descValue = null;
        }
        super.buttonPressed(buttonId);
    }    

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        createButton(parent, IDialogConstants.OK_ID,IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID,IDialogConstants.CANCEL_LABEL, false);
        
        // do this here because setting the text will set enablement on the ok button
        //text.setFocus();
        
//        if (value != null) {
//            text.setText(value);
//            text.selectAll();
//        }
    }

    /**
     */
    protected Control createDialogArea(Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);
        final GridLayout layout = (GridLayout)composite.getLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        
        // name label
        final Label nameLbl = new Label(composite,SWT.LEFT);
        nameLbl.setText(Messages.TagDialog_Label_Name);
        nameLbl.setFont(parent.getFont());
        
        // name text
        nameTxt = new Text(composite,SWT.SINGLE | SWT.BORDER);
        final GridData nameData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        nameData.widthHint = 200;
        nameTxt.setLayoutData(nameData);
        nameTxt.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){
		        validate(nameTxt,Messages.TagDialog_Error_NoName);
			}
        });
        nameTxt.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e){}
			public void focusLost(FocusEvent e) {
				validate(nameTxt,Messages.TagDialog_Error_NoName);
			}
        });
        
        // description label
        final Label descLbl = new Label(composite,SWT.LEFT);
        descLbl.setText(Messages.TagDialog_Label_Description);
        descLbl.setFont(parent.getFont());
        descLbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        
        // description text
        descTxt = new Text(composite,SWT.MULTI | SWT.WRAP | SWT.BORDER);
        final GridData descData = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        descData.widthHint = 200;
        descData.heightHint = 100;
        descTxt.setLayoutData(descData);
        descTxt.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){
		        validate(descTxt,Messages.TagDialog_Error_NoDescription);
			}
        });
        descTxt.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e){}
			public void focusLost(FocusEvent e) {
				validate(descTxt,Messages.TagDialog_Error_NoDescription);
			}
        });
        
        errorMessageText = new Text(composite, SWT.READ_ONLY);
        final GridData errData = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
        errData.horizontalSpan = 2;
        errorMessageText.setLayoutData(errData);
        errorMessageText.setBackground(errorMessageText.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        errorMessageText.setForeground(composite.getDisplay().getSystemColor(SWT.COLOR_RED));
        
        setErrorMessage(errorMessage);

        applyDialogFont(composite);
        return composite;
    }
    
    private void validate(final Text txt, final String errMsg){
    	setErrorMessage(new TextInputValidator(errMsg).isValid(txt.getText()));
    }
    
    private static final class TextInputValidator implements IInputValidator {

    	private final String msg;
    	
    	private TextInputValidator(final String msg){
    		super();
    		this.msg = msg;
    	}
    	
		public String isValid(String newText){
			return(newText != null && newText.length() > 0 ? null : msg);
		}
    }

    /**
     * Sets or clears the error message.
     * If not <code>null</code>, the OK button is disabled.
     *
     * @param errorMessage
     *            the error message, or <code>null</code> to clear
     * @since 3.0
     */
    public void setErrorMessage(String errorMessage) {
    	this.errorMessage = errorMessage;
    	if (errorMessageText != null && !errorMessageText.isDisposed()) {
    		errorMessageText.setText(errorMessage == null ? "" : errorMessage); //$NON-NLS-1$
    		errorMessageText.getParent().update();
    		// Access the ok button by id, in case clients have overridden button creation.
    		// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=113643
    		Control button = getButton(IDialogConstants.OK_ID);
    		if (button != null) {
    			button.setEnabled(errorMessage == null);
    		}
    	}
    }
}
