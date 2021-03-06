package com.app.alexseguin.travislauncher;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    static final String TRAVIS_URL = "https://api.travis-ci.org";

    public void useAppContext() {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.app.alexseguin.travislauncher", appContext.getPackageName());
    }

    // Class to be used as a final so lambda expr can call it
    class ResponseResult {
        String result;
    }

    public void testAPICall() throws InterruptedException {

        final ResponseResult res = new ResponseResult();

        await().atMost(5, SECONDS).until(() -> {
            // Context of the app under test.
            Context appContext = InstrumentationRegistry.getTargetContext();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(appContext);
            String url = "https://jsonplaceholder.typicode.com/todos/1";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            res.result = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assert(false);
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            return true;
        });
        System.out.println(res.result);
        assert(res != null);
    }

    public void testTravisAPICall() throws InterruptedException {

        final ResponseResult res = new ResponseResult();

        await().atMost(5, SECONDS).until(() -> {
            // Context of the app under test.
            Context appContext = InstrumentationRegistry.getTargetContext();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(appContext);
            String url = TRAVIS_URL + "/user";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            res.result = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assert(false);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Travis-API-Version", "3");
                    params.put("User-Agent", "Android Travis API");
                    params.put("Authorization", "token xQJwwZUh0MXz08mk9AMoBA");
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            return true;
        });
        System.out.println(res.result);
        assert(res != null);
    }


}
