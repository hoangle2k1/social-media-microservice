from flask import Flask, request
from keras.preprocessing.text import Tokenizer
from keras.utils import pad_sequences
from nltk.corpus import stopwords
import nltk
import numpy as np
import re
import keras
import pickle
import string

app = Flask(__name__)


@app.route("/")
def home():
    return "Hello, World!"


def clean_text(text):
    print(text)
    text = str(text).lower()
    text = re.sub('\[.*?\]', '', text)
    text = re.sub('https?://\S+|www\.\S+', '', text)
    text = re.sub('<.*?>+', '', text)
    text = re.sub('[%s]' % re.escape(string.punctuation), '', text)
    text = re.sub('\n', '', text)
    text = re.sub('\w*\d\w*', '', text)
    print(text)
    # text = [word for word in text.split(' ') if word not in stopword]
    # text=" ".join(text)
    stemmer = nltk.SnowballStemmer('english')
    text = [stemmer.stem(word) for word in text.split(' ')]
    text=" ".join(text)
    return text


@app.route("/filter")
def filterHateSpeech():

    text = request.args.get('text')
    load_model=keras.models.load_model("./model.h5")
    with open('tokenizer.pickle', 'rb') as handle:
        load_tokenizer = pickle.load(handle)
    
    test=[clean_text(text)]
    print(test)
    seq = load_tokenizer.texts_to_sequences(test)
    padded = pad_sequences(seq, maxlen=300)
    print(seq)
    pred = load_model.predict(padded)
    print("pred", pred)
    if np.any(pred < 0.5):
        return "0"
    return "1"
    

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001, debug=True)