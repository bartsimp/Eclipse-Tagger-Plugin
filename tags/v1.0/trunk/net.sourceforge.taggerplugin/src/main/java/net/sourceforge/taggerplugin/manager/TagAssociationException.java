package net.sourceforge.taggerplugin.manager;

public class TagAssociationException extends RuntimeException {

	private static final long serialVersionUID = -3106727165779954211L;

	public TagAssociationException() {
		super();
	}

	public TagAssociationException(String msg) {
		super(msg);
	}

	public TagAssociationException(Throwable cause) {
		super(cause);
	}

	public TagAssociationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
