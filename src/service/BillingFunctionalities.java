package service;

import model.Billing;

public class BillingFunctionalities {

    public static int validateChoice(String choice) {
        if(choice.equalsIgnoreCase("yes")) return 1;
        else if(choice.equalsIgnoreCase("no")) return 0;
        else if(choice.equals("")) return -1;
        else return -2;
    }

}
