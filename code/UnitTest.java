package drivewyzeProblem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;

import drivewyzeProblem.GraphProperties.VertexColour;

public class UnitTest {
	
	//testing that the vertices are added to a graph correctly
	@Test
	public void testVerticesAdded() {
		PolygonGraph gTest = new PolygonGraph();
		gTest.setNrstNbrs(1, 2);
		gTest.setNrstNbrs(1, 3);
		gTest.setNrstNbrs(3, 2);
		
		//testing that the names of the vertices in the hashmap vertices are as expected
		for(int i=1; i<=3; i++) {
			assertEquals((int)gTest.vertices.get(i).getName(), i);
		}
		
	}
	
	//Testing that only unique vertices are added as nearest neighbours to vertex 1
	@Test
	public void testNearestNeighbours() {
		PolygonGraph gTest = new PolygonGraph();
		gTest.setNrstNbrs(1, 2);
		gTest.setNrstNbrs(1, 3);
		gTest.setNrstNbrs(3, 1);
		
		ArrayList<Vertex> aNearestNeighbours = gTest.getNrstNbrs(1);
		
		//Checking that vertex 1 has exactly 2 neighbours
		assertEquals(2, aNearestNeighbours.size());
		
		//Checking that the each neighbour occurs exactly once
		assertEquals(2, (int)aNearestNeighbours.get(0).getName());
		assertEquals(3, (int)aNearestNeighbours.get(1).getName());
	}
	
	//Testing that the colour of a vertex in a graph can be set multiple times
	@Test
	public void testColourSetting() {
		PolygonGraph gTest = new PolygonGraph();
		gTest.setNrstNbrs(1, 2);
		gTest.setNrstNbrs(1, 3);
		gTest.setNrstNbrs(3, 2);
				
		//Checking that vertex 3 currently has no colour
		assertEquals("null", gTest.getVertexColourName(3));
		
		//Setting the colour of vertex 3 mulitple times and checking that it is correct each time
		gTest.setVertexColour(3, "red");
		assertEquals("red", gTest.getVertexColourName(3));
		
		gTest.setVertexColour(3, "red");
		assertEquals("red", gTest.getVertexColourName(3));
		
		gTest.setVertexColour(3, (VertexColour)null);
		assertEquals("null", gTest.getVertexColourName(3));
	}

	
	//Testing that neutral and non-neutral triangles are correctly identified
	@Test
	public void testTriangleNeutrality() {
		PolygonGraph gTest = new PolygonGraph();
		gTest.setNrstNbrs(1, 2);
		gTest.setNrstNbrs(1, 3);
		gTest.setNrstNbrs(3, 2);
		
				
		//Checking that when no vertex has colour, the triangle is non-neutral
		assertEquals(false, NeutralTriangles.isTriangleNeutral(gTest.triangles.get(1).get(0)));
		
		gTest.setVertexColour(1, "blue");
		gTest.setVertexColour(2, "yellow");
		
		//Checking that when one vertex is colourless, the triangle is still non-neutral		
		assertEquals(false, NeutralTriangles.isTriangleNeutral(gTest.triangles.get(1).get(0)));		
		
		//Checking that when all 3 vertices have colour, but the triangle is non-neutral, it is evaluated as such
		gTest.setVertexColour(3, "blue");
		assertEquals(false, NeutralTriangles.isTriangleNeutral(gTest.triangles.get(1).get(0)));		

		//Checking that when the triangle is neutral, it is evaluated as such
		gTest.setVertexColour(3, "red");
		assertEquals(true, NeutralTriangles.isTriangleNeutral(gTest.triangles.get(1).get(0)));		
	}
	
