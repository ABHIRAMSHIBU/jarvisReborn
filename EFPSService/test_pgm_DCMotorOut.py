import pandas as pd
import numpy as np
df = pd.read_csv("data/DCMotorOut.txt",header=None)
data = np.array(df).reshape(-1,)
print(data.shape)

from TimeSeriesAnomalyDetector import downsample
sampled_data = np.array(downsample(data,660))
print(sampled_data.shape)

# import matplotlib.pyplot as plt
# plt.plot(sampled_data)
# plt.show()



from TimeSeriesAnomalyDetector import NeuralTimeSeriesAnomolyDetectorTrainer



ntsad = NeuralTimeSeriesAnomolyDetectorTrainer(sampled_data)
ntsad.train(time_steps = 10,model_dir="DCMotor",epochs=20,batch_size=1000,
evaluation_batch_size=1000,evaluation_workers=10)
ntsad.export_model("DCMotor")

import pickle
params = pickle.load(open("DCMotor/params.pkl","rb"))

pred = ntsad.predict(float(params["training_loss"]/2),predict_batch_size=1000,predict_workers=10)
# ntsad.plot_anomaly_threshold(pred)

anomalies=ntsad.get_anomalies(pred)
print("Percentage Anomaly is {}".format(len(anomalies)/len(pred)))
ntsad.plot_anomaly(anomalies)

