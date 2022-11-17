package cn.mango.mangoblog.utils;

import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import cn.mango.mangoblog.entity.VerifyResult;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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
        Optional<DecodedJWT> decodedJWT = Optional.of((VERIFIER.verify(token)));
        if (decodedJWT.isEmpty()) {
            return new ResultWrapper<>(2, "Invalid token", null);
        } else {
            VerifyResult verifyresult = new VerifyResult(decodedJWT.get().getClaim("user_id").asLong(), decodedJWT.get().getClaim("privilege").asInt());
            return new ResultWrapper<>(0, "Success", verifyresult);
        }
    }
}
