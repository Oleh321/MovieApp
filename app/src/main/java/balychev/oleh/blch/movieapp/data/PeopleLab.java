package balychev.oleh.blch.movieapp.data;

import java.util.ArrayList;

import balychev.oleh.blch.movieapp.model.Person;

public class PeopleLab {
        private static PeopleLab sPeopleLab;

        private ArrayList<Person> mPeople;

        public static PeopleLab get(){
            if (sPeopleLab == null)
                sPeopleLab = new PeopleLab();
            return sPeopleLab;
        }

    public ArrayList<Person> getPeople() {
        return mPeople;
    }

    public void setPeople(ArrayList<Person> people) {
        mPeople = people;
    }
}
