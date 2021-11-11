package util;

import java.util.ArrayList;

public class DataObjectLine {
    private ArrayList<Integer> data = new ArrayList<>(); // création d'une arraylist privé de type integer nommé "data".
    private int occurence;
    public DataObjectLine() {}

    public void addData(int i) {
        data.add(i);
    } //fonction qui ajoute la variable int i placé en paramètre à l'arraylist "data"

    public void setOccurence(int i){
        occurence = i;
    }

    public ArrayList<Integer> getData() { //getter, permet de retourner l'arraylist "data".
        return data;
    }

    @Override
    public String toString() {
        return "DataObjectLine{" +
                "data=" + data +
                ", occurence=" + occurence +
                '}';
    }
}
