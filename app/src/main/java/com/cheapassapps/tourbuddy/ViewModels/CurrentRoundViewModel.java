package com.cheapassapps.tourbuddy.ViewModels;

import android.app.Application;
import android.os.AsyncTask;
import com.cheapassapps.tourbuddy.Models.Course;
import com.cheapassapps.tourbuddy.Models.Hole;
import com.cheapassapps.tourbuddy.Models.Round;
import com.cheapassapps.tourbuddy.Models.Tee;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CurrentRoundViewModel extends AndroidViewModel {
    private MutableLiveData<Round> currentRound = new MutableLiveData<>();

    public CurrentRoundViewModel(@NonNull Application application){ super(application); Round round = new Round(); currentRound.setValue(round); }

    public MutableLiveData<Round> getCurrentRound(){
        getCourseInfo("http://nelsonbernard.com/tourbuddy/courseinfo.php");
        return currentRound;
    }

    public void setCurrentRound(Round round) {
        currentRound.setValue(round);
    }

    public void setCurrentCourse(Course course){
        Round round = currentRound.getValue();
        round.setCourse(course);
        currentRound.setValue(round);
    }

    public void setCurrentTee(Tee tee){
        Round round = currentRound.getValue();
        round.setTee(tee);
        currentRound.setValue(round);
    }

    public void setHoleScore(int holeNum, int score){
        Round round = currentRound.getValue();
        round.setScore(holeNum, score);
        currentRound.setValue(round);
    }

    private void getCourseInfo(final String urlWebService){
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("result");

                    Round round;
                    round = currentRound.getValue();
                    Course course = new Course();
                    course.setName("River Oaks");
                    course.setCourseId(4);
                    round.setCourse(course);

                    for(int i = 0; i < jsonArray.length(); i++){
                        jsonObject = jsonArray.getJSONObject(i);

                        Hole hole = new Hole();
                        hole.setHole_number(Integer.parseInt(jsonObject.getString("holenum")));
                        hole.setPar(Integer.parseInt(jsonObject.getString("par")));
                        hole.setYards(Integer.parseInt(jsonObject.getString("yards")));
                        hole.setHdcpMen(Integer.parseInt(jsonObject.getString("hdcp_men")));
                        hole.setHdcpWomen(Integer.parseInt(jsonObject.getString("hdcp_women")));
                        hole.setFrontLat(Double.parseDouble(jsonObject.getString("frontlat")));
                        hole.setFrontLong(Double.parseDouble(jsonObject.getString("frontlong")));
                        hole.setMiddleLat(Double.parseDouble(jsonObject.getString("middlelat")));
                        hole.setMiddleLong(Double.parseDouble(jsonObject.getString("middlelong")));
                        hole.setBackLat(Double.parseDouble(jsonObject.getString("backlat")));
                        hole.setBackLong(Double.parseDouble(jsonObject.getString("backlong")));

                        round.setHole(i, hole);

                    }

                    currentRound.setValue(round);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;

                    while((json = bufferedReader.readLine()) != null){
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e){
                    return null;
                }
            }
        }

        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
}
