import joblib
model = joblib.load("processed/models/LedOut_combined_SVM.model")
print(model.predict([[0.1]*100])[0])
