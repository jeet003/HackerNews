package com.example.jeet.urbanpiper.Interface;

import com.example.jeet.urbanpiper.Models.CommentsModel;

import java.util.ArrayList;

/**
 * Created by jeet on 10/30/2017.
 */

public interface FragmentCommunicator {
    interface FragmentComments{
        public ArrayList<CommentsModel> returnComments();
        public String returnUrl();
    }
}
