package com.duogesi.Utils;

import com.duogesi.beans.items;
import org.springframework.stereotype.Component;

@Component
public class Swtich {
    public String switch_mudigang_fan(String origin) {
        switch (origin) {
            case "美西":
                return "west";
            case "美东":
                return "east";
            case "美中":
                return "middle";

        }
        return null;
    }

    public String switch_mudigang_zhong_ouzhou(String origin) {
        switch (origin) {
            case ("英国"):
                return "zone1";
            case ("德国"):
                return "zone2";
            case ("法国"):
                return "zone2";
            case ("卢森堡"):
                return "zone2";
            case ("荷兰"):
                return "zone2";
            case ("比利时"):
                return "zone2";
            case ("爱尔兰"):
                return "zone2";
            case ("西班牙"):
                return "zone3";
            case ("意大利"):
                return "zone3";
            case ("奥地利"):
                return "zone3";
            case ("丹麦"):
                return "zone3";
            case ("捷克"):
                return "zone3";
            case ("其他"):
                return "zone3";
        }
        return null;
    }


    public String switch_mudigang_zhong(String origin) {
        switch (origin) {
            case "west":
                return "west";
            case "east":
                return "east";
            case "middle":
                return "middle";
            case "美西":
                return "west";
            case "美东":
                return "east";
            case "美中":
                return "middle";
            case "温哥华":
                return "YVR";
            case "卡尔加多":
                return "YYC";
            case "多伦多":
                return "YYZ";
            case "渥太华":
                return "YOW";
            case ("英国"):
                return "英国";
            case ("德国"):
                return "德国";
            case ("法国"):
                return "法国";
            case ("卢森堡"):
                return "卢森堡";
            case ("荷兰"):
                return "荷兰";
            case ("比利时"):
                return "比利时";
            case ("爱尔兰"):
                return "爱尔兰";
            case ("西班牙"):
                return "西班牙";
            case ("意大利"):
                return "意大利";
            case ("奥地利"):
                return "奥地利";
            case ("丹麦"):
                return "丹麦";
            case ("捷克"):
                return "捷克";
            case ("其他"):
                return "其他";
            case ("日本"):
                return "Japan";

        }
        return null;
    }

    public String switch_mudigang(String origin) {
        switch (origin) {
            case "west":
                return "美西";
            case "east":
                return "美东";
            case "middle":
                return "美中";

        }
        return null;
    }

    public items swtich_schdule(items items) {
        switch (items.getStatus()) {
            case 0:
                items.setStatu("未拼柜");
                return items;
            case 1:
                items.setStatu("已装柜");
                return items;
            case 2:
                items.setStatu("报关中");
                return items;
            case 3:
                items.setStatu("开船");
                return items;
            case 4:
                items.setStatu("货到港");
                return items;
            case 5:
                items.setStatu("查验中");
                return items;
            case 6:
                items.setStatu("已完成");
                return items;
        }
        return null;
    }

}
