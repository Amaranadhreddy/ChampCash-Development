package com.tms.govt.champcash.home.services;

import android.content.Context;

/**
 * Created by govt on 19-04-2017.
 */

public class ListOfURLs {

    private Context context;
    private String protocol;
    private String centralString;
    private String onlineServicesString;
    private String infoString;
    private String downloadString;
    private String tInfoString;

    private String cancelString;

    public ListOfURLs(Context context) {
        this.context = context;

//        centralString = "http://220.225.38.234/ds/ci";
//        onlineServicesString = "http://220.225.38.234/ds/os";
//        infoString = "http://220.225.38.234/portal/info/";
//        downloadString = "http://220.225.38.234/Payment/Getfile?";
//        cancelString = "http://220.225.38.234/User/os/";
//        tInfoString = "http://220.225.38.234/ds/ti/";


        centralString = "https://tms.ap.gov.in/ds/ci";
        onlineServicesString = "https://tms.ap.gov.in/ds/os";
        infoString = "https://tms.ap.gov.in/portal/info/";
        downloadString = "https://tms.ap.gov.in/Payment/Getfile?";
        cancelString = "https://tms.ap.gov.in/User/os/";
        tInfoString = "https://tms.ap.gov.in/ds/ti/";

//        centralString = "http://112.133.220.130:8091/ci/";
//        onlineServicesString = "http://112.133.220.130:8091/os/";
//        infoString = "http://112.133.220.130:8090/portal/info/";
//        downloadString = "http://112.133.220.130:8095/Payment/Getfile?";
//        cancelString = "http://112.133.220.130:8091/User/os/";
//        tInfoString = "http://112.133.220.130:8091/ci/";
    }

    public String getCancelString() {
        return cancelString;
    }

    public void setCancelString(String cancelString) {
        this.cancelString = cancelString;
    }

    //central url's
    public String getTemplesListURL() {
        return centralString + "/Info/GetTemples";
    }

    public String getComplaintTemplesListURL() {
        return centralString + "/Info/GetActiveTemples";
    }

    public String getPilgrimLogInURL() {
        return centralString + "/Authentication/PilgrimLogIn";
    }

    public String getPilgrimRegistrationURL() {
        return centralString + "/Authentication/PilgrimRegistration";
    }

    public String getPilgrimForgotPasswordURL() {
        return centralString + "/Authentication/PilgrimForgotPassword";
    }

    public String getPilgrimPilgrimResendOTPURL() {
        return centralString + "/Authentication/PilgrimResendOTP";
    }

    public String getPilgrimUpdateForgotPasswordURL() {
        return centralString + "/Authentication/PilgrimUpdateForgotPassword";
    }

    public String getPilgrimVerificationURL() {
        return centralString + "/Authentication/PilgrimVerification";
    }

    public String getGetTemplesFilterURL() {
        return centralString + "/Info/GetTemples";
    }

    public String getPilgrimProfileURL() {
        return centralString + "/Authentication/PilgrimProfile";
    }

    public String getPilgrimProfileUpdateURL() {
        return centralString + "/Authentication/PilgrimProfileUpdate";
    }

    public String getTemplesByServiceURL() {
        return centralString + "/Info/GetTemplesByService";
    }

    public String getPilgrimChangePasswordURL() {
        return centralString + "/Authentication/PilgrimChangePassword";
    }

    public String getCountriesURL() {
        return centralString + "/Info/GetCountries";
    }

    public String getStatesURL() {
        return centralString + "/Info/GetStates";
    }

    public String getDistrictsURL() {
        return centralString + "/Info/GetDistricts";
    }

    public String getCitiesURL() {
        return centralString + "/Info/GetCities";
    }

    public String getComplaintTypesURL() {
        return centralString + "/UserSettings/GetComplaintTypes";
    }

    public String getDeactivateAccountURL() {
        return centralString + "/Authentication/DeactivatePilgrim";
    }

    public String setRateUsURL() {
        return centralString + "/UserSettings/SetRateUs";
    }

    public String setUserSettingsURL() {
        return centralString + "/UserSettings/GetUserSettings";
    }

    public String getPilgrimSignoutURL() {
        return centralString + "/UserSettings/PilgrimSignout";
    }

    public String getProofsURL() {
        return centralString + "/Info/GetProofTypes";
    }

    public String getActiveDeityListURL() {
        return centralString + "/Info/GetActiveDeityList";
    }

    // online services url's
    public String getPDarshanCalendarURL() {
        return onlineServicesString + "/OnlineServices/GetPDarshanCalendar";
    }

    public String getPDarshanSlotsURL() {
        return onlineServicesString + "/OnlineServices/GetPDarshanSlots";
    }

