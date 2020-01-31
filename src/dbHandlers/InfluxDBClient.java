package dbHandlers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;

public class InfluxDBClient {
	public static InfluxDB db;
	public List<List<Object>> getValues(String name,int limit){
		QueryResult resultQuerry = db.query(new Query("SELECT * FROM " + name + " GROUP BY * ORDER BY DESC LIMIT "+limit));
		List<Result> result = resultQuerry.getResults();
		Result r = result.get(0);
		Series series= r.getSeries().get(0);
		List<List<Object>> object = series.getValues();		
		return object;
	}
	public void connect() {
		db = InfluxDBFactory.connect("http://127.0.0.1:8086");
		db.setDatabase("ssal");
	}
	public void insert(String name,int mcu, int sensor,int value) {
		db.write(Point.measurement(name)
			    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
			    .addField("value", value)
			    .tag("mcu", ""+mcu)
			    .tag("sensor", ""+sensor)
			    .build());
	}
	public QueryResult query(String arg0) {
		QueryResult resultQuerry = db.query(new Query(arg0));
		return resultQuerry;
	}
	public InfluxDBClient(){
		this.connect();	
	}
//	public static void main(String[] args) {
//		InfluxDBClient client = new InfluxDBClient();
//		client.connect();
//		client.insert("test", 0, 0, 10);
//		
//	}
}
