package com.foursquare.takehome.model;

import java.util.List;

/**
 * Interface for PeopleHere Model. Encapsulates the json parsing and List creation Logic
 */

public interface PeopleHereModel {

    /**
     * get the visitorList
     */
    void getVisitorList();

    void setVisitorFetchListener(OnVisitorListFetchListener listener);


    /**
     * Callback interface for visitor list fetching action
     */
    public interface OnVisitorListFetchListener {

        /**
         * Callback which called from model when visitor list is formed
         * @param list
         */
        void onVisitorListSuccess(List<Person> list);

        /**
         * Callback when visitor list creation fails for some reason
         * @param exception
         */
        void onVisitorListFailure(Exception exception);
    }
}
