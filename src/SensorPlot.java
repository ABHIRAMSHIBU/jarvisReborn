


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import CommandHandlers.MainCMDHandler;
import javafx.application.Application;
import javafx.application.Platform;
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
    MainCMDHandler mch ;
    String arg;
    Integer sensorIndex;
    public void setSensorPlotData(MainCMDHandler mch,String arg,int sensorIndex) {
    	
    	this.mch = mch;
    	this.arg=String.valueOf(arg);
    	this.sensorIndex = sensorIndex; 
    	System.out.println("The argument is "+this.arg+" mch is "+mch+" sensorIndex is "+sensorIndex);
    }
    public void plot() {
    	System.out.println("Launch recieved");
    	launch();
    	
    }
    
    public Double getNextData() {
    	System.out.println("The argument is "+this.arg+" mch is "+mch+" sensorIndex is "+sensorIndex);
    	this.mch.parseSensors(this.arg);
    	System.out.println("DONE");
    	String[] sensorData = mch.output.split("s\\+");
    	System.out.println("The sensor data double to be returned to plot is index = "+this.sensorIndex+" Data = "+sensorData[this.sensorIndex]);
    	return Double.parseDouble(sensorData[this.sensorIndex]);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SSAL Sensor Data");

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
    public void stop() throws Exception {
        super.stop();
        scheduledExecutorService.shutdownNow();
    }
}
