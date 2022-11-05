package cn.mango.mangoblog.function;

import cn.mango.mangoblog.entity.ResultWrapper;
import cn.mango.mangoblog.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserFunction", url = "demo.service.url")
public interface IUserFunction {
    @GetMapping(value = "/user/{id}")
    ResultWrapper<User> getUser(@PathVariable("id") Long id);

    @PostMapping(value = "/user")
    ResultWrapper<Boolean> addUser(@RequestBody User user);

}
