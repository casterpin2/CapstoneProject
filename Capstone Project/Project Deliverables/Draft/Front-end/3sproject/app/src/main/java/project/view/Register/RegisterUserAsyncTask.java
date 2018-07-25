package project.view.Register;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterUserAsyncTask extends AsyncTask<Call, Void, String> {
    private String result = "";
    @Override
    protected String doInBackground(Call... calls) {
        try {
            Call<String> call = calls[0];
            Response<String> re = call.execute();
//            if (re.body() != null) {
                return re.body();
//            } else {
//                return null;
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        if (aVoid == null) return;
        result = aVoid;
    }

    public String getResult() {
        return result;
    }
}
