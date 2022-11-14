package cn.mango.mangoblog.utils;

import cn.mango.mangoblog.entity.User;
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
    public static String gen_token(User user){
        //用户认证完成，生成token
        Calendar instance = Calendar.getInstance();//获取当前时间
        instance.add(Calendar.SECOND, 30);//过期时间
        //生成令牌
        String token = JWT.create()
                .withClaim("user_id", user.getId())//设置自定义用户名，载体，可以设置多个
                .withExpiresAt(instance.getTime())//设置过期时间
                .sign(Algorithm.HMAC256(SIGN_KEY));//设置签名

        System.out.println(token);

        return token;
    }

    public static Optional<DecodedJWT> verify(String token){
        if (token == null || token.equals("")){
            return Optional.empty();
        }
        return Optional.of(VERIFIER.verify(token));
    }
}
