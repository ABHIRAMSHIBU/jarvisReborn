package CommandHandlers;
import org.jfree.chart.ChartPanel;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;

import jarvisReborn.Core;
import jarvisReborn.Details;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;


public class JFreeChartSensor extends JFrame {

   ArrayList<String> x = new ArrayList<String>();
   ArrayList<Double> y = new ArrayList<Double>();
   int WINDOW =10;
   int counter=WINDOW;
   JFreeChart lineChart;
   String chartTitle;
   final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
   ChartPanel chartPanel;
   boolean userCloseButtonClick=false;
   
 JFreeChart setAxes(JFreeChart lineChart) {
	    CategoryPlot xyPlot = lineChart.getCategoryPlot();
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
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
		y.add(0,getSensorData());
		  
	      lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "Years","Number of Schools",
	         getNextDataSet(x,y),
	         PlotOrientation.VERTICAL,
	         true,true,false);
	      
	      lineChart = setAxes(lineChart);
	      chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 1200 ,760) );
	      add( chartPanel );
	      
  }
 
  Double getSensorData() {
	  System.out.println("Called GetSensorData with input="+Details.plotInput);
	    String input = Details.plotInput;
	    String[] args = input.split("\\s+");
	    Integer mcu = Integer.parseInt(args[1]);
	    Integer sensorIndex = Integer.parseInt(args[0]); 
		String output;
				//mcu=Integer.valueOf(input.substring(space1+1));
		try {
			synchronized (Core.telnet[mcu]) {
				output=Core.telnet[mcu].echo("sensor"+"\r");
				if(output.equals("No input available")) {
					Core.telnet[mcu].checkTelnet(0);
					output=Core.telnet[mcu].echo("sensor"+"\r");
				}
			}
		}
		catch(Exception e){
			output="Error contacting ESP";
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
		}
		System.out.println("output reieved from sensor "+output);
		String[] sensorData = output.split("\\s+");
		
		if(sensorData.length!=2) {
			System.out.println("Error occured while getting sensor data");
			return (double)0;
		}
		System.out.println("SensorIndex data is "+sensorData[sensorIndex]);
	
		return Double.parseDouble(sensorData[sensorIndex]);
		
		
	}
   public JFreeChartSensor( String applicationTitle , String chartTitle ) {
	   
      super(applicationTitle);
      this.chartTitle=chartTitle;
      this.initXY();
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      JFrame frame = this;
      this.addWindowListener(new WindowAdapter() {
    	  @Override
    	public void windowClosing(WindowEvent e) {
    		  System.out.println("Close button clicked");
    		  userCloseButtonClick=true;
    		  frame.dispose();
    		
    	}
	});

   }

   void update() {
	   lineChart = ChartFactory.createLineChart(
		         chartTitle,
		         "Years","Number of Schools",
		         getNextDataSet(x,y),
		         PlotOrientation.VERTICAL,
		         true,true,false);
	   	lineChart = setAxes(lineChart);
	      chartPanel = new ChartPanel( lineChart );
	      add( chartPanel );
	      this.revalidate();
		         
   }
   private DefaultCategoryDataset getNextDataSet(ArrayList<String> x,ArrayList<Double> y) {
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );    
	      Date d = new Date();
	      String s = simpleDateFormat.format(d);
	      x.add(s);
		  y.add(getSensorData());
	      x.remove(0);
	      y.remove(0);
	      for(int i=0;i<x.size();i++) {
	    	  dataset.addValue( y.get(i) , "Sensor" ,x.get(i)  );
	      }
	      counter++;
	      return dataset;
   }
   
}