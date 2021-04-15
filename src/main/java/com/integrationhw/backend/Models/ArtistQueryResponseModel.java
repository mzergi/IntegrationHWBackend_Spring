package com.integrationhw.backend.Models;

public class ArtistQueryResponseModel {
    private String artistname;
    private String arttitle;

    public ArtistQueryResponseModel(String artistname, String arttitle){
        this.artistname = artistname;
        this.arttitle = arttitle;
    }

    public String getArtistName(){
        return artistname;
    }

    public String getArtTitle(){
        return arttitle;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("artistname='").append(artistname).append('\'');
        sb.append(", arttitle='").append(arttitle).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
