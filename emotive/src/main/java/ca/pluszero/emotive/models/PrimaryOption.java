package ca.pluszero.emotive.models;

public class PrimaryOption {
	
	private final int compoundDrawable;
	private final int unselectedRes;
	private final int selectedRes;
	private final String text;
	
	public PrimaryOption(int compoundDrawable, int unselectedRes, int selectedRes, String text) {
	    this.compoundDrawable = compoundDrawable;
		this.unselectedRes = unselectedRes;
		this.selectedRes = selectedRes;
		this.text = text;
	}

	public int getCompoundDrawable() {
        return compoundDrawable;
    }

    public int getUnselectedRes() {
		return unselectedRes;
	}

	public int getSelectedRes() {
		return selectedRes;
	}

	public String getText() {
		return text;
	}

}
