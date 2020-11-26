package com.example.uplift;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Content {

public static final ArrayList<Object> wellness = new ArrayList<Object>(
        Arrays.asList(
                "Have you gone outside today? Don't forget to go get some fresh air and your daily dose of Vitamin D!",
                "Now is not the time to be hard on yourself. You are more than capable, even if you can't always see it.",
                "Don't forget to check in with your friends and family - it's important to stick together while we have to be apart!"
        )
);

public static final ArrayList<Object> goodNews = new ArrayList<Object>(
        Arrays.asList(
                "Zoom is lifting its 40-minute time limit for Thanksgiving Day so families can hang out together.",
                "After 45 years, the Gray Wolf has successfully been lifted off the US endangered species list.",
                "Giuseppe Paternò has become Italy’s oldest graduate at the age of 96 after being awarded first-class honours in philosophy from the University of Palermo in Sicily."
        )
);


public static final ArrayList<Object> exercise = new ArrayList<Object>(
        Arrays.asList(
                "Pace yourself. Have at least one recovery day each week to rest. If you are experiencing pain, rest until the pain has gone.",
                "Stay hydrated. You can lose around one and a half litres of fluid for every hour of exercise; so drink water before, during and after a session.",
                "Warm up and cool down. Try slow stretches and go through the motions of your sport or activity before starting. Cool down with slow stretching."
        )
);

public static final ArrayList<Object> animals = new ArrayList<Object>(
        Arrays.asList(
                R.drawable.lamb,
                R.drawable.penguin,
                R.drawable.baby_seal,
                R.drawable.bunny,
                R.drawable.cat
        )
);


public static final ArrayList<Object> travel = new ArrayList<Object>(
        Arrays.asList(
                R.drawable.greece,
                R.drawable.iceland,
                R.drawable.maldives,
                R.drawable.whitsundays,
                R.drawable.travel1,
                R.drawable.travel2
        )
);

public static final ArrayList<Object> nature = new ArrayList<Object>(
        Arrays.asList(
                R.drawable.waterfall,
                R.drawable.joffre_lakes,
                R.drawable.nothern_lights,
                R.drawable.chile,
                R.drawable.winter
        )
);

public static Map<String, ArrayList<Object>> contentMap = new HashMap<String, ArrayList<Object>>() {{
        put("Wellness", wellness);
        put("Good News", goodNews);
        put("Exercise", exercise);
        put("Animals", animals);
        put("Travel", travel);
        put("Nature", nature);
}};
}
