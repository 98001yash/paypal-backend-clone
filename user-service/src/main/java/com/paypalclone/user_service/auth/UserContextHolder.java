package com.paypalclone.user_service.auth;

import java.util.List;

public class UserContextHolder {

    private static  final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<List<String>> currentUserRoles = new ThreadLocal<>();

    public static Long getCurrentUserId(){
        return currentUserId.get();
    }
    public static void setCurrentUserId(Long userId){
        currentUserId.set(userId);
    }


    public static List<String> getCurrentUserRoles() {
        return currentUserRoles.get();
    }

   public  static void setCurrentUserRoles(List<String> roles) {


        currentUserRoles.set(roles);
    }

    static void clear() {
        currentUserId.remove();
        currentUserRoles.remove(); // <-- Add this line
    }

}