    public String getActiveSevaListURL() {
        return onlineServicesString + "/OnlineServices/GetActiveSevaList";
    }

    public String getSevaBookingSlotsURL() {
        return onlineServicesString + "/OnlineServices/GetSevaBookingSlots";
    }

    public String getSevaCalendarURL() {
        return onlineServicesString + "/OnlineServices/GetSevaCalendar";
    }

    public String getTrustURL() {
        return onlineServicesString + "/OnlineServices/GetTrust";
    }

    public String getAnnadaanamTrustURL() {
        return onlineServicesString + "/AdminOnlineService/GetActiveTrustList";
    }

    public String getSevaListByDateURL() {
        return onlineServicesString + "/OnlineServices/GetSevaListByDate";
    }

    public String getPDarsanTypesByDateURL() {
        return onlineServicesString + "/OnlineServices/GetPDarsanTypesByDate";
    }

    public String getSevaSlotBookingStatsURL() {
        return onlineServicesString + "/OnlineServices/GetSevaSlotBookingStats";
    }

    public String getPDStatusBySlotIdURL() {
        return onlineServicesString + "/OnlineServices/GetPDStatusBySlotId";
    }

    public String getRoomTypesURL() {
        return onlineServicesString + "/Accommodation/GetRoomTypes";
    }

    public String getCalendarByRoomTypeURL() {
        return onlineServicesString + "/Accommodation/GetCalendarByRoomType";
    }

    public String getRoomTypesByCalDateURL() {
        return onlineServicesString + "/Accommodation/GetRoomTypesByCalDate";
    }

    public String getHundiOcassionTypesURL() {
        return onlineServicesString + "/OnlineServices/GetHundiOcassionTypes";
    }

    public String setUserComplaintsURL() {
        return onlineServicesString + "/OnlineServices/SetUserComplaints";
    }

    // cart services url's
    public String AddHundiOfferingURL() {
        return onlineServicesString + "/Cart/AddHundiOffering";
    }

    public String AddDonationURL() {
        return onlineServicesString + "/Cart/AddDonation";
    }

    public String setUserMemberURL() {
        return onlineServicesString + "/Cart/SetUserMember";
    }

    public String getSevaCartBookingURL() {
        return onlineServicesString + "/Cart/SevaCartBooking";
    }

    public String getGenerateOrderURL() {
        return onlineServicesString + "/Cart/GenerateOrder";
    }

    public String addDarshanBookingURL() {
        return onlineServicesString + "/Cart/AddDarshanBooking";
    }

    public String getRoomCartDetailsURL() {
        return onlineServicesString + "/Cart/RoomCartDetails";
    }

    public String getCartCountByUserIdURL() {
        return onlineServicesString + "/Cart/GetCartCountByUserId";
    }

    public String getUserCartByTempleURL() {
        return onlineServicesString + "/Cart/GetUserCartByTemple";
    }

    public String getRemoveCartItemsURL() {
        return onlineServicesString + "/Cart/RemoveCartItems";
    }

    public String getCartItemsURL() {
        return onlineServicesString + "/Cart/GetCartItems";
    }

    public String getCartMembersURL() {
        return onlineServicesString + "/Cart/GetCartMembers";
    }

    public String getUserMyaccountURL() {
        return onlineServicesString + "/Cart/GetUserMyaccount";
    }

    public String getUserBookingHistoryURL() {
        return onlineServicesString + "/Cart/GetUserBookingHistory";
    }

    public String getTermsAndConditionsURL() {
        return infoString+"MTermsConditions";
    }

    public String getDonationPrivilegesURL() {
        return infoString+"MPriviliges";
    }

    public String getHelpFaqURL() {
        return infoString+"MGuideLines";
    }

    public String getPaymentOrderDetailsURL() {
        return onlineServicesString + "/Cart/GetSFOrderDetails";
    }

    public String getOrderDetailsByOrderItemIDURL() {
        return onlineServicesString + "/Cart/PaidOrdDetailsByOrdItmId";
    }

    public String getAdhaarDetails() {
        return centralString + "/Authentication/GetUidDetails";
    }
    public String getSpecialdays() {
        return onlineServicesString + "/OnlineServices/GetDSpecialDays";
    }

    public String getDownloadString() {
        return downloadString;
    }

    public void setDownloadString(String downloadString) {
        this.downloadString = downloadString;
    }

    public String getNotificationURL() {
        return onlineServicesString + "/OnlineServices/GetPDBNotification";
    }

    public String getNewsFeedNotificationURL() {
        return tInfoString + "/TempleInformation/GetAppActiveTempleNotification";
    }
    public String getComplaintsTemplesList() {
        return centralString+"/Info/GetMasterTemples";
    }
}
