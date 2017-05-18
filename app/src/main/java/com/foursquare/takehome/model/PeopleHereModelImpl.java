package com.foursquare.takehome.model;

import android.content.Context;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 */

public class PeopleHereModelImpl implements PeopleHereModel {
    private PeopleHereModel.OnVisitorListFetchListener visitorListFetchListener;
    private Context context;
    private Executor executor;
    private List<Person> visitorList;

    private Comparator<Person> comparator = new Comparator<Person>() {
        @Override
        public int compare(Person o1, Person o2) {

            if (o1.getArriveTime() - o2.getArriveTime() > 0) {
                return 1;
            } else if (o1.getArriveTime() - o2.getArriveTime() < 0) {
                return -1;
            } else if (o1.getArriveTime() == o2.getArriveTime()) {
                if ((o1.getLeaveTime() - o1.getArriveTime()) > (o2.getLeaveTime() - o2.getArriveTime())) {
                    return 1;
                }
                return -1;

            }else{
                return 0;
            }
        }
    };

    public PeopleHereModelImpl(Context applicationContext) {
        context = applicationContext;
        executor = Executors.newSingleThreadExecutor();
    }


    @Override
    public void getVisitorList() {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Fetch the visitor List if its null, if its already fetched no need to fetch it again
                    // re use the fetched value
                    if( visitorList == null ) {
                        InputStream is = context.getAssets().open("people.json");
                        InputStreamReader inputStreamReader = new InputStreamReader(is);

                        PeopleHereJsonResponse response = new Gson().fromJson(inputStreamReader, PeopleHereJsonResponse.class);

                        visitorList = createVisitorFrequencyList(response.getVenue(), comparator);
                    }

                    if(visitorListFetchListener != null) {
                        visitorListFetchListener.onVisitorListSuccess(visitorList);
                    } else {
                        throw new Exception("visitorListFetchListener not set, call setVisitorFetchListener on model");
                    }
                } catch (Exception e) {
                    if(visitorListFetchListener != null) {
                        visitorListFetchListener.onVisitorListFailure(e);
                    }
                }
            }
        });


    }

    @Override
    public void setVisitorFetchListener(OnVisitorListFetchListener listener) {
        visitorListFetchListener = listener;
    }


    private static List<Person> createVisitorFrequencyList(Venue venue, Comparator<Person> comparator) {
        List<Person> daysVisitorList = new ArrayList<>();
        List<Person> visitorList = venue.getVisitors();

        //Sort the visitorList by their ArrivalTime
        Collections.sort(visitorList, comparator);

        long arrivalTime = venue.getOpenTime();
        long leaveTime = venue.getOpenTime();

        for(Person visitor : visitorList) {

            //Initial arrivalTime lessthan visitors arrival time && initial exit time is lessthan visitors arrival time
            //means no one is present at that time. Create a empty person for that range
            if(arrivalTime < visitor.getArriveTime() && leaveTime < visitor.getArriveTime()) {
                daysVisitorList.add(createVisitor(leaveTime, visitor.getArriveTime(), null));
            }
            arrivalTime = visitor.getArriveTime();

            //leaveTime is maximum of older leavetime and visitors leavetime (for ex: leaveTime = 8:00AM and
            //visitors leave time is 10:00 AM then update leaveTime as 10:00AM
            leaveTime = Math.max(visitor.getLeaveTime(), leaveTime);
            daysVisitorList.add(visitor);
        }

        if(leaveTime < venue.getCloseTime()) {
            daysVisitorList.add(createVisitor(leaveTime, venue.getCloseTime(), null));
        }

        return daysVisitorList;
    }

    private static Person createVisitor(long start, long end, String personName) {
        final Person emptyPerson = new Person();
        emptyPerson.setArriveTime(start);
        emptyPerson.setLeaveTime(end);
        emptyPerson.setName(personName);
        return emptyPerson;
    }
}
