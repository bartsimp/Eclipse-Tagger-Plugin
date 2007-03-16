package net.sourceforge.taggerplugin.dialog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.model.ITagSetContainer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IModificationDate;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.XMLMemento;

/**
 *
 * http://wiki.eclipse.org/index.php/FAQ_How_do_I_create_a_compare_editor%3F
 * http://wiki.eclipse.org/index.php/FAQ_How_do_I_create_a_Compare_dialog%3F
 * 
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class CompareInput extends CompareEditorInput {
	
	public CompareInput(CompareConfiguration config) {
		super(config);
	}
	
	protected Object prepareInput(IProgressMonitor pm) {
		try {
			pm.beginTask("comparing", 1);
			
			// FIXME: this is a big test hack!
			
			final ITagSetContainer tsc = TaggerActivator.getDefault().getTagSetManager().getTagSetContainer("test-project-a");
			
			final XMLMemento memento = XMLMemento.createWriteRoot("tag-set");
			tsc.write(memento);
	
			StringWriter writer = null;
			try {
				writer = new StringWriter();
				memento.save(writer);
			} finally {
				IOUtils.closeQuietly(writer);
			}
			
			CompareItem left = new CompareItem(tsc.getName(),writer.toString(),System.currentTimeMillis());
			
			File file = new File("C:/Documents and Settings/Owner/runtime-EclipseApplication/test-project-a/.tagset");
			String str = FileUtils.readFileToString(file, null);
			CompareItem right = new CompareItem(file.toString(),str,file.lastModified());
	
			//		return new DiffNode(null, Differencer.CONFLICTING, null, left, right);
			pm.done();
			return new DiffNode(left, right);
		} catch(Exception ex){
			ex.printStackTrace();
			return(null);
		}
	}
	
	@Override
	public void saveChanges(IProgressMonitor pm) throws CoreException {
		super.saveChanges(pm);
	}

	private static class CompareItem implements IStreamContentAccessor,ITypedElement, IModificationDate,IEditableContent {
		
		private String contents, name;
		private long lastModified;
		
		CompareItem(String name, String contents, long lastModified) {
			this.name = name;
			this.contents = contents;
			this.lastModified = lastModified;
		}
		
		public InputStream getContents() throws CoreException {
			return new ByteArrayInputStream(contents.getBytes());
		}
		
		public Image getImage() {return null;}
		
		public long getModificationDate() {return lastModified;}
		
		public String getName() {return name;}
		
		public String getString() {return contents;}
		
		public String getType() {return ITypedElement.TEXT_TYPE;}

		public boolean isEditable() {
			return true;
		}

		public ITypedElement replace(ITypedElement dest, ITypedElement src) {
			System.out.println("replace");
			return null;
		}

		public void setContent(byte[] newContent) {
			System.out.println("setContent");
		}
	}
}