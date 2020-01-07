package jarvisReborn;

import java.awt.Color; 
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

   public XYLineChart_AWT( String applicationTitle, String chartTitle ) {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         "Category" ,
         "Score" ,
         createDataset() ,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      NumberAxis domain = (NumberAxis) plot.getDomainAxis();
      domain.setTickUnit(new NumberTickUnit(1000));
      
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
      final XYSeries firefox = new XYSeries( "Firefox" );     
      for(int i=0;i<100;i++) {
    	  firefox.add( i*i , i );       
      }
   
      
//      final XYSeries chrome = new XYSeries( "Chrome" );          
//      chrome.add( 1.0 , 4.0 );          
//      chrome.add( 2.0 , 5.0 );          
//      chrome.add( 3.0 , 6.0 );          
//      
//      final XYSeries iexplorer = new XYSeries( "InternetExplorer" );          
//      iexplorer.add( 3.0 , 4.0 );          
//      iexplorer.add( 4.0 , 5.0 );          
//      iexplorer.add( 5.0 , 4.0 );          
//      
      final XYSeriesCollection dataset = new XYSeriesCollection( );          
      dataset.addSeries( firefox );          
//      dataset.addSeries( chrome );          
//      dataset.addSeries( iexplorer );
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
