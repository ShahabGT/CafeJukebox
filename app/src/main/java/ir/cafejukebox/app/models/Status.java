package ir.cafejukebox.app.models;

public class Status {
//    Status = 0 -> There is no 'number' in POST
//    Status = 1 -> Insert Failed
//    Status = 2 -> Update Failed
//    Status = 200 -> Insert Successful / SMS Successful
//    Status = 201 -> Insert Successful / SMS Failed
//    Status = 202 -> Update Successful / SMS Successful
//& =>[user_id,active,first_time]
//
//    Status = 203 -> Update Successful / SMS Failed
//    Status = 204 -> User is Deactive

    public static int NO_NUMBER = 0;
    public static int INSERT_FAILED = 1;
    public static int UPDATE_FAILED = 2;
    public static int INSERT_SUCCESSFUL = 200;
    public static int INSERT_SUCCESSFUL_SMS_FAILED = 201;
    public static int UPDATE_SUCCESSFUL = 202;
    public static int UPDATE_SUCCESSFUL_SMS_FAILED = 203;
    public static int USER_BLOCKED = 204;

}
