package isep.lapr3.g094.domain.type;

import java.time.Duration;
import java.time.LocalTime;

public class InfoLocation {
    
    private String name;
    private boolean isHub;
    private LocalTime arriveTime;
    private LocalTime departTime;
    private boolean isCharged;
    private Duration timeToCharge;


    //Caso tenha carregado
    public InfoLocation(String name, boolean isHub, LocalTime arriveTime, LocalTime departTime, Duration timeToCharge) {
        this.name = name;
        this.isHub = isHub;
        this.arriveTime = arriveTime;
        this.departTime = departTime;
        this.isCharged = true;
        this.timeToCharge = timeToCharge;
    }

    //Caso não tenha carregado
    public InfoLocation(String name, boolean isHub, LocalTime arriveTime, LocalTime departTime) {
        this.name = name;
        this.isHub = isHub;
        this.arriveTime = arriveTime;
        this.departTime = departTime;
        this.isCharged = false;
        this.timeToCharge = Duration.ZERO;
    }


    public String getName() {
        return name;
    }

    public boolean isHub() {
        return isHub;
    }

    public LocalTime getArriveTime() {
        return arriveTime;
    }

    public LocalTime getDepartTime() {
        return departTime;
    }

    public boolean isCharged() {
        return isCharged;
    }

    public Duration getTimeToCharge() {
        return timeToCharge;
    }

    public void setCharged(boolean charged) {
        isCharged = charged;
    }

    public void setTimeToCharge(Duration timeToCharge) {
        this.timeToCharge = timeToCharge;
    }

    @Override
    public String toString() {
        return "InfoLocation{" +
                "name='" + name + '\'' +
                ", isHub=" + isHub +
                ", arriveTime=" + arriveTime +
                ", departTime=" + departTime +
                ", isCharged=" + isCharged +
                ", timeToCharge=" + timeToCharge +
                '}';
    }




    
}
