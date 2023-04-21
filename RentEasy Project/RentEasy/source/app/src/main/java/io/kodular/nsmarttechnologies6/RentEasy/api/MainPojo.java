package io.kodular.nsmarttechnologies6.RentEasy.api;

import java.util.ArrayList;

public class MainPojo {
    String status;
    ArrayList<Listclass> predictions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Listclass> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<Listclass> predictions) {
        this.predictions = predictions;
    }
}
