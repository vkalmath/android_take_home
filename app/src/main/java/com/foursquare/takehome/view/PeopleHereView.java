package com.foursquare.takehome.view;

import com.foursquare.takehome.model.Person;

import java.util.List;

/**
 * View interface for PeopleHere Screen
 */

public interface PeopleHereView {

    /**
     * Show progress indicator
     */
    void showProgress();

    /**
     * Stop progress indicator
     */
    void stopProgress();

    /**
     * present the visitor list on UI
     * @param list
     */
    void showVisitors(List<Person> list);

    /**
     * present the error on UI
     * @param e
     */
    void showError(Exception e);
}
