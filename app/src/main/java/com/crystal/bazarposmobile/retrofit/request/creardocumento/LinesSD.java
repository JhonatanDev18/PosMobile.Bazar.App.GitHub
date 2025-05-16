
package com.crystal.bazarposmobile.retrofit.request.creardocumento;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinesSD {

    @SerializedName("LineSD")
    @Expose
    private List<LineSD> lineSD;

    public LinesSD(List<LineSD> lineSD) {
        this.lineSD = lineSD;
    }

    public List<LineSD> getLineSD() {
        return lineSD;
    }

    public void setLineSD(List<LineSD> lineSD) {
        this.lineSD = lineSD;
    }

}
