package com.cheapassapps.tourbuddy.ui.courses;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cheapassapps.tourbuddy.MainActivity;
import com.cheapassapps.tourbuddy.Models.Course;
import com.cheapassapps.tourbuddy.R;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    private CoursesViewModel mViewModel;
    private ListView listView;
    private CourseAdapter adapter;

    public static CoursesFragment newInstance() {
        return new CoursesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.courses_fragment, container, false);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CoursesViewModel.class);
        mViewModel.getCourses().observe(this, new Observer<ArrayList<Course>>(){
                @Override
                public void onChanged(ArrayList<Course> courses) {
                    adapter = new CourseAdapter(getContext(), courses);
                    listView.setAdapter(adapter);
                }
        });

    }

    public class CourseAdapter extends ArrayAdapter<Course>{
        public CourseAdapter(Context context, ArrayList<Course> courses){
            super(context, 0, courses);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Course course = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.simplelistitem, parent, false);
            }

            TextView txtName = convertView.findViewById(R.id.txtName);
            TextView txtAddress = convertView.findViewById(R.id.txtAddress);

            txtName.setText(course.getName());
            txtAddress.setText(course.getAddress1());

            return convertView;
        }
    }
}
