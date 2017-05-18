package com.foursquare.takehome.presenter;

import com.foursquare.takehome.model.Person;
import com.foursquare.takehome.model.PeopleHereModel;
import com.foursquare.takehome.view.PeopleHereView;

import java.util.List;

/**
 *
 */

public class PeopleHerePresenterImpl implements PeopleHerePresenter, PeopleHereModel.OnVisitorListFetchListener {

    private PeopleHereView view;
    private PeopleHereModel model;

    public PeopleHerePresenterImpl(PeopleHereView view, PeopleHereModel model) {
        this.view = view;
        this.model = model;
    }


    @Override
    public void fetchVisitors() {
        if(view != null){
            view.showProgress();
        }
        if(model != null){
            model.getVisitorList();
        }
    }

    @Override
    public void onVisitorListSuccess(List<Person> list) {
        if(view != null){
            view.stopProgress();
            view.showVisitors(list);
        }
    }

    @Override
    public void onVisitorListFailure(Exception exception) {
        if(view != null){
            view.stopProgress();

            view.showError(exception);
        }
    }
}
