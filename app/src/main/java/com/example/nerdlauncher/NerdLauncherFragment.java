package com.example.nerdlauncher;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ActivityAdapter mActivityAdapter;
    List<ResolveInfo> mActivities;

    public static NerdLauncherFragment newInstance() {
        
        Bundle args = new Bundle();
        
        NerdLauncherFragment fragment = new NerdLauncherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NerdLauncherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        mRecyclerView = view.findViewById(R.id.nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        final PackageManager packageManager = getActivity().getPackageManager();

        Intent queryIntent = new Intent(Intent.ACTION_MAIN);
        queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = packageManager.queryIntentActivities(queryIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                String appName1 = o1.loadLabel(packageManager).toString();
                String appName2 = o2.loadLabel(packageManager).toString();

                return String.CASE_INSENSITIVE_ORDER.compare(appName1, appName2);
            }
        });

        mActivities = activities;

        if (mActivityAdapter == null) {
            mActivityAdapter = new ActivityAdapter(mActivities);
            mRecyclerView.setAdapter(mActivityAdapter);
        } else {
            mActivityAdapter.setActivities(mActivities);
            mActivityAdapter.notifyDataSetChanged();
        }
    }

    private class ActivityHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private ResolveInfo mActivity;

        public ActivityHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView;
            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String packageName = mActivity.activityInfo.packageName;
                    String className = mActivity.activityInfo.name;

                    Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
                    launcherIntent.setClassName(packageName, className);
                    launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(launcherIntent);
                }
            });
        }

        public void bindActivity(ResolveInfo activity) {
            mActivity = activity;

            String appName = activity.loadLabel(getActivity().getPackageManager()).toString();
            mTitleTextView.setText(appName);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {

        List<ResolveInfo> mActivities;

        public void setActivities(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ActivityHolder(new TextView(getActivity()));
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            ResolveInfo activity = mActivities.get(position);
            holder.bindActivity(activity);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}
