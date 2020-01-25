TRAINING_DIR = "processed/train"
import pandas as pd
import os
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn import svm
from sklearn.metrics import accuracy_score
import numpy as np
training_files = [os.path.join(TRAINING_DIR,i) for i in os.listdir(TRAINING_DIR)]


def scikit_learn_pipeline(modelClass,X_Train, X_Test,Y_Train,Y_Test):
    model = modelClass()
    model.fit(X_Train,Y_Train)
    Y_Train_pred=model.predict(X_Train)
    Y_Test_pred= model.predict(X_Test)
    training_accuracy=accuracy_score(Y_Train_pred,Y_Train)
    testing_accuracy = accuracy_score(Y_Test_pred,Y_Test)
    return training_accuracy,testing_accuracy

modelClassList=[LogisticRegression,DecisionTreeClassifier,RandomForestClassifier,svm.SVC]
modelClassNameList = ["LogisticRegression","DecisionTreeClassifier","RandomForestClassifier","SVM"]
for i in training_files:
    m=pd.read_csv(i)
    X = np.array(m.iloc[:,:100])
    Y = np.array(m.iloc[:,-1])
    X_Train,X_Test,Y_Train,Y_Test = train_test_split(X,Y,random_state=2)
    for j in range(len(modelClassList)):
        training_accuracy,testing_accuracy = scikit_learn_pipeline(modelClassList[j], X_Train, X_Test,Y_Train,Y_Test)
        print(modelClassNameList[j],"Training accuracy =",training_accuracy)
        print(modelClassNameList[j],"Testing accuracy=",testing_accuracy)
