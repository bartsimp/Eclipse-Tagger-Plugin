/*   ********************************************************************** **
**   Copyright (c) 2006-2007 Christopher J. Stehno (chris@stehno.com)       **
**   http://www.stehno.com                                                  **
**                                                                          **
**   All rights reserved                                                    **
**                                                                          **
**   This program and the accompanying materials are made available under   **
**   the terms of the Eclipse Public License v1.0 which accompanies this    **
**   distribution, and is available at:                                     **
**   http://www.stehno.com/legal/epl-1_0.html                               **
**                                                                          **
**   A copy is found in the file license.txt.                               **
**                                                                          **
**   This copyright notice MUST APPEAR in all copies of the file!           **
**  **********************************************************************  */
package net.sourceforge.taggerplugin;

import net.sourceforge.taggerplugin.model.TagSetContainerManager;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TaggerActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "net.sourceforge.taggerplugin";

	private static TaggerActivator plugin;
	private TagSetContainerManager tagRepositoryManager;

	public TaggerActivator() {
		plugin = this;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		tagRepositoryManager = new TagSetContainerManager();
		tagRepositoryManager.init();
		
		// FIXME: event registers should go in trm
		ResourcesPlugin.getWorkspace().addResourceChangeListener(tagRepositoryManager, IResourceChangeEvent.POST_CHANGE);
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		ResourcesPlugin.getWorkspace().removeResourceChangeListener(tagRepositoryManager);
		
		tagRepositoryManager.destroy();
		tagRepositoryManager = null;

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

	public TagSetContainerManager getTagSetContainerManager(){
		return(tagRepositoryManager);
	}
}
