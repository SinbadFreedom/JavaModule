package redis.clients.jedis;

import redis.clients.util.SafeEncoder;

public class GeoRadiusResponse {
    private byte[] member;
    private double distance;
    private GeoCoordinate coordinate;

    public GeoRadiusResponse(byte[] member) {
        this.member = member;
    }

    public byte[] getMember() {
        return member;
    }

    public String getMemberByString() {
        return SafeEncoder.encode(member);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
    }
}
