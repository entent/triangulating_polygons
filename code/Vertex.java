package drivewyzeProblem;

import drivewyzeProblem.GraphProperties.VertexColour;

public class Vertex {
	
	//Since the vertices are numbered, the name of the vertex will just be the number it was assigned in the diagram in the README file.
	private Integer name;
	private VertexColour colour;
	
	public Vertex(int vertName, VertexColour vertColour) {
		name = vertName;
		colour = vertColour;
	}
	
	public Vertex(int vertName) {
		name = vertName;
		colour = null;
	}
	
	// some getters and setters
	
	public Integer getName() {
		return name;
	}
	
	public VertexColour getColour() {
		return this.colour;
	}
	
	public String getColourName() {
		if( this.colour == null ) {
			return "null";
		} else {
			return this.colour.getColourName();
		}
	}
	
	public void setColour(VertexColour newColour) {
		if(newColour == null) {
			colour = null;
		} else {
			colour = newColour;
		}
	}
	
	// Will be useful since the csv for creating the graph only references the integer names of the vertices
	@Override
	public boolean equals(Object v) {
		
		if ( this.getName() == ((Vertex) v).getName() ) return true;
		else return false;
	}

	
}
