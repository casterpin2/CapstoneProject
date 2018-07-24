package project.view.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultRegister {
    @SerializedName("result")
    @Expose
    private String result;

    public ResultRegister() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
