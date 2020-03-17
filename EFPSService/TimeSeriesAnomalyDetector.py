class NeuralTimeSeriesAnomolyDetectorTrainer:
    def __init__(self,data):
        self.data = data
        self.history=None
        self.scaler=None
        self.model=None
        self.time_steps=None
        self.threshold=None
        self.mae_loss=None
        self.plt=None
        self.sns=None
        self.training_loss=None
        self.testing_loss=None
        self.params=None
    def set_plotting_parameters(self):
        import seaborn as sns
        import matplotlib.pyplot as plt
        from pylab import rcParams
        from pandas.plotting import register_matplotlib_converters
        self.plt=plt
        self.sns=sns
        rcParams['figure.figsize'] = 10, 6
        register_matplotlib_converters()
        sns.set(style='whitegrid', palette='muted', font_scale=1.5)
        
    def create_dataset(self,X, y, time_steps=1):
        import numpy as np
        Xs, ys = [], []
        for i in range(len(X) - time_steps):
            v = X[i:(i + time_steps)]
            Xs.append(v)
            ys.append(y[i + time_steps])
        return np.array(Xs), np.array(ys)  
    def scale(self,data):
        from sklearn.preprocessing import StandardScaler
        scaler = StandardScaler()
        scaler = scaler.fit(data.reshape(-1,1))
        self.scaler=scaler
        return scaler.transform(data.reshape(-1,1)).reshape(-1,)

    def split_train_test(self,train_ratio):
        train_size = int(len(self.data) *train_ratio)
        test_size = len(self.data) - train_size
        train, test = self.data[0:train_size], self.data[train_size:len(self.data)]
        print(train.shape, test.shape)
        return self.data,self.data
    def train(self,time_steps,model_dir=None,epochs=10,batch_size=1000,evaluation_batch_size=None,evaluation_workers=1):
        import os
        self.time_steps=time_steps
        train,test = self.split_train_test(0.95)
        train = self.scale(train)
        test  = self.scale(test)
        X_train, y_train = self.create_dataset(train, train, time_steps)
        X_test, y_test = self.create_dataset(test, test, time_steps)
        X_train = X_train.reshape(len(X_train),time_steps,1)
        X_test = X_test.reshape(len(X_test),time_steps,1)
        if(model_dir==None or (not os.path.exists(model_dir))):
            print("Model does not already exists in the provided path. Creating Model")
            self.model = self.create_model(X_train,y_train)
            print("Model Created is ")
            print(self.model.summary())
        else:
            params_path = os.path.join(model_dir,"params.pkl")
            self.params=self.load_params(params_path)
#             print("Loaded the parameters from file {}".join(params_path))
            self.model = self.load_model()
            print("Model Already exists in the directory and loaded")
            print(self.model.summary())
        
        print(X_train.shape,y_train.shape)
        self.history = self.model.fit(
            X_train,y_train,
            epochs = epochs,
            batch_size = batch_size,
            validation_split = 0.1,
            shuffle = False
        )
        self.plot_training()
        self.training_loss = self.evaluate(X_train,y_train,batch_size=evaluation_batch_size,workers=evaluation_workers)
        self.testing_loss = self.evaluate(X_test,y_test,batch_size=evaluation_batch_size,workers=evaluation_workers)
        print("Training and Testing errors are {} and {}".format(self.training_loss,self.testing_loss))
    def plot_training(self):
        self.set_plotting_parameters()
        self.plt.plot(self.history.history['loss'], label='train')
        self.plt.plot(self.history.history['val_loss'], label='test')
        self.plt.legend();
        self.plt.show()
    def create_model(self,X_train,y_train):
        from tensorflow import keras
        print(X_train.shape)
        model = keras.Sequential()
        model.add(keras.layers.LSTM(
            units=64, 
            input_shape=(X_train.shape[1], X_train.shape[2])
        ))
        model.add(keras.layers.Dropout(rate=0.2))
        model.add(keras.layers.RepeatVector(n=X_train.shape[1]))
        model.add(keras.layers.LSTM(units=64, return_sequences=True))
        model.add(keras.layers.Dropout(rate=0.2))
        model.add(keras.layers.Flatten())
