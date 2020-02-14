package jarvisReborn;

public class SensorParser {
	int arr[][];
	int x=Specification.sensorBufferLength;
	int y=Specification.sensorCount;
	public SensorParser(String input) {
		parseSensorList(input);
	}
	public String toString() {
		String output="";
		for(int i=0;i<x;i++) {
			for(int j=0;j<y;j++) {
				if(j<y-1) {
					output+=arr[i][j]+",\t";
				}
				else {
					output+=arr[i][j];
				}
			}
			output+="\n";
		}
		return output;	
	}
	public int[][] parseSensorList(String input) {
		arr=new int[Specification.sensorBufferLength][Specification.sensorCount];
		for(int i=0;i<Specification.sensorBufferLength;i++) {
			String temp;
			int tab=input.indexOf("\t");
			if(tab!=-1) {
				temp=input.substring(0,tab);
			}
			else {
				temp=input;
			}
			input=input.substring(tab+1);
			for(int j=0;j<Specification.sensorCount;j++) {
				String temp2;
				int comma = temp.indexOf(",");
				if(comma!=-1) {
					temp2=temp.substring(0,comma);
				}
				else {
					temp2=temp;
				}
				temp=temp.substring(comma+2);
				arr[i][j]=Integer.valueOf(temp2);
			}
		}
		//SensorParser.printArray(arr, Specification.sensorBufferLength, Specification.sensorCount);
		return arr;
	}
	public int[][] getArray(){
		return arr;
	}
	public static void printArray(int[][] arr,int x,int y) {
		for(int i=0;i<x;i++) {
			for(int j=0;j<y;j++) {
				System.out.println("StringHander: i="+i+" j="+j+"\t"+arr[i][j]); 
			}
		}
	}
}
