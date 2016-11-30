package ru.lightg.listtest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


class Line {
    int id;
    String firstName;
    String lastName;
    Object[] data;

    Line(JSONObject jsonObject) {
        id = Integer.valueOf(jsonObject.get("id").toString());
        firstName = jsonObject.get("first_name").toString();
        lastName = jsonObject.get("last_name").toString();
        JSONArray array = (JSONArray) jsonObject.get("data");
        data = new Object[array.size()];
    }

    @Override
    public String toString() {
        return String.format("%d %s %s Data size:%d", id, firstName, lastName, data.length);
    }
}