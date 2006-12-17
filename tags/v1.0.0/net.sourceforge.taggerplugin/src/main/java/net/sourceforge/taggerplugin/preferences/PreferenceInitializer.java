package net.sourceforge.taggerplugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IDecoration;

import net.sourceforge.taggerplugin.TaggerActivator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		final IPreferenceStore store = TaggerActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.CONFIRM_CLEAR_ASSOCIATIONS.getKey(),true);
		store.setDefault(PreferenceConstants.CONFIRM_DELETE_TAG.getKey(),true);
		store.setDefault(PreferenceConstants.POSITION_LABEL_DECORATION.getKey(),String.valueOf(IDecoration.TOP_RIGHT));
	}
}
