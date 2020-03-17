from TimeSeriesAnomalyDetector import NeuralTimeSeriesAnomolyDetectorPredictor
ntsad_pred = NeuralTimeSeriesAnomolyDetectorPredictor("LED")


import pandas as pd
import numpy as np
df = pd.read_csv('data/LedOutProcessed/labelled_data/LedOut_combined.csv')
data = np.array(df[["Current"]]).reshape(-1,)


score_df = ntsad_pred.predict(data.reshape(-1,),0.06,predict_batch_size=10000,predict_workers=10)

anomalies = ntsad_pred.get_anomalies(score_df)

ntsad_pred.plot_anomaly(anomalies)