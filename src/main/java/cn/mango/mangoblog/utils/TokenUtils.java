package cn.mango.mangoblog.utils;

import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.entity.VerifyResult;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

public class TokenUtils {

    private static final String SIGN_KEY = "dvjaw9mjcd3cmhda";
    private static final JWTVerifier VERIFIER = JWT.require(Algorithm.HMAC256(SIGN_KEY)).build();

    public static String gen_token(User user) {
        //用户认证完成，生成token
        Calendar instance = Calendar.getInstance();//获取当前时间
        instance.add(Calendar.SECOND, 21600);//过期时间
        //生成令牌
        String token = JWT.create()
                .withClaim("user_id", user.getId())//设置自定义用户名，载体，可以设置多个
                .withClaim("privilege", user.getPrivilege())
                .withExpiresAt(instance.getTime())//设置过期时间
                .sign(Algorithm.HMAC256(SIGN_KEY));//设置签名

        System.out.println(token);
        return token;
    }

    public static ResultWrapper<VerifyResult> Verify(String token) {//校验token并返回user_id
        if (token == null || token.equals("")) {
            return new ResultWrapper<>(3, "Empty token", null);
        }
        try {
            DecodedJWT decodedJWT = VERIFIER.verify(token);
            VerifyResult verifyresult = new VerifyResult(decodedJWT.getClaim("user_id").asLong(), decodedJWT.getClaim("privilege").asInt());
            return new ResultWrapper<>(verifyresult);
        } catch (TokenExpiredException e){
            return new ResultWrapper<>(2, "Token过期了: " + e.getMessage(), null);
        } catch (SignatureVerificationException e){
            return new ResultWrapper<>(2, "Token签名不正确: " + e.getMessage(), null);
        } catch (JWTVerificationException e) {
            return new ResultWrapper<>(2, "Token错误: " + e.getMessage(), null);
        }

    }

}
