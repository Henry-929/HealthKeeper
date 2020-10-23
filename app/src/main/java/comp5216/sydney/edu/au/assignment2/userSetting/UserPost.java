package comp5216.sydney.edu.au.assignment2.userSetting;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserPost {
    public String username;

    public UserPost() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public UserPost(String username){
        this.username = username;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