	//Testing that the counting algorithm correctly counts neutral and non-neutral triangles
	@Test
	public void testNeutralTriangleCount() {
		PolygonGraph gTest = new PolygonGraph();
		//this is not yet a triangle
		gTest.setNrstNbrs(1, 2);
		gTest.setNrstNbrs(1, 3);
		
		//making the vertices neutral
		gTest.setVertexColour(1, "blue");
		gTest.setVertexColour(2, "yellow");

		//Checking that when a vertex has no triangles, none is counted
		int countNeutral = NeutralTriangles.countNeutralTriangles(gTest, gTest.vertices.get(1));
		assertEquals(0, countNeutral);
		
		//completing the triangle, but leaving vertex 3 uncoloured
		gTest.setNrstNbrs(2, 3);
		countNeutral = NeutralTriangles.countNeutralTriangles(gTest, gTest.vertices.get(1));
		assertEquals(0, countNeutral);
		
		//colouring vertex 3 a colour so that the triangle is non-neutral
		gTest.setVertexColour(3, "yellow");
		countNeutral = NeutralTriangles.countNeutralTriangles(gTest, gTest.vertices.get(1));
		assertEquals(0, countNeutral);

		//colouring vertex 3 so that the triangle is neutral
		gTest.setVertexColour(3, "red");
		countNeutral = NeutralTriangles.countNeutralTriangles(gTest, gTest.vertices.get(1));
		assertEquals(1, countNeutral);
	}

	
	//Testing the colourGraph method works in some simple cases
	@Test
	public void testTriangle1() {
		//setting up the triangle to be coloured
		PolygonGraph triangle1 = setupTriangle1();
		int neutralCount1 = 0;
		neutralCount1 = NeutralTriangles.colourGraph(triangle1, 1 /*goal*/);
		
		//testing that colourGraph successfully achieved the goal of colouring the triangle such that there is 1 neutral triangle
		assertEquals(1, neutralCount1);
		
		//testing that vertex 3 was successfully coloured blue to make the triangle neutral
		assertEquals("blue", triangle1.vertices.get(3).getColourName());
	}
	
	@Test
	public void testSquare1() {
		//setting up the triangle to be coloured
		PolygonGraph square1 = setupSquare1();
		int neutralCount2 = 0;
		neutralCount2 = NeutralTriangles.colourGraph(square1, 2 /*goal*/);
		
		//testing that colourGraph successfully achieved the goal of colouring the the square such that there are 2 neutral triangles
		assertEquals(2, neutralCount2);
		
		//testing that vertices 3 and 4 were successfully coloured to make the triangle neutral
		assertEquals("red", square1.vertices.get(3).getColourName());
		assertEquals("yellow", square1.vertices.get(4).getColourName());
	}

	@Test
	public void testSquare2() {
		//setting up the triangle to be coloured
		PolygonGraph square2 = setupSquare2();
		int neutralCount3 = 0;
		neutralCount3 = NeutralTriangles.colourGraph(square2, 2 /*goal*/);
		
		//testing that colourGraph failed to achieve this impossible task
		assertEquals(-1, neutralCount3);
		
		//testing that vertices 3 and 4 were are uncoloured after colourGraph fails to find a solution
		assertEquals("null", square2.vertices.get(3).getColourName());
		assertEquals("null", square2.vertices.get(4).getColourName());
	}
	
	/*
	 * A very simple graph.  Just a triangle to test the graph colouring algorithm.
	 */
	public static PolygonGraph setupTriangle1() {
		PolygonGraph g = new PolygonGraph();
		
		g.setNrstNbrs(1, 2); g.setNrstNbrs(1, 3); g.setNrstNbrs(2, 3);
		g.setVertexColour(1, "red");
		g.setVertexColour(2, "yellow");
		
		return g;
	}
	
	/*
	 * A slightly more complicated graph.  A square with two opposite corners connected.  2 neutral triangles are possible.
	 */
	public static PolygonGraph setupSquare1() {
		PolygonGraph g = new PolygonGraph();
		
		g.setNrstNbrs(1, 2); g.setNrstNbrs(1, 4); g.setNrstNbrs(2, 3); g.setNrstNbrs(2, 4); g.setNrstNbrs(3, 4);
		g.setVertexColour(1, "red");
		g.setVertexColour(2, "blue");
		
		return g;
	}

	/*
	 * The same graph as above, but with two corners of a triangle having the same colour.  2 neutral triangles are impossible.
	 */
	public static PolygonGraph setupSquare2() {
		PolygonGraph g = new PolygonGraph();
		
		g.setNrstNbrs(1, 2); g.setNrstNbrs(1, 4); g.setNrstNbrs(2, 3); g.setNrstNbrs(2, 4); g.setNrstNbrs(3, 4);
		g.setVertexColour(1, "red");
		g.setVertexColour(2, "red");
		
		return g;
	}

	
}
