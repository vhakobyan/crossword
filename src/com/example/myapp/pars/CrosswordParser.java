package com.example.myapp.pars;

import com.example.myapp.data.Word;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class CrosswordParser extends DefaultHandler {
	
	private ArrayList<Word> entries;
	private Word 			currentFeed;
	private StringBuffer 	buffer;
	private boolean			inHorizontal;

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public CrosswordParser() {
		super();
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		entries = new ArrayList<Word>();
	}

	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		buffer = new StringBuffer();

		if (localName.equalsIgnoreCase("horizontal")) {
			this.inHorizontal = true;
		}

		if (localName.equalsIgnoreCase("vertical")) {
			this.inHorizontal = false;
		}
		
		if (localName.equalsIgnoreCase("word")) {
			this.currentFeed = new Word();
			this.currentFeed.setX(Integer.parseInt(attributes.getValue("x")));
			this.currentFeed.setY(Integer.parseInt(attributes.getValue("y")));
			this.currentFeed.setTmp(attributes.getValue("tmp"));
			this.currentFeed.setDescription(attributes.getValue("description"));
			this.currentFeed.setHorizontal(this.inHorizontal);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equalsIgnoreCase("word")) {
			this.currentFeed.setText(buffer.toString());
			entries.add(this.currentFeed);
			System.out.println("Word, text: " + this.currentFeed.getText() + ", tmp: " + this.currentFeed.getTmp() + ", x: " + this.currentFeed.getX() + ", y: " + this.currentFeed.getY());
			buffer = null;
		}
	}

	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public ArrayList<Word> getData() { return entries; }
}
