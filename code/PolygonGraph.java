package drivewyzeProblem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import drivewyzeProblem.GraphProperties.VertexColour;

public class PolygonGraph {
	
	// The fundamental properties of the graph are the vertices it contains and how they are related
	public Map<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
	public Map<Integer, ArrayList<Vertex>> nearestNeighbours = new HashMap<Integer, ArrayList<Vertex>>();
	public Map<Integer, ArrayList<ArrayList<Vertex>>> triangles = new HashMap<Integer, ArrayList<ArrayList<Vertex>>>();	
	
	public void setNrstNbrs (int a, int b) {
				
		Vertex aVert;
		Vertex bVert;
		
		//Adding vertices if they don't exist yet
		if(!this.vertices.keySet().contains(a)) {
			aVert = new Vertex(a);
			vertices.put(a, aVert);
		} else {
			aVert = vertices.get(a);
		}
		if(!this.vertices.keySet().contains(b)) {
			bVert = new Vertex(b);
			vertices.put(b, bVert);
		} else {
			bVert = vertices.get(b);
		}
		
		//Adding bVert to the nearest neighbours of aVert		
		if(this.nearestNeighbours.get(a) == null) {
			ArrayList<Vertex> aNeighbours = new ArrayList<Vertex>();
			aNeighbours.add(bVert);
			this.nearestNeighbours.put(a, aNeighbours);
		} else if(!this.nearestNeighbours.get(a).contains(bVert)) {
			this.nearestNeighbours.get(a).add(bVert);
		}
		
		//Adding aVert to the nearest neighbours of bVert
		if(this.nearestNeighbours.get(b) == null) {
			ArrayList<Vertex> bNeighbours = new ArrayList<Vertex>();
			bNeighbours.add(aVert);
			this.nearestNeighbours.put(b, bNeighbours);
		} else if(!this.nearestNeighbours.get(b).contains(aVert)) {
			this.nearestNeighbours.get(b).add(aVert);
		}
		
		//Adding the vertices to triangles, if they have other vertices in common
		
		for(Vertex cVert : this.nearestNeighbours.get(a) ) {
			if(this.nearestNeighbours.get(b).contains(cVert)) {
				ArrayList<Vertex> triangle = new ArrayList<Vertex>();
				triangle.add(aVert); triangle.add(bVert); triangle.add(cVert);
				
				for(Vertex v : triangle){
					if(this.triangles.containsKey(v.getName()) ) {
						this.triangles.get(v.getName()).add(triangle);
					} else {
						ArrayList<ArrayList<Vertex>> vertexsTriangles = new ArrayList<ArrayList<Vertex>>();
						vertexsTriangles.add(triangle);
						this.triangles.put(v.getName(), vertexsTriangles);
					}
				}
				
			}
		}
		
	}
	
	public ArrayList<Vertex> getNrstNbrs (int a) {
		return this.nearestNeighbours.get(a);
	}
	
	public void setVertexColour(int vertexName, VertexColour col) {
		
		Vertex v = this.vertices.get(vertexName);
		v.setColour(col);
		
	}

	public void setVertexColour(int vertexName, String col) {
		
		VertexColour c = VertexColour.getColour(col);
		this.setVertexColour(vertexName, c);
		
	}
	
	public VertexColour getVertexColour(int vertexName) {
		
		Vertex v = this.vertices.get(vertexName);
		return v.getColour();
		
	}
	
	public String getVertexColourName(int vertexName) {
		
		Vertex v = this.vertices.get(vertexName);
		return v.getColourName();
		
	}
}
