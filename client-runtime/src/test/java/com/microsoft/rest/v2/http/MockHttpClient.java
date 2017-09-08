/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.rest.v2.http;

import com.microsoft.rest.v2.HttpBinJSON;
import rx.Single;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * This HttpClient attempts to mimic the behavior of http://httpbin.org without ever making a network call.
 */
public class MockHttpClient extends HttpClient {
    @Override
    public Single<? extends HttpResponse> sendRequestAsync(HttpRequest request) {
        HttpResponse response = new MockHttpResponse();

        try {
            final URI requestUrl = new URI(request.url());
            final String requestHost = requestUrl.getHost();
            if (requestHost.equalsIgnoreCase("httpbin.org")) {
                final String requestPath = requestUrl.getPath();
                final String requestPathLower = requestPath.toLowerCase();
                if (requestPathLower.equals("/anything") || requestPathLower.startsWith("/anything/")) {
                    final HttpBinJSON json = new HttpBinJSON();
                    json.url = request.url()
                            // This is just to mimic the behavior we've seen with httpbin.org.
                            .replace("%20", " ");
                    json.headers = toMap(request.headers());
                    response = new MockHttpResponse(json);
                }
                else if (requestPathLower.startsWith("/bytes/")) {
                    final String byteCountString = requestPath.substring("/bytes/".length());
                    final int byteCount = Integer.parseInt(byteCountString);
                    response = new MockHttpResponse(new byte[byteCount]);
                }
                else if (requestPathLower.equals("/delete")) {
                    final HttpBinJSON json = new HttpBinJSON();
                    json.data = request.body();
                    response = new MockHttpResponse(json);
                }
                else if (requestPathLower.equals("/get")) {
                    final HttpBinJSON json = new HttpBinJSON();
                    json.url = request.url();
                    json.headers = toMap(request.headers());
                    response = new MockHttpResponse(json);
                }
                else if (requestPathLower.equals("/patch")) {
                    final HttpBinJSON json = new HttpBinJSON();
                    json.data = request.body();
                    response = new MockHttpResponse(json);
                }
                else if (requestPathLower.equals("/post")) {
                    final HttpBinJSON json = new HttpBinJSON();
                    json.data = request.body();
                    response = new MockHttpResponse(json);
                }
                else if (requestPathLower.equals("/put")) {
                    final HttpBinJSON json = new HttpBinJSON();
                    json.data = request.body();
                    response = new MockHttpResponse(json);
                }
            }
        }
        catch (Exception ignored) {
        }

        return Single.just(response);
    }

    private static Map<String, String> toMap(HttpHeaders headers) {
        final Map<String, String> result = new HashMap<>();
        for (final HttpHeader header : headers) {
            result.put(header.name(), header.value());
        }
        return result;
    }
}
