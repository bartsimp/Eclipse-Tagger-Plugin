package net.sourceforge.taggerplugin.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import net.sourceforge.taggerplugin.TaggerActivator;

public class TaggerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	// FIXME: externalize

	public TaggerPreferencePage() {
		super(GRID);
		setPreferenceStore(TaggerActivator.getDefault().getPreferenceStore());
		setDescription("Preferences for the Resource Tagger plug-in.");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.CONFIRM_CLEAR_ASSOCIATIONS.getKey(),"Confirm tag/resource association clearing.",getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.CONFIRM_DELETE_TAG.getKey(),"Confirm tag deletion.",getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
			PreferenceConstants.POSITION_LABEL_DECORATION.getKey(),
			"Tagged resource label decoration position.",
			1,
			new String[][]{
				{"Top Right",String.valueOf(IDecoration.TOP_RIGHT)},
				{"Top Left",String.valueOf(IDecoration.TOP_LEFT)},
				{"Bottom Right",String.valueOf(IDecoration.BOTTOM_RIGHT)},
				{"Bottom Left",String.valueOf(IDecoration.BOTTOM_LEFT)}
			},
			getFieldEditorParent()
		));
	}

	public void init(IWorkbench workbench) {}
}