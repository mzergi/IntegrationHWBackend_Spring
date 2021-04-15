package com.integrationhw.backend.Models;

public class addDataDemoResponseModel {
    private String artistname;
    private String birthplace;
    private String buried;

    public addDataDemoResponseModel(String artistname, String birthplace, String buried) {
        this.artistname = artistname;
        this.birthplace = birthplace;
        this.buried = buried;
    }

    public String getArtistname() {
        return artistname;
    }
    public String getBirthplace(){
        return birthplace;
    }
    public String getBuried(){
        return buried;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("artistname='").append(artistname).append('\'');
        sb.append(", birthplace='").append(birthplace).append('\'');
        sb.append(", buried='").append(buried).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
