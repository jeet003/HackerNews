package com.example.jeet.urbanpiper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.jeet.urbanpiper.interfaces.FragmentCommunicator;
import com.example.jeet.urbanpiper.R;

/**
 * Created by jeet on 10/29/2017.
 *
 * Second View Pager fragment for displaying the web view
 */

public class NewsDetailWebView extends Fragment{

    private FragmentCommunicator.FragmentComments fragmentComments;

    public NewsDetailWebView(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentComments=(FragmentCommunicator.FragmentComments) getActivity();
        WebView wv1 = (WebView) getActivity().findViewById(R.id.webview);
        TextView empty=(TextView) getActivity().findViewById(R.id.empty);

        String url=fragmentComments.returnUrl();

        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if(!url.equals("")){
            wv1.setWebViewClient(new MyBrowser());
            wv1.loadUrl(url);
        }
        else{
            wv1.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
