import pandas as pd 
import numpy as np
import json

# Leer los datos
raw_instrument_path = 'data/raw/instrument_data.csv'
raw_measurement_path = 'data/raw/measurement_data.csv'
raw_pollutant_path = 'data/raw/pollutant_data.csv'

raw_instrument_df = pd.read_csv(raw_instrument_path)
raw_measurement_df = pd.read_csv(raw_measurement_path)
raw_pollutant_df = pd.read_csv(raw_pollutant_path)

raw_instrument_df["Measurement date"] = pd.to_datetime(raw_instrument_df["Measurement date"])

#All Qs
instrument_status_normal = 0

#Q1
pollutantSO2_item_code = raw_pollutant_df.loc[raw_pollutant_df["Item name"] == 'SO2', "Item code"].iloc[0]

#Q2
pollutantCO_item_code = raw_pollutant_df.loc[raw_pollutant_df["Item name"] == 'CO', "Item code"].iloc[0]
question2_station = 209

#Q3
pollutantO3_item_code = raw_pollutant_df.loc[raw_pollutant_df["Item name"] == 'O3', "Item code"].iloc[0]

#Q4
instrument_status_abnormal = 9

#Q5

#Q6
pollutantPM25_item_code = raw_pollutant_df.loc[raw_pollutant_df["Item name"] == 'PM2.5', "Item code"].iloc[0]

#Q1
normal_SO2_measurement = raw_instrument_df.loc[(raw_instrument_df["Item code"] == pollutantSO2_item_code)&(raw_instrument_df["Instrument status"]==instrument_status_normal)
    ]["Average value"].to_numpy()
average_SO2_concentration = normal_SO2_measurement.mean().round(5)

#Q2
def id_season(date_col):
    month = date_col.month

    if month in [12,1,2]:
        return 1
    elif month in [3,4,5]:
        return 2
    elif month in [6,7,8]:
        return 3
    else:
        return 4

raw_instrument_df['Season'] = raw_instrument_df["Measurement date"].apply(id_season)

CO_station209 = raw_instrument_df.loc[
    (raw_instrument_df["Item code"]==pollutantCO_item_code)
    &(raw_instrument_df["Station code"]==question2_station)
    &(raw_instrument_df["Instrument status"]==instrument_status_normal)
    ]

avg_seasonal_co_pollution = CO_station209.groupby("Season")['Average value'].mean().round(5)

#Q3
raw_instrument_df['Hour'] = raw_instrument_df["Measurement date"].dt.hour

instrument_positives = raw_instrument_df[
    (raw_instrument_df["Average value"]>0)
    ]

pollutantO3_subset = instrument_positives.loc[
    (instrument_positives["Item code"]==pollutantO3_item_code)
    ]

pollutantO3_subset = pollutantO3_subset.copy()

pollutantO3_subset["Normalized Value"] = pollutantO3_subset.groupby("Station code")["Average value"].transform(
    lambda x: (x - x.mean()) / x.std()
)

pollutantO3_hourly_stdv = pollutantO3_subset.groupby("Hour")["Normalized Value"].std().sort_values(ascending=False)
highest_variability_hour = pollutantO3_hourly_stdv.idxmax()

#Q4
abnormal_subset = raw_instrument_df.loc[
    (raw_instrument_df["Instrument status"]==instrument_status_abnormal)
    ]
station_most_abnormal = abnormal_subset["Station code"].value_counts().sort_values(ascending=False).idxmax()

#Q5
notnormal_subset = raw_instrument_df.loc[
    (raw_instrument_df["Instrument status"]!=0)
     ]

station_most_notnormal = notnormal_subset["Station code"].value_counts().sort_values(ascending=False).idxmax()


#Q6
def record_status(row):
    
    item_col = row["Item code"]
    value_col = row["Average value"]

    if item_col == pollutantPM25_item_code:
        
        if value_col <= 15.0:
            return "Good"
        
        elif value_col <= 35.0:
            return "Normal"
        
        elif value_col <= 75.0:
            return "Bad"

        else:
            return "Very bad"
    
    return None

#encoding 
raw_instrument_df['Pollutant Status'] = raw_instrument_df.apply(record_status, axis=1)

#filter 1: ensuring instrument status 'normal':0
raw_instrument_df = raw_instrument_df.loc[raw_instrument_df["Instrument status"]==instrument_status_normal]

#filter 2: removing negatives
instrument_positives = raw_instrument_df[
    (raw_instrument_df["Average value"]>0)
    ]

#filter 3: PM.25 only
pm25_subset = instrument_positives.dropna(subset=['Pollutant Status'])
distinct_status_count = pm25_subset['Pollutant Status'].value_counts()


#RESPUESTAS AGRUPADAS EN FORMATO JSON

Q1 = average_SO2_concentration.item()
Q2 = avg_seasonal_co_pollution.to_dict()
Q3 = highest_variability_hour.item()
Q4 = station_most_abnormal.item()
Q5 = station_most_notnormal.item()
Q6 = distinct_status_count.to_dict()

data = {
    "target": {
        "Q1": Q1,
        "Q2": Q2, 
        "Q3": Q3,
        "Q4": Q4,
        "Q5": Q5,
        "Q6": Q6
    }
}

with open('predictions/questions.json', 'w') as json_file:
    json.dump(data, json_file, indent=4) 
