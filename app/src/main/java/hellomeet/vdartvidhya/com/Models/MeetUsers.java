package hellomeet.vdartvidhya.com.Models;

/**
 * Created by Vidhya on 2/10/2019.
 * HelloMeet
 */
public class MeetUsers {

    private String userid;
    private String username;
    private String useremail;

    public MeetUsers(String userid, String username, String useremail) {
        this.userid = userid;
        this.username = username;
        this.useremail = useremail;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }


}
