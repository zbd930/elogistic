package com.duogesi.Utils;

public class details {

    public String swith(int status) {
        String result = "";
        switch (status) {
            case 0:
                result = "Shipment info received";
                break;
            case 1:
                result = "Container loaded";
                break;
            case 2:
                result = "Custom performing";
                break;
            case 3:
                result = "Vessel departed";
                break;
            case 4:
                result = "Vessel arrived in LA";
                break;
            case 5:
                result = "Custom processing";
                break;
            case 6:
                result = "Custom processing complete";
                break;
            case 7:
                result = "Picked up carrier";
                break;
        }
        return result;
    }
}
