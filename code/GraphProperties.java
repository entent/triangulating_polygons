package drivewyzeProblem;

public class GraphProperties {
	
	public enum VertexColour {
		RED("red"),
		BLUE("blue"),
		YELLOW("yellow");
		
		private String colourName;
		
		private VertexColour(String s) {
			colourName = s;
		}
		
		public String getColourName() {
			return colourName;
		}
		
		public static VertexColour getColour(String s) {
			for(VertexColour colour : VertexColour.values()) {
				if(s.equals(colour.colourName) ) {
					return colour;
				}
			}
			
			return null;
		}
		
	}
	
}
