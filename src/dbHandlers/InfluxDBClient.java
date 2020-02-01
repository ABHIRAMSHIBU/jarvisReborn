package dbHandlers;
/* Author Abhiram Shibu
 * Copyleft 2020 
 * Project SSAL
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
	public void insert(String name,int mcu, int sensor,float sensor2) {
		db.write(Point.measurement(name)
			    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
			    .addField("value", sensor2)
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
