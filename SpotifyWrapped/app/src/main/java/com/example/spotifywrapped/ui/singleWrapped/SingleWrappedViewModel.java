package com.example.spotifywrapped.ui.singleWrapped;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SingleWrappedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SingleWrappedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is single wrapped fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}