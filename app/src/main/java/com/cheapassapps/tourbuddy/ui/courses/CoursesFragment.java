package com.cheapassapps.tourbuddy.ui.courses;

import androidx.fragment.app.FragmentTransaction;
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
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheapassapps.tourbuddy.MainActivity;
import com.cheapassapps.tourbuddy.Models.Course;
import com.cheapassapps.tourbuddy.R;
import com.cheapassapps.tourbuddy.ViewModels.CurrentRoundViewModel;
import com.cheapassapps.tourbuddy.ui.NewRoundFragment;
import com.cheapassapps.tourbuddy.ui.RoundGUI;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    private CurrentRoundViewModel mRoundViewModel;
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

                mRoundViewModel.setCurrentCourse(mViewModel.getCourse(i));

//                NewRoundFragment fragment = new NewRoundFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//
//                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                RoundGUI fragment = new RoundGUI();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

        mRoundViewModel = ViewModelProviders.of(this).get(CurrentRoundViewModel.class);
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
