package com.cheapassapps.tourbuddy.ui.courses;

import android.app.Application;
import android.os.AsyncTask;
import com.cheapassapps.tourbuddy.Models.Course;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CoursesViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Course>> courses = new MutableLiveData<>();
    private Course currentCourse;

    public CoursesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Course>> getCourses(){

        getAllCourses("http://nelsonbernard.com/tourbuddy/allcourses.php");
        return courses;
    }

    public Course getCourse(int index){
        getCourseById("http://nelsonbernard.com/tourbuddy/coursebyid.php");
        return currentCourse;
    }

    private void getCourseById(final String urlWebService){
        class GetCourseById extends AsyncTask<Void, Void, String>{
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
                    Course course = new Course();

                    for(int i = 0; i < jsonArray.length(); i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        course.setCourseId(Integer.parseInt(jsonObject.getString("id")));
                        course.setName(jsonObject.getString("name"));
                        course.setAddress1(jsonObject.getString("address1"));
                        course.setAddress2(jsonObject.getString("address2"));
                        course.setCity(jsonObject.getString("city"));
                        course.setState(jsonObject.getString("state"));
                        course.setZip(jsonObject.getString("zip"));
                        course.setPhone(jsonObject.getString("phone"));
                    }

                    currentCourse = course;

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
    }

    private void getAllCourses(final String urlWebService){
        class GetJSON extends AsyncTask<Void, Void, String>{

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
                    ArrayList<Course> c = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        Course course = new Course();
                        course.setCourseId(Integer.parseInt(jsonObject.getString("id")));
                        course.setName(jsonObject.getString("name"));
                        course.setAddress1(jsonObject.getString("address1"));
                        course.setAddress2(jsonObject.getString("address2"));
                        course.setCity(jsonObject.getString("city"));
                        course.setState(jsonObject.getString("state"));
                        course.setZip(jsonObject.getString("zip"));
                        course.setPhone(jsonObject.getString("phone"));

                        c.add(course);
                    }

                    courses.setValue(c);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
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
