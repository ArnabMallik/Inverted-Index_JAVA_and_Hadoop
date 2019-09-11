import java.io.Serializable;

public class Location implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int docId;
	private int wordId;
	
	
	
	public Location(int docId, int wordId) {
		super();
		this.docId = docId;
		this.wordId = wordId;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getWordId() {
		return wordId;
	}
	public void setWordId(int wordId) {
		this.wordId = wordId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + docId;
		result = prime * result + wordId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (docId != other.docId)
			return false;
		if (wordId != other.wordId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return " [docId=" + docId + ", wordId=" + wordId + "]";
	}
	
	public int compareTo(Location l) {
        if (docId == l.docId)
            return l.wordId-wordId;
        return l.docId - docId;
    }
	
	
	

}
