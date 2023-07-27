package org.apache.guacamole.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.auth.Credentials;
import org.apache.guacamole.net.auth.credentials.CredentialsInfo;
import org.apache.guacamole.net.auth.credentials.GuacamoleInvalidCredentialsException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class AuthenticationProviderService {

    /**
     * Service for retrieving configuration values
     */
    @Inject
    private ConfigurationService confService;

    /**
     * Provider for CloudflareUser objects
     */
    @Inject
    private Provider<CloudflareUser> authenticatedUserProvider;

    public CloudflareUser authenticateUser(Credentials credentials) throws GuacamoleException {
        HttpServletRequest req = credentials.getRequest();
        String authToken = req.getHeader("Cf-Access-Jwt-Assertion");

        try {
            // decode JWT
            DecodedJWT token = JWT.decode(authToken);

            // fetch cert
            RSAPublicKey publicKey = getCloudflareCert(token.getKeyId());

            // validate token
            Algorithm algo = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algo)
                    .withAudience(confService.getCloudflareAccessAudience())
                    .withIssuer(String.format("https://%s.cloudflareaccess.com", confService.getCloudflareAccessTeam()))
                    .build();

            verifier.verify(token);

            CloudflareUser user = authenticatedUserProvider.get();
            user.init(token, credentials);
            return user;

        } catch (Exception exception) {
            // catch all other exceptions
            System.out.println("cloudflare access auth failed");
            System.out.println(exception.toString());
            throw new GuacamoleInvalidCredentialsException("Invalid login", exception,
                    CredentialsInfo.USERNAME_PASSWORD);
        }
    }

    private RSAPublicKey getCloudflareCert(String keyId)
            throws URISyntaxException, IOException, InterruptedException, Exception {
        OkHttpClient client = new OkHttpClient();

        Request req = new Request.Builder()
                .url(String.format("https://%s.cloudflareaccess.com/cdn-cgi/access/certs",
                        confService.getCloudflareAccessTeam()))
                .build();

        Response resp = client.newCall(req).execute();
        if (!resp.isSuccessful()) {
            throw new Exception("failed to fetch keys from cloudflare (http error)");
        }

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Decoder decoder = Base64.getUrlDecoder();

        JSONObject obj = new JSONObject(resp.body().string());

        JSONArray keys = obj.getJSONArray("keys");
        for (int i = 0; i < keys.length(); i++) {
            JSONObject key = keys.getJSONObject(i);
            String kid = key.getString("kid");
            if (keyId.equals(kid)) {
                BigInteger exp = new BigInteger(1, decoder.decode(key.getString("e")));
                BigInteger mod = new BigInteger(1, decoder.decode(key.getString("n")));

                RSAPublicKeySpec spec = new RSAPublicKeySpec(mod, exp);
                return (RSAPublicKey) keyFactory.generatePublic(spec);
            }
        }

        throw new Exception("cert not found");
    }
}
