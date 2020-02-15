package jarvisReborn;
import org.jfree.chart.ChartPanel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;


public class PlotCurrentGUI extends JFrame {

   ArrayList<String> x = new ArrayList<String>();
   ArrayList<Double> y = new ArrayList<Double>();
   int WINDOW =100;
   int counter=WINDOW;
   JFreeChart lineChart;
   String chartTitle;
   final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
   ChartPanel chartPanel;
   public boolean userCloseButtonClick=false;
   String xAxisString="Time 1 minute";
   String yAxisString="Current";
   
 JFreeChart setAxes(JFreeChart lineChart) {
	    CategoryPlot xyPlot = lineChart.getCategoryPlot();
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        CategoryAxis Domain = xyPlot.getDomainAxis();
        Domain.setTickLabelsVisible(false);
        range.setRange(0.0, 3.5);
        range.setTickUnit(new NumberTickUnit(0.1));
        return lineChart;
	 
 }
       
  public void initXY() {
	   for(int i=0;i<WINDOW;i++) {
		  Date d = new Date();
		  String s = simpleDateFormat.format(d);
		  x.add(i,s);
		  y.add(i,(double) 0);
	   }
//		y.add(0,getSensorData());
		 setNewLineChart();
		 chartPanel.setPreferredSize( new java.awt.Dimension( 1200 ,760) );
	      
  }
 
//  Double getSensorData() {
//	 // System.out.println("Called GetSensorData with input="+Details.plotInput);
//	    String input = Specification.plotInput;
//	    String[] args = input.split("\\s+");
//	    Integer mcu = Integer.parseInt(args[1]);
//	    Integer sensorIndex = Integer.parseInt(args[0]); 
//		String output;
//				//mcu=Integer.valueOf(input.substring(space1+1));
//		try {
//			synchronized (Core.telnet[mcu]) {
//				output=Core.telnet[mcu].echo("sensor"+"\r");
//				if(output.equals("No input available")) {
//					Core.telnet[mcu].checkTelnet(0);
//					output=Core.telnet[mcu].echo("sensor"+"\r");
//				}
//			}
//		}
//		catch(Exception e){
//			output="Error contacting ESP";
//		}
//		if(!Core.telnet[mcu].run) {
//			output="Error contacting ESP";
//		}
//		//System.out.println("output reieved from sensor "+output);
//		String[] sensorData = output.split("\\s+");
//		
//		if(sensorData.length!=2) {
//			System.out.println("Error occured while getting sensor data");
//			return (double)0;
//		}
//		else if(output.contains("allowed")) {
//			System.out.println("Not Allowed Error occured while getting sensor data");
//			return (double)0;
//		}
//		//System.out.println("SensorIndex data is "+sensorData[sensorIndex]);
//	
//		return Double.parseDouble(sensorData[sensorIndex]);
//		
//		
//	}
   public PlotCurrentGUI( String applicationTitle , String chartTitle ) {
	   
      super(applicationTitle);
      this.chartTitle=chartTitle;
      this.initXY();
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      JFrame frame = this;
      this.addWindowListener(new WindowAdapter() {
    	  @Override
    	public void windowClosing(WindowEvent e) {
    		  //System.out.println("Close button clicked");
    		  userCloseButtonClick=true;
    		  frame.dispose();
    		
    	}
	});

   }
   void setNewLineChart() {
	   lineChart = ChartFactory.createLineChart(
		         chartTitle,
		         xAxisString,yAxisString,
		         getNextDataSet(x,y),
		         PlotOrientation.VERTICAL,
		         true,true,false);
	   	lineChart = setAxes(lineChart);
	      chartPanel = new ChartPanel( lineChart );
	      add( chartPanel );

   }
   public void update() {
	   	  setNewLineChart();
	      this.revalidate();
		         
   }
   private DefaultCategoryDataset getNextDataSet(ArrayList<String> x,ArrayList<Double> y) {
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );    

	    String input = Specification.plotInput;
	    String[] args = input.split("\\s+");
	    Integer mcu = Integer.parseInt(args[1]);
	    Integer sensorIndex = Integer.parseInt(args[0]);
	    
	    System.out.println("PlotCurrent: The database string is "+(mcu*4+sensorIndex));
	    List<List<Object>> nextPoints= Core.dbClient.getValues(Integer.toString(mcu*4+sensorIndex), WINDOW);
	    for(int i=WINDOW-1;i>=0;i--) {
//	    	 Date date  = new Date(nextPoints.get(i).get(0).toString());
//	    	 System.out.println("PlotCurrent: The window value and i value are "+WINDOW+" "+i);
		      Date d = new Date();
		      String s = simpleDateFormat.format(d);
		      //System.out.println("PlotCurrent: TIme = "+s);
//	    	System.out.println("Plot: Data Recieved "+Double.parseDouble(nextPoints.get(i).get(0).toString()));
//	    	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//	    	Calendar calendar = Calendar.getInstance();
//	    	Long milliSeconds = Long.parseLong(nextPoints.get(i).get(0).toString());
//	    	calendar.setTimeInMillis(milliSeconds);
//	    	System.out.println("PlotCurrent: The time is "+formatter.format(calendar.getTime()));
	    	
	    	dataset.addValue(Double.parseDouble(nextPoints.get(i).get(1).toString())/10000 , "Sensor" ,s+i );
	     }
//	      x.add(s);
//		  y.add(getSensorData());
//	      x.remove(0);
//	      y.remove(0);
//	      for(int i=0;i<x.size();i++) {
//	    	  dataset.addValue( y.get(i) , "Sensor" ,x.get(i)  );
//	      }
	      
//	      counter++;
	      return dataset;
   }
   
}