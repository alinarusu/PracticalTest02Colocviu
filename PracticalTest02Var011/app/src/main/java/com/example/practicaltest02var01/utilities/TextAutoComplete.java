package com.example.practicaltest02var01.utilities;


public class TextAutoComplete {

	private String insertedText;

	public TextAutoComplete() {
		this.insertedText = null;
	}

	public TextAutoComplete(
            String insertedText) {
		this.insertedText = insertedText;
	}
	
	public String getInsertedText() {
		return insertedText;
	}

	
	public void setInsertedText(String insertedText) {
		this.insertedText = insertedText;
	}

	
	@Override
	public String toString() {
		return insertedText;
	}

}
