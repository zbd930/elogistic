package com.duogesi.Utils;

import org.springframework.stereotype.Component;

@Component
public class Swtich {
    public String switch_mudigang_fan(String origin){
        switch (origin){
            case "美西":
                return "west";
            case "美东":
                return "east";
            case "美中":
                return "middle";
        }
        return null;
    }

    public String switch_mudigang(String origin){
        switch (origin){
            case "west":
                return "美西";
            case "east":
                return "美东";
            case "middle":
                return "美中";
        }
        return null;
    }

}
