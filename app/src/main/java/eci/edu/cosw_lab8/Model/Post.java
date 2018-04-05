package eci.edu.cosw_lab8.Model;

import java.io.Serializable;

public class Post implements Serializable{
    public String message;
    public String imageUri;

    public Post(String message, String imageUri) {
        this.message = message;
        this.imageUri = imageUri;
    }

    @Override
    public String toString(){
        return "This is de message: "+message+", and this is the uir: "+imageUri;
    }
}
