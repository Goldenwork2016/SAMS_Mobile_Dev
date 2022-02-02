package com.sherdle.universal;

import com.sherdle.universal.model.LocalUserVariable;

public class ContractUrl {
    // for awards section . . . ;
    public static final String AWARDS_URL="https://sams.emsystemsolutions.com/api/awards";
    public static final String UPDATES_URL="https://sams.emsystemsolutions.com/api/update";
    public static final String ADD_URL="https://sams.emsystemsolutions.com/api/add_award";

    // for support section . . . ;
    public static final String SUPPORT_URL ="https://sams.emsystemsolutions.com/api/support?user_id="+ LocalUserVariable.userid +"&user_type="+LocalUserVariable.usertype;

    public static final String ADD_SUPPORT ="https://sams.emsystemsolutions.com/api/add_support" ;
    public static final String CHAT_DATA = "https://sams.emsystemsolutions.com/api/chat_data";
    public static final String ADD_CHAT="https://sams.emsystemsolutions.com/api/add_data";
    public static final String REMOVE_CHAT="https://sams.emsystemsolutions.com/api/remove_chat?chat_id=";
    public static final String EDIT_CHAT="https://sams.emsystemsolutions.com/api/edit_chat";

    public static final String DASHBOARD_URL = "https://sams.emsystemsolutions.com/api/dashboard_info";
    public static final String ADD_YEAR ="https://sams.emsystemsolutions.com/api/year_selection" ;
    public static final String ADD_SPORT ="https://sams.emsystemsolutions.com/api/sport_selection" ;
    public static final String UPDATE_AWARD_ACTIVE_STATUS ="https://sams.emsystemsolutions.com/api/award_active_status";

}
