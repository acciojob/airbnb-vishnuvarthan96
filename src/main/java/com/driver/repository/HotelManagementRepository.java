package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {


    HashMap<String,Hotel> hotelDb=new HashMap<>();
    HashMap<Integer,User> userDb=new HashMap<>();

    HashMap<String,Booking> bookingDb=new HashMap<>();
    public String addHotel(Hotel hotel){
        if(hotel.getHotelName() == null || hotelDb.containsKey(hotel.getHotelName()) ){
            return "FAILURE";
        }
        else{
            hotelDb.put(hotel.getHotelName(),hotel);
            return "SUCCESS";
        }
    }

    public Integer addUser(User user){
        if(!userDb.containsKey(user.getaadharCardNo())){
            userDb.put(user.getaadharCardNo(),user);
            return user.getaadharCardNo();
        }
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {

        try {
            SortedSet<String> ts = new TreeSet<String>();
            int tempFacilitiesCount = 0;
            Hotel tempHotel = null;
            for (Hotel hotel : hotelDb.values()) {
                if (tempFacilitiesCount <= hotel.getFacilities().size()) {
                    tempFacilitiesCount = hotel.getFacilities().size();
                    tempHotel = hotel;
                }
            }

            assert tempHotel != null;
            if(!(tempHotel.getFacilities().isEmpty())){
            ts.add(tempHotel.getHotelName());
                for (Hotel hotel : hotelDb.values()) {
                    if (hotel.getFacilities().size() == tempHotel.getFacilities().size()) {
                        ts.add(hotel.getHotelName());
                    }
                }
            }
            if (!ts.isEmpty()) {
                return ts.first();
            }
            return "";
        }
        catch (Exception e){
            System.out.println("exception found"+e);
        }
        return "";
    }

    public int bookARoom(Booking booking){
        String uuid= UUID.randomUUID().toString();
        booking.setBookingId(uuid);
        bookingDb.put(uuid,booking);
        Hotel getHotel=hotelDb.get(booking.getHotelName());

        if(booking.getNoOfRooms() > getHotel.getAvailableRooms()){
            return -1;
        }
        return booking.getNoOfRooms()*getHotel.getPricePerNight();
    }

    public int getBookings(Integer aadharCard) {
        int ans=0;
        for(Booking booking:bookingDb.values()){
            if(booking.getBookingAadharCard()==aadharCard)
                ans++;
    }
        return ans;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){
        Hotel hotelToUpdate=hotelDb.get(hotelName);
        if(hotelToUpdate.getFacilities().equals(newFacilities))
            return hotelToUpdate;
        List<Facility> updated=new ArrayList<>();
        List<Facility> oldFacilities=hotelToUpdate.getFacilities();
        for(int i=0;i<newFacilities.size();i++){
            for (int j=0;j<oldFacilities.size();j++) {
                if (newFacilities.get(i) == oldFacilities.get(j)) {
                    updated.add(newFacilities.get(i));
                    break;
                } else if (j == oldFacilities.size() - 1) {
                    updated.add(newFacilities.get(i));
                }

            }
        }
        for(int i=0;i<oldFacilities.size();i++){
            for(int j=0;j<newFacilities.size();j++){
                if (newFacilities.get(i) == oldFacilities.get(j)) {
//                    updated.add(newFacilities.get(i));
                    break;
                } else if (j == newFacilities.size() - 1) {
                    updated.add(oldFacilities.get(i));
                }
            }
        }
        hotelToUpdate.setFacilities(updated);
        hotelDb.put(hotelToUpdate.getHotelName(),hotelToUpdate);
         return hotelToUpdate;
    }

}
