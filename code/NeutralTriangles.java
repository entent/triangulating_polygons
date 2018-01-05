package drivewyzeProblem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import drivewyzeProblem.GraphProperties.VertexColour;

/* This is where the triangles will be counted.
 * The graph and vertices' properties are separate classes.
 */

public class NeutralTriangles {
	
	//Given three vertices, this method checks if all three colours are present
	public static boolean isTriangleNeutral(Vertex v1, Vertex v2, Vertex v3) {
		
		Set<VertexColour> triangleColours = new HashSet<VertexColour>();
		triangleColours.add(v1.getColour());
		triangleColours.add(v2.getColour());
		triangleColours.add(v3.getColour());
		
		boolean triangleIsNeutral = true;
		
		//making sure all three colours are contained in the triangle
		for(VertexColour col : VertexColour.values()) {
			triangleIsNeutral &= (triangleColours.contains(col));
		}
		
		return triangleIsNeutral;
	}
	
	//Taking a "triangle" array of vertices, this method determines if all three colours are present
	public static boolean isTriangleNeutral(ArrayList<Vertex> triangle) {
		return isTriangleNeutral(triangle.get(0), triangle.get(1), triangle.get(2));
	}
	
	/*
	 * Counts the number of neutral triangles that have Vertrex v as a corner
	 */
	public static int countNeutralTriangles(PolygonGraph g, Vertex v) {
		
		int count = 0;
		
		if(g.triangles.keySet().contains(v.getName())){
			ArrayList<ArrayList<Vertex>> vTriangles = g.triangles.get(v.getName());
			
			for(ArrayList<Vertex> triangle : vTriangles) {
				if(isTriangleNeutral(triangle)) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	/* 
	 * Taking an graph g, this method colours it such that there are exactly "goal" number of neutral triangles.
	 * If possible, the graph will remain coloured, and the number "goal" is output.  If not possible, the int -1 is 
	 * returned, and the graph remains incomplete.
	 * This is a recursive method, so that if the goal is exceeded, the current vertex is given a new colour, and the colouring continues.
	 * If all colours are used up in this trial-and-error fashion for the last vertex and the goal is not met, 
	 * its colour is changed to null, and the previous vertex is assigned a new colour, and so forth.
	 * Note that this is more efficient that a brute force calculation where all triangles must be checked for every possible colouring of 
	 * the vertices.
	 */
	public static int colourGraph
		(
			PolygonGraph g, 
			int goal, 
			int currentVertex /* This indexes the vertex to be coloured next, as ordered in the HashMap PolygonGraph.vertices*/, 
			int currentCount /* Method has to make sure that current count + number of new neutral triangles does not exceed goal */
		) {
		
		if(currentVertex > g.vertices.size()) {
			/*
			 * As can be seen in the for-loop below, this if-statement is only true if all vertices have been successfully coloured, 
			 * and currentVertex has run over the highest key value of vertices in the graph.  If currentCount == goal, the method will break out of 
			 * all recursions, and return the goal value with the graph coloured.  If not, the final vertex will be coloured with the next colour 
			 * in the for-loop.
			 */
			return currentCount;
		}
		
		int newCount = 0;
		int numNeutralAdjacent = 0;
		Vertex v = g.vertices.get(currentVertex);
		for(VertexColour col : VertexColour.values()) {
			v.setColour(col);
			numNeutralAdjacent = countNeutralTriangles(g, v);
			if(currentCount + numNeutralAdjacent <= goal) {
				newCount = colourGraph(g, goal, currentVertex + 1, currentCount + numNeutralAdjacent);
			}
			if(newCount == goal) {
				 // We have coloured the entire graph, with the number of neutral triangles desired.  Break out of the recursive loop.
				return newCount;
			}
			// if the goal has not been achieved, we try colouring the vertex with the next colour
		}
		
		/* 
		 * If this part of the method is reached, it means all colours have been tried for the vertex, and the goal hasn't been achieved.
		 * Now we change the colour of the vertex back to null, break out of this level of recursion, and try the next colour for the previous vertex.
		 * If the goal can't be achieved, then at this point all possible colours have been tried for the first uncoloured vertex, and we break out of 
		 * the method altogether, and return -1, meaning that the goal is unachievable.
		 */
		
		v.setColour(null);
		return -1;
	}
	
	//The overloaded method that can be called without specifying the initial score and the current vertex
	public static int colourGraph
	(
		PolygonGraph g, 
		int goal
	) {
		
		Vertex startVertex;
		
		//finding the first uncoloured vertex
		for(int i = 1; i <= g.vertices.keySet().size(); i++) {
			startVertex = g.vertices.get(i);
			if(startVertex.getColour()==null) {
				return colourGraph(g, goal, startVertex.getName(), 0);
			}
			
		}
		
		//this code is only returned if the graph is already completely coloured
		return -2;
		
	}	
	/*
	 * Solution to the problem
	 * 
	 */
	public static void main(String[] args) {
		PolygonGraph g = drivewyzeGraphSetup();
		int neutralCount = 0;
		neutralCount = NeutralTriangles.colourGraph(g, 2 /*the goal number of neutral triangles*/);
		System.out.println("If no solution exists, the output will be -1.");		
		System.out.println("If a solution is found, the output will be the goal (2), and the colours of the vertices that solve the problem will be given.");		
		System.out.println("output: " + neutralCount);
		if(neutralCount!=-1){
			for(Vertex v : g.vertices.values()) {
				System.out.println("Vertex " + v.getName() + ": " + v.getColourName());
			}
		}
		System.out.println("Therefore no solution exists.");
		System.out.println();
		System.out.println("Now to find the number of neutral triangles that can be achieved in this graph (out of the 31 total triangles).");		
		for(int i = 1; i<=31; i++) {
			PolygonGraph graph = drivewyzeGraphSetup();
			int output = NeutralTriangles.colourGraph(graph, i /*the goal number of neutral triangles*/);
			if(output != -1) {
				System.out.println(i);
			}
		}
		

	}
	
	/*
	 * The graph of the problem posed by Drivewyze.  See the README for a diagram showing the numbering convention used for the vertices.
	 */
	public static PolygonGraph drivewyzeGraphSetup() {
		PolygonGraph g = new PolygonGraph();
		
		// Defining the nearest neighbour relations between the vertices in the diagram
		g.setNrstNbrs(1, 2); g.setNrstNbrs(1, 11); g.setNrstNbrs(1, 12);
		g.setNrstNbrs(2, 3); g.setNrstNbrs(2, 12); g.setNrstNbrs(2, 13);
		g.setNrstNbrs(3, 4); g.setNrstNbrs(3, 13); g.setNrstNbrs(3, 14);
		g.setNrstNbrs(4, 5); g.setNrstNbrs(4, 14);
		g.setNrstNbrs(5, 6); g.setNrstNbrs(5, 14); g.setNrstNbrs(5, 15); g.setNrstNbrs(5, 16);
		g.setNrstNbrs(6, 7); g.setNrstNbrs(6, 16); g.setNrstNbrs(6, 17);
		g.setNrstNbrs(7, 8); g.setNrstNbrs(7, 17);
		g.setNrstNbrs(8, 9); g.setNrstNbrs(8, 17); g.setNrstNbrs(8, 18);
		g.setNrstNbrs(9, 10); g.setNrstNbrs(9, 18); g.setNrstNbrs(9, 22);
		g.setNrstNbrs(10, 11); g.setNrstNbrs(10, 20); g.setNrstNbrs(10, 21); g.setNrstNbrs(10, 22);
		g.setNrstNbrs(11, 12); g.setNrstNbrs(11, 20);
		g.setNrstNbrs(12, 13); g.setNrstNbrs(12, 20);
		g.setNrstNbrs(13, 14); g.setNrstNbrs(13, 15); g.setNrstNbrs(13, 19); g.setNrstNbrs(13, 20);
		g.setNrstNbrs(14, 15);
		g.setNrstNbrs(15, 16); g.setNrstNbrs(15, 17); g.setNrstNbrs(15, 18); g.setNrstNbrs(15, 19);
		g.setNrstNbrs(16, 17);
		g.setNrstNbrs(17, 18);
		g.setNrstNbrs(18, 19); g.setNrstNbrs(18, 22);
		g.setNrstNbrs(19, 20); g.setNrstNbrs(19, 21); g.setNrstNbrs(19, 22);
		g.setNrstNbrs(20, 21);
		g.setNrstNbrs(21, 22);
		
		//Setting the colours of the vertices on the boundary of the polygon
		g.setVertexColour(1, "red");
		g.setVertexColour(2, "red");
		g.setVertexColour(3, "yellow");
		g.setVertexColour(4, "blue");
		g.setVertexColour(5, "red");
		g.setVertexColour(6, "yellow");
		g.setVertexColour(7, "blue");
		g.setVertexColour(8, "red");
		g.setVertexColour(9, "red");
		g.setVertexColour(10, "yellow");
		g.setVertexColour(11, "blue");
		
		return g;
	}
	
}
