package net.sourceforge.taggerplugin.io;

public class TagIoFactory {

	private TagIoFactory(){super();}
	
	public static final ITagIo create(TagIoFormat format) throws Exception {
		if(format.equals(TagIoFormat.XML)){
			return(new TagXmlIo());
		} else if(format.equals(TagIoFormat.CSV)){
			return(new TagCsvIo());
		} else if(format.equals(TagIoFormat.MEMENTO)){
			return(new TagMementoIo());
		}
		return(null);
	}
}
