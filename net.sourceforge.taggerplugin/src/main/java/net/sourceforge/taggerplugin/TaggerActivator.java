package net.sourceforge.taggerplugin;

import net.sourceforge.taggerplugin.manager.TagManager;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggerActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "net.sourceforge.taggerplugin";
	private static TaggerActivator plugin;

	public TaggerActivator() {
		plugin = this;
	}

	/**
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		// automatically persist the tag set when plugin is stopped
		TagManager.getInstance().saveTags();

		plugin = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TaggerActivator getDefault() {
		return plugin;
	}
}
