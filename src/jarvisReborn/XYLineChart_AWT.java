package jarvisReborn;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BasicStroke; 

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {
	
  int XTickUnit = 10000000;

  public XYLineChart_AWT( String applicationTitle, String chartTitle ) {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         "Category" ,
         "Score" ,
         createDataset() ,
         PlotOrientation.VERTICAL,true,true,true );


      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
//      plot.getRangeAxis().setAutoRange( false );
      NumberAxis domain = (NumberAxis) plot.getDomainAxis();
      domain.setTickUnit(new NumberTickUnit(XTickUnit));
      
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      renderer.setSeriesPaint( 0 , Color.RED );
      renderer.setSeriesPaint( 1 , Color.GREEN );
      renderer.setSeriesPaint( 2 , Color.YELLOW );
      renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
      renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
      renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
   }
   
  private XYDataset createDataset( ) {

  final XYSeriesCollection dataset = new XYSeriesCollection( );          
   	File file = new File("/home/abhijithnraj/pgms/python/efps/data_visualization/data.txt"); 
   	BufferedReader br;
    final XYSeries current_series = new XYSeries( "Current Series" );     
	try {
		br = new BufferedReader(new FileReader(file));
		String current; 
		int i=5; //starting row
		while ((current = br.readLine()) != null) { 
			i++;
	   		if(i%1000==0) {
	      	  current_series.add( i , Double.parseDouble(current) );   
//	      	  System.out.println(current);
	   		}
		   	if(i==100000000) {
		   		break;
		   	}
	   		
   } 
		   System.out.println("Done");
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	dataset.addSeries(current_series);
	return dataset;
   }

   public static void main( String[ ] args ) {
      XYLineChart_AWT chart = new XYLineChart_AWT("Browser Usage Statistics",
         "Which Browser are you using?");
      chart.pack( );          
      RefineryUtilities.centerFrameOnScreen( chart );          
      chart.setVisible( true ); 
   }
}
