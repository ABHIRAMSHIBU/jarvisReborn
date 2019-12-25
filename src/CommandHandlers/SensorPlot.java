package CommandHandlers;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import jarvisReborn.Core;
import jarvisReborn.Details;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;


public class SensorPlot extends Application {
    final int WINDOW_SIZE = 10;
    private ScheduledExecutorService scheduledExecutorService;
    
    
//    public static void main(String[] args) {
//        launch(args);
//    }
    public MainCMDHandler mch;
    public String arg;
    public int test;
    public Integer sensorIndex;
//    public void setSensorPlotData(MainCMDHandler mch,String arg,int sensorIndex) {
//    	
//    	this.mch = mch;
//    	this.arg=String.valueOf(arg);
//    	this.sensorIndex = sensorIndex; 
//    	System.out.println("The argument is "+this.arg+" mch is "+mch+" sensorIndex is "+sensorIndex);
//    }
    public void plot() {
    	System.out.println("Launch recieved");
    	System.out.println("The argument is "+this.arg+" mch is "+mch+" sensorIndex is "+sensorIndex);
    	test=100; // Some random value
    	launch();
    	
    }
    public Double getNextData() {
    	String[] args = Details.plotInput.split("\\s+");
		System.out.println("The input recieved is "+Details.plotInput);
		if(args.length!=2) {
			System.out.println("Some error with plot argument = "+Details.plotInput);
		}
		Integer sensorIndex = Integer.parseInt(args[0]);
		Integer mcu = Integer.parseInt(args[1]);
		System.out.println("sensorIndex  = "+sensorIndex+" mcu = "+mcu);
		String output;
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
		System.out.println("Output returned is "+output);
    	String[] sensorData = output.split("\\s+");
    	System.out.println("Sensor Data is "+sensorData[0]+" "+sensorData[1]);
    	System.out.println("The sensor data double to be returned to plot is index = "+sensorIndex+" Data = "+sensorData[sensorIndex]);
    	return Double.parseDouble(sensorData[sensorIndex]);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
    	
        primaryStage.setTitle("SSAL Sensor Data");
        Platform.setImplicitExit(false);
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("Current Value");
        yAxis.setAnimated(false); // axis animations are removed

        //creating the line chart with two axis created above
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Realtime JavaFX Charts");
        lineChart.setAnimated(false); // disable animations

        //defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data Series");

        // add series to chart
        lineChart.getData().add(series);

        // setup scene
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setScene(scene);

        // show the stage
        primaryStage.show();

        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
//            Integer random = ThreadLocalRandom.current().nextInt(10);
        	Double random = this.getNextData();


            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), random));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        try {
			super.stop();
			Platform.exit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        scheduledExecutorService.shutdownNow();
    }

}
