package com.example.myapp.pars;

import android.util.Log;
import com.example.myapp.data.Grid;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GridParser extends DefaultHandler {

    public static final String 	NAME = "Crossword";
	
	private Grid grid = new Grid();
	private StringBuffer	buffer;

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		super.processingInstruction(target, data);
	}
	public GridParser() {
		super();
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
		buffer = new StringBuffer();
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (localName.equalsIgnoreCase("name")) {
			this.grid.setName(buffer.toString());
		}

		else if (localName.equalsIgnoreCase("description")) {
			this.grid.setDescription(buffer.toString());
		}

		else if (localName.equalsIgnoreCase("level")) {
			this.grid.setLevel(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("width")) {
			this.grid.setWidth(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("height")) {
			this.grid.setHeight(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("percent")) {
			this.grid.setPercent(Integer.parseInt(buffer.toString()));
		}

		else if (localName.equalsIgnoreCase("date")) {
			System.out.println(buffer.toString());
			try {
				this.grid.setRawDate(buffer.toString());
				this.grid.setDate((new SimpleDateFormat("dd/MM/yyyy")).parse(buffer.toString()));
			} catch (ParseException e) {
				Log.w(NAME, "GridParser: Unable to parse grid date");
			}
		}

		else if (localName.equalsIgnoreCase("author")) {
			this.grid.setAuthor(buffer.toString());
		}
	}

	public void characters(char[] ch,int start, int length)	throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);
	}

	public Grid getData() {
		return this.grid;
	}
	
	public void setFileName(String name) {
		this.grid.setFileName(name);
	}
	
}
