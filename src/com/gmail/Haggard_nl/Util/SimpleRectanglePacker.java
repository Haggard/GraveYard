package com.gmail.Haggard_nl.Util;

import com.gmail.Haggard_nl.GraveYard.Managers.MessageManager;

public class SimpleRectanglePacker{

	// cuboid representation of the packagingArea
	private Cuboid packagingArea = null; 

	private int packagingAreaHeight = 0;
	private int packagingAreaWidth = 0;
	
	private int currentLine = 0;
	private int lineHeight = 0;
	private int column = 0;
	
	// height and width of the rectangle to allocate in the packagingArea
	private int rectangleWidth = 0;
	private int rectangleHeight = 0;
	
	// flag true is with and height of the rectangle are switched for 
	// optimal filling of the area
	private Boolean switchedWH = false;

	// relative row and colum
	private int pointX = 0; // column
	private int pointZ = 0; // row
		
	// constructor
	public SimpleRectanglePacker(Cuboid c){
		packagingArea = c;
	}
	
	// start filling area with recangles
	public Boolean startPacking(int rectangleWidth, int rectangleHeight, String GraveName){
		// set packaging area
		this.packagingAreaWidth = packagingArea.getSizeX();
		this.packagingAreaHeight = packagingArea.getSizeZ();
		
		// return false if area is smaller than de rectangle to place
		if ((packagingAreaWidth < rectangleWidth) || (packagingAreaHeight < rectangleHeight)){
			MessageManager.getInstance().consoleErrorMsg("area is smaller than rectangle to place" );
			return false;
		}
		// find settings for optimal filling of the area
		this.rectangleWidth = rectangleWidth;
		this.rectangleHeight = rectangleHeight;

		if(packagingAreaWidth < packagingAreaHeight){
			if (rectangleWidth < rectangleHeight){
				this.switchedWH = true;
				this.rectangleHeight = rectangleWidth;
				this.rectangleWidth = rectangleHeight;
			}	
		} else {
			if (rectangleWidth > rectangleHeight){
				this.switchedWH = true;
				this.rectangleHeight = rectangleWidth;
				this.rectangleWidth = rectangleHeight;
			}	
		}
		
		Grave g = new Grave(this.switchedWH, GraveName);
		MessageManager.getInstance().debugMessage("[startPacking] Switched grave " + this.switchedWH );
		MessageManager.getInstance().debugMessage("[startPacking] packagingAreaWidth " + this.packagingAreaWidth );
		MessageManager.getInstance().debugMessage("[startPacking] packagingAreaHeight " + this.packagingAreaHeight );
		MessageManager.getInstance().debugMessage("[startPacking] Grave object is null: " + (g == null));
		int plotNr = 0;
		// start packaging te area
		while(tryPack()){
			plotNr += 1;
			MessageManager.getInstance().debugMessage("[startPacking] While tryPack: Grave " + plotNr + " column " + pointX + " row " + pointZ );
			MessageManager.getInstance().debugMessage("[startPacking] While tryPack: Grave loc: " + packagingArea.getLowerNE().toString());
			g.create(packagingArea.getLowerNE(), pointX, pointZ, plotNr); 
			MessageManager.getInstance().debugMessage("[startPacking] Grave created");
		}
		MessageManager.getInstance().debugMessage("[startPacking] return true");
		return true;
	}

	//  packaging routine
	private Boolean tryPack(){
		MessageManager.getInstance().debugMessage("[tryPack]");
		// do we have to start a ne line?
		if ((this.column + rectangleWidth) > packagingAreaWidth){
			MessageManager.getInstance().debugMessage("[tryPack] (this.column + rectangleWidth) > packagingAreaWidth");
			this.currentLine += this.lineHeight;
			this.lineHeight = 0;
			this.column = 0;
		}
		
		// is it doesn't fit in height, than area is max filled
		if ((this.currentLine + rectangleHeight) > packagingAreaHeight){
			MessageManager.getInstance().debugMessage("[tryPack] this.currentLine + rectangleHeight) > packagingAreaHeight");
			MessageManager.getInstance().debugMessage("[tryPack] return true");
			return false;
		}
		
		// rectangle fits a current location
		pointX = this.column;
		pointZ = this.currentLine;
		
		this.column += rectangleWidth;
		if(rectangleHeight > this.lineHeight){
			MessageManager.getInstance().debugMessage("[tryPack] rectangleHeight > this.lineHeight");
			this.lineHeight += rectangleHeight;
		}
		MessageManager.getInstance().debugMessage("[tryPack] return true");
		return true;
	}


}