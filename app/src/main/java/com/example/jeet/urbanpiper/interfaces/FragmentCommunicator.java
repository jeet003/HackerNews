package com.example.jeet.urbanpiper.interfaces;

import com.example.jeet.urbanpiper.models.CommentsModel;

import java.util.ArrayList;

/**
 * Created by jeet on 10/30/2017.
 *
 * Interface to communicate
 */

public interface FragmentCommunicator {
    interface FragmentComments{
        ArrayList<CommentsModel> returnComments();
        String returnUrl();
    }
}
