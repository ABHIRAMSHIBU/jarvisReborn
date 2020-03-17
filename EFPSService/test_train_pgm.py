from TimeSeriesAnomalyDetector import NeuralTimeSeriesAnomolyDetectorTrainer

import pandas as pd
import numpy as np
df = pd.read_csv('data/LedOutProcessed/labelled_data/LedOut_combined.csv')
data = np.array(df[["Current"]]).reshape(-1,)


ntsad = NeuralTimeSeriesAnomolyDetectorTrainer(data)
ntsad.train(time_steps = 5,model_dir="LED",epochs=10,batch_size=4000,
evaluation_batch_size=4000,evaluation_workers=10)



pred = ntsad.predict(0.04,predict_batch_size=4000,predict_workers=10)
# ntsad.plot_anomaly_threshold(pred)

anomalies=ntsad.get_anomalies(pred)
ntsad.plot_anomaly(anomalies)
ntsad.export_model("LED")
