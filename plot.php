<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>

  </head>
  <body>
    <div class="container" style="margin-top: 4%">
          <div class="container">
              <h3 style="margin-left: 10%"><span class="badge badge-info">Air Conditioner</span> &nbsp; <span class="badge badge-success">Working</span> </h3>
              <div id="myDiv" style="height: 40%"></div>
          </div>     
          <br>
          <div class="container">
              <!--<h3><span class="badge badge-secondary">Device 2</span></h3>-->
              <h3 style="margin-left: 10%"><span class="badge badge-info">Refrigerator</span> &nbsp; <span class="badge badge-success">Working</span> </h3>
              <div id="myDiv1" style="height: 40%"></div>
          </div>
        <script type="text/javascript">
        // fetch data here and return value
        function rand() {
          return Math.random();
        }
        var time = new Date();
        // Sensor value 1
        var data = [{
          x: [time],
          y: [rand],
          mode: 'lines',
          line: {color: '#80CAF6'}
          }]
        // Sensor value 2
        var data1 = [{
          x: [time],
          y: [rand],
          mode: 'lines',
          line: {color: '#80CAF6'}
          }]
        Plotly.newPlot('myDiv', data);
        var cnt = 0;
        var interval = setInterval(function() {
        var time = new Date();
        var update = {
        x:  [[time]],
        y: [[rand()]] // insert our sensor data
      }
  var olderTime = time.setMinutes(time.getMinutes() - 1);
  var futureTime = time.setMinutes(time.getMinutes() + 1);
  var minuteView = {
        xaxis: {
          type: 'date',
          range: [olderTime,futureTime]
        }
      };

  Plotly.relayout('myDiv', minuteView);
  Plotly.extendTraces('myDiv', update, [0])

  if(++cnt === 100) clearInterval(interval);
  }, 1000);    

  //Device 2
  Plotly.newPlot('myDiv1', data1);
        var cnt1 = 0;
        var interval = setInterval(function() {
        var time = new Date();
        var update = {
        x:  [[time]],
        y: [[rand()]] // insert our sensor data
      }
  var olderTime = time.setMinutes(time.getMinutes() - 1);
  var futureTime = time.setMinutes(time.getMinutes() + 1);
  var minuteView = {
        xaxis: {
          type: 'date',
          range: [olderTime,futureTime]
        }
      };

  Plotly.relayout('myDiv1', minuteView);
  Plotly.extendTraces('myDiv1', update, [0])

  if(++cnt1 === 100) clearInterval(interval);
}, 1000);
    </script>
    </div>
  </body>