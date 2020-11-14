package com.song.christopher.podsquad.GroupActivityScreens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.song.christopher.podsquad.Adapter.UserAdapter;
import com.song.christopher.podsquad.ViewModel.MembersListViewModel;
import com.song.christopher.podsquad.ViewModel.MembersListViewModelFactory;
import com.song.christopher.podsquad.R;

import java.util.List;

// Fragment to display all members of a given group
public class MemberListFragment extends Fragment {
    // Fields to store current group and user IDs
    private int groupID;
    private int userID;

    // RecyclerView (& its related components) to display all users in the current group
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // 'ViewModel' object allows us to track live data (i.e. the members in the current group)
    // 'Factory' object allows us to pass the current groupID to filter the live data appropriately
    private MembersListViewModel membersListViewModel;
    private MembersListViewModelFactory factory;

    // Constructor: initializes group and user ID number fields
    public MemberListFragment(int groupID, int userID) {
        this.groupID = groupID;
        this.userID = userID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up 'RecyclerView' object displaying all users of the current group
        recyclerView = view.findViewById(R.id.group_members_list);
        adapter = new UserAdapter(groupID);
        layoutManager = new LinearLayoutManager(getContext());

        /* Initialize the 'Factory' and 'ViewModel' objects to monitor the live data
         * regarding the members in the current group */
        factory = new MembersListViewModelFactory(getActivity().getApplication(), groupID);
        membersListViewModel = ViewModelProviders.of(this, factory).get(MembersListViewModel.class);
        membersListViewModel.getAllGroupMemberIDs().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                adapter.reloadMembersList(integers);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}