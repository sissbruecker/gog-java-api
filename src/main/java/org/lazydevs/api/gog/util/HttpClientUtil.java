package org.lazydevs.api.gog.util;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.lazydevs.api.gog.GogApiException;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sascha Ißbrücker
 * Date: 03.02.13
 * Time: 08:26
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class HttpClientUtil {

    public static HttpResponse post(HttpClient client, HttpContext context, String url, List<NameValuePair> params) {

        HttpPost post = new HttpPost(url);
        HttpResponse response;

        post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

        try {
            response = client.execute(post, context);
        } catch (IOException e) {
            throw new GogApiException("Error executing POST request. url=" + url, e);
        }

        return response;
    }

    public static HttpResponse get(HttpClient client, HttpContext context, String url) {

        HttpGet get = new HttpGet(url);
        HttpResponse response;

        try {
            response = client.execute(get, context);
        } catch (IOException e) {
            throw new GogApiException("Error executing GET request. url=" + url, e);
        }

        return response;
    }

    public static String getBody(HttpClient client, HttpContext context, String url) {

        HttpResponse response = get(client, context, url);
        String body;

        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new GogApiException("Error reading response body.", e);
        }

        return body;
    }

    public static void initHttpsNoCertNoHostVerification(HttpClient base, int httpsPort) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            X509HostnameVerifier verifier = new X509HostnameVerifier() {

                @Override
                public void verify(String string, SSLSocket ssls) throws IOException {
                }

                @Override
                public void verify(String string, X509Certificate xc) throws SSLException {
                }

                @Override
                public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                }

                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };
            ClientConnectionManager ccm = base.getConnectionManager();
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, verifier);
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", httpsPort, ssf));
            //      return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            throw new IllegalStateException("couldn't initialize HttpClient: ", ex);
        }
    }
}