#         model.add(keras.layers.TimeDistributed(keras.layers.Dense(units=X_train.shape[2])))
        model.add(keras.layers.Dense(self.time_steps))
        model.compile(loss='mae', optimizer='adam')
        return model
    def evaluate(self,X,y,batch_size=None,workers=1):
        return self.model.evaluate(X,y,batch_size=batch_size,workers=workers)
    def predict(self,threshold,predict_batch_size=None,predict_workers=1):
        import numpy as np
        import pandas as pd
        self.threshold=threshold
        X,y = self.create_dataset(self.data,self.data,self.time_steps)
        X=X.reshape(X.shape[0],X.shape[1],1)
        X_pred = self.model.predict(X,batch_size=predict_batch_size,workers=predict_workers)
        X_pred = X_pred.reshape(X_pred.shape[0],X_pred.shape[1],1)
        mae_loss = np.mean(np.abs(X_pred - X), axis=1)
        self.mae_loss=mae_loss
        score_df = pd.DataFrame(index=list(range(len(self.data[self.time_steps:]))))
        score_df['anomaly'] = mae_loss > self.threshold
        return score_df
    def get_anomalies(self,score_df):
        anomalies = score_df[score_df.anomaly == True]
        return anomalies
    def plot_anomaly_threshold(self,score_df):
        self.set_plotting_parameters()
        self.plt.plot(score_df.index, self.mae_loss, label='loss')
        self.plt.plot(score_df.index, np.array([self.threshold]*len(score_df)), label='threshold')
        self.plt.xticks(rotation=25)
        self.plt.legend();
    def plot_anomaly(self,anomalies):
        import numpy as np
        self.set_plotting_parameters()
        self.plt.plot(
          np.array(list(range(len(self.data[self.time_steps:])))), 
          self.data[self.time_steps:], 
          label='Sensor Data'
        );
        self.sns.scatterplot(
          anomalies.index,
          self.data[self.time_steps:][anomalies.index],
          color=self.sns.color_palette()[3],
          s=25,
          label='Anomaly'
        )
        self.plt.show()
    def export_model(self,model_dir):
        import os
        import pickle
        if(not os.path.exists(model_dir)):
            os.mkdir(model_dir)
        model_location = os.path.join(model_dir,"model.h5")
        scaler_location = os.path.join(model_dir,"scaler.pkl")
        self.model.save(model_location)
        self.params={}
        self.params["model_path"]=model_location
        self.params["training_loss"]=self.training_loss
        self.params["testing_loss"]=self.testing_loss
        self.params["time_steps"]=self.time_steps
        self.params["scaler_path"]=scaler_location
        params_location = os.path.join(model_dir,"params.pkl")
        pickle.dump(self.params,open(params_location,"wb"))
        pickle.dump(self.scaler,open(scaler_location,"wb"))
        print("The model is saved at location ",model_location)
        print("The model parameter file is saved at location ",params_location)
    def load_model(self):
        model_path = self.params["model_path"]
        from tensorflow.keras.models import load_model
        model = load_model(model_path)
        return model
    def load_params(self,params_path):
        import pickle
        params = pickle.load(open(params_path,"rb"))
        return params


class NeuralTimeSeriesAnomolyDetectorPredictor:
    def __init__(self,model_dir):
        import os
        self.model_dir=model_dir
        self.params_path = os.path.join(model_dir,"params.pkl")
        self.model_path =None
        self.params=self.load_params(self.params_path)
        self.model = self.load_model()
        self.threshold=None
        self.data=None
        self.time_steps=None
        self.scaler=self.load_scaler()
    def set_plotting_parameters(self):
        import seaborn as sns
        import matplotlib.pyplot as plt
        from pylab import rcParams
        from pandas.plotting import register_matplotlib_converters
        self.plt=plt
        self.sns=sns
        rcParams['figure.figsize'] = 10, 6
        register_matplotlib_converters()
        sns.set(style='whitegrid', palette='muted', font_scale=1.5)
    def load_model(self):
        model_path = self.params["model_path"]
        from tensorflow.keras.models import load_model
        model = load_model(model_path)
        return model
    def load_scaler(self):
        import pickle
        scaler_path = self.params["scaler_path"]
        scaler = pickle.load(open(scaler_path,"rb"))
        return scaler
    def load_params(self,params_path):
        import pickle
        from pprint import pprint
        params = pickle.load(open(params_path,"rb"))
        print("Params Loaded are ")
        pprint(params)
        return params
    def create_dataset(self,X, y, time_steps=1):
        import numpy as np
        Xs, ys = [], []
        for i in range(len(X) - time_steps):
            v = X[i:(i + time_steps)]
            Xs.append(v)
            ys.append(y[i + time_steps])
        return np.array(Xs), np.array(ys) 
    def get_anomalies(self,score_df):
        anomalies = score_df[score_df.anomaly == True]
        return anomalies
    def predict(self,data,threshold=None,predict_batch_size=None,predict_workers=1):  
        import numpy as np
        import pandas as pd
        self.data=data
        if(threshold==None):
            self.threshold = float(self.params["training_loss"]/2)
        else:
            self.threshold=threshold
        self.time_steps = self.params["time_steps"]
        X,y = self.create_dataset(self.data,self.data,self.time_steps)
        X=X.reshape(X.shape[0],X.shape[1],1)
        X_pred = self.model.predict(X,batch_size=predict_batch_size,workers=predict_workers)
        X_pred = X_pred.reshape(X_pred.shape[0],X_pred.shape[1],1)
        mae_loss = np.mean(np.abs(X_pred - X), axis=1)
        self.mae_loss=mae_loss
        score_df = pd.DataFrame(index=list(range(len(self.data[self.time_steps:]))))
        score_df['anomaly'] = mae_loss > self.threshold
        return score_df
    def plot_anomaly(self,anomalies):
        import numpy as np
        self.set_plotting_parameters()
        self.plt.plot(
          np.array(list(range(len(self.data[self.time_steps:])))), 
          self.data[self.time_steps:], 
          label='Sensor Data'
        );
        self.sns.scatterplot(
          anomalies.index,
          self.data[self.time_steps:][anomalies.index],
          color=self.sns.color_palette()[3],
          s=52,
          label='Anomaly'
        )
        self.plt.show()
        
import pandas as pd
from tqdm import tqdm
def downsample(data,window):
    i=0
    sampled_list=[]
    with tqdm(total=int(len(data)/window)) as pbar:
        while(i<len(data)):
            points = data[i:i+window]
            chosen_point = points[int(len(points)/2)]
            sampled_list.append(chosen_point)
            i=i+window
            pbar.update(1)
    return sampled_list
