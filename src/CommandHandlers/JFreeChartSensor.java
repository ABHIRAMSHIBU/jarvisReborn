package CommandHandlers;
import org.jfree.chart.ChartPanel;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import jarvisReborn.Core;
import jarvisReborn.Details;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;

public class JFreeChartSensor extends ApplicationFrame {

   ArrayList<String> x = new ArrayList<String>();
   ArrayList<Double> y = new ArrayList<Double>();
   int WINDOW =10;
   int counter=WINDOW;
   JFreeChart lineChart;
   String chartTitle;
   final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
   boolean exitCondition=false;

       
  public void initXY() {


	  
	   for(int i=0;i<WINDOW;i++) {
		  Date d = new Date();
		  String s = simpleDateFormat.format(d);
		  x.add(i,s);
		  y.add(i,getSensorData());
	   }
	   
	      for (String n:x) {
	    	  System.out.println(n);
	      }
	      for (Double num:y) {
	    	  System.out.println(num);
	      }
		   
	      lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "Years","Number of Schools",
	         getNextDataSet(x,y),
	         PlotOrientation.VERTICAL,
	         true,true,false);
	         
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 1200 ,760) );
	      setContentPane( chartPanel );
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
      this.addWindowListener(new WindowAdapter() {
    	  public void windowClosing(WindowEvent w) {
    		  System.out.println("Doing this");
    		  exitCondition=true;
    	  }
	});

   }

   void update() {
	   System.out.println("Called update");
	   lineChart = ChartFactory.createLineChart(
		         chartTitle,
		         "Years","Number of Schools",
		         getNextDataSet(x,y),
		         PlotOrientation.VERTICAL,
		         true,true,false);
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
	      setContentPane( chartPanel );
		         
   }
   private DefaultCategoryDataset getNextDataSet(ArrayList<String> x,ArrayList<Double> y) {
	   	  System.out.println("Called Get Next Dataset");
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
	      for (String n:x) {
	    	  System.out.println(n);
	      }
	      for (Double num:y) {
	    	  System.out.println(num);
	      }
	      counter++;
	      
	      
	      return dataset;
   }
   
//   void plot() {
//      JFreeChartSensor chart = new JFreeChartSensor(
//         "School Vs Years" ,
//         "Numer of Schools vs years");
//      chart.pack( );
//      RefineryUtilities.centerFrameOnScreen( chart );
//      chart.setVisible( true );
//      while(true) {
//    	  try {
//			Thread.sleep(10000);
//			System.out.println("What");
//		      chart.update();
//		      chart.setVisible(true);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//      }
//   }



}